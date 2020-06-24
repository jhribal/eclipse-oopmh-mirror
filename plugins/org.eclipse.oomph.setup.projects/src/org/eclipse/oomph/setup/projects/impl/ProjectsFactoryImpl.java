/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.projects.impl;

import org.eclipse.oomph.setup.projects.PathVariableTask;
import org.eclipse.oomph.setup.projects.ProjectsBuildTask;
import org.eclipse.oomph.setup.projects.ProjectsFactory;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectsFactoryImpl extends EFactoryImpl implements ProjectsFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ProjectsFactory init()
  {
    try
    {
      ProjectsFactory theProjectsFactory = (ProjectsFactory)EPackage.Registry.INSTANCE.getEFactory(ProjectsPackage.eNS_URI);
      if (theProjectsFactory != null)
      {
        return theProjectsFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ProjectsFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case ProjectsPackage.PATH_VARIABLE_TASK:
        return createPathVariableTask();
      case ProjectsPackage.PROJECTS_IMPORT_TASK:
        return createProjectsImportTask();
      case ProjectsPackage.PROJECTS_BUILD_TASK:
        return createProjectsBuildTask();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsImportTask createProjectsImportTask()
  {
    ProjectsImportTaskImpl projectsImportTask = new ProjectsImportTaskImpl();
    return projectsImportTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsBuildTask createProjectsBuildTask()
  {
    ProjectsBuildTaskImpl projectsBuildTask = new ProjectsBuildTaskImpl();
    return projectsBuildTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PathVariableTask createPathVariableTask()
  {
    PathVariableTaskImpl pathVariableTask = new PathVariableTaskImpl();
    return pathVariableTask;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsPackage getProjectsPackage()
  {
    return (ProjectsPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ProjectsPackage getPackage()
  {
    return ProjectsPackage.eINSTANCE;
  }

} // ProjectsFactoryImpl
