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
package org.eclipse.oomph.setup.workingsets.impl;

import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage;
import org.eclipse.oomph.setup.workingsets.WorkingSetTask;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.util.WorkingSetsUtil;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Set Working Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workingsets.impl.WorkingSetTaskImpl#getWorkingSets <em>Working Sets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WorkingSetTaskImpl extends SetupTaskImpl implements WorkingSetTask
{
  /**
   * The cached value of the '{@link #getWorkingSets() <em>Working Sets</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWorkingSets()
   * @generated
   * @ordered
   */
  protected EList<WorkingSet> workingSets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WorkingSetTaskImpl()
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
    return SetupWorkingSetsPackage.Literals.WORKING_SET_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<WorkingSet> getWorkingSets()
  {
    if (workingSets == null)
    {
      workingSets = new EObjectContainmentEList<WorkingSet>(WorkingSet.class, this, SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS);
    }
    return workingSets;
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return ((InternalEList<?>)getWorkingSets()).basicRemove(otherEnd, msgs);
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return getWorkingSets();
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        getWorkingSets().clear();
        getWorkingSets().addAll((Collection<? extends WorkingSet>)newValue);
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        getWorkingSets().clear();
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return workingSets != null && !workingSets.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    WorkingSetGroup defaultWorkingSetGroup = WorkingSetsUtil.getWorkingSetGroup();
    Map<String, WorkingSet> existingWorkingSets = new HashMap<String, WorkingSet>();
    for (WorkingSet workingSet : defaultWorkingSetGroup.getWorkingSets())
    {
      existingWorkingSets.put(workingSet.getID(), workingSet);
    }

    String prefix = "";
    for (EObject eContainer = eContainer(); eContainer != null; eContainer = eContainer.eContainer())
    {
      if (eContainer instanceof Project)
      {
        prefix += ((Project)eContainer).getName() + " ";
      }
      else if (eContainer instanceof Stream)
      {
        prefix += ((Stream)eContainer).getName() + " ";
      }
      else if (eContainer instanceof User)
      {
        prefix += "<user> ";
      }
    }

    for (WorkingSet workingSet : getWorkingSets())
    {
      String id = prefix + workingSet.getName();
      workingSet.setID(id);
    }

    for (WorkingSet workingSet : getWorkingSets())
    {
      context.checkCancelation();

      WorkingSet existingWorkingSet = existingWorkingSets.put(workingSet.getID(), workingSet);
      if (existingWorkingSet == null || !EcoreUtil.equals(workingSet.getPredicates(), existingWorkingSet.getPredicates()))
      {
        return true;
      }
    }

    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    WorkingSetGroup defaultWorkingSetGroup = WorkingSetsUtil.getWorkingSetGroup();
    Map<String, WorkingSet> existingIds = new HashMap<String, WorkingSet>();
    EList<WorkingSet> workingSets = defaultWorkingSetGroup.getWorkingSets();
    for (WorkingSet workingSet : workingSets)
    {
      existingIds.put(workingSet.getID(), workingSet);
    }

    EList<WorkingSet> newWorkingSetGroups = getWorkingSets();
    int index = 0;
    for (WorkingSet workingSet : new ArrayList<WorkingSet>(newWorkingSetGroups))
    {
      context.checkCancelation();

      String id = workingSet.getID();
      WorkingSet existingWorkingSet = existingIds.put(id, workingSet);
      if (existingWorkingSet == null)
      {
        workingSets.add(index++, workingSet);
      }
      else
      {
        index = workingSets.indexOf(existingWorkingSet);
        workingSets.set(index, workingSet);
        ++index;
      }
    }

    Resource resource = defaultWorkingSetGroup.eResource();
    resource.save(null);
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_LATE;
  }
} // SetWorkingTaskImpl
