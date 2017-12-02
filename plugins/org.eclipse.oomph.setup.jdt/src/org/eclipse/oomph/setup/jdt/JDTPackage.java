/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt;

import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.jdt.JDTFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/Ecore schemaLocation='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/JDT.ecore'"
 *        annotation="http://www.eclipse.org/oomph/setup/Enablement variableName='setup.jdt.p2' repository='${oomph.update.url}' installableUnits='org.eclipse.oomph.setup.jdt.feature.group'"
 *        annotation="http://www.eclipse.org/oomph/base/LabelProvider imageBaseURI='http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.jdt.edit/icons/full/obj16'"
 * @generated
 */
public interface JDTPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "jdt";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/jdt/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "jdt";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  JDTPackage eINSTANCE = org.eclipse.oomph.setup.jdt.impl.JDTPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl <em>JRE Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.jdt.impl.JRETaskImpl
   * @see org.eclipse.oomph.setup.jdt.impl.JDTPackageImpl#getJRETask()
   * @generated
   */
  int JRE_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__ANNOTATIONS = SetupPackage.SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__ID = SetupPackage.SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__DESCRIPTION = SetupPackage.SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__SCOPE_TYPE = SetupPackage.SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__EXCLUDED_TRIGGERS = SetupPackage.SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__DISABLED = SetupPackage.SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__PREDECESSORS = SetupPackage.SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__SUCCESSORS = SetupPackage.SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__RESTRICTIONS = SetupPackage.SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__FILTER = SetupPackage.SETUP_TASK__FILTER;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__VERSION = SetupPackage.SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__LOCATION = SetupPackage.SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__NAME = SetupPackage.SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>VM Install Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__VM_INSTALL_TYPE = SetupPackage.SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Execution Environment Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>VM Arguments</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__VM_ARGUMENTS = SetupPackage.SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>JRE Libraries</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK__JRE_LIBRARIES = SetupPackage.SETUP_TASK_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>JRE Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_TASK_FEATURE_COUNT = SetupPackage.SETUP_TASK_FEATURE_COUNT + 7;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl <em>JRE Library</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl
   * @see org.eclipse.oomph.setup.jdt.impl.JDTPackageImpl#getJRELibrary()
   * @generated
   */
  int JRE_LIBRARY = 1;

  /**
   * The feature id for the '<em><b>Library Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_LIBRARY__LIBRARY_PATH = 0;

  /**
   * The feature id for the '<em><b>External Annotations Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH = 1;

  /**
   * The number of structural features of the '<em>JRE Library</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int JRE_LIBRARY_FEATURE_COUNT = 2;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.jdt.JRETask <em>JRE Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JRE Task</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask
   * @generated
   */
  EClass getJRETask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getVersion()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_Version();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getLocation()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_Location();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getName()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#getVMInstallType <em>VM Install Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>VM Install Type</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getVMInstallType()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_VMInstallType();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#isExecutionEnvironmentDefault <em>Execution Environment Default</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Execution Environment Default</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#isExecutionEnvironmentDefault()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_ExecutionEnvironmentDefault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRETask#getVMArguments <em>VM Arguments</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>VM Arguments</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getVMArguments()
   * @see #getJRETask()
   * @generated
   */
  EAttribute getJRETask_VMArguments();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.jdt.JRETask#getJRELibraries <em>JRE Libraries</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>JRE Libraries</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRETask#getJRELibraries()
   * @see #getJRETask()
   * @generated
   */
  EReference getJRETask_JRELibraries();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.jdt.JRELibrary <em>JRE Library</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>JRE Library</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRELibrary
   * @generated
   */
  EClass getJRELibrary();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRELibrary#getLibraryPath <em>Library Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Library Path</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRELibrary#getLibraryPath()
   * @see #getJRELibrary()
   * @generated
   */
  EAttribute getJRELibrary_LibraryPath();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.jdt.JRELibrary#getExternalAnnotationsPath <em>External Annotations Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>External Annotations Path</em>'.
   * @see org.eclipse.oomph.setup.jdt.JRELibrary#getExternalAnnotationsPath()
   * @see #getJRELibrary()
   * @generated
   */
  EAttribute getJRELibrary_ExternalAnnotationsPath();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  JDTFactory getJDTFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl <em>JRE Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.jdt.impl.JRETaskImpl
     * @see org.eclipse.oomph.setup.jdt.impl.JDTPackageImpl#getJRETask()
     * @generated
     */
    EClass JRE_TASK = eINSTANCE.getJRETask();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__VERSION = eINSTANCE.getJRETask_Version();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__LOCATION = eINSTANCE.getJRETask_Location();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__NAME = eINSTANCE.getJRETask_Name();

    /**
     * The meta object literal for the '<em><b>VM Install Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__VM_INSTALL_TYPE = eINSTANCE.getJRETask_VMInstallType();

    /**
     * The meta object literal for the '<em><b>Execution Environment Default</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT = eINSTANCE.getJRETask_ExecutionEnvironmentDefault();

    /**
     * The meta object literal for the '<em><b>VM Arguments</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_TASK__VM_ARGUMENTS = eINSTANCE.getJRETask_VMArguments();

    /**
     * The meta object literal for the '<em><b>JRE Libraries</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference JRE_TASK__JRE_LIBRARIES = eINSTANCE.getJRETask_JRELibraries();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl <em>JRE Library</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.jdt.impl.JRELibraryImpl
     * @see org.eclipse.oomph.setup.jdt.impl.JDTPackageImpl#getJRELibrary()
     * @generated
     */
    EClass JRE_LIBRARY = eINSTANCE.getJRELibrary();

    /**
     * The meta object literal for the '<em><b>Library Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_LIBRARY__LIBRARY_PATH = eINSTANCE.getJRELibrary_LibraryPath();

    /**
     * The meta object literal for the '<em><b>External Annotations Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH = eINSTANCE.getJRELibrary_ExternalAnnotationsPath();

  }

} // JDTPackage
