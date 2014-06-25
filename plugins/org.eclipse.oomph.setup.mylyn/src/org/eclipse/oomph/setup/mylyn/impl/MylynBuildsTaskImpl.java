/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 425815 - Add authentication to the tasks repository
 */
package org.eclipse.oomph.setup.mylyn.impl;

import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.mylyn.BuildPlan;
import org.eclipse.oomph.setup.mylyn.MylynBuildsTask;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynPackage;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.mylyn.builds.core.IBuildPlan;
import org.eclipse.mylyn.builds.core.IBuildServer;
import org.eclipse.mylyn.builds.internal.core.BuildFactory;
import org.eclipse.mylyn.builds.ui.BuildsUi;
import org.eclipse.mylyn.commons.repositories.core.RepositoryLocation;
import org.eclipse.mylyn.commons.repositories.core.auth.AuthenticationType;
import org.eclipse.mylyn.commons.repositories.core.auth.UserCredentials;
import org.eclipse.mylyn.internal.builds.ui.BuildsUiInternal;
import org.eclipse.mylyn.internal.builds.ui.BuildsUiPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mylyn Builds Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl#getConnectorKind <em>Connector Kind</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl#getServerURL <em>Server URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl#getUserID <em>User ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.mylyn.impl.MylynBuildsTaskImpl#getBuildPlans <em>Build Plans</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
@SuppressWarnings("restriction")
public class MylynBuildsTaskImpl extends SetupTaskImpl implements MylynBuildsTask
{
  /**
   * The default value of the '{@link #getConnectorKind() <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectorKind()
   * @generated
   * @ordered
   */
  protected static final String CONNECTOR_KIND_EDEFAULT = "org.eclipse.mylyn.hudson";

  /**
   * The cached value of the '{@link #getConnectorKind() <em>Connector Kind</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectorKind()
   * @generated
   * @ordered
   */
  protected String connectorKind = CONNECTOR_KIND_EDEFAULT;

  /**
   * The default value of the '{@link #getServerURL() <em>Server URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerURL()
   * @generated
   * @ordered
   */
  protected static final String SERVER_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getServerURL() <em>Server URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerURL()
   * @generated
   * @ordered
   */
  protected String serverURL = SERVER_URL_EDEFAULT;

  /**
   * The default value of the '{@link #getUserID() <em>User ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserID()
   * @generated
   * @ordered
   */
  protected static final String USER_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUserID() <em>User ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserID()
   * @generated
   * @ordered
   */
  protected String userID = USER_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected static final String PASSWORD_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassword()
   * @generated
   * @ordered
   */
  protected String password = PASSWORD_EDEFAULT;

  /**
   * The cached value of the '{@link #getBuildPlans() <em>Build Plans</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBuildPlans()
   * @generated
   * @ordered
   */
  protected EList<BuildPlan> buildPlans;

  private transient MylynHelper mylynHelper;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MylynBuildsTaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MylynPackage.Literals.MYLYN_BUILDS_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectorKind()
  {
    return connectorKind;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectorKind(String newConnectorKind)
  {
    String oldConnectorKind = connectorKind;
    connectorKind = newConnectorKind;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_BUILDS_TASK__CONNECTOR_KIND, oldConnectorKind, connectorKind));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getServerURL()
  {
    return serverURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServerURL(String newServerURL)
  {
    String oldServerURL = serverURL;
    serverURL = newServerURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_BUILDS_TASK__SERVER_URL, oldServerURL, serverURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<BuildPlan> getBuildPlans()
  {
    if (buildPlans == null)
    {
      buildPlans = new EObjectContainmentEList<BuildPlan>(BuildPlan.class, this, MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS);
    }
    return buildPlans;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUserID()
  {
    return userID;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUserID(String newUserID)
  {
    String oldUserID = userID;
    userID = newUserID;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_BUILDS_TASK__USER_ID, oldUserID, userID));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPassword(String newPassword)
  {
    String oldPassword = password;
    password = newPassword;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, MylynPackage.MYLYN_BUILDS_TASK__PASSWORD, oldPassword, password));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS:
        return ((InternalEList<?>)getBuildPlans()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MylynPackage.MYLYN_BUILDS_TASK__CONNECTOR_KIND:
        return getConnectorKind();
      case MylynPackage.MYLYN_BUILDS_TASK__SERVER_URL:
        return getServerURL();
      case MylynPackage.MYLYN_BUILDS_TASK__USER_ID:
        return getUserID();
      case MylynPackage.MYLYN_BUILDS_TASK__PASSWORD:
        return getPassword();
      case MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS:
        return getBuildPlans();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MylynPackage.MYLYN_BUILDS_TASK__CONNECTOR_KIND:
        setConnectorKind((String)newValue);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__SERVER_URL:
        setServerURL((String)newValue);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__USER_ID:
        setUserID((String)newValue);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__PASSWORD:
        setPassword((String)newValue);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS:
        getBuildPlans().clear();
        getBuildPlans().addAll((Collection<? extends BuildPlan>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MylynPackage.MYLYN_BUILDS_TASK__CONNECTOR_KIND:
        setConnectorKind(CONNECTOR_KIND_EDEFAULT);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__SERVER_URL:
        setServerURL(SERVER_URL_EDEFAULT);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__USER_ID:
        setUserID(USER_ID_EDEFAULT);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__PASSWORD:
        setPassword(PASSWORD_EDEFAULT);
        return;
      case MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS:
        getBuildPlans().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MylynPackage.MYLYN_BUILDS_TASK__CONNECTOR_KIND:
        return CONNECTOR_KIND_EDEFAULT == null ? connectorKind != null : !CONNECTOR_KIND_EDEFAULT.equals(connectorKind);
      case MylynPackage.MYLYN_BUILDS_TASK__SERVER_URL:
        return SERVER_URL_EDEFAULT == null ? serverURL != null : !SERVER_URL_EDEFAULT.equals(serverURL);
      case MylynPackage.MYLYN_BUILDS_TASK__USER_ID:
        return USER_ID_EDEFAULT == null ? userID != null : !USER_ID_EDEFAULT.equals(userID);
      case MylynPackage.MYLYN_BUILDS_TASK__PASSWORD:
        return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
      case MylynPackage.MYLYN_BUILDS_TASK__BUILD_PLANS:
        return buildPlans != null && !buildPlans.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (connectorKind: ");
    result.append(connectorKind);
    result.append(", serverURL: ");
    result.append(serverURL);
    result.append(", userID: ");
    result.append(userID);
    result.append(", password: ");
    result.append(password);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    mylynHelper = MylynHelperImpl.create();
    return mylynHelper.isNeeded(context, this);
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    mylynHelper.perform(context, this);
  }

  @Override
  public void collectSniffers(List<Sniffer> sniffers)
  {
    sniffers.add(new BasicSniffer(this, "Creates one or several tasks for the build plans in the Mylyn builds list.")
    {
      public void sniff(SetupTaskContainer container, List<Sniffer> dependencies, IProgressMonitor monitor) throws Exception
      {
        MylynHelper mylynHelper = MylynHelperImpl.create();
        mylynHelper.sniff(container, null, monitor);
      }
    });
  }

  /**
   * @author Eike Stepper
   */
  protected interface MylynHelper
  {
    public boolean isNeeded(SetupTaskContext context, MylynBuildsTask task) throws Exception;

    public void perform(SetupTaskContext context, MylynBuildsTask task) throws Exception;

    public void sniff(SetupTaskContainer container, List<Sniffer> dependencies, IProgressMonitor monitor);
  }

  /**
   * @author Eike Stepper
   */
  private static class MylynHelperImpl implements MylynHelper
  {
    private IBuildServer server;

    private Set<String> buildPlanNames = new HashSet<String>();

    public boolean isNeeded(SetupTaskContext context, MylynBuildsTask task) throws Exception
    {
      String serverURL = task.getServerURL();
      server = getServer(serverURL);

      for (BuildPlan buildPlan : task.getBuildPlans())
      {
        buildPlanNames.add(buildPlan.getName());
      }

      for (IBuildPlan buildPlan : BuildsUi.getModel().getPlans())
      {
        if (buildPlan.getServer() == server)
        {
          buildPlanNames.remove(buildPlan.getName());
        }
      }

      if (server == null)
      {
        return true;
      }

      if (!buildPlanNames.isEmpty())
      {
        return true;
      }

      return false;
    }

    public void perform(final SetupTaskContext context, final MylynBuildsTask task) throws Exception
    {
      UserCallback callback = context.getPrompter().getUserCallback();
      callback.execInUI(false, new Runnable()
      {
        public void run()
        {
          String connectorKind = task.getConnectorKind();

          if (server == null)
          {
            String serverURL = task.getServerURL();
            String userID = task.getUserID();
            String password = context.expandString(task.getPassword(), true);

            context.log("Adding " + connectorKind + " server: " + serverURL);

            server = BuildsUi.createServer(connectorKind);
            server.setUrl(serverURL);
            server.getAttributes().put("id", serverURL);
            server.getAttributes().put("url", serverURL);

            boolean authenticate = !StringUtil.isEmpty(userID) && !StringUtil.isEmpty(password);
            if (authenticate)
            {
              server.getAttributes().put("org.eclipse.mylyn.tasklist.repositories.enabled", "true");
              server.getAttributes().put("org.eclipse.mylyn.repositories.username", userID);
            }

            // Add credentials to the repository
            RepositoryLocation repositoryLocation = new RepositoryLocation(server.getAttributes());
            repositoryLocation.setUrl(serverURL);
            if (authenticate)
            {
              UserCredentials credentials = new UserCredentials(userID, password, true);
              repositoryLocation.setCredentials(AuthenticationType.REPOSITORY, credentials);
            }

            server.setLocation(repositoryLocation);

            BuildsUiInternal.getModel().getServers().add(server);
          }

          for (String buildPlanName : buildPlanNames)
          {
            context.log("Adding " + connectorKind + " build plan: " + buildPlanName);

            IBuildPlan buildPlan = BuildFactory.eINSTANCE.createBuildPlan();
            buildPlan.setId(buildPlanName);
            buildPlan.setName(buildPlanName);
            buildPlan.setServer(server);
            buildPlan.setSelected(true);

            BuildsUiInternal.getModel().getPlans().add(buildPlan);
          }

          BuildsUiPlugin.getDefault().refreshBuilds();
        }
      });
    }

    private IBuildServer getServer(String serverURL)
    {
      for (IBuildServer server : BuildsUi.getModel().getServers())
      {
        if (ObjectUtil.equals(server.getUrl(), serverURL))
        {
          return server;
        }
      }

      return null;
    }

    public void sniff(SetupTaskContainer container, List<Sniffer> dependencies, IProgressMonitor monitor)
    {
      Map<String, MylynBuildsTask> tasks = new HashMap<String, MylynBuildsTask>();
      for (IBuildPlan buildPlan : BuildsUiInternal.getModel().getPlans())
      {
        IBuildServer buildServer = buildPlan.getServer();
        String serverURL = buildServer.getUrl();
        MylynBuildsTask task = tasks.get(serverURL);
        if (task == null)
        {
          task = MylynFactory.eINSTANCE.createMylynBuildsTask();
          task.setServerURL(serverURL);
          task.setConnectorKind(buildServer.getConnectorKind());
          container.getSetupTasks().add(task);
          tasks.put(serverURL, task);
        }

        BuildPlan plan = MylynFactory.eINSTANCE.createBuildPlan();
        plan.setName(buildPlan.getName());
        task.getBuildPlans().add(plan);
      }
    }

    public static MylynHelper create()
    {
      return new MylynHelperImpl();
    }
  }

} // MylynBuildsTaskImpl
