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
package org.eclipse.oomph.setup.projects.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.projects.PathVariableTask;
import org.eclipse.oomph.setup.projects.ProjectsBuildTask;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;
import org.eclipse.oomph.setup.projects.ProjectsPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.projects.ProjectsPackage
 * @generated
 */
public class ProjectsSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ProjectsPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ProjectsPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case ProjectsPackage.PATH_VARIABLE_TASK:
      {
        PathVariableTask pathVariableTask = (PathVariableTask)theEObject;
        T result = casePathVariableTask(pathVariableTask);
        if (result == null)
        {
          result = caseSetupTask(pathVariableTask);
        }
        if (result == null)
        {
          result = caseModelElement(pathVariableTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ProjectsPackage.PROJECTS_IMPORT_TASK:
      {
        ProjectsImportTask projectsImportTask = (ProjectsImportTask)theEObject;
        T result = caseProjectsImportTask(projectsImportTask);
        if (result == null)
        {
          result = caseSetupTask(projectsImportTask);
        }
        if (result == null)
        {
          result = caseModelElement(projectsImportTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case ProjectsPackage.PROJECTS_BUILD_TASK:
      {
        ProjectsBuildTask projectsBuildTask = (ProjectsBuildTask)theEObject;
        T result = caseProjectsBuildTask(projectsBuildTask);
        if (result == null)
        {
          result = caseSetupTask(projectsBuildTask);
        }
        if (result == null)
        {
          result = caseModelElement(projectsBuildTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      default:
        return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Import Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Import Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectsImportTask(ProjectsImportTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Build Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Build Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectsBuildTask(ProjectsBuildTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Path Variable Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Path Variable Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePathVariableTask(PathVariableTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelElement(ModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSetupTask(SetupTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} // ProjectsSwitch
