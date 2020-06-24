/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.DropinLocation;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Targlet</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getSourceLocators <em>Source Locators</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getInstallableUnitGenerators <em>Installable Unit Generators</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getRepositoryLists <em>Repository Lists</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getActiveRepositoryListName <em>Active Repository List Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getActiveRepositoryList <em>Active Repository List</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getActiveRepositories <em>Active Repositories</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeSources <em>Include Sources</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeAllPlatforms <em>Include All Platforms</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeAllRequirements <em>Include All Requirements</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getDropinLocations <em>Dropin Locations</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#isIncludeBinaryEquivalents <em>Include Binary Equivalents</em>}</li>
 *   <li>{@link org.eclipse.oomph.targlets.impl.TargletImpl#getProfileProperties <em>Profile Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TargletImpl extends ModelElementImpl implements Targlet
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getSourceLocators() <em>Source Locators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceLocators()
   * @generated
   * @ordered
   */
  protected EList<SourceLocator> sourceLocators;

  /**
   * The cached value of the '{@link #getInstallableUnitGenerators() <em>Installable Unit Generators</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstallableUnitGenerators()
   * @generated
   * @ordered
   */
  protected EList<IUGenerator> installableUnitGenerators;

  /**
   * The cached value of the '{@link #getRepositoryLists() <em>Repository Lists</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepositoryLists()
   * @generated
   * @ordered
   */
  protected EList<RepositoryList> repositoryLists;

  /**
   * The default value of the '{@link #getActiveRepositoryListName() <em>Active Repository List Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveRepositoryListName()
   * @generated
   * @ordered
   */
  protected static final String ACTIVE_REPOSITORY_LIST_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getActiveRepositoryListName() <em>Active Repository List Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveRepositoryListName()
   * @generated
   * @ordered
   */
  protected String activeRepositoryListName = ACTIVE_REPOSITORY_LIST_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #isIncludeSources() <em>Include Sources</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSources()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_SOURCES_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isIncludeSources() <em>Include Sources</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeSources()
   * @generated
   * @ordered
   */
  protected boolean includeSources = INCLUDE_SOURCES_EDEFAULT;

  /**
   * The default value of the '{@link #isIncludeAllPlatforms() <em>Include All Platforms</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllPlatforms()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_ALL_PLATFORMS_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIncludeAllPlatforms() <em>Include All Platforms</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllPlatforms()
   * @generated
   * @ordered
   */
  protected boolean includeAllPlatforms = INCLUDE_ALL_PLATFORMS_EDEFAULT;

  /**
   * The default value of the '{@link #isIncludeAllRequirements() <em>Include All Requirements</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllRequirements()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_ALL_REQUIREMENTS_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isIncludeAllRequirements() <em>Include All Requirements</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeAllRequirements()
   * @generated
   * @ordered
   */
  protected boolean includeAllRequirements = INCLUDE_ALL_REQUIREMENTS_EDEFAULT;

  /**
   * The cached value of the '{@link #getDropinLocations() <em>Dropin Locations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDropinLocations()
   * @generated
   * @ordered
   */
  protected EList<DropinLocation> dropinLocations;

  /**
   * The default value of the '{@link #isIncludeBinaryEquivalents() <em>Include Binary Equivalents</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeBinaryEquivalents()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_BINARY_EQUIVALENTS_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isIncludeBinaryEquivalents() <em>Include Binary Equivalents</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeBinaryEquivalents()
   * @generated
   * @ordered
   */
  protected boolean includeBinaryEquivalents = INCLUDE_BINARY_EQUIVALENTS_EDEFAULT;

  /**
   * The default value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected static final String PROFILE_PROPERTIES_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProfileProperties() <em>Profile Properties</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProfileProperties()
   * @generated
   * @ordered
   */
  protected String profileProperties = PROFILE_PROPERTIES_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TargletImpl()
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
    return TargletPackage.Literals.TARGLET;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, TargletPackage.TARGLET__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<SourceLocator> getSourceLocators()
  {
    if (sourceLocators == null)
    {
      sourceLocators = new EObjectContainmentEList<SourceLocator>(SourceLocator.class, this, TargletPackage.TARGLET__SOURCE_LOCATORS);
    }
    return sourceLocators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<IUGenerator> getInstallableUnitGenerators()
  {
    if (installableUnitGenerators == null)
    {
      installableUnitGenerators = new EObjectContainmentEList<IUGenerator>(IUGenerator.class, this, TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS);
    }
    return installableUnitGenerators;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<DropinLocation> getDropinLocations()
  {
    if (dropinLocations == null)
    {
      dropinLocations = new EObjectContainmentEList<DropinLocation>(DropinLocation.class, this, TargletPackage.TARGLET__DROPIN_LOCATIONS);
    }
    return dropinLocations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeBinaryEquivalents()
  {
    return includeBinaryEquivalents;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeBinaryEquivalents(boolean newIncludeBinaryEquivalents)
  {
    boolean oldIncludeBinaryEquivalents = includeBinaryEquivalents;
    includeBinaryEquivalents = newIncludeBinaryEquivalents;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS, oldIncludeBinaryEquivalents,
          includeBinaryEquivalents));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProfileProperties()
  {
    return profileProperties;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProfileProperties(String newProfileProperties)
  {
    String oldProfileProperties = profileProperties;
    profileProperties = newProfileProperties;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__PROFILE_PROPERTIES, oldProfileProperties, profileProperties));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<RepositoryList> getRepositoryLists()
  {
    if (repositoryLists == null)
    {
      repositoryLists = new EObjectContainmentEList<RepositoryList>(RepositoryList.class, this, TargletPackage.TARGLET__REPOSITORY_LISTS);
    }
    return repositoryLists;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getActiveRepositoryListName()
  {
    return activeRepositoryListName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActiveRepositoryListName(String newActiveRepositoryListName)
  {
    String oldActiveRepositoryListName = activeRepositoryListName;
    activeRepositoryListName = newActiveRepositoryListName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME, oldActiveRepositoryListName,
          activeRepositoryListName));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public RepositoryList getActiveRepositoryList()
  {
    EList<RepositoryList> repositoryLists = getRepositoryLists();
    String name = getActiveRepositoryListName();
    if (name == null && !repositoryLists.isEmpty())
    {
      return repositoryLists.get(0);
    }

    if (name != null)
    {
      for (RepositoryList repositoryList : repositoryLists)
      {
        if (name.equals(repositoryList.getName()))
        {
          return repositoryList;
        }
      }
    }

    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public EList<Repository> getActiveRepositories()
  {
    EList<Repository> result = new EObjectEList<Repository>(Repository.class, this, TargletPackage.TARGLET__ACTIVE_REPOSITORIES);

    RepositoryList activeRepositoryList = getActiveRepositoryList();
    if (activeRepositoryList != null)
    {
      result.addAll(activeRepositoryList.getRepositories());
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeSources()
  {
    return includeSources;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeSources(boolean newIncludeSources)
  {
    boolean oldIncludeSources = includeSources;
    includeSources = newIncludeSources;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_SOURCES, oldIncludeSources, includeSources));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeAllPlatforms()
  {
    return includeAllPlatforms;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeAllPlatforms(boolean newIncludeAllPlatforms)
  {
    boolean oldIncludeAllPlatforms = includeAllPlatforms;
    includeAllPlatforms = newIncludeAllPlatforms;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS, oldIncludeAllPlatforms, includeAllPlatforms));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeAllRequirements()
  {
    return includeAllRequirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeAllRequirements(boolean newIncludeAllRequirements)
  {
    boolean oldIncludeAllRequirements = includeAllRequirements;
    includeAllRequirements = newIncludeAllRequirements;
    if (eNotificationRequired())
    {
      eNotify(
          new ENotificationImpl(this, Notification.SET, TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS, oldIncludeAllRequirements, includeAllRequirements));
    }
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
      case TargletPackage.TARGLET__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return ((InternalEList<?>)getSourceLocators()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
        return ((InternalEList<?>)getInstallableUnitGenerators()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return ((InternalEList<?>)getRepositoryLists()).basicRemove(otherEnd, msgs);
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
        return ((InternalEList<?>)getDropinLocations()).basicRemove(otherEnd, msgs);
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
      case TargletPackage.TARGLET__NAME:
        return getName();
      case TargletPackage.TARGLET__REQUIREMENTS:
        return getRequirements();
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return getSourceLocators();
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
        return getInstallableUnitGenerators();
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return getRepositoryLists();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME:
        return getActiveRepositoryListName();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        return getActiveRepositoryList();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORIES:
        return getActiveRepositories();
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        return isIncludeSources();
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        return isIncludeAllPlatforms();
      case TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS:
        return isIncludeAllRequirements();
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
        return getDropinLocations();
      case TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS:
        return isIncludeBinaryEquivalents();
      case TargletPackage.TARGLET__PROFILE_PROPERTIES:
        return getProfileProperties();
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
      case TargletPackage.TARGLET__NAME:
        setName((String)newValue);
        return;
      case TargletPackage.TARGLET__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        getSourceLocators().clear();
        getSourceLocators().addAll((Collection<? extends SourceLocator>)newValue);
        return;
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
        getInstallableUnitGenerators().clear();
        getInstallableUnitGenerators().addAll((Collection<? extends IUGenerator>)newValue);
        return;
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        getRepositoryLists().clear();
        getRepositoryLists().addAll((Collection<? extends RepositoryList>)newValue);
        return;
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME:
        setActiveRepositoryListName((String)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        setIncludeSources((Boolean)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        setIncludeAllPlatforms((Boolean)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS:
        setIncludeAllRequirements((Boolean)newValue);
        return;
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
        getDropinLocations().clear();
        getDropinLocations().addAll((Collection<? extends DropinLocation>)newValue);
        return;
      case TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS:
        setIncludeBinaryEquivalents((Boolean)newValue);
        return;
      case TargletPackage.TARGLET__PROFILE_PROPERTIES:
        setProfileProperties((String)newValue);
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
      case TargletPackage.TARGLET__NAME:
        setName(NAME_EDEFAULT);
        return;
      case TargletPackage.TARGLET__REQUIREMENTS:
        getRequirements().clear();
        return;
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        getSourceLocators().clear();
        return;
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
        getInstallableUnitGenerators().clear();
        return;
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        getRepositoryLists().clear();
        return;
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME:
        setActiveRepositoryListName(ACTIVE_REPOSITORY_LIST_NAME_EDEFAULT);
        return;
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        setIncludeSources(INCLUDE_SOURCES_EDEFAULT);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        setIncludeAllPlatforms(INCLUDE_ALL_PLATFORMS_EDEFAULT);
        return;
      case TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS:
        setIncludeAllRequirements(INCLUDE_ALL_REQUIREMENTS_EDEFAULT);
        return;
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
        getDropinLocations().clear();
        return;
      case TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS:
        setIncludeBinaryEquivalents(INCLUDE_BINARY_EQUIVALENTS_EDEFAULT);
        return;
      case TargletPackage.TARGLET__PROFILE_PROPERTIES:
        setProfileProperties(PROFILE_PROPERTIES_EDEFAULT);
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
      case TargletPackage.TARGLET__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case TargletPackage.TARGLET__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
        return sourceLocators != null && !sourceLocators.isEmpty();
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
        return installableUnitGenerators != null && !installableUnitGenerators.isEmpty();
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
        return repositoryLists != null && !repositoryLists.isEmpty();
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME:
        return ACTIVE_REPOSITORY_LIST_NAME_EDEFAULT == null ? activeRepositoryListName != null
            : !ACTIVE_REPOSITORY_LIST_NAME_EDEFAULT.equals(activeRepositoryListName);
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST:
        return getActiveRepositoryList() != null;
      case TargletPackage.TARGLET__ACTIVE_REPOSITORIES:
        return !getActiveRepositories().isEmpty();
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
        return includeSources != INCLUDE_SOURCES_EDEFAULT;
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
        return includeAllPlatforms != INCLUDE_ALL_PLATFORMS_EDEFAULT;
      case TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS:
        return includeAllRequirements != INCLUDE_ALL_REQUIREMENTS_EDEFAULT;
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
        return dropinLocations != null && !dropinLocations.isEmpty();
      case TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS:
        return includeBinaryEquivalents != INCLUDE_BINARY_EQUIVALENTS_EDEFAULT;
      case TargletPackage.TARGLET__PROFILE_PROPERTIES:
        return PROFILE_PROPERTIES_EDEFAULT == null ? profileProperties != null : !PROFILE_PROPERTIES_EDEFAULT.equals(profileProperties);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (name: "); //$NON-NLS-1$
    result.append(name);
    result.append(", activeRepositoryListName: "); //$NON-NLS-1$
    result.append(activeRepositoryListName);
    result.append(", includeSources: "); //$NON-NLS-1$
    result.append(includeSources);
    result.append(", includeAllPlatforms: "); //$NON-NLS-1$
    result.append(includeAllPlatforms);
    result.append(", includeAllRequirements: "); //$NON-NLS-1$
    result.append(includeAllRequirements);
    result.append(", includeBinaryEquivalents: "); //$NON-NLS-1$
    result.append(includeBinaryEquivalents);
    result.append(", profileProperties: "); //$NON-NLS-1$
    result.append(profileProperties);
    result.append(')');
    return result.toString();
  }

} // TargletImpl
