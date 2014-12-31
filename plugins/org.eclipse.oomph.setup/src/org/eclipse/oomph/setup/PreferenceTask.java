/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Eclipse Preference Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.PreferenceTask#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.PreferenceTask#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getPreferenceTask()
 * @model annotation="http://www.eclipse.org/oomph/setup/ValidTriggers triggers='STARTUP MANUAL'"
 * @generated
 */
public interface PreferenceTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Key</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Key</em>' attribute.
   * @see #setKey(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getPreferenceTask_Key()
   * @model required="true"
   * @generated
   */
  String getKey();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.PreferenceTask#getKey <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Key</em>' attribute.
   * @see #getKey()
   * @generated
   */
  void setKey(String value);

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getPreferenceTask_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.PreferenceTask#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // PreferenceTask
