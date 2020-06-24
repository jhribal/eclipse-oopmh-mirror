/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.provider;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.base.Annotation} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AnnotationItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AnnotationItemProvider(AdapterFactory adapterFactory)
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

      addSourcePropertyDescriptor(object);
      addReferencesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Source feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSourcePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Annotation_source_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Annotation_source_feature", "_UI_Annotation_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        BasePackage.Literals.ANNOTATION__SOURCE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the References feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addReferencesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Annotation_references_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Annotation_references_feature", "_UI_Annotation_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        BasePackage.Literals.ANNOTATION__REFERENCES, true, false, true, null, null, null));
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
      childrenFeatures.add(BasePackage.Literals.ANNOTATION__DETAILS);
      childrenFeatures.add(BasePackage.Literals.ANNOTATION__CONTENTS);
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
   * This returns Annotation.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Annotation")); //$NON-NLS-1$
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
  public String getText(Object object)
  {
    Annotation annotation = (Annotation)object;
    if (annotation.getSource() != null)
    {
      int index = getParent(annotation) instanceof EAnnotation ? -1 : annotation.getSource().lastIndexOf("/"); //$NON-NLS-1$
      if (index == -1)
      {
        return annotation.getSource();
      }

      return annotation.getSource().substring(index + 1);
    }

    return getString("_UI_Annotation_type"); //$NON-NLS-1$
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

    switch (notification.getFeatureID(Annotation.class))
    {
      case BasePackage.ANNOTATION__SOURCE:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case BasePackage.ANNOTATION__DETAILS:
      case BasePackage.ANNOTATION__CONTENTS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * The Annotation.contents feature is marked for child creation, but for now we disable it via the code.
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unused")
  private void collectNewChildDescriptorsGen(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors
        .add(createChildParameter(BasePackage.Literals.ANNOTATION__DETAILS, BaseFactory.eINSTANCE.create(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY)));

    newChildDescriptors.add(createChildParameter(BasePackage.Literals.ANNOTATION__CONTENTS, BaseFactory.eINSTANCE.createAnnotation()));

    newChildDescriptors
        .add(createChildParameter(BasePackage.Literals.ANNOTATION__CONTENTS, BaseFactory.eINSTANCE.create(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY)));

    newChildDescriptors.add(createChildParameter(BasePackage.Literals.ANNOTATION__CONTENTS, XMLTypeFactory.eINSTANCE.createAnyType()));
  }

  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);

    newChildDescriptors
        .add(createChildParameter(BasePackage.Literals.ANNOTATION__DETAILS, BaseFactory.eINSTANCE.create(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY)));
  }

  /**
   * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
   * <!-- begin-user-doc -->
   * The Annotation.contents feature is marked for child creation, but for now we disable it via the code.
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unused")
  private String getCreateChildTextGen(Object owner, Object feature, Object child, Collection<?> selection)
  {
    Object childFeature = feature;
    Object childObject = child;

    boolean qualify = childFeature == BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS || childFeature == BasePackage.Literals.ANNOTATION__CONTENTS
        || childFeature == BasePackage.Literals.ANNOTATION__DETAILS;

    if (qualify)
    {
      return getString("_UI_CreateChild_text2", //$NON-NLS-1$
          new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
    }
    return super.getCreateChildText(owner, feature, child, selection);
  }

  @Override
  protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    return new DragAndDropCommand(domain, owner, location, operations, operation, collection)
    {
      @Override
      protected boolean prepareDropLinkOn()
      {
        dragCommand = IdentityCommand.INSTANCE;
        dropCommand = AddCommand.create(domain, owner, BasePackage.Literals.ANNOTATION__REFERENCES, collection);
        boolean result = dropCommand.canExecute();
        return result;
      }
    };
  }

}
