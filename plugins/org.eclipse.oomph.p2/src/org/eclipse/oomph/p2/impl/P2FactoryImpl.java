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
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.p2.Configuration;
import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.RepositoryType;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.RequirementType;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.text.MessageFormat;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class P2FactoryImpl extends EFactoryImpl implements P2Factory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static P2Factory init()
  {
    try
    {
      P2Factory theP2Factory = (P2Factory)EPackage.Registry.INSTANCE.getEFactory(P2Package.eNS_URI);
      if (theP2Factory != null)
      {
        return theP2Factory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new P2FactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public P2FactoryImpl()
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
      case P2Package.PROFILE_DEFINITION:
        return createProfileDefinition();
      case P2Package.CONFIGURATION:
        return createConfiguration();
      case P2Package.REQUIREMENT:
        return createRequirement();
      case P2Package.REPOSITORY_LIST:
        return createRepositoryList();
      case P2Package.REPOSITORY:
        return createRepository();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case P2Package.REPOSITORY_TYPE:
        return createRepositoryTypeFromString(eDataType, initialValue);
      case P2Package.VERSION_SEGMENT:
        return createVersionSegmentFromString(eDataType, initialValue);
      case P2Package.REQUIREMENT_TYPE:
        return createRequirementTypeFromString(eDataType, initialValue);
      case P2Package.VERSION:
        return createVersionFromString(eDataType, initialValue);
      case P2Package.VERSION_RANGE:
        return createVersionRangeFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case P2Package.REPOSITORY_TYPE:
        return convertRepositoryTypeToString(eDataType, instanceValue);
      case P2Package.VERSION_SEGMENT:
        return convertVersionSegmentToString(eDataType, instanceValue);
      case P2Package.REQUIREMENT_TYPE:
        return convertRequirementTypeToString(eDataType, instanceValue);
      case P2Package.VERSION:
        return convertVersionToString(eDataType, instanceValue);
      case P2Package.VERSION_RANGE:
        return convertVersionRangeToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProfileDefinition createProfileDefinition()
  {
    ProfileDefinitionImpl profileDefinition = new ProfileDefinitionImpl();
    return profileDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Configuration createConfiguration()
  {
    ConfigurationImpl configuration = new ConfigurationImpl();
    return configuration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Requirement createRequirement()
  {
    RequirementImpl requirement = new RequirementImpl();
    return requirement;
  }

  public Requirement createRequirement(String id)
  {
    Requirement requirement = createRequirement();
    requirement.setName(id);
    return requirement;
  }

  public Requirement createRequirement(String id, VersionRange versionRange)
  {
    Requirement requirement = createRequirement(id);
    requirement.setVersionRange(versionRange);
    return requirement;
  }

  public Requirement createRequirement(String id, VersionRange versionRange, boolean optional)
  {
    Requirement requirement = createRequirement(id, versionRange);
    requirement.setOptional(optional);
    return requirement;
  }

  public Requirement createRequirement(String id, VersionRange versionRange, boolean optional, boolean greedy)
  {
    Requirement requirement = createRequirement(id, versionRange);
    requirement.setOptional(optional);
    requirement.setGreedy(greedy);
    return requirement;
  }

  public Requirement createRequirement(String id, Version version)
  {
    if (version == null)
    {
      version = Version.emptyVersion;
    }

    VersionRange versionRange = createVersionRange(version, VersionSegment.MICRO);
    return createRequirement(id, versionRange);
  }

  public Requirement createRequirement(String id, Version version, boolean optional)
  {
    Requirement requirement = createRequirement(id, version);
    requirement.setOptional(optional);
    return requirement;
  }

  public Requirement createRequirement(String id, Version version, boolean optional, boolean greedy)
  {
    Requirement requirement = createRequirement(id, version);
    requirement.setOptional(optional);
    requirement.setGreedy(greedy);
    return requirement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RepositoryList createRepositoryList()
  {
    RepositoryListImpl repositoryList = new RepositoryListImpl();
    return repositoryList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Repository createRepository()
  {
    RepositoryImpl repository = new RepositoryImpl();
    return repository;
  }

  public Repository createRepository(String url)
  {
    Repository repository = createRepository();
    repository.setURL(url);
    return repository;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RepositoryType createRepositoryTypeFromString(EDataType eDataType, String initialValue)
  {
    RepositoryType result = RepositoryType.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertRepositoryTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VersionSegment createVersionSegmentFromString(EDataType eDataType, String initialValue)
  {
    VersionSegment result = VersionSegment.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertVersionSegmentToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RequirementType createRequirementTypeFromString(EDataType eDataType, String initialValue)
  {
    RequirementType result = RequirementType.get(initialValue);
    if (result == null)
    {
      throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertRequirementTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Version createVersionFromString(EDataType eDataType, String initialValue)
  {
    return initialValue == null ? null : Version.create(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertVersionToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : ((Version)instanceValue).toString();
  }

  public VersionRange createVersionRange(Version version, VersionSegment segment)
  {
    return createVersionRange(version, segment, false);
  }

  public VersionRange createVersionRange(Version version, VersionSegment segment, boolean compatible)
  {
    if (version == null || version.equals(Version.emptyVersion))
    {
      return VersionRange.emptyRange;
    }

    if (segment == null || segment == VersionSegment.QUALIFIER)
    {
      if (compatible && version.getSegmentCount() != 0)
      {
        Comparable<?> firstSegment = version.getSegment(0);
        if (firstSegment instanceof Integer)
        {
          Integer major = (Integer)firstSegment;
          return new VersionRange(version, true, Version.createOSGi(major + 1, 0, 0), false);
        }
      }

      return new VersionRange(version, true, version, true);
    }

    if (!version.isOSGiCompatible())
    {
      throw new P2Exception(MessageFormat.format(Messages.P2FactoryImpl_IncompatibleVersion_exception, version));
    }

    org.osgi.framework.Version osgiVersion = new org.osgi.framework.Version(version.toString());
    int major = osgiVersion.getMajor();
    int minor = osgiVersion.getMinor();
    int micro = osgiVersion.getMicro();

    switch (segment)
    {
      case MAJOR:
        return new VersionRange(Version.createOSGi(major, 0, 0), true, Version.createOSGi(major + 1, 0, 0), false);

      case MINOR:
        return new VersionRange(Version.createOSGi(major, minor, 0), true,
            compatible ? Version.createOSGi(major + 1, 0, 0) : Version.createOSGi(major, minor + 1, 0), false);

      case MICRO:
        return new VersionRange(Version.createOSGi(major, minor, micro), true,
            compatible ? Version.createOSGi(major + 1, 0, 0) : Version.createOSGi(major, minor, micro + 1), false);

      default:
        throw new P2Exception(MessageFormat.format(Messages.P2FactoryImpl_InvalidSegment_exception, segment));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public VersionRange createVersionRangeFromString(EDataType eDataType, String initialValue)
  {
    return initialValue == null ? null : new VersionRange(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertVersionRangeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : ((VersionRange)instanceValue).toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public P2Package getP2Package()
  {
    return (P2Package)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static P2Package getPackage()
  {
    return P2Package.eINSTANCE;
  }

} // P2FactoryImpl
