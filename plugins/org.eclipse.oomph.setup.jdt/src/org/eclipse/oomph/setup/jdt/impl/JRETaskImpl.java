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
package org.eclipse.oomph.setup.jdt.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRETask;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;

import java.io.File;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JRE Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#isIsDefault <em>Is Default</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JRETaskImpl extends SetupTaskImpl implements JRETask
{
  /**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected static final String VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected String version = VERSION_EDEFAULT;

  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

  /**
   * The default value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsDefault()
   * @generated
   * @ordered
   */
  protected static final boolean IS_DEFAULT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsDefault()
   * @generated
   * @ordered
   */
  protected boolean isDefault = IS_DEFAULT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JRETaskImpl()
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
    return JDTPackage.Literals.JRE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVersion(String newVersion)
  {
    String oldVersion = version;
    version = newVersion;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__VERSION, oldVersion, version));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsDefault()
  {
    return isDefault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsDefault(boolean newIsDefault)
  {
    boolean oldIsDefault = isDefault;
    isDefault = newIsDefault;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__IS_DEFAULT, oldIsDefault, isDefault));
    }
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
      case JDTPackage.JRE_TASK__VERSION:
        return getVersion();
      case JDTPackage.JRE_TASK__LOCATION:
        return getLocation();
      case JDTPackage.JRE_TASK__IS_DEFAULT:
        return isIsDefault();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JDTPackage.JRE_TASK__VERSION:
        setVersion((String)newValue);
        return;
      case JDTPackage.JRE_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case JDTPackage.JRE_TASK__IS_DEFAULT:
        if (null != newValue)
        {
          setIsDefault((Boolean)newValue);
        }
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
      case JDTPackage.JRE_TASK__VERSION:
        setVersion(VERSION_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__IS_DEFAULT:
        setIsDefault(IS_DEFAULT_EDEFAULT);
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
      case JDTPackage.JRE_TASK__VERSION:
        return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
      case JDTPackage.JRE_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case JDTPackage.JRE_TASK__IS_DEFAULT:
        return isDefault != IS_DEFAULT_EDEFAULT;
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
    result.append(" (version: ");
    result.append(version);
    result.append(", location: ");
    result.append(location);
    result.append(", isDefault: ");
    result.append(isDefault);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return JREHelper.isNeeded(context, getVersion(), getLocation(), isIsDefault());
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    JREHelper.perform(context, getVersion(), getLocation(), isIsDefault());
  }

  private static class JREHelper
  {
    public static void perform(SetupTaskContext context, String version, String location, Boolean isDefault) throws Exception
    {
      IVMInstallType[] types = JavaRuntime.getVMInstallTypes();

      for (IVMInstallType type : types)
      {
        if ("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType".equals(type.getId()))
        {
          context.log("Configurating a " + version + " JRE for location " + location);
          File installLocation = new File(location);

          IStatus validationStatus = type.validateInstallLocation(installLocation);
          if (!validationStatus.isOK())
          {
            throw new CoreException(validationStatus);
          }

          VMStandin vmStandin = new VMStandin(type, EcoreUtil.generateUUID());
          vmStandin.setInstallLocation(installLocation);
          vmStandin.setName("JRE for " + version);
          IVMInstall realVM = vmStandin.convertToRealVM();

          JavaRuntime.setDefaultVMInstall(realVM, new NullProgressMonitor());

          if ("J2SE-1.4".equals(version))
          {
            IExecutionEnvironment[] executionEnvironments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();

            for (IExecutionEnvironment executionEnvironment : executionEnvironments)
            {
              String id = executionEnvironment.getId();
              if (id.equals("CDC-1.1/Foundation-1.1"))
              {
                if (executionEnvironment.getDefaultVM() == null)
                {
                  executionEnvironment.setDefaultVM(realVM);
                  break;
                }
              }
            }
          }

          // if the user selected this JRE/JDK to be default execution environment, we propagate this
          IExecutionEnvironment[] executionEnvironments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();

          for (IExecutionEnvironment executionEnvironment : executionEnvironments)
          {
            String id = executionEnvironment.getId();
            if (version.equals(id) && isDefault)
            {
              if (executionEnvironment.getDefaultVM() == null)
              {
                executionEnvironment.setDefaultVM(realVM);
                break;
              }
            }
          }

          return;
        }
      }
    }

    public static boolean isNeeded(SetupTaskContext context, String version, String location, Boolean isDefault) throws Exception
    {
      for (IVMInstallType vmInstallType : JavaRuntime.getVMInstallTypes())
      {
        for (IVMInstall vmInstall : vmInstallType.getVMInstalls())
        {
          File installLocation = vmInstall.getInstallLocation();
          if (new File(location).equals(installLocation))
          {
            return false;
          }
        }
      }

      return true;
    }
  }

} // JRETaskImpl
