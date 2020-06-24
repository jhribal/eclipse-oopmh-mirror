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
package org.eclipse.oomph.p2.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.RequirementType;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.p2.Requirement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RequirementItemProvider extends ModelElementItemProvider
{
  public static final String NAMESPACE_PACKAGE_ID = "java.package"; //$NON-NLS-1$

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RequirementItemProvider(AdapterFactory adapterFactory)
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

      addNamePropertyDescriptor(object);
      addNamespacePropertyDescriptor(object);
      addVersionRangePropertyDescriptor(object);
      addOptionalPropertyDescriptor(object);
      addGreedyPropertyDescriptor(object);
      addFilterPropertyDescriptor(object);
      addTypePropertyDescriptor(object);
      addMinPropertyDescriptor(object);
      addMaxPropertyDescriptor(object);
      addDescriptionPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
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
        getString("_UI_Requirement_name_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_name_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Namespace feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamespacePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_namespace_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_namespace_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__NAMESPACE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Version Range feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addVersionRangePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_versionRange_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_versionRange_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__VERSION_RANGE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Optional feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addOptionalPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_optional_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_optional_feature", "_UI_Requirement_type"), P2Package.Literals.REQUIREMENT__OPTIONAL, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null)
    {
      @Override
      public boolean isPropertySet(Object object)
      {
        Requirement requirement = (Requirement)object;
        return requirement.getMin() != 1;
      }

      @Override
      public void resetPropertyValue(Object object)
      {
        setPropertyValue(object, Boolean.FALSE);
      }

      @Override
      public void setPropertyValue(Object object, Object value)
      {
        getPropertyDescriptor(object, P2Package.Literals.REQUIREMENT__MIN).setPropertyValue(object, (Boolean)value ? 0 : 1);
      }
    });
  }

  /**
   * This adds a property descriptor for the Filter feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addFilterPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_filter_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_filter_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__FILTER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_type_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_type_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__TYPE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null)
    {
      @Override
      public void setPropertyValue(Object object, Object value)
      {
        EditingDomain domain = getEditingDomain(object);
        Requirement requirement = (Requirement)object;
        RequirementType type = requirement.getType();
        RequirementType newType = (RequirementType)value;
        if (type != newType)
        {
          String name = requirement.getName();
          switch (type)
          {
            case NONE:
            {
              break;
            }
            case FEATURE:
            {
              name = name.substring(0, name.length() - Requirement.FEATURE_SUFFIX.length());
              break;
            }
            case PROJECT:
            {
              name = name.substring(0, name.length() - Requirement.PROJECT_SUFFIX.length());
              break;
            }
          }

          switch (newType)
          {
            case NONE:
            {
              break;
            }
            case FEATURE:
            {
              // .feature.group
              name += Requirement.FEATURE_SUFFIX;
              break;
            }
            case PROJECT:
            {
              name += Requirement.PROJECT_SUFFIX;
              break;
            }
          }

          if (domain == null)
          {
            requirement.setName(name);
          }
          else
          {
            domain.getCommandStack().execute(SetCommand.create(domain, object, P2Package.Literals.REQUIREMENT__NAME, name));
          }
        }
      }

      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        Requirement requirement = (Requirement)object;
        String name = requirement.getName();
        if (StringUtil.isEmpty(name))
        {
          return Collections.singleton(RequirementType.NONE);
        }

        return RequirementType.VALUES;
      }
    });
  }

  /**
   * This adds a property descriptor for the Min feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addMinPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_min_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_min_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__MIN, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null)
    {
      @Override
      public String[] getFilterFlags(Object object)
      {
        Requirement requirement = (Requirement)object;
        int min = requirement.getMin();
        if (min < 0 || min > 1)
        {
          return null;
        }

        // Otherwise, treat it as an expert feature.
        return PropertiesUtil.EXPERT_FILTER;
      }

      @Override
      public void setPropertyValue(Object object, Object value)
      {
        Requirement requirement = (Requirement)object;
        EditingDomain editingDomain = getEditingDomain(object);
        Integer max = requirement.getMax();
        Integer newMin = (Integer)value;
        Integer newMax = newMin.compareTo(max) > 0 ? newMin : null;
        if (editingDomain == null)
        {
          requirement.eSet(feature, value);
          if (newMax != null)
          {
            requirement.eSet(P2Package.Literals.REQUIREMENT__MAX, newMax);
          }
        }
        else
        {
          CommandStack commandStack = editingDomain.getCommandStack();
          CompoundCommand subcommands = new CompoundCommand(0);
          Command setCommand = SetCommand.create(editingDomain, getCommandOwner(requirement), feature, value);
          subcommands.append(setCommand);
          if (newMax != null)
          {
            subcommands.append(SetCommand.create(editingDomain, getCommandOwner(requirement), P2Package.Literals.REQUIREMENT__MAX, newMax));
          }

          commandStack.execute(subcommands.unwrap());
        }
      }
    });
  }

  /**
   * This adds a property descriptor for the Max feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addMaxPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_max_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_max_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__MAX, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null)
    {
      @Override
      public String[] getFilterFlags(Object object)
      {
        Requirement requirement = (Requirement)object;
        int max = requirement.getMax();
        if (max != 1)
        {
          return null;
        }

        // Otherwise, treat it as an expert feature.
        return PropertiesUtil.EXPERT_FILTER;
      }

      @Override
      public void setPropertyValue(Object object, Object value)
      {
        Requirement requirement = (Requirement)object;
        EditingDomain editingDomain = getEditingDomain(object);
        Integer min = requirement.getMin();
        Integer newMax = (Integer)value;
        Integer newMin = newMax.compareTo(min) < 0 ? newMax : null;
        if (editingDomain == null)
        {
          requirement.eSet(feature, value);
          if (newMin != null)
          {
            requirement.eSet(P2Package.Literals.REQUIREMENT__MIN, newMin);
          }
        }
        else
        {
          CommandStack commandStack = editingDomain.getCommandStack();
          CompoundCommand subcommands = new CompoundCommand(0);
          Command setCommand = SetCommand.create(editingDomain, getCommandOwner(requirement), feature, value);
          subcommands.append(setCommand);
          if (newMin != null)
          {
            subcommands.append(SetCommand.create(editingDomain, getCommandOwner(requirement), P2Package.Literals.REQUIREMENT__MIN, value));
          }

          commandStack.execute(subcommands.unwrap());
        }
      }
    });
  }

  /**
   * This adds a property descriptor for the Description feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDescriptionPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_description_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_description_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__DESCRIPTION, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Greedy feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGreedyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_greedy_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_greedy_feature", "_UI_Requirement_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        P2Package.Literals.REQUIREMENT__GREEDY, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> alternatives)
  {
    return super.filterAlternatives(domain, owner, location, operations, operation, filterAlternatives(alternatives));
  }

  public static Collection<?> filterAlternatives(Collection<?> alternatives)
  {
    Collection<Object> result = new ArrayList<Object>();
    for (Object object : alternatives)
    {
      if (object instanceof Requirement)
      {
        Requirement requirement = (Requirement)object;

        String namespace = requirement.getNamespace();
        if ("osgi.bundle".equals(namespace)) //$NON-NLS-1$
        {
          requirement.setNamespace(IInstallableUnit.NAMESPACE_IU_ID);
        }
        else if (!"org.eclipse.equinox.p2.iu".equals(namespace) && !NAMESPACE_PACKAGE_ID.equals(namespace)) //$NON-NLS-1$
        {
          continue;
        }

        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange != null)
        {
          Version minimum = versionRange.getMinimum();
          if (minimum.toString().endsWith(".qualifier")) //$NON-NLS-1$
          {
            VersionRange minimumVersionRange = P2Factory.eINSTANCE.createVersionRange(minimum, VersionSegment.MICRO);
            requirement.setVersionRange(minimumVersionRange);
          }
        }
      }

      result.add(object);
    }

    return result;
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == P2Package.Literals.REQUIREMENT__NAMESPACE)
    {
      return Arrays.asList(new String[] { IInstallableUnit.NAMESPACE_IU_ID, NAMESPACE_PACKAGE_ID });
    }

    return super.filterChoices(choices, feature, object);
  }

  @Override
  protected boolean isChoiceArbitrary(EStructuralFeature feature, Object object)
  {
    return feature == P2Package.Literals.REQUIREMENT__NAMESPACE;
  }

  /**
   * This returns InstallableUnit.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Object getImage(Object object)
  {
    String key = "full/obj16/Requirement"; //$NON-NLS-1$

    Requirement requirement = (Requirement)object;
    String namespace = requirement.getNamespace();
    if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
    {
      if ("*".equals(requirement.getName())) //$NON-NLS-1$
      {
        key += "_AllSources"; //$NON-NLS-1$
      }
      else
      {
        switch (requirement.getType())
        {
          case NONE:
          {
            key += "_Plugin"; //$NON-NLS-1$
            break;
          }
          case FEATURE:
          {
            key += "_Feature"; //$NON-NLS-1$
            break;
          }
          case PROJECT:
          {
            key += "_Project"; //$NON-NLS-1$
            break;
          }
        }
      }
    }
    else if (NAMESPACE_PACKAGE_ID.equals(namespace))
    {
      key += "_Package"; //$NON-NLS-1$
    }

    Object result = overlayImage(object, getResourceLocator().getImage(key));
    return getImage(result, requirement.getMin(), requirement.isGreedy(), requirement.getMax());
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
    Requirement requirement = (Requirement)object;
    String name = requirement.getName();
    if (name == null || name.length() == 0)
    {
      name = getString("_UI_Requirement_type"); //$NON-NLS-1$
    }
    else
    {
      switch (requirement.getType())
      {
        case NONE:
        {
          break;
        }
        case FEATURE:
        {
          name = name.substring(0, name.length() - Requirement.FEATURE_SUFFIX.length());
          break;
        }
        case PROJECT:
        {
          name = name.substring(0, name.length() - Requirement.PROJECT_SUFFIX.length());
          break;
        }
      }
    }

    VersionRange versionRange = requirement.getVersionRange();
    String filter = requirement.getFilter();
    return name + (versionRange == null || VersionRange.emptyRange.equals(versionRange) ? "" : " " + versionRange) //$NON-NLS-1$ //$NON-NLS-2$
        + (StringUtil.isEmpty(filter) ? "" : " " + filter); //$NON-NLS-1$ //$NON-NLS-2$
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

    switch (notification.getFeatureID(Requirement.class))
    {
      case P2Package.REQUIREMENT__ID:
      case P2Package.REQUIREMENT__NAME:
      case P2Package.REQUIREMENT__NAMESPACE:
      case P2Package.REQUIREMENT__VERSION_RANGE:
      case P2Package.REQUIREMENT__OPTIONAL:
      case P2Package.REQUIREMENT__GREEDY:
      case P2Package.REQUIREMENT__FILTER:
      case P2Package.REQUIREMENT__TYPE:
      case P2Package.REQUIREMENT__MIN:
      case P2Package.REQUIREMENT__MAX:
      case P2Package.REQUIREMENT__DESCRIPTION:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
  }

  public static Object getImage(Object baseImage, boolean optional, boolean greedy)
  {
    if (optional)
    {
      List<Object> images = new ArrayList<Object>(2);
      images.add(baseImage);
      images.add(P2EditPlugin.INSTANCE.getImage(greedy ? "full/ovr16/greedy" : "full/ovr16/optional")); //$NON-NLS-1$ //$NON-NLS-2$
      return new DecoratedImage(images);
    }

    return baseImage;
  }

  public static Object getImage(Object baseImage, int min, boolean greedy, int max)
  {
    if (max == 0)
    {
      List<Object> images = new ArrayList<Object>(2);
      images.add(baseImage);
      images.add(P2EditPlugin.INSTANCE.getImage("full/ovr16/excluded")); //$NON-NLS-1$
      return new DecoratedImage(images);
    }

    if (min == 0)
    {
      List<Object> images = new ArrayList<Object>(2);
      images.add(baseImage);
      images.add(P2EditPlugin.INSTANCE.getImage(greedy ? "full/ovr16/greedy" : "full/ovr16/optional")); //$NON-NLS-1$ //$NON-NLS-2$
      return new DecoratedImage(images);
    }

    return baseImage;
  }

  /**
   * @author Ed Merks
   */
  private static final class DecoratedImage extends ComposedImage
  {
    private DecoratedImage(Collection<?> images)
    {
      super(images);
    }

    @Override
    public List<Point> getDrawPoints(Size size)
    {
      Point point = new Point();
      point.x = size.width - 5;
      return Arrays.asList(new Point[] { new Point(), point });
    }
  }
}
