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

import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupFactory
 * @model kind="package"
 * @generated
 */
public interface SetupPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "setup";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/oomph/setup/1.0";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "setup";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SetupPackage eINSTANCE = org.eclipse.oomph.setup.impl.SetupPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.IndexImpl <em>Index</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.IndexImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getIndex()
   * @generated
   */
  int INDEX = 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.SetupTaskContainerImpl <em>Task Container</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.SetupTaskContainerImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getSetupTaskContainer()
   * @generated
   */
  int SETUP_TASK_CONTAINER = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProjectImpl <em>Project</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProjectImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProject()
   * @generated
   */
  int PROJECT = 10;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.SetupTaskImpl <em>Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.SetupTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getSetupTask()
   * @generated
   */
  int SETUP_TASK = 0;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__ANNOTATIONS = BasePackage.MODEL_ELEMENT__ANNOTATIONS;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.LinkLocationTaskImpl <em>Link Location Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.LinkLocationTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getLinkLocationTask()
   * @generated
   */
  int LINK_LOCATION_TASK = 24;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl <em>Eclipse Ini Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.EclipseIniTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getEclipseIniTask()
   * @generated
   */
  int ECLIPSE_INI_TASK = 23;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl <em>Resource Copy Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getResourceCopyTask()
   * @generated
   */
  int RESOURCE_COPY_TASK = 26;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.TextModifyTaskImpl <em>Text Modify Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.TextModifyTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTextModifyTask()
   * @generated
   */
  int TEXT_MODIFY_TASK = 28;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.TextModificationImpl <em>Text Modification</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.TextModificationImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTextModification()
   * @generated
   */
  int TEXT_MODIFICATION = 29;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl <em>Resource Creation Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getResourceCreationTask()
   * @generated
   */
  int RESOURCE_CREATION_TASK = 27;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.RedirectionTaskImpl <em>Redirection Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.RedirectionTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getRedirectionTask()
   * @generated
   */
  int REDIRECTION_TASK = 22;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.VariableChoiceImpl <em>Variable Choice</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.VariableChoiceImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableChoice()
   * @generated
   */
  int VARIABLE_CHOICE = 20;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.AttributeRuleImpl <em>Attribute Rule</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.AttributeRuleImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getAttributeRule()
   * @generated
   */
  int ATTRIBUTE_RULE = 13;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.InstallationTaskImpl <em>Installation Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.InstallationTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getInstallationTask()
   * @generated
   */
  int INSTALLATION_TASK = 15;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.WorkspaceTaskImpl <em>Workspace Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.WorkspaceTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getWorkspaceTask()
   * @generated
   */
  int WORKSPACE_TASK = 17;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__ID = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__DESCRIPTION = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__SCOPE_TYPE = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__EXCLUDED_TRIGGERS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__DISABLED = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__PREDECESSORS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__SUCCESSORS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK__RESTRICTIONS = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The number of structural features of the '<em>Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK_FEATURE_COUNT = BasePackage.MODEL_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK_CONTAINER__SETUP_TASKS = 0;

  /**
   * The number of structural features of the '<em>Task Container</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SETUP_TASK_CONTAINER_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ScopeImpl <em>Scope</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ScopeImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getScope()
   * @generated
   */
  int SCOPE = 2;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCOPE__SETUP_TASKS = SETUP_TASK_CONTAINER__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCOPE__NAME = SETUP_TASK_CONTAINER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCOPE__LABEL = SETUP_TASK_CONTAINER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCOPE__DESCRIPTION = SETUP_TASK_CONTAINER_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Scope</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SCOPE_FEATURE_COUNT = SETUP_TASK_CONTAINER_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.UserImpl <em>User</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.UserImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getUser()
   * @generated
   */
  int USER = 12;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.WorkspaceImpl <em>Workspace</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.WorkspaceImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getWorkspace()
   * @generated
   */
  int WORKSPACE = 16;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.InstallationImpl <em>Installation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.InstallationImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getInstallation()
   * @generated
   */
  int INSTALLATION = 14;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl <em>Catalog Selection</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.CatalogSelectionImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getCatalogSelection()
   * @generated
   */
  int CATALOG_SELECTION = 4;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProductCatalogImpl <em>Product Catalog</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProductCatalogImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductCatalog()
   * @generated
   */
  int PRODUCT_CATALOG = 5;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProductImpl <em>Product</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProductImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProduct()
   * @generated
   */
  int PRODUCT = 6;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProductVersionImpl <em>Product Version</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProductVersionImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductVersion()
   * @generated
   */
  int PRODUCT_VERSION = 7;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProjectCatalogImpl <em>Project Catalog</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProjectCatalogImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectCatalog()
   * @generated
   */
  int PROJECT_CATALOG = 9;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.StreamImpl <em>Stream</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.StreamImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getStream()
   * @generated
   */
  int STREAM = 11;

  /**
   * The feature id for the '<em><b>Product Catalogs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX__PRODUCT_CATALOGS = 0;

  /**
   * The feature id for the '<em><b>Project Catalogs</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX__PROJECT_CATALOGS = 1;

  /**
   * The number of structural features of the '<em>Index</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX_FEATURE_COUNT = 2;

  /**
   * The feature id for the '<em><b>Product Catalogs</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATALOG_SELECTION__PRODUCT_CATALOGS = 0;

  /**
   * The feature id for the '<em><b>Project Catalogs</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATALOG_SELECTION__PROJECT_CATALOGS = 1;

  /**
   * The feature id for the '<em><b>Default Product Versions</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS = 2;

  /**
   * The feature id for the '<em><b>Default Streams</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATALOG_SELECTION__DEFAULT_STREAMS = 3;

  /**
   * The number of structural features of the '<em>Catalog Selection</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CATALOG_SELECTION_FEATURE_COUNT = 4;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Index</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__INDEX = SCOPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Products</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG__PRODUCTS = SCOPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Product Catalog</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_CATALOG_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Product Catalog</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__PRODUCT_CATALOG = SCOPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Versions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT__VERSIONS = SCOPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Product</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Product</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION__PRODUCT = SCOPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Product Version</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_VERSION_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.ProjectContainer <em>Project Container</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.ProjectContainer
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectContainer()
   * @generated
   */
  int PROJECT_CONTAINER = 8;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Projects</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER__PROJECTS = SCOPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Project Container</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CONTAINER_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__SETUP_TASKS = PROJECT_CONTAINER__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__NAME = PROJECT_CONTAINER__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__LABEL = PROJECT_CONTAINER__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__DESCRIPTION = PROJECT_CONTAINER__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Projects</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__PROJECTS = PROJECT_CONTAINER__PROJECTS;

  /**
   * The feature id for the '<em><b>Index</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG__INDEX = PROJECT_CONTAINER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Project Catalog</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_CATALOG_FEATURE_COUNT = PROJECT_CONTAINER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__SETUP_TASKS = PROJECT_CONTAINER__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__NAME = PROJECT_CONTAINER__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__LABEL = PROJECT_CONTAINER__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__DESCRIPTION = PROJECT_CONTAINER__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Projects</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PROJECTS = PROJECT_CONTAINER__PROJECTS;

  /**
   * The feature id for the '<em><b>Streams</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__STREAMS = PROJECT_CONTAINER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Project Container</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PROJECT_CONTAINER = PROJECT_CONTAINER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Logical Project Container</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__LOGICAL_PROJECT_CONTAINER = PROJECT_CONTAINER_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Parent Project</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PARENT_PROJECT = PROJECT_CONTAINER_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Project Catalog</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT__PROJECT_CATALOG = PROJECT_CONTAINER_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Project</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_FEATURE_COUNT = PROJECT_CONTAINER_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Project</b></em>' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM__PROJECT = SCOPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Stream</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STREAM_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Attribute Rules</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__ATTRIBUTE_RULES = SCOPE_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Accepted Licenses</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER__ACCEPTED_LICENSES = SCOPE_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>User</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int USER_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Attribute URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_RULE__ATTRIBUTE_URI = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_RULE__VALUE = 1;

  /**
   * The number of structural features of the '<em>Attribute Rule</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_RULE_FEATURE_COUNT = 2;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Product Version</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION__PRODUCT_VERSION = SCOPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Installation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK__LOCATION = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Installation Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INSTALLATION_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE__SETUP_TASKS = SCOPE__SETUP_TASKS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE__NAME = SCOPE__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE__LABEL = SCOPE__LABEL;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE__DESCRIPTION = SCOPE__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Streams</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE__STREAMS = SCOPE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Workspace</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_FEATURE_COUNT = SCOPE_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK__LOCATION = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Workspace Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WORKSPACE_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.CompoundTaskImpl <em>Compound Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.CompoundTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getCompoundTask()
   * @generated
   */
  int COMPOUND_TASK = 18;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Setup Tasks</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__SETUP_TASKS = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK__NAME = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Compound Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int COMPOUND_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.VariableTaskImpl <em>Variable Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.VariableTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableTask()
   * @generated
   */
  int VARIABLE_TASK = 19;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__TYPE = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__NAME = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__VALUE = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Store Prompted Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__STORE_PROMPTED_VALUE = SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__LABEL = SETUP_TASK_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Choices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__CHOICES = SETUP_TASK_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Default</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK__DEFAULT = SETUP_TASK_FEATURE_COUNT + 6;

  /**
   * The number of structural features of the '<em>Variable Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_CHOICE__VALUE = 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_CHOICE__LABEL = 1;

  /**
   * The number of structural features of the '<em>Variable Choice</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int VARIABLE_CHOICE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.StringSubstitutionTaskImpl <em>String Substitution Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.StringSubstitutionTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getStringSubstitutionTask()
   * @generated
   */
  int STRING_SUBSTITUTION_TASK = 21;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__NAME = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK__VALUE = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>String Substitution Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STRING_SUBSTITUTION_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Source URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__SOURCE_URL = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK__TARGET_URL = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Redirection Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REDIRECTION_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Option</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__OPTION = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__VALUE = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Vm</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK__VM = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Eclipse Ini Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ECLIPSE_INI_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__PATH = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK__NAME = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Link Location Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int LINK_LOCATION_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl <em>Preference Task</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.PreferenceTaskImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getPreferenceTask()
   * @generated
   */
  int PREFERENCE_TASK = 25;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__KEY = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK__VALUE = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Preference Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PREFERENCE_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Source URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__SOURCE_URL = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK__TARGET_URL = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Resource Copy Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_COPY_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>Content</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__CONTENT = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__TARGET_URL = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK__ENCODING = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Resource Creation Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_CREATION_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__ANNOTATIONS = SETUP_TASK__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__ID = SETUP_TASK__ID;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__DESCRIPTION = SETUP_TASK__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Scope Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__SCOPE_TYPE = SETUP_TASK__SCOPE_TYPE;

  /**
   * The feature id for the '<em><b>Excluded Triggers</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__EXCLUDED_TRIGGERS = SETUP_TASK__EXCLUDED_TRIGGERS;

  /**
   * The feature id for the '<em><b>Disabled</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__DISABLED = SETUP_TASK__DISABLED;

  /**
   * The feature id for the '<em><b>Predecessors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__PREDECESSORS = SETUP_TASK__PREDECESSORS;

  /**
   * The feature id for the '<em><b>Successors</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__SUCCESSORS = SETUP_TASK__SUCCESSORS;

  /**
   * The feature id for the '<em><b>Restrictions</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__RESTRICTIONS = SETUP_TASK__RESTRICTIONS;

  /**
   * The feature id for the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__URL = SETUP_TASK_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Modifications</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__MODIFICATIONS = SETUP_TASK_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK__ENCODING = SETUP_TASK_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Text Modify Task</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFY_TASK_FEATURE_COUNT = SETUP_TASK_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFICATION__PATTERN = 0;

  /**
   * The feature id for the '<em><b>Substitutions</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFICATION__SUBSTITUTIONS = 1;

  /**
   * The number of structural features of the '<em>Text Modification</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TEXT_MODIFICATION_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProductToProductVersionMapEntryImpl <em>Product To Product Version Map Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProductToProductVersionMapEntryImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductToProductVersionMapEntry()
   * @generated
   */
  int PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY = 30;

  /**
   * The feature id for the '<em><b>Key</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE = 1;

  /**
   * The number of structural features of the '<em>Product To Product Version Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.impl.ProjectToStreamMapEntryImpl <em>Project To Stream Map Entry</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.impl.ProjectToStreamMapEntryImpl
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectToStreamMapEntry()
   * @generated
   */
  int PROJECT_TO_STREAM_MAP_ENTRY = 31;

  /**
   * The feature id for the '<em><b>Key</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_TO_STREAM_MAP_ENTRY__KEY = 0;

  /**
   * The feature id for the '<em><b>Value</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_TO_STREAM_MAP_ENTRY__VALUE = 1;

  /**
   * The number of structural features of the '<em>Project To Stream Map Entry</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PROJECT_TO_STREAM_MAP_ENTRY_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.ScopeType <em>Scope Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.ScopeType
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getScopeType()
   * @generated
   */
  int SCOPE_TYPE = 32;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.Trigger <em>Trigger</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.Trigger
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTrigger()
   * @generated
   */
  int TRIGGER = 33;

  /**
   * The meta object id for the '{@link org.eclipse.oomph.setup.VariableType <em>Variable Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.VariableType
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableType()
   * @generated
   */
  int VARIABLE_TYPE = 34;

  /**
   * The meta object id for the '<em>License Info</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.oomph.setup.LicenseInfo
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getLicenseInfo()
   * @generated
   */
  int LICENSE_INFO = 36;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Index <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Index</em>'.
   * @see org.eclipse.oomph.setup.Index
   * @generated
   */
  EClass getIndex();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.Index#getProjectCatalogs <em>Project Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Project Catalogs</em>'.
   * @see org.eclipse.oomph.setup.Index#getProjectCatalogs()
   * @see #getIndex()
   * @generated
   */
  EReference getIndex_ProjectCatalogs();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.CatalogSelection <em>Catalog Selection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Catalog Selection</em>'.
   * @see org.eclipse.oomph.setup.CatalogSelection
   * @generated
   */
  EClass getCatalogSelection();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.CatalogSelection#getProductCatalogs <em>Product Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Product Catalogs</em>'.
   * @see org.eclipse.oomph.setup.CatalogSelection#getProductCatalogs()
   * @see #getCatalogSelection()
   * @generated
   */
  EReference getCatalogSelection_ProductCatalogs();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.CatalogSelection#getProjectCatalogs <em>Project Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Project Catalogs</em>'.
   * @see org.eclipse.oomph.setup.CatalogSelection#getProjectCatalogs()
   * @see #getCatalogSelection()
   * @generated
   */
  EReference getCatalogSelection_ProjectCatalogs();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.setup.CatalogSelection#getDefaultProductVersions <em>Default Product Versions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Default Product Versions</em>'.
   * @see org.eclipse.oomph.setup.CatalogSelection#getDefaultProductVersions()
   * @see #getCatalogSelection()
   * @generated
   */
  EReference getCatalogSelection_DefaultProductVersions();

  /**
   * Returns the meta object for the map '{@link org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams <em>Default Streams</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>Default Streams</em>'.
   * @see org.eclipse.oomph.setup.CatalogSelection#getDefaultStreams()
   * @see #getCatalogSelection()
   * @generated
   */
  EReference getCatalogSelection_DefaultStreams();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.Index#getProductCatalogs <em>Product Catalogs</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Product Catalogs</em>'.
   * @see org.eclipse.oomph.setup.Index#getProductCatalogs()
   * @see #getIndex()
   * @generated
   */
  EReference getIndex_ProductCatalogs();

  /**
   * The meta object id for the '<em>Trigger Set</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.util.Set
   * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTriggerSet()
   * @generated
   */
  int TRIGGER_SET = 35;

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Project <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project</em>'.
   * @see org.eclipse.oomph.setup.Project
   * @generated
   */
  EClass getProject();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.Project#getStreams <em>Streams</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Streams</em>'.
   * @see org.eclipse.oomph.setup.Project#getStreams()
   * @see #getProject()
   * @generated
   */
  EReference getProject_Streams();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.Project#getProjectContainer <em>Project Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Project Container</em>'.
   * @see org.eclipse.oomph.setup.Project#getProjectContainer()
   * @see #getProject()
   * @generated
   */
  EReference getProject_ProjectContainer();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.Project#getLogicalProjectContainer <em>Logical Project Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Logical Project Container</em>'.
   * @see org.eclipse.oomph.setup.Project#getLogicalProjectContainer()
   * @see #getProject()
   * @generated
   */
  EReference getProject_LogicalProjectContainer();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.Project#getParentProject <em>Parent Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Parent Project</em>'.
   * @see org.eclipse.oomph.setup.Project#getParentProject()
   * @see #getProject()
   * @generated
   */
  EReference getProject_ParentProject();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.Project#getProjectCatalog <em>Project Catalog</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Project Catalog</em>'.
   * @see org.eclipse.oomph.setup.Project#getProjectCatalog()
   * @see #getProject()
   * @generated
   */
  EReference getProject_ProjectCatalog();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Stream <em>Stream</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Stream</em>'.
   * @see org.eclipse.oomph.setup.Stream
   * @generated
   */
  EClass getStream();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.Stream#getProject <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Project</em>'.
   * @see org.eclipse.oomph.setup.Stream#getProject()
   * @see #getStream()
   * @generated
   */
  EReference getStream_Project();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Installation <em>Installation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Installation</em>'.
   * @see org.eclipse.oomph.setup.Installation
   * @generated
   */
  EClass getInstallation();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.oomph.setup.Installation#getProductVersion <em>Product Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Product Version</em>'.
   * @see org.eclipse.oomph.setup.Installation#getProductVersion()
   * @see #getInstallation()
   * @generated
   */
  EReference getInstallation_ProductVersion();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ProductCatalog <em>Product Catalog</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Product Catalog</em>'.
   * @see org.eclipse.oomph.setup.ProductCatalog
   * @generated
   */
  EClass getProductCatalog();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.ProductCatalog#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Index</em>'.
   * @see org.eclipse.oomph.setup.ProductCatalog#getIndex()
   * @see #getProductCatalog()
   * @generated
   */
  EReference getProductCatalog_Index();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.ProductCatalog#getProducts <em>Products</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Products</em>'.
   * @see org.eclipse.oomph.setup.ProductCatalog#getProducts()
   * @see #getProductCatalog()
   * @generated
   */
  EReference getProductCatalog_Products();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Product <em>Product</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Product</em>'.
   * @see org.eclipse.oomph.setup.Product
   * @generated
   */
  EClass getProduct();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.Product#getProductCatalog <em>Product Catalog</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Product Catalog</em>'.
   * @see org.eclipse.oomph.setup.Product#getProductCatalog()
   * @see #getProduct()
   * @generated
   */
  EReference getProduct_ProductCatalog();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.Product#getVersions <em>Versions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Versions</em>'.
   * @see org.eclipse.oomph.setup.Product#getVersions()
   * @see #getProduct()
   * @generated
   */
  EReference getProduct_Versions();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ProductVersion <em>Product Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Product Version</em>'.
   * @see org.eclipse.oomph.setup.ProductVersion
   * @generated
   */
  EClass getProductVersion();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.ProductVersion#getProduct <em>Product</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Product</em>'.
   * @see org.eclipse.oomph.setup.ProductVersion#getProduct()
   * @see #getProductVersion()
   * @generated
   */
  EReference getProductVersion_Product();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ProjectCatalog <em>Project Catalog</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project Catalog</em>'.
   * @see org.eclipse.oomph.setup.ProjectCatalog
   * @generated
   */
  EClass getProjectCatalog();

  /**
   * Returns the meta object for the container reference '{@link org.eclipse.oomph.setup.ProjectCatalog#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the container reference '<em>Index</em>'.
   * @see org.eclipse.oomph.setup.ProjectCatalog#getIndex()
   * @see #getProjectCatalog()
   * @generated
   */
  EReference getProjectCatalog_Index();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.InstallationTask <em>Installation Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Installation Task</em>'.
   * @see org.eclipse.oomph.setup.InstallationTask
   * @generated
   */
  EClass getInstallationTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.InstallationTask#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.setup.InstallationTask#getLocation()
   * @see #getInstallationTask()
   * @generated
   */
  EAttribute getInstallationTask_Location();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.WorkspaceTask <em>Workspace Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Workspace Task</em>'.
   * @see org.eclipse.oomph.setup.WorkspaceTask
   * @generated
   */
  EClass getWorkspaceTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.WorkspaceTask#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.oomph.setup.WorkspaceTask#getLocation()
   * @see #getWorkspaceTask()
   * @generated
   */
  EAttribute getWorkspaceTask_Location();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.CompoundTask <em>Compound Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Compound Task</em>'.
   * @see org.eclipse.oomph.setup.CompoundTask
   * @generated
   */
  EClass getCompoundTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.CompoundTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.CompoundTask#getName()
   * @see #getCompoundTask()
   * @generated
   */
  EAttribute getCompoundTask_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.VariableTask <em>Variable Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable Task</em>'.
   * @see org.eclipse.oomph.setup.VariableTask
   * @generated
   */
  EClass getVariableTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getType()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_Type();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getName()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getValue()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_Value();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#isStorePromptedValue <em>Store Prompted Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Store Prompted Value</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#isStorePromptedValue()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_StorePromptedValue();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getLabel()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_Label();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.VariableTask#getChoices <em>Choices</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Choices</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getChoices()
   * @see #getVariableTask()
   * @generated
   */
  EReference getVariableTask_Choices();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableTask#getDefault <em>Default</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default</em>'.
   * @see org.eclipse.oomph.setup.VariableTask#getDefault()
   * @see #getVariableTask()
   * @generated
   */
  EAttribute getVariableTask_Default();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.SetupTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Task</em>'.
   * @see org.eclipse.oomph.setup.SetupTask
   * @generated
   */
  EClass getSetupTask();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.SetupTask#getPredecessors <em>Predecessors</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Predecessors</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getPredecessors()
   * @see #getSetupTask()
   * @generated
   */
  EReference getSetupTask_Predecessors();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.SetupTask#getSuccessors <em>Successors</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Successors</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getSuccessors()
   * @see #getSetupTask()
   * @generated
   */
  EReference getSetupTask_Successors();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.SetupTask#getID <em>ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>ID</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getID()
   * @see #getSetupTask()
   * @generated
   */
  EAttribute getSetupTask_ID();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.SetupTask#getScopeType <em>Scope Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Scope Type</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getScopeType()
   * @see #getSetupTask()
   * @generated
   */
  EAttribute getSetupTask_ScopeType();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.SetupTask#getRestrictions <em>Restrictions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Restrictions</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getRestrictions()
   * @see #getSetupTask()
   * @generated
   */
  EReference getSetupTask_Restrictions();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.SetupTask#getExcludedTriggers <em>Excluded Triggers</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Excluded Triggers</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getExcludedTriggers()
   * @see #getSetupTask()
   * @generated
   */
  EAttribute getSetupTask_ExcludedTriggers();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.SetupTask#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#getDescription()
   * @see #getSetupTask()
   * @generated
   */
  EAttribute getSetupTask_Description();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.SetupTask#isDisabled <em>Disabled</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Disabled</em>'.
   * @see org.eclipse.oomph.setup.SetupTask#isDisabled()
   * @see #getSetupTask()
   * @generated
   */
  EAttribute getSetupTask_Disabled();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ResourceCopyTask <em>Resource Copy Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Resource Copy Task</em>'.
   * @see org.eclipse.oomph.setup.ResourceCopyTask
   * @generated
   */
  EClass getResourceCopyTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.ResourceCopyTask#getSourceURL <em>Source URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Source URL</em>'.
   * @see org.eclipse.oomph.setup.ResourceCopyTask#getSourceURL()
   * @see #getResourceCopyTask()
   * @generated
   */
  EAttribute getResourceCopyTask_SourceURL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.ResourceCopyTask#getTargetURL <em>Target URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target URL</em>'.
   * @see org.eclipse.oomph.setup.ResourceCopyTask#getTargetURL()
   * @see #getResourceCopyTask()
   * @generated
   */
  EAttribute getResourceCopyTask_TargetURL();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.TextModifyTask <em>Text Modify Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Text Modify Task</em>'.
   * @see org.eclipse.oomph.setup.TextModifyTask
   * @generated
   */
  EClass getTextModifyTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.TextModifyTask#getURL <em>URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URL</em>'.
   * @see org.eclipse.oomph.setup.TextModifyTask#getURL()
   * @see #getTextModifyTask()
   * @generated
   */
  EAttribute getTextModifyTask_URL();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.TextModifyTask#getModifications <em>Modifications</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Modifications</em>'.
   * @see org.eclipse.oomph.setup.TextModifyTask#getModifications()
   * @see #getTextModifyTask()
   * @generated
   */
  EReference getTextModifyTask_Modifications();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.TextModifyTask#getEncoding <em>Encoding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Encoding</em>'.
   * @see org.eclipse.oomph.setup.TextModifyTask#getEncoding()
   * @see #getTextModifyTask()
   * @generated
   */
  EAttribute getTextModifyTask_Encoding();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.TextModification <em>Text Modification</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Text Modification</em>'.
   * @see org.eclipse.oomph.setup.TextModification
   * @generated
   */
  EClass getTextModification();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.TextModification#getPattern <em>Pattern</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pattern</em>'.
   * @see org.eclipse.oomph.setup.TextModification#getPattern()
   * @see #getTextModification()
   * @generated
   */
  EAttribute getTextModification_Pattern();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.setup.TextModification#getSubstitutions <em>Substitutions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Substitutions</em>'.
   * @see org.eclipse.oomph.setup.TextModification#getSubstitutions()
   * @see #getTextModification()
   * @generated
   */
  EAttribute getTextModification_Substitutions();

  /**
   * Returns the meta object for class '{@link java.util.Map.Entry <em>Product To Product Version Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Product To Product Version Map Entry</em>'.
   * @see java.util.Map.Entry
   * @model keyType="org.eclipse.oomph.setup.Product" keyRequired="true"
   *        keyExtendedMetaData="kind='attribute'"
   *        valueType="org.eclipse.oomph.setup.ProductVersion" valueRequired="true"
   *        valueExtendedMetaData="kind='attribute'"
   * @generated
   */
  EClass getProductToProductVersionMapEntry();

  /**
   * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Key</em>'.
   * @see java.util.Map.Entry
   * @see #getProductToProductVersionMapEntry()
   * @generated
   */
  EReference getProductToProductVersionMapEntry_Key();

  /**
   * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Value</em>'.
   * @see java.util.Map.Entry
   * @see #getProductToProductVersionMapEntry()
   * @generated
   */
  EReference getProductToProductVersionMapEntry_Value();

  /**
   * Returns the meta object for class '{@link java.util.Map.Entry <em>Project To Stream Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project To Stream Map Entry</em>'.
   * @see java.util.Map.Entry
   * @model keyType="org.eclipse.oomph.setup.Project" keyRequired="true"
   *        keyExtendedMetaData="kind='attribute'"
   *        valueType="org.eclipse.oomph.setup.Stream" valueRequired="true"
   *        valueExtendedMetaData="kind='attribute'"
   * @generated
   */
  EClass getProjectToStreamMapEntry();

  /**
   * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Key</em>'.
   * @see java.util.Map.Entry
   * @see #getProjectToStreamMapEntry()
   * @generated
   */
  EReference getProjectToStreamMapEntry_Key();

  /**
   * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Value</em>'.
   * @see java.util.Map.Entry
   * @see #getProjectToStreamMapEntry()
   * @generated
   */
  EReference getProjectToStreamMapEntry_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.StringSubstitutionTask <em>String Substitution Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>String Substitution Task</em>'.
   * @see org.eclipse.oomph.setup.StringSubstitutionTask
   * @generated
   */
  EClass getStringSubstitutionTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.StringSubstitutionTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.StringSubstitutionTask#getName()
   * @see #getStringSubstitutionTask()
   * @generated
   */
  EAttribute getStringSubstitutionTask_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.StringSubstitutionTask#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.StringSubstitutionTask#getValue()
   * @see #getStringSubstitutionTask()
   * @generated
   */
  EAttribute getStringSubstitutionTask_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ProjectContainer <em>Project Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Project Container</em>'.
   * @see org.eclipse.oomph.setup.ProjectContainer
   * @generated
   */
  EClass getProjectContainer();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.ProjectContainer#getProjects <em>Projects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Projects</em>'.
   * @see org.eclipse.oomph.setup.ProjectContainer#getProjects()
   * @see #getProjectContainer()
   * @generated
   */
  EReference getProjectContainer_Projects();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.ScopeType <em>Scope Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Scope Type</em>'.
   * @see org.eclipse.oomph.setup.ScopeType
   * @generated
   */
  EEnum getScopeType();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.AttributeRule <em>Attribute Rule</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attribute Rule</em>'.
   * @see org.eclipse.oomph.setup.AttributeRule
   * @generated
   */
  EClass getAttributeRule();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.AttributeRule#getAttributeURI <em>Attribute URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Attribute URI</em>'.
   * @see org.eclipse.oomph.setup.AttributeRule#getAttributeURI()
   * @see #getAttributeRule()
   * @generated
   */
  EAttribute getAttributeRule_AttributeURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.AttributeRule#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.AttributeRule#getValue()
   * @see #getAttributeRule()
   * @generated
   */
  EAttribute getAttributeRule_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.RedirectionTask <em>Redirection Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Redirection Task</em>'.
   * @see org.eclipse.oomph.setup.RedirectionTask
   * @generated
   */
  EClass getRedirectionTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.RedirectionTask#getSourceURL <em>Source URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Source URL</em>'.
   * @see org.eclipse.oomph.setup.RedirectionTask#getSourceURL()
   * @see #getRedirectionTask()
   * @generated
   */
  EAttribute getRedirectionTask_SourceURL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.RedirectionTask#getTargetURL <em>Target URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target URL</em>'.
   * @see org.eclipse.oomph.setup.RedirectionTask#getTargetURL()
   * @see #getRedirectionTask()
   * @generated
   */
  EAttribute getRedirectionTask_TargetURL();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.VariableChoice <em>Variable Choice</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Variable Choice</em>'.
   * @see org.eclipse.oomph.setup.VariableChoice
   * @generated
   */
  EClass getVariableChoice();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableChoice#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.VariableChoice#getValue()
   * @see #getVariableChoice()
   * @generated
   */
  EAttribute getVariableChoice_Value();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.VariableChoice#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.eclipse.oomph.setup.VariableChoice#getLabel()
   * @see #getVariableChoice()
   * @generated
   */
  EAttribute getVariableChoice_Label();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.ResourceCreationTask <em>Resource Creation Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Resource Creation Task</em>'.
   * @see org.eclipse.oomph.setup.ResourceCreationTask
   * @generated
   */
  EClass getResourceCreationTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.ResourceCreationTask#getContent <em>Content</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Content</em>'.
   * @see org.eclipse.oomph.setup.ResourceCreationTask#getContent()
   * @see #getResourceCreationTask()
   * @generated
   */
  EAttribute getResourceCreationTask_Content();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.ResourceCreationTask#getTargetURL <em>Target URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target URL</em>'.
   * @see org.eclipse.oomph.setup.ResourceCreationTask#getTargetURL()
   * @see #getResourceCreationTask()
   * @generated
   */
  EAttribute getResourceCreationTask_TargetURL();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.ResourceCreationTask#getEncoding <em>Encoding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Encoding</em>'.
   * @see org.eclipse.oomph.setup.ResourceCreationTask#getEncoding()
   * @see #getResourceCreationTask()
   * @generated
   */
  EAttribute getResourceCreationTask_Encoding();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.EclipseIniTask <em>Eclipse Ini Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Eclipse Ini Task</em>'.
   * @see org.eclipse.oomph.setup.EclipseIniTask
   * @generated
   */
  EClass getEclipseIniTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.EclipseIniTask#getOption <em>Option</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Option</em>'.
   * @see org.eclipse.oomph.setup.EclipseIniTask#getOption()
   * @see #getEclipseIniTask()
   * @generated
   */
  EAttribute getEclipseIniTask_Option();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.EclipseIniTask#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.EclipseIniTask#getValue()
   * @see #getEclipseIniTask()
   * @generated
   */
  EAttribute getEclipseIniTask_Value();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.EclipseIniTask#isVm <em>Vm</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Vm</em>'.
   * @see org.eclipse.oomph.setup.EclipseIniTask#isVm()
   * @see #getEclipseIniTask()
   * @generated
   */
  EAttribute getEclipseIniTask_Vm();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.Trigger <em>Trigger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Trigger</em>'.
   * @see org.eclipse.oomph.setup.Trigger
   * @generated
   */
  EEnum getTrigger();

  /**
   * Returns the meta object for enum '{@link org.eclipse.oomph.setup.VariableType <em>Variable Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Variable Type</em>'.
   * @see org.eclipse.oomph.setup.VariableType
   * @generated
   */
  EEnum getVariableType();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.User <em>User</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>User</em>'.
   * @see org.eclipse.oomph.setup.User
   * @generated
   */
  EClass getUser();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.User#getAttributeRules <em>Attribute Rules</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Attribute Rules</em>'.
   * @see org.eclipse.oomph.setup.User#getAttributeRules()
   * @see #getUser()
   * @generated
   */
  EReference getUser_AttributeRules();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.oomph.setup.User#getAcceptedLicenses <em>Accepted Licenses</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Accepted Licenses</em>'.
   * @see org.eclipse.oomph.setup.User#getAcceptedLicenses()
   * @see #getUser()
   * @generated
   */
  EAttribute getUser_AcceptedLicenses();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Workspace <em>Workspace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Workspace</em>'.
   * @see org.eclipse.oomph.setup.Workspace
   * @generated
   */
  EClass getWorkspace();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.oomph.setup.Workspace#getStreams <em>Streams</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Streams</em>'.
   * @see org.eclipse.oomph.setup.Workspace#getStreams()
   * @see #getWorkspace()
   * @generated
   */
  EReference getWorkspace_Streams();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.LinkLocationTask <em>Link Location Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Link Location Task</em>'.
   * @see org.eclipse.oomph.setup.LinkLocationTask
   * @generated
   */
  EClass getLinkLocationTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.LinkLocationTask#getPath <em>Path</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Path</em>'.
   * @see org.eclipse.oomph.setup.LinkLocationTask#getPath()
   * @see #getLinkLocationTask()
   * @generated
   */
  EAttribute getLinkLocationTask_Path();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.LinkLocationTask#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.LinkLocationTask#getName()
   * @see #getLinkLocationTask()
   * @generated
   */
  EAttribute getLinkLocationTask_Name();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.PreferenceTask <em>Preference Task</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Preference Task</em>'.
   * @see org.eclipse.oomph.setup.PreferenceTask
   * @generated
   */
  EClass getPreferenceTask();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.PreferenceTask#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see org.eclipse.oomph.setup.PreferenceTask#getKey()
   * @see #getPreferenceTask()
   * @generated
   */
  EAttribute getPreferenceTask_Key();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.PreferenceTask#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see org.eclipse.oomph.setup.PreferenceTask#getValue()
   * @see #getPreferenceTask()
   * @generated
   */
  EAttribute getPreferenceTask_Value();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.SetupTaskContainer <em>Task Container</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Task Container</em>'.
   * @see org.eclipse.oomph.setup.SetupTaskContainer
   * @generated
   */
  EClass getSetupTaskContainer();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.oomph.setup.SetupTaskContainer#getSetupTasks <em>Setup Tasks</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Setup Tasks</em>'.
   * @see org.eclipse.oomph.setup.SetupTaskContainer#getSetupTasks()
   * @see #getSetupTaskContainer()
   * @generated
   */
  EReference getSetupTaskContainer_SetupTasks();

  /**
   * Returns the meta object for class '{@link org.eclipse.oomph.setup.Scope <em>Scope</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Scope</em>'.
   * @see org.eclipse.oomph.setup.Scope
   * @generated
   */
  EClass getScope();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.Scope#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.oomph.setup.Scope#getName()
   * @see #getScope()
   * @generated
   */
  EAttribute getScope_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.Scope#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.eclipse.oomph.setup.Scope#getLabel()
   * @see #getScope()
   * @generated
   */
  EAttribute getScope_Label();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.oomph.setup.Scope#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see org.eclipse.oomph.setup.Scope#getDescription()
   * @see #getScope()
   * @generated
   */
  EAttribute getScope_Description();

  /**
   * Returns the meta object for data type '{@link org.eclipse.oomph.setup.LicenseInfo <em>License Info</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>License Info</em>'.
   * @see org.eclipse.oomph.setup.LicenseInfo
   * @model instanceClass="org.eclipse.oomph.setup.LicenseInfo"
   * @generated
   */
  EDataType getLicenseInfo();

  /**
   * Returns the meta object for data type '{@link java.util.Set <em>Trigger Set</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Trigger Set</em>'.
   * @see java.util.Set
   * @model instanceClass="java.util.Set<org.eclipse.oomph.setup.Trigger>"
   * @generated
   */
  EDataType getTriggerSet();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SetupFactory getSetupFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.IndexImpl <em>Index</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.IndexImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getIndex()
     * @generated
     */
    EClass INDEX = eINSTANCE.getIndex();

    /**
     * The meta object literal for the '<em><b>Project Catalogs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INDEX__PROJECT_CATALOGS = eINSTANCE.getIndex_ProjectCatalogs();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.CatalogSelectionImpl <em>Catalog Selection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.CatalogSelectionImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getCatalogSelection()
     * @generated
     */
    EClass CATALOG_SELECTION = eINSTANCE.getCatalogSelection();

    /**
     * The meta object literal for the '<em><b>Product Catalogs</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CATALOG_SELECTION__PRODUCT_CATALOGS = eINSTANCE.getCatalogSelection_ProductCatalogs();

    /**
     * The meta object literal for the '<em><b>Project Catalogs</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CATALOG_SELECTION__PROJECT_CATALOGS = eINSTANCE.getCatalogSelection_ProjectCatalogs();

    /**
     * The meta object literal for the '<em><b>Default Product Versions</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS = eINSTANCE.getCatalogSelection_DefaultProductVersions();

    /**
     * The meta object literal for the '<em><b>Default Streams</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference CATALOG_SELECTION__DEFAULT_STREAMS = eINSTANCE.getCatalogSelection_DefaultStreams();

    /**
     * The meta object literal for the '<em><b>Product Catalogs</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INDEX__PRODUCT_CATALOGS = eINSTANCE.getIndex_ProductCatalogs();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProjectImpl <em>Project</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProjectImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProject()
     * @generated
     */
    EClass PROJECT = eINSTANCE.getProject();

    /**
     * The meta object literal for the '<em><b>Streams</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__STREAMS = eINSTANCE.getProject_Streams();

    /**
     * The meta object literal for the '<em><b>Project Container</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__PROJECT_CONTAINER = eINSTANCE.getProject_ProjectContainer();

    /**
     * The meta object literal for the '<em><b>Logical Project Container</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__LOGICAL_PROJECT_CONTAINER = eINSTANCE.getProject_LogicalProjectContainer();

    /**
     * The meta object literal for the '<em><b>Parent Project</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__PARENT_PROJECT = eINSTANCE.getProject_ParentProject();

    /**
     * The meta object literal for the '<em><b>Project Catalog</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT__PROJECT_CATALOG = eINSTANCE.getProject_ProjectCatalog();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.StreamImpl <em>Stream</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.StreamImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getStream()
     * @generated
     */
    EClass STREAM = eINSTANCE.getStream();

    /**
     * The meta object literal for the '<em><b>Project</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference STREAM__PROJECT = eINSTANCE.getStream_Project();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.InstallationImpl <em>Installation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.InstallationImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getInstallation()
     * @generated
     */
    EClass INSTALLATION = eINSTANCE.getInstallation();

    /**
     * The meta object literal for the '<em><b>Product Version</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference INSTALLATION__PRODUCT_VERSION = eINSTANCE.getInstallation_ProductVersion();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProductCatalogImpl <em>Product Catalog</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProductCatalogImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductCatalog()
     * @generated
     */
    EClass PRODUCT_CATALOG = eINSTANCE.getProductCatalog();

    /**
     * The meta object literal for the '<em><b>Index</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT_CATALOG__INDEX = eINSTANCE.getProductCatalog_Index();

    /**
     * The meta object literal for the '<em><b>Products</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT_CATALOG__PRODUCTS = eINSTANCE.getProductCatalog_Products();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProductImpl <em>Product</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProductImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProduct()
     * @generated
     */
    EClass PRODUCT = eINSTANCE.getProduct();

    /**
     * The meta object literal for the '<em><b>Product Catalog</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT__PRODUCT_CATALOG = eINSTANCE.getProduct_ProductCatalog();

    /**
     * The meta object literal for the '<em><b>Versions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT__VERSIONS = eINSTANCE.getProduct_Versions();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProductVersionImpl <em>Product Version</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProductVersionImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductVersion()
     * @generated
     */
    EClass PRODUCT_VERSION = eINSTANCE.getProductVersion();

    /**
     * The meta object literal for the '<em><b>Product</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT_VERSION__PRODUCT = eINSTANCE.getProductVersion_Product();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProjectCatalogImpl <em>Project Catalog</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProjectCatalogImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectCatalog()
     * @generated
     */
    EClass PROJECT_CATALOG = eINSTANCE.getProjectCatalog();

    /**
     * The meta object literal for the '<em><b>Index</b></em>' container reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT_CATALOG__INDEX = eINSTANCE.getProjectCatalog_Index();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.InstallationTaskImpl <em>Installation Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.InstallationTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getInstallationTask()
     * @generated
     */
    EClass INSTALLATION_TASK = eINSTANCE.getInstallationTask();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INSTALLATION_TASK__LOCATION = eINSTANCE.getInstallationTask_Location();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.WorkspaceTaskImpl <em>Workspace Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.WorkspaceTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getWorkspaceTask()
     * @generated
     */
    EClass WORKSPACE_TASK = eINSTANCE.getWorkspaceTask();

    /**
     * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute WORKSPACE_TASK__LOCATION = eINSTANCE.getWorkspaceTask_Location();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.CompoundTaskImpl <em>Compound Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.CompoundTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getCompoundTask()
     * @generated
     */
    EClass COMPOUND_TASK = eINSTANCE.getCompoundTask();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute COMPOUND_TASK__NAME = eINSTANCE.getCompoundTask_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.VariableTaskImpl <em>Variable Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.VariableTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableTask()
     * @generated
     */
    EClass VARIABLE_TASK = eINSTANCE.getVariableTask();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__TYPE = eINSTANCE.getVariableTask_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__NAME = eINSTANCE.getVariableTask_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__VALUE = eINSTANCE.getVariableTask_Value();

    /**
     * The meta object literal for the '<em><b>Store Prompted Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__STORE_PROMPTED_VALUE = eINSTANCE.getVariableTask_StorePromptedValue();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__LABEL = eINSTANCE.getVariableTask_Label();

    /**
     * The meta object literal for the '<em><b>Choices</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference VARIABLE_TASK__CHOICES = eINSTANCE.getVariableTask_Choices();

    /**
     * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_TASK__DEFAULT = eINSTANCE.getVariableTask_Default();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.SetupTaskImpl <em>Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.SetupTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getSetupTask()
     * @generated
     */
    EClass SETUP_TASK = eINSTANCE.getSetupTask();

    /**
     * The meta object literal for the '<em><b>Predecessors</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SETUP_TASK__PREDECESSORS = eINSTANCE.getSetupTask_Predecessors();

    /**
     * The meta object literal for the '<em><b>Successors</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SETUP_TASK__SUCCESSORS = eINSTANCE.getSetupTask_Successors();

    /**
     * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SETUP_TASK__ID = eINSTANCE.getSetupTask_ID();

    /**
     * The meta object literal for the '<em><b>Scope Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SETUP_TASK__SCOPE_TYPE = eINSTANCE.getSetupTask_ScopeType();

    /**
     * The meta object literal for the '<em><b>Restrictions</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SETUP_TASK__RESTRICTIONS = eINSTANCE.getSetupTask_Restrictions();

    /**
     * The meta object literal for the '<em><b>Excluded Triggers</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SETUP_TASK__EXCLUDED_TRIGGERS = eINSTANCE.getSetupTask_ExcludedTriggers();

    /**
     * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SETUP_TASK__DESCRIPTION = eINSTANCE.getSetupTask_Description();

    /**
     * The meta object literal for the '<em><b>Disabled</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SETUP_TASK__DISABLED = eINSTANCE.getSetupTask_Disabled();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl <em>Resource Copy Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ResourceCopyTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getResourceCopyTask()
     * @generated
     */
    EClass RESOURCE_COPY_TASK = eINSTANCE.getResourceCopyTask();

    /**
     * The meta object literal for the '<em><b>Source URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_COPY_TASK__SOURCE_URL = eINSTANCE.getResourceCopyTask_SourceURL();

    /**
     * The meta object literal for the '<em><b>Target URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_COPY_TASK__TARGET_URL = eINSTANCE.getResourceCopyTask_TargetURL();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.TextModifyTaskImpl <em>Text Modify Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.TextModifyTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTextModifyTask()
     * @generated
     */
    EClass TEXT_MODIFY_TASK = eINSTANCE.getTextModifyTask();

    /**
     * The meta object literal for the '<em><b>URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TEXT_MODIFY_TASK__URL = eINSTANCE.getTextModifyTask_URL();

    /**
     * The meta object literal for the '<em><b>Modifications</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TEXT_MODIFY_TASK__MODIFICATIONS = eINSTANCE.getTextModifyTask_Modifications();

    /**
     * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TEXT_MODIFY_TASK__ENCODING = eINSTANCE.getTextModifyTask_Encoding();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.TextModificationImpl <em>Text Modification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.TextModificationImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTextModification()
     * @generated
     */
    EClass TEXT_MODIFICATION = eINSTANCE.getTextModification();

    /**
     * The meta object literal for the '<em><b>Pattern</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TEXT_MODIFICATION__PATTERN = eINSTANCE.getTextModification_Pattern();

    /**
     * The meta object literal for the '<em><b>Substitutions</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TEXT_MODIFICATION__SUBSTITUTIONS = eINSTANCE.getTextModification_Substitutions();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProductToProductVersionMapEntryImpl <em>Product To Product Version Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProductToProductVersionMapEntryImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProductToProductVersionMapEntry()
     * @generated
     */
    EClass PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY = eINSTANCE.getProductToProductVersionMapEntry();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY = eINSTANCE.getProductToProductVersionMapEntry_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE = eINSTANCE.getProductToProductVersionMapEntry_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ProjectToStreamMapEntryImpl <em>Project To Stream Map Entry</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ProjectToStreamMapEntryImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectToStreamMapEntry()
     * @generated
     */
    EClass PROJECT_TO_STREAM_MAP_ENTRY = eINSTANCE.getProjectToStreamMapEntry();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT_TO_STREAM_MAP_ENTRY__KEY = eINSTANCE.getProjectToStreamMapEntry_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT_TO_STREAM_MAP_ENTRY__VALUE = eINSTANCE.getProjectToStreamMapEntry_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.StringSubstitutionTaskImpl <em>String Substitution Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.StringSubstitutionTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getStringSubstitutionTask()
     * @generated
     */
    EClass STRING_SUBSTITUTION_TASK = eINSTANCE.getStringSubstitutionTask();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_SUBSTITUTION_TASK__NAME = eINSTANCE.getStringSubstitutionTask_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STRING_SUBSTITUTION_TASK__VALUE = eINSTANCE.getStringSubstitutionTask_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.ProjectContainer <em>Project Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.ProjectContainer
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getProjectContainer()
     * @generated
     */
    EClass PROJECT_CONTAINER = eINSTANCE.getProjectContainer();

    /**
     * The meta object literal for the '<em><b>Projects</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PROJECT_CONTAINER__PROJECTS = eINSTANCE.getProjectContainer_Projects();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.ScopeType <em>Scope Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.ScopeType
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getScopeType()
     * @generated
     */
    EEnum SCOPE_TYPE = eINSTANCE.getScopeType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.AttributeRuleImpl <em>Attribute Rule</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.AttributeRuleImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getAttributeRule()
     * @generated
     */
    EClass ATTRIBUTE_RULE = eINSTANCE.getAttributeRule();

    /**
     * The meta object literal for the '<em><b>Attribute URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_RULE__ATTRIBUTE_URI = eINSTANCE.getAttributeRule_AttributeURI();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_RULE__VALUE = eINSTANCE.getAttributeRule_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.RedirectionTaskImpl <em>Redirection Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.RedirectionTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getRedirectionTask()
     * @generated
     */
    EClass REDIRECTION_TASK = eINSTANCE.getRedirectionTask();

    /**
     * The meta object literal for the '<em><b>Source URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REDIRECTION_TASK__SOURCE_URL = eINSTANCE.getRedirectionTask_SourceURL();

    /**
     * The meta object literal for the '<em><b>Target URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REDIRECTION_TASK__TARGET_URL = eINSTANCE.getRedirectionTask_TargetURL();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.VariableChoiceImpl <em>Variable Choice</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.VariableChoiceImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableChoice()
     * @generated
     */
    EClass VARIABLE_CHOICE = eINSTANCE.getVariableChoice();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_CHOICE__VALUE = eINSTANCE.getVariableChoice_Value();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute VARIABLE_CHOICE__LABEL = eINSTANCE.getVariableChoice_Label();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl <em>Resource Creation Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ResourceCreationTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getResourceCreationTask()
     * @generated
     */
    EClass RESOURCE_CREATION_TASK = eINSTANCE.getResourceCreationTask();

    /**
     * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_CREATION_TASK__CONTENT = eINSTANCE.getResourceCreationTask_Content();

    /**
     * The meta object literal for the '<em><b>Target URL</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_CREATION_TASK__TARGET_URL = eINSTANCE.getResourceCreationTask_TargetURL();

    /**
     * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RESOURCE_CREATION_TASK__ENCODING = eINSTANCE.getResourceCreationTask_Encoding();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl <em>Eclipse Ini Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.EclipseIniTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getEclipseIniTask()
     * @generated
     */
    EClass ECLIPSE_INI_TASK = eINSTANCE.getEclipseIniTask();

    /**
     * The meta object literal for the '<em><b>Option</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECLIPSE_INI_TASK__OPTION = eINSTANCE.getEclipseIniTask_Option();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECLIPSE_INI_TASK__VALUE = eINSTANCE.getEclipseIniTask_Value();

    /**
     * The meta object literal for the '<em><b>Vm</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ECLIPSE_INI_TASK__VM = eINSTANCE.getEclipseIniTask_Vm();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.Trigger <em>Trigger</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.Trigger
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTrigger()
     * @generated
     */
    EEnum TRIGGER = eINSTANCE.getTrigger();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.VariableType <em>Variable Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.VariableType
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getVariableType()
     * @generated
     */
    EEnum VARIABLE_TYPE = eINSTANCE.getVariableType();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.UserImpl <em>User</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.UserImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getUser()
     * @generated
     */
    EClass USER = eINSTANCE.getUser();

    /**
     * The meta object literal for the '<em><b>Attribute Rules</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference USER__ATTRIBUTE_RULES = eINSTANCE.getUser_AttributeRules();

    /**
     * The meta object literal for the '<em><b>Accepted Licenses</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute USER__ACCEPTED_LICENSES = eINSTANCE.getUser_AcceptedLicenses();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.WorkspaceImpl <em>Workspace</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.WorkspaceImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getWorkspace()
     * @generated
     */
    EClass WORKSPACE = eINSTANCE.getWorkspace();

    /**
     * The meta object literal for the '<em><b>Streams</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference WORKSPACE__STREAMS = eINSTANCE.getWorkspace_Streams();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.LinkLocationTaskImpl <em>Link Location Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.LinkLocationTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getLinkLocationTask()
     * @generated
     */
    EClass LINK_LOCATION_TASK = eINSTANCE.getLinkLocationTask();

    /**
     * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LINK_LOCATION_TASK__PATH = eINSTANCE.getLinkLocationTask_Path();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute LINK_LOCATION_TASK__NAME = eINSTANCE.getLinkLocationTask_Name();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl <em>Preference Task</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.PreferenceTaskImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getPreferenceTask()
     * @generated
     */
    EClass PREFERENCE_TASK = eINSTANCE.getPreferenceTask();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_TASK__KEY = eINSTANCE.getPreferenceTask_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PREFERENCE_TASK__VALUE = eINSTANCE.getPreferenceTask_Value();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.SetupTaskContainerImpl <em>Task Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.SetupTaskContainerImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getSetupTaskContainer()
     * @generated
     */
    EClass SETUP_TASK_CONTAINER = eINSTANCE.getSetupTaskContainer();

    /**
     * The meta object literal for the '<em><b>Setup Tasks</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference SETUP_TASK_CONTAINER__SETUP_TASKS = eINSTANCE.getSetupTaskContainer_SetupTasks();

    /**
     * The meta object literal for the '{@link org.eclipse.oomph.setup.impl.ScopeImpl <em>Scope</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.impl.ScopeImpl
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getScope()
     * @generated
     */
    EClass SCOPE = eINSTANCE.getScope();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCOPE__NAME = eINSTANCE.getScope_Name();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCOPE__LABEL = eINSTANCE.getScope_Label();

    /**
     * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute SCOPE__DESCRIPTION = eINSTANCE.getScope_Description();

    /**
     * The meta object literal for the '<em>License Info</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.oomph.setup.LicenseInfo
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getLicenseInfo()
     * @generated
     */
    EDataType LICENSE_INFO = eINSTANCE.getLicenseInfo();

    /**
     * The meta object literal for the '<em>Trigger Set</em>' data type.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see java.util.Set
     * @see org.eclipse.oomph.setup.impl.SetupPackageImpl#getTriggerSet()
     * @generated
     */
    EDataType TRIGGER_SET = eINSTANCE.getTriggerSet();

  }

} // SetupPackage
