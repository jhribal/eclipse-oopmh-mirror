/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.sync.DataProvider.Location;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.RemoteData;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncDelta;
import org.eclipse.oomph.setup.sync.SyncDeltaType;
import org.eclipse.oomph.setup.sync.SyncFactory;
import org.eclipse.oomph.setup.sync.SyncPackage;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Synchronization
{
  private final ResourceSet resourceSet = SyncUtil.createResourceSet();

  private final Set<String> ids = new HashSet<String>();

  private final Map<String, String> preferenceIDs = new HashMap<String, String>();

  private final Synchronizer synchronizer;

  private final WorkingCopy remoteWorkingCopy;

  private final EMap<String, SyncPolicy> remotePolicies;

  private final Map<String, SyncDelta> remoteDeltas;

  private WorkingCopy localWorkingCopy;

  private Map<String, SyncDelta> localDeltas;

  private Map<String, SyncAction> actions;

  private Map<String, SyncAction> unresolvedActions;

  private boolean committed;

  private boolean disposed;

  private int lastID;

  public Synchronization(Synchronizer synchronizer, boolean deferLocal) throws IOException
  {
    this.synchronizer = synchronizer;
    synchronizer.syncStarted();

    remoteWorkingCopy = createWorkingCopy(Location.REMOTE);
    synchronizer.workingCopyCreated(remoteWorkingCopy);

    remotePolicies = getPolicies(remoteWorkingCopy);

    // Compute remote deltas first to make sure that new local tasks don't pick remotely existing IDs.
    remoteDeltas = computeDeltas(Location.REMOTE);

    if (!deferLocal)
    {
      synchronizeLocal();
    }
  }

  public Synchronizer getSynchronizer()
  {
    return synchronizer;
  }

  public ResourceSet getResourceSet()
  {
    return resourceSet;
  }

  public EMap<String, SyncPolicy> getRemotePolicies()
  {
    return remotePolicies;
  }

  /**
   * Returns the mappings from preference keys to sync IDs.
   */
  public Map<String, String> getPreferenceIDs()
  {
    return preferenceIDs;
  }

  public Map<String, SyncAction> synchronizeLocal() throws IOException
  {
    if (localWorkingCopy != null)
    {
      localWorkingCopy.dispose();
    }

    // Compute local deltas.
    localWorkingCopy = createWorkingCopy(Location.LOCAL);
    synchronizer.workingCopyCreated(localWorkingCopy);

    localDeltas = computeDeltas(Location.LOCAL);

    // Compute sync actions.
    actions = computeSyncActions();
    synchronizer.actionsComputed(actions);

    return actions;
  }

  private WorkingCopy createWorkingCopy(Location location) throws IOException
  {
    Snapshot snapshot = location.pick(synchronizer.getLocalSnapshot(), synchronizer.getRemoteSnapshot());
    WorkingCopy workingCopy = snapshot.createWorkingCopy();

    File oldFile = snapshot.getOldFile();
    unloadResource(oldFile);

    if (oldFile != null && !oldFile.exists())
    {
      SyncUtil.inititalizeFile(oldFile, location.getDataType(), resourceSet);
    }

    File tmpFile = workingCopy.getTmpFile();
    unloadResource(tmpFile);

    File newFile = snapshot.getNewFile();
    if (!newFile.exists())
    {
      SyncUtil.inititalizeFile(tmpFile, location.getDataType(), resourceSet);
    }
    else
    {
      IOUtil.copyFile(newFile, tmpFile);
    }

    return workingCopy;
  }

  private EMap<String, SyncPolicy> getPolicies(WorkingCopy remoteWorkingCopy)
  {
    File file = remoteWorkingCopy.getTmpFile();
    RemoteData remoteData = loadObject(file, Location.REMOTE.getDataType());
    return remoteData.getPolicies();
  }

  private boolean isIncluded(String id)
  {
    return SyncPolicy.EXCLUDE != remotePolicies.get(id);
  }

  private Map<String, SyncDelta> computeDeltas(Location location)
  {
    EClass dataType = location.getDataType();

    WorkingCopy workingCopy = location.pick(localWorkingCopy, remoteWorkingCopy);
    Snapshot snapshot = workingCopy.getSnapshot();

    File oldFile = snapshot.getOldFile();
    File tmpFile = workingCopy.getTmpFile();

    SetupTaskContainer oldData = oldFile != null ? (SetupTaskContainer)loadObject(oldFile, dataType) : SetupFactory.eINSTANCE.createCompoundTask();
    SetupTaskContainer newData = loadObject(tmpFile, dataType);

    return compareTasks(location, oldData, newData);
  }

  private Map<String, SyncAction> computeSyncActions()
  {
    Map<String, SyncAction> actions = new HashMap<String, SyncAction>();
    Map<String, SyncDelta> tmpRemoteDeltas = new HashMap<String, SyncDelta>(remoteDeltas);

    for (Map.Entry<String, SyncDelta> localEntry : localDeltas.entrySet())
    {
      String id = localEntry.getKey();

      SyncDelta localDelta = localEntry.getValue();
      SyncDelta remoteDelta = tmpRemoteDeltas.remove(id);

      SyncAction action = compareDeltas(localDelta, remoteDelta);
      if (action != null)
      {
        actions.put(id, action);
      }
    }

    for (SyncDelta remoteDelta : tmpRemoteDeltas.values())
    {
      String id = remoteDelta.getID();
      SyncAction action = compareDeltas(null, remoteDelta);
      actions.put(id, action);
    }

    for (Map.Entry<String, SyncAction> entry : actions.entrySet())
    {
      String id = entry.getKey();
      SyncAction action = entry.getValue();
      new ActionAdapter(action, id);
    }

    return actions;
  }

  private SyncAction compareDeltas(SyncDelta localDelta, SyncDelta remoteDelta)
  {
    SyncDeltaType localDeltaType = localDelta == null ? SyncDeltaType.UNCHANGED : localDelta.getType();
    SyncDeltaType remoteDeltaType = remoteDelta == null ? SyncDeltaType.UNCHANGED : remoteDelta.getType();

    SyncActionType actionType = compareDeltaTypes(localDeltaType, remoteDeltaType);
    if (actionType == SyncActionType.NONE)
    {
      PreferenceTask localPreference = (PreferenceTask)localDelta.getNewTask();
      PreferenceTask remotePreference = (PreferenceTask)remoteDelta.getNewTask();

      // The comparison has returned a Changed/Changed delta conflict, so compare the values.
      if (ObjectUtil.equals(localPreference.getValue(), remotePreference.getValue()))
      {
        // Ignore unchanged values.
        actionType = null;
      }
      else
      {
        actionType = SyncActionType.CONFLICT;
      }
    }

    if (actionType != null)
    {
      return SyncFactory.eINSTANCE.createSyncAction(localDelta, remoteDelta, actionType);
    }

    return null;
  }

  private SyncActionType compareDeltaTypes(SyncDeltaType localDeltaType, SyncDeltaType remoteDeltaType)
  {
    switch (localDeltaType)
    {
      case UNCHANGED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return null;

          case CHANGED:
            return SyncActionType.SET_REMOTE;

          case REMOVED:
            return SyncActionType.REMOVE_REMOTE;
        }
        break;

      case CHANGED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return SyncActionType.SET_LOCAL;

          case CHANGED:
            // Will be changed to CONFLICT or null by the caller.
            return SyncActionType.NONE;

          case REMOVED:
            return SyncActionType.CONFLICT;
        }
        break;

      case REMOVED:
        switch (remoteDeltaType)
        {
          case UNCHANGED:
            return SyncActionType.REMOVE_LOCAL;

          case CHANGED:
            return SyncActionType.CONFLICT;

          case REMOVED:
            return null;
        }
        break;
    }

    throw new IllegalArgumentException();
  }

  private Map<String, SyncDelta> compareTasks(Location location, SetupTaskContainer oldTaskContainer, SetupTaskContainer newTaskContainer)
  {
    Map<String, SyncDelta> deltas = new HashMap<String, SyncDelta>();

    Map<String, SetupTask> oldTasks = collectTasks(oldTaskContainer);
    Map<String, SetupTask> newTasks = collectTasks(newTaskContainer);
    synchronizer.tasksCollected(this, location, oldTasks, newTasks);

    for (Map.Entry<String, SetupTask> oldEntry : oldTasks.entrySet())
    {
      String id = oldEntry.getKey();
      if (isIncluded(id))
      {
        SetupTask oldTask = oldEntry.getValue();
        SetupTask newTask = newTasks.remove(id);

        SyncDelta delta = compareTasks(id, oldTask, newTask);
        if (delta != null)
        {
          deltas.put(id, delta);
        }
      }
    }

    for (Map.Entry<String, SetupTask> newEntry : newTasks.entrySet())
    {
      String id = newEntry.getKey();
      if (isIncluded(id))
      {
        SetupTask newTask = newEntry.getValue();

        SyncDelta delta = compareTasks(id, null, newTask);
        deltas.put(id, delta);
      }
    }

    return deltas;
  }

  private SyncDelta compareTasks(String id, SetupTask oldTask, SetupTask newTask)
  {
    if (oldTask == null)
    {
      if (newTask == null)
      {
        return null;
      }

      return SyncFactory.eINSTANCE.createSyncDelta(id, oldTask, newTask, SyncDeltaType.CHANGED);
    }

    if (newTask == null)
    {
      return SyncFactory.eINSTANCE.createSyncDelta(id, oldTask, newTask, SyncDeltaType.REMOVED);
    }

    PreferenceTask oldPreference = (PreferenceTask)oldTask;
    PreferenceTask newPreference = (PreferenceTask)newTask;

    if (!ObjectUtil.equals(oldPreference.getKey(), newPreference.getKey()))
    {
      // Ignore changed keys.
      return null;
    }

    if (ObjectUtil.equals(oldPreference.getValue(), newPreference.getValue()))
    {
      // Ignore unchanged values.
      return null;
    }

    return SyncFactory.eINSTANCE.createSyncDelta(id, oldPreference, newPreference, SyncDeltaType.CHANGED);
  }

  private Map<String, SetupTask> collectTasks(SetupTaskContainer taskContainer)
  {
    // Visit all tasks to collect all IDs first to ensure that we don't create a duplicate ID while collecting tasks.
    EList<SetupTask> setupTasks = taskContainer.getSetupTasks();
    collectIDs(setupTasks);

    Map<String, SetupTask> tasks = new HashMap<String, SetupTask>();
    if (collectTasks(setupTasks, tasks))
    {
      new ChangedAdapter(taskContainer);
    }

    return tasks;
  }

  private void collectIDs(EList<SetupTask> tasks)
  {
    for (SetupTask task : tasks)
    {
      rememberID(task);
      if (task instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)task;
        collectIDs(compoundTask.getSetupTasks());
      }
    }
  }

  private boolean collectTasks(EList<SetupTask> tasks, Map<String, SetupTask> result)
  {
    boolean changed = false;

    for (SetupTask task : tasks)
    {
      String id = rememberID(task);

      if (isSychronizable(task))
      {
        if (StringUtil.isEmpty(id))
        {
          id = getPreferenceID(task);

          if (StringUtil.isEmpty(id))
          {
            id = createID();
          }
          else
          {
            ids.add(id);
          }

          String oldID = task.getID();
          if (!id.equals(oldID))
          {
            task.setID(id);
            changed |= true;
          }

          rememberPreferenceID(task);
        }

        if (result.put(id, task) != null)
        {
          throw new DuplicateIDException(id);
        }
      }
      else if (task instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)task;
        changed |= collectTasks(compoundTask.getSetupTasks(), result);
      }
    }

    return changed;
  }

  private boolean isSychronizable(SetupTask task)
  {
    return task instanceof PreferenceTask;
  }

  private void rememberIDs(Resource resource)
  {
    for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
    {
      EObject object = it.next();

      String id = EcoreUtil.getID(object);
      if (!StringUtil.isEmpty(id))
      {
        // Make sure existing IDs are not reused.
        ids.add(id);
      }
    }
  }

  private String rememberID(SetupTask task)
  {
    String id = task.getID();
    if (!StringUtil.isEmpty(id))
    {
      // Make sure existing IDs are not reused.
      ids.add(id);

      rememberPreferenceID(task);
    }

    return id;
  }

  private void rememberPreferenceID(SetupTask task)
  {
    String id = task.getID();
    if (!StringUtil.isEmpty(id))
    {
      if (task instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)task;
        String key = preferenceTask.getKey();

        if (!StringUtil.isEmpty(key))
        {
          preferenceIDs.put(key, id);
        }
      }
    }
  }

  private String getPreferenceID(SetupTask task)
  {
    if (task instanceof PreferenceTask)
    {
      PreferenceTask preferenceTask = (PreferenceTask)task;
      String key = preferenceTask.getKey();

      if (!StringUtil.isEmpty(key))
      {
        return preferenceIDs.get(key);
      }
    }

    return null;
  }

  private String createID()
  {
    for (int i = lastID + 1; i < Integer.MAX_VALUE; i++)
    {
      String id = "sync" + i; //$NON-NLS-1$
      if (ids.add(id))
      {
        lastID = i;
        return id;
      }
    }

    throw new IllegalStateException(Messages.Synchronization_TooManyIDs_exception);
  }

  public String getID(SyncAction action)
  {
    String id = action.getID();
    if (id != null)
    {
      return id;
    }

    ActionAdapter adapter = (ActionAdapter)EcoreUtil.getAdapter(action.eAdapters(), ActionAdapter.class);
    if (adapter != null)
    {
      return adapter.getID();
    }

    return null;
  }

  public Map<String, SyncAction> getActions()
  {
    return actions;
  }

  public Map<String, SyncAction> getUnresolvedActions()
  {
    if (unresolvedActions == null)
    {
      unresolvedActions = new HashMap<String, SyncAction>();

      for (Map.Entry<String, SyncAction> entry : actions.entrySet())
      {
        SyncAction action = entry.getValue();

        if (action.getEffectiveType() == SyncActionType.CONFLICT)
        {
          String id = entry.getKey();
          unresolvedActions.put(id, action);
        }
      }
    }

    return unresolvedActions;
  }

  public Synchronization resolve(String id, SyncActionType resolvedType)
  {
    SyncAction action = actions.get(id);
    if (action != null)
    {
      action.setResolvedType(resolvedType);
    }

    return this;
  }

  public void commit() throws IOException, NotCurrentException
  {
    if (!committed && !disposed)
    {
      committed = true;
      doCommit();
    }
  }

  private void doCommit() throws IOException, NotCurrentException
  {
    synchronizer.commitStarted();

    try
    {
      boolean updateRemoteDataProvider = applyActions(Location.REMOTE);
      remoteWorkingCopy.commit(updateRemoteDataProvider);

      boolean updateLocalDataProvider = applyActions(Location.LOCAL);
      localWorkingCopy.commit(updateLocalDataProvider);

      synchronizer.commitFinished(null);
    }
    catch (IOException ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
    catch (RuntimeException ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
    catch (Error ex)
    {
      synchronizer.commitFinished(ex);
      throw ex;
    }
  }

  private boolean applyActions(Location location)
  {
    WorkingCopy workingCopy = location.pick(localWorkingCopy, remoteWorkingCopy);
    File file = workingCopy.getTmpFile();

    SetupTaskContainer taskContainer = loadObject(file, location.getDataType());
    Map<String, SetupTask> tasks = collectTasks(taskContainer);

    boolean changed = ChangedAdapter.isChanged(taskContainer);

    for (Map.Entry<String, SyncAction> entry : actions.entrySet())
    {
      String id = entry.getKey();
      SyncAction action = entry.getValue();
      SyncActionType type = action.getEffectiveType();

      switch (type)
      {
        case CONFLICT:
          throw new ConflictException(action);

        case SET_LOCAL:
          changed |= include(id);
          changed |= applySetAction(taskContainer, tasks, id, action.getLocalDelta());
          break;

        case SET_REMOTE:
          if (location != Location.REMOTE) // Don't modify remote unnecessarily
          {
            changed |= include(id);
            changed |= applySetAction(taskContainer, tasks, id, action.getRemoteDelta());
          }
          break;

        case REMOVE_LOCAL:
          changed |= include(id);
          changed |= applyRemoveAction(taskContainer, tasks, action.getLocalDelta());
          break;

        case REMOVE_REMOTE:
          if (location != Location.REMOTE) // Don't modify remote unnecessarily
          {
            changed |= include(id);
            changed |= applyRemoveAction(taskContainer, tasks, action.getRemoteDelta());
          }
          break;

        case EXCLUDE:
          changed |= exclude(id);
          changed |= applyRemoveAction(taskContainer, tasks, action.getRemoteDelta());
          break;

        default:
          // Do nothing.
          break;
      }
    }

    if (changed)
    {
      BaseUtil.saveEObject(taskContainer);
    }

    return changed;

  }

  private boolean applySetAction(SetupTaskContainer taskContainer, Map<String, SetupTask> tasks, String id, SyncDelta delta)
  {
    if (delta != null)
    {
      SetupTask newTask = delta.getNewTask();
      if (newTask != null)
      {
        newTask = EcoreUtil.copy(newTask);
        newTask.setID(id);
        newTask.getRestrictions().clear();
        newTask.getPredecessors().clear();
        newTask.getSuccessors().clear();

        SetupTask oldTask = tasks.get(id);
        if (oldTask != null)
        {
          EcoreUtil.replace(oldTask, newTask);
        }
        else
        {
          taskContainer.getSetupTasks().add(newTask);
        }

        return true;
      }
    }

    return false;
  }

  private boolean applyRemoveAction(SetupTaskContainer taskContainer, Map<String, SetupTask> tasks, SyncDelta delta)
  {
    if (delta != null)
    {
      String id = delta.getID();

      SetupTask oldTask = tasks.get(id);
      if (oldTask != null)
      {
        EcoreUtil.remove(oldTask);
        return true;
      }
    }

    return false;
  }

  private boolean include(String id)
  {
    return remotePolicies.put(id, SyncPolicy.INCLUDE) != SyncPolicy.INCLUDE;
  }

  private boolean exclude(String id)
  {
    return remotePolicies.put(id, SyncPolicy.EXCLUDE) != SyncPolicy.EXCLUDE;
  }

  public void dispose()
  {
    if (!disposed)
    {
      doDispose();
    }
  }

  private void doDispose()
  {
    disposed = true;

    try
    {
      if (localWorkingCopy != null)
      {
        localWorkingCopy.dispose();
      }
    }
    catch (Throwable ex)
    {
      SetupSyncPlugin.INSTANCE.log(ex);
    }

    try
    {
      if (remoteWorkingCopy != null)
      {
        remoteWorkingCopy.dispose();
      }
    }
    catch (Throwable ex)
    {
      SetupSyncPlugin.INSTANCE.log(ex);
    }

    try
    {
      if (synchronizer != null)
      {
        synchronizer.releaseLock();
      }
    }
    catch (Throwable ex)
    {
      SetupSyncPlugin.INSTANCE.log(ex);
    }
  }

  private Resource getResource(File file, boolean loadOnDemand)
  {
    URI uri = URI.createFileURI(file.getAbsolutePath());
    return resourceSet.getResource(uri, loadOnDemand);
  }

  private <T extends EObject> T loadObject(File file, EClass eClass)
  {
    Resource resource = getResource(file, true);
    rememberIDs(resource);

    return BaseUtil.getObjectByType(resource.getContents(), eClass);
  }

  private void unloadResource(File file)
  {
    Resource resource = getResource(file, false);
    if (resource != null)
    {
      resource.unload();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ChangedAdapter extends AdapterImpl
  {
    public ChangedAdapter(EObject object)
    {
      object.eAdapters().add(this);
    }

    public static boolean isChanged(EObject object)
    {
      for (Adapter adapter : object.eAdapters())
      {
        if (adapter.getClass() == ChangedAdapter.class)
        {
          return true;
        }
      }

      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ActionAdapter extends AdapterImpl
  {
    private final String id;

    public ActionAdapter(SyncAction action, String id)
    {
      this.id = id;
      action.eAdapters().add(this);
    }

    public String getID()
    {
      return id;
    }

    @Override
    public void notifyChanged(Notification msg)
    {
      if (msg.getFeature() == SyncPackage.Literals.SYNC_ACTION__RESOLVED_TYPE && !msg.isTouch())
      {
        unresolvedActions = null;

        SyncAction action = (SyncAction)getTarget();
        synchronizer.actionResolved(action, id);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class DuplicateIDException extends SynchronizerException
  {
    private static final long serialVersionUID = 1L;

    public DuplicateIDException(String id)
    {
      super(MessageFormat.format(Messages.Synchronization_DuplicateID_exception, id));
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ConflictException extends SynchronizerException
  {
    private static final long serialVersionUID = 1L;

    public ConflictException(SyncAction action)
    {
      super(MessageFormat.format(Messages.Synchronization_Conflict_exception, action));
    }
  }
}
