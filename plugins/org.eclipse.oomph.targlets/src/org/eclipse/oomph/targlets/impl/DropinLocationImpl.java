/**
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.targlets.DropinLocation;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dropin Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.impl.DropinLocationImpl#getRootFolder <em>Root Folder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DropinLocationImpl extends MinimalEObjectImpl.Container implements DropinLocation
{
  /**
   * The default value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected static final String ROOT_FOLDER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRootFolder() <em>Root Folder</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRootFolder()
   * @generated
   * @ordered
   */
  protected String rootFolder = ROOT_FOLDER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DropinLocationImpl()
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
    return TargletPackage.Literals.DROPIN_LOCATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRootFolder()
  {
    return rootFolder;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRootFolder(String newRootFolder)
  {
    String oldRootFolder = rootFolder;
    rootFolder = newRootFolder;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.DROPIN_LOCATION__ROOT_FOLDER, oldRootFolder, rootFolder));
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
      case TargletPackage.DROPIN_LOCATION__ROOT_FOLDER:
        return getRootFolder();
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
      case TargletPackage.DROPIN_LOCATION__ROOT_FOLDER:
        setRootFolder((String)newValue);
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
      case TargletPackage.DROPIN_LOCATION__ROOT_FOLDER:
        setRootFolder(ROOT_FOLDER_EDEFAULT);
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
      case TargletPackage.DROPIN_LOCATION__ROOT_FOLDER:
        return ROOT_FOLDER_EDEFAULT == null ? rootFolder != null : !ROOT_FOLDER_EDEFAULT.equals(rootFolder);
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
    result.append(" (rootFolder: ");
    result.append(rootFolder);
    result.append(')');
    return result.toString();
  }

} // DropinLocationImpl
