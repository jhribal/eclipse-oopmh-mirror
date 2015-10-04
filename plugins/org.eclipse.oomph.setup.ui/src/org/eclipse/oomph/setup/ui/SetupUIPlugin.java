/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.OomphPreferencePage;
import org.eclipse.oomph.internal.ui.TaskItemDecorator;
import org.eclipse.oomph.jreinfo.ui.JREInfoUIPlugin;
import org.eclipse.oomph.p2.internal.ui.P2UIPlugin;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.p2.provider.SetupP2EditPlugin;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.SynchronizationController;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerPreferencePage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.osgi.framework.BundleContext;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class SetupUIPlugin extends OomphUIPlugin
{
  public static final SetupUIPlugin INSTANCE = new SetupUIPlugin();

  public static final String PLUGIN_ID = INSTANCE.getSymbolicName();

  public static final String PREF_HEADLESS = "headless.startup";

  public static final String PREF_SKIP_STARTUP_TASKS = "skip.startup.tasks";

  public static final String PREF_ENABLE_PREFERENCE_RECORDER = "enable.preference.recorder";

  public static final String PREF_PREFERENCE_RECORDER_TARGET = "preference.recorder.target";

  public static final boolean QUESTIONNAIRE_SKIP = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_QUESTIONNAIRE_SKIP);

  private static final String RESTARTING_FILE_NAME = "restarting";

  private static final String ANNOTATION_SOURCE_INITIAL = "initial";

  private static final String ANNOTATION_DETAILS_KEY_OFFLINE = "offline";

  private static final String ANNOTATION_DETAILS_KEY_MIRRORS = "mirrors";

  private static final boolean SETUP_SKIP = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SKIP);

  private static Implementation plugin;

  public SetupUIPlugin()
  {
    super(new ResourceLocator[] { JREInfoUIPlugin.INSTANCE, SetupEditPlugin.INSTANCE, SetupCorePlugin.INSTANCE, SetupP2EditPlugin.INSTANCE,
        P2UIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static boolean isInstallerProduct()
  {
    String productID = PropertiesUtil.getProperty("eclipse.product");
    return SetupUtil.INSTALLER_PRODUCT_ID.equals(productID);
  }

  public static void initialStart(File ws, boolean offline, boolean mirrors)
  {
    Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
    annotation.setSource(ANNOTATION_SOURCE_INITIAL);
    annotation.getDetails().put(ANNOTATION_DETAILS_KEY_OFFLINE, Boolean.toString(offline));
    annotation.getDetails().put(ANNOTATION_DETAILS_KEY_MIRRORS, Boolean.toString(mirrors));

    File file = new File(ws, ".metadata/.plugins/" + SetupUIPlugin.INSTANCE.getSymbolicName() + "/" + RESTARTING_FILE_NAME);
    saveRestartFile(file, annotation);
  }

  public static void restart(Trigger trigger, EList<SetupTask> setupTasks)
  {
    if (!setupTasks.isEmpty())
    {
      Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(trigger.toString());
      annotation.getReferences().addAll(setupTasks);

      saveRestartFile(getRestartingFile(), annotation);
    }

    PlatformUI.getWorkbench().restart();
  }

  private static void saveRestartFile(File file, Annotation annotation)
  {
    try
    {
      Resource resource = SetupCoreUtil.createResourceSet().createResource(URI.createFileURI(file.toString()));
      resource.getContents().add(annotation);
      resource.save(null);
    }
    catch (Exception ex)
    {
      // Ignore
    }
  }

  public static boolean isSkipStartupTasks()
  {
    return plugin.getPreferenceStore().getBoolean(PREF_SKIP_STARTUP_TASKS);
  }

  private static File getRestartingFile()
  {
    return new File(INSTANCE.getStateLocation().toString(), RESTARTING_FILE_NAME);
  }

  private static void performStartup()
  {
    if (!PropertiesUtil.isProperty(PREF_HEADLESS))
    {
      final Display display = Display.getDefault();
      display.asyncExec(new Runnable()
      {
        public void run()
        {
          if (!isInstallerProduct())
          {
            SetupPropertyTester.setStarting(true);

            final IWorkbench workbench = PlatformUI.getWorkbench();
            IExtensionTracker extensionTracker = workbench.getExtensionTracker();
            if (extensionTracker == null || workbench.getWorkbenchWindowCount() == 0)
            {
              display.timerExec(1000, this);
            }
            else
            {
              if (!SynchronizerManager.ENABLED)
              {
                PreferenceManager preferenceManager = workbench.getPreferenceManager();
                preferenceManager.remove("/" + OomphPreferencePage.ID + "/" + SetupPreferencePage.ID + "/" + SynchronizerPreferencePage.ID);
              }

              if (SetupTaskPerformer.REMOTE_DEBUG)
              {
                MessageDialog.openInformation(UIUtil.getShell(), "Remote Debug Pause", "The setup tasks are paused to allow you to attach a remote debugger");
              }

              if (Platform.OS_MACOSX.equals(Platform.getOS()))
              {
                new TaskItemDecorator();
              }

              RecorderManager.Lifecycle.start(display);

              if (!SETUP_SKIP && !isSkipStartupTasks())
              {
                new Job("Setup check")
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    try
                    {
                      performStartup(workbench, monitor);
                      return Status.OK_STATUS;
                    }
                    finally
                    {
                      SetupPropertyTester.setStarting(false);
                    }
                  }
                }.schedule();
              }
              else
              {
                Job mirrorJob = new Job("Initialize Setup Models")
                {
                  @Override
                  protected IStatus run(IProgressMonitor monitor)
                  {
                    try
                    {
                      monitor.beginTask("Loading resources", 10);

                      ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
                      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
                      mirror(resourceSet, monitor, 10);
                      SetupContext.setSelf(SetupContext.createSelf(resourceSet));

                      return Status.OK_STATUS;
                    }
                    finally
                    {
                      SetupPropertyTester.setStarting(false);
                    }
                  }
                };

                mirrorJob.schedule();
              }
            }
          }
        }
      });
    }
  }

  private static void mirror(final ResourceSet resourceSet, IProgressMonitor monitor, int work)
  {
    ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
    {
      @Override
      protected void run(String taskName, IProgressMonitor monitor)
      {
        List<URI> uris = new ArrayList<URI>();
        URIConverter uriConverter = resourceSet.getURIConverter();
        for (URI uri : new URI[] { SetupContext.INSTALLATION_SETUP_URI, SetupContext.WORKSPACE_SETUP_URI, SetupContext.USER_SETUP_URI })
        {
          if (uriConverter.exists(uri, null))
          {
            uris.add(uri);
          }
        }

        perform(uris);
      }
    };

    resourceMirror.begin(SubMonitor.convert(monitor, work));
  }

  private static Set<? extends EObject> checkCrossReferences(ResourceSet resourceSet, URI uri)
  {
    Set<EObject> result = new HashSet<EObject>();

    Resource resource = resourceSet.getResource(uri, false);
    if (resource != null)
    {
      EList<EObject> contents = resource.getContents();
      if (!contents.isEmpty())
      {
        EObject eObject = contents.get(0);
        for (EObject eCrossReference : eObject.eCrossReferences())
        {
          Resource eResource = eCrossReference.eResource();
          if (eResource != null)
          {
            for (EObject content : eResource.getContents())
            {
              EObject eContainer = content.eContainer();
              if (eContainer != null)
              {
                result.add(eContainer);
              }
            }

            eResource.unload();
          }
        }
      }
    }

    return result;
  }

  private static void performStartup(final IWorkbench workbench, IProgressMonitor monitor)
  {
    monitor.beginTask("", 105);
    Trigger trigger = Trigger.STARTUP;
    boolean restarting = false;
    Set<URI> neededRestartTasks = new HashSet<URI>();

    try
    {
      File restartingFile = getRestartingFile();
      if (restartingFile.exists())
      {
        monitor.setTaskName("Loading restart tasks " + restartingFile);
        Resource resource = SetupCoreUtil.createResourceSet().getResource(URI.createFileURI(restartingFile.toString()), true);

        Annotation annotation = (Annotation)EcoreUtil.getObjectByType(resource.getContents(), BasePackage.Literals.ANNOTATION);
        resource.getContents().remove(annotation);

        if (ANNOTATION_SOURCE_INITIAL.equals(annotation.getSource()))
        {
          if ("true".equals(annotation.getDetails().get(ANNOTATION_DETAILS_KEY_OFFLINE)))
          {
            System.setProperty(SetupProperties.PROP_SETUP_OFFLINE_STARTUP, "true");
          }

          if ("true".equals(annotation.getDetails().get(ANNOTATION_DETAILS_KEY_MIRRORS)))
          {
            System.setProperty(SetupProperties.PROP_SETUP_MIRRORS_STARTUP, "true");
          }
        }
        else
        {
          for (EObject eObject : annotation.getReferences())
          {
            neededRestartTasks.add(EcoreUtil.getURI(eObject));
          }

          trigger = Trigger.get(annotation.getSource());
          restarting = true;
        }

        IOUtil.deleteBestEffort(restartingFile);
      }
    }
    catch (Exception ex)
    {
      // Ignore
    }

    monitor.worked(1);

    // Disabled for bug 459486:
    // if (!QUESTIONNAIRE_SKIP)
    // {
    // Questionnaire.perform(UIUtil.getShell(), false);
    // }

    // This performer is only used to detect a need to update or to open the setup wizard.
    SetupTaskPerformer performer = null;
    final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

    monitor.setTaskName("Creating a setup task performer");

    try
    {
      SynchronizationController synchronizationController = SynchronizerManager.INSTANCE.startSynchronization();

      // Ensure that the demand created resources for the installation, workspace, and user are loaded and created.
      // Load the resource set quickly without doing ETag checking.
      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
      mirror(resourceSet, monitor, 25);

      // Check the installation and workspace resources for cross references.
      // This unloads the cross referenced resources and returns the container objects of the root object(s) of those resources.
      Set<EObject> eContainers = new HashSet<EObject>();
      eContainers.addAll(checkCrossReferences(resourceSet, SetupContext.INSTALLATION_SETUP_URI));
      eContainers.addAll(checkCrossReferences(resourceSet, SetupContext.WORKSPACE_SETUP_URI));

      if (!eContainers.isEmpty())
      {
        // Reload any resources that have been unloaded, this time with ETag checking.
        resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_WITH_ETAG_CHECKING);
        mirror(resourceSet, monitor, 75);

        // Resolve the containment proxies of the containers.
        for (EObject eContainer : eContainers)
        {
          for (@SuppressWarnings("unused")
          EObject eObject : eContainer.eContents())
          {
            // Resolve all containment proxies.
          }
        }
      }
      else
      {
        monitor.worked(75);
      }

      if (synchronizationController != null)
      {
        Synchronization synchronization = synchronizationController.await();
        if (synchronization != null)
        {
          // TODO Implement startup synchronization.
        }
      }

      // Create the performer with a fully populated resource set.
      performer = SetupTaskPerformer.createForIDE(resourceSet, SetupPrompter.CANCEL, trigger);
    }
    catch (OperationCanceledException ex)
    {
      //$FALL-THROUGH$
    }
    catch (Throwable ex)
    {
      INSTANCE.log(ex);
      return;
    }
    finally
    {
      SetupContext.setSelf(SetupContext.createSelf(resourceSet));
    }

    monitor.worked(1);

    if (performer != null)
    {
      monitor.setTaskName("Initializing the setup task performer");

      try
      {
        // At this point we know that no prompt was needed.
        EList<SetupTask> neededTasks = performer.initNeededSetupTasks(new SubProgressMonitor(monitor, 2));
        if (restarting)
        {
          for (Iterator<SetupTask> it = neededTasks.iterator(); it.hasNext();)
          {
            SetupTask setupTask = it.next();
            if (setupTask.getPriority() == SetupTask.PRIORITY_INSTALLATION || !neededRestartTasks.contains(EcoreUtil.getURI(setupTask)))
            {
              it.remove();
            }
          }
        }

        if (neededTasks.isEmpty())
        {
          // No tasks are needed, either. Nothing to do.
          return;
        }

        performer.setSkipConfirmation(true);
      }
      catch (Throwable ex)
      {
        INSTANCE.log(ex);
        return;
      }
    }
    else
    {
      monitor.worked(2);
    }

    monitor.worked(1);
    monitor.setTaskName("Launching the setup wizard");

    final SetupTaskPerformer finalPerfomer = performer;
    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        if (finalPerfomer != null)
        {
          resourceSet.getResources().add(finalPerfomer.getUser().eResource());
        }

        IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
        if (workbenchWindow == null)
        {
          workbenchWindow = workbench.getWorkbenchWindows()[0];
        }

        SetupWizard updater = finalPerfomer != null ? new SetupWizard.Updater(finalPerfomer)
            : new SetupWizard.Updater(SetupContext.createInstallationWorkspaceAndUser(resourceSet));
        updater.openDialog(workbenchWindow.getShell());
      }
    });

    monitor.worked(1);
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      performStartup();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
      if (!isInstallerProduct())
      {
        RecorderManager.Lifecycle.stop();
      }

      super.stop(context);
    }
  }
}
