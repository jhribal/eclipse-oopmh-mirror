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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Context Variable Task</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#isStorePromptedValue <em>Store Prompted Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getChoices <em>Choices</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.VariableTask#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask()
 * @model
 * @generated
 */
public interface VariableTask extends SetupTask
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The default value is <code>"STRING"</code>.
   * The literals are from the enumeration {@link org.eclipse.oomph.setup.VariableType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.VariableType
   * @see #setType(VariableType)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Type()
   * @model default="STRING" required="true"
   * @generated
   */
  VariableType getType();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.setup.VariableType
   * @see #getType()
   * @generated
   */
  void setType(VariableType value);

  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

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
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Label</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Choices</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.VariableChoice}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Choices</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Choices</em>' containment reference list.
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Choices()
   * @model containment="true" resolveProxies="true"
   *        extendedMetaData="name='choice'"
   * @generated
   */
  EList<VariableChoice> getChoices();

  /**
   * Returns the value of the '<em><b>Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default</em>' attribute.
   * @see #setDefault(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_Default()
   * @model
   * @generated
   */
  String getDefault();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#getDefault <em>Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default</em>' attribute.
   * @see #getDefault()
   * @generated
   */
  void setDefault(String value);

  /**
   * Returns the value of the '<em><b>Store Prompted Value</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Store Prompted Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Store Prompted Value</em>' attribute.
   * @see #setStorePromptedValue(boolean)
   * @see org.eclipse.oomph.setup.SetupPackage#getVariableTask_StorePromptedValue()
   * @model default="true"
   * @generated
   */
  boolean isStorePromptedValue();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.VariableTask#isStorePromptedValue <em>Store Prompted Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Store Prompted Value</em>' attribute.
   * @see #isStorePromptedValue()
   * @generated
   */
  void setStorePromptedValue(boolean value);

} // VariableTask
