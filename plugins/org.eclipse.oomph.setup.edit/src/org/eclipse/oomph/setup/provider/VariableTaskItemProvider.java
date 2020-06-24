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
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.VariableTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VariableTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VariableTaskItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

      addTypePropertyDescriptor(object);
      addNamePropertyDescriptor(object);
      addValuePropertyDescriptor(object);
      addDefaultValuePropertyDescriptor(object);
      addStorageURIPropertyDescriptor(object);
      addLabelPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  @Override
  protected void addPredecessorsPropertyDescriptor(Object object)
  {
    // This property isn't sensible for variables.
  }

  @Override
  protected void addSuccessorsPropertyDescriptor(Object object)
  {
    // This property isn't sensible for variables.
  }

  /**
   * This adds a property descriptor for the Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_type_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_type_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__TYPE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_name_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_name_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Value feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addValuePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_value_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_value_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__VALUE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Default Value feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDefaultValuePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_defaultValue_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_defaultValue_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__DEFAULT_VALUE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Label feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLabelPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_label_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_label_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__LABEL, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Storage URI feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addStorageURIPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_VariableTask_storageURI_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_VariableTask_storageURI_feature", "_UI_VariableTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        SetupPackage.Literals.VARIABLE_TASK__STORAGE_URI, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == SetupPackage.Literals.VARIABLE_TASK__STORAGE_URI)
    {
      VariableTask variableTask = (VariableTask)object;
      if (variableTask.getType() == VariableType.PASSWORD)
      {
        return null;
      }

      return Arrays.asList(new URI[] { null, VariableTask.DEFAULT_STORAGE_URI, VariableTask.INSTALLATION_STORAGE_URI, VariableTask.WORKSPACE_STORAGE_URI });
    }

    return super.filterChoices(choices, feature, object);
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(SetupPackage.Literals.VARIABLE_TASK__CHOICES);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * This returns VariableTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/VariableTask")); //$NON-NLS-1$
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean shouldComposeCreationImage()
  {
    return true;
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  @SuppressWarnings("nls")
  public String getText(Object object)
  {
    VariableTask variable = (VariableTask)object;
    String name = variable.getName();
    String value = variable.getValue();

    if (StringUtil.isEmpty(name))
    {
      return getString("_UI_VariableTask_type");
    }

    String label = "" + name;
    if (value != null)
    {
      if (value.length() == 0)
      {
        label += " = \"\"";
      }
      else
      {
        label += " = " + value;
      }
    }

    String defaultValue = variable.getDefaultValue();
    if (defaultValue != null)
    {
      if (defaultValue.length() == 0)
      {
        label += " (default: \"\")";
      }
      else
      {
        label += " (default: " + defaultValue + ")";
      }
    }

    return label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(VariableTask.class))
    {
      case SetupPackage.VARIABLE_TASK__TYPE:
      case SetupPackage.VARIABLE_TASK__NAME:
      case SetupPackage.VARIABLE_TASK__VALUE:
      case SetupPackage.VARIABLE_TASK__DEFAULT_VALUE:
      case SetupPackage.VARIABLE_TASK__STORAGE_URI:
      case SetupPackage.VARIABLE_TASK__LABEL:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SetupPackage.VARIABLE_TASK__CHOICES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors.add(createChildParameter(SetupPackage.Literals.VARIABLE_TASK__CHOICES, SetupFactory.eINSTANCE.createVariableChoice()));
  }

}
