/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Andreas Scharf - Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseAnnotationConstants;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.preferences.impl.PreferencesURIHandlerImpl;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.ReflectUtil.ReflectionException;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.security.storage.ISecurePreferences;

import org.osgi.framework.Bundle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class SetupCoreUtil
{
  public static final String OOMPH_NAMESPACE = "org.eclipse.oomph";

  private static final ECFURIHandlerImpl.AuthorizationHandlerImpl AUTHORIZATION_HANDLER;

  static
  {
    IProvisioningAgent agent = (IProvisioningAgent)org.eclipse.equinox.internal.p2.core.helpers.ServiceHelper
        .getService(org.eclipse.equinox.internal.p2.repository.Activator.getContext(), IProvisioningAgent.SERVICE_NAME);
    UIServices uiServices = (UIServices)agent.getService(UIServices.SERVICE_NAME);

    ISecurePreferences root = PreferencesUtil.getSecurePreferences();
    ISecurePreferences securePreferences = root.node(OOMPH_NAMESPACE).node("hosts");

    AUTHORIZATION_HANDLER = new ECFURIHandlerImpl.AuthorizationHandlerImpl(uiServices, securePreferences);
  }

  private SetupCoreUtil()
  {
  }

  public static String getLabel(Scope scope)
  {
    if (scope == null)
    {
      return "";
    }

    String label = scope.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = StringUtil.safe(scope.getName());
    }

    return label;
  }

  public static ResourceSet createResourceSet()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    configureResourceSet(resourceSet);
    return resourceSet;
  }

  public static void configureResourceSet(final ResourceSet resourceSet)
  {
    Resource.Factory factory = new BaseResourceFactoryImpl();

    Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
    extensionToFactoryMap.put("targlet", factory);
    extensionToFactoryMap.put("def", factory);
    extensionToFactoryMap.put("ext", factory);

    URIConverter uriConverter = resourceSet.getURIConverter();
    Map<URI, URI> uriMap = uriConverter.getURIMap();

    EList<URIHandler> uriHandlers = uriConverter.getURIHandlers();
    uriHandlers.add(4, new UserURIHandlerImpl());
    uriHandlers.add(5, new SelfProductCatalogURIHandlerImpl());
    uriHandlers.add(6, new PreferencesURIHandlerImpl());
    uriHandlers.add(7, new ECFURIHandlerImpl());

    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_AUTHORIZATION_HANDLER, AUTHORIZATION_HANDLER);

    class ModelResourceSet extends ResourceSetImpl
    {
      private Map<Resource, Resource> redirectedResources = new HashMap<Resource, Resource>();

      public ModelResourceSet()
      {
        uriConverter = resourceSet.getURIConverter();
        packageRegistry = resourceSet.getPackageRegistry();
        resourceFactoryRegistry = resourceSet.getResourceFactoryRegistry();
        loadOptions = resourceSet.getLoadOptions();
      }

      @Override
      protected Resource delegatedGetResource(URI uri, boolean loadOnDemand)
      {
        Resource result = super.delegatedGetResource(uri, loadOnDemand);
        if (result == null)
        {
          result = super.delegatedGetResource(uriConverter.normalize(uri), loadOnDemand);
        }

        return result;
      }

      @Override
      public Resource getResource(URI uri, boolean loadOnDemand)
      {
        try
        {
          Resource resource = super.getResource(uri, true);
          Resource redirectedResource = redirectedResources.get(resource);
          if (redirectedResource != null)
          {
            return redirectedResource;
          }

          if (resource.getResourceSet() == this)
          {
            synchronized (resourceSet)
            {
              resourceSet.getResources().add(resource);
            }
          }

          return resource;
        }
        catch (RuntimeException throwable)
        {
          Resource resource = super.getResource(uri, false);
          if (resource.getResourceSet() == this)
          {
            synchronized (resourceSet)
            {
              resourceSet.getResources().add(resource);
            }
          }

          if (loadOnDemand)
          {
            throw throwable;
          }
          else
          {
            return null;
          }
        }
      }

      @Override
      protected void demandLoad(Resource resource) throws IOException
      {
        super.demandLoad(resource);

        EPackage ePackage = (EPackage)EcoreUtil.getObjectByType(resource.getContents(), EcorePackage.Literals.EPACKAGE);
        if (ePackage != null)
        {
          String nsURI = ePackage.getNsURI();
          EPackage redirectedEPackage = packageRegistry.getEPackage(nsURI);

          for (EClassifier eClassifier : ePackage.getEClassifiers())
          {
            String instanceClassName = eClassifier.getInstanceClassName();
            if (instanceClassName != null)
            {
              if (eClassifier instanceof EDataType)
              {
                eClassifier.setInstanceClass(String.class);
              }
              else
              {
                eClassifier.setInstanceClassName(null);
                eClassifier.setInstanceClass(null);
              }
            }
          }

          if (redirectedEPackage != null)
          {
            redirectedResources.put(resource, redirectedEPackage.eResource());

            packageRegistry.put(resource.getURI().toString(), redirectedEPackage);
            packageRegistry.put(uriConverter.normalize(resource.getURI()).toString(), redirectedEPackage);
          }
          else
          {
            packageRegistry.put(resource.getURI().toString(), ePackage);
            packageRegistry.put(uriConverter.normalize(resource.getURI()).toString(), ePackage);

            ((EPackageImpl)ePackage).freeze();
          }
        }
      }
    }

    final ModelResourceSet modelResourceSet = new ModelResourceSet();

    new ResourceSetImpl.MappedResourceLocator((ResourceSetImpl)resourceSet)
    {
      @Override
      public Resource getResource(URI uri, boolean loadOnDemand)
      {
        if ("ecore".equals(uri.fileExtension()))
        {
          Resource resource = null;
          synchronized (resourceSet)
          {
            resource = super.getResource(uri, false);
            if (resource != null)
            {
              if (!resource.isLoaded())
              {
                demandLoadHelper(resource);
              }

              return resource;
            }

            resource = modelResourceSet.getResource(uri, loadOnDemand);
            if (resource != null)
            {
              return resource;
            }
          }
        }

        return super.getResource(uri, loadOnDemand);
      }
    };

    uriMap.put(SetupContext.INDEX_SETUP_URI.trimSegments(1), SetupContext.INDEX_SETUP_LOCATION_URI.trimSegments(1).appendSegment(""));

    for (Map.Entry<Object, Object> entry : System.getProperties().entrySet())
    {
      Object key = entry.getKey();
      if (key instanceof String)
      {
        if (((String)key).startsWith(SetupProperties.PROP_REDIRECTION_BASE))
        {
          String[] mapping = ((String)entry.getValue()).split("->");
          if (mapping.length == 2)
          {
            URI sourceURI = URI.createURI(mapping[0]);
            URI targetURI = URI.createURI(mapping[1].replace("\\", "/"));

            // Only include the mapping if the target exists.
            // For example, we often include a redirection of the remote setup to the local git clone in an installed IDE,
            // but if that clone hasn't been cloned yet, we want to continue to use the remote version.
            //
            if (targetURI.isFile())
            {
              File file = new File(targetURI.toFileString());
              if (!file.exists())
              {
                continue;
              }
            }

            uriMap.put(sourceURI, targetURI);
          }
        }
      }
    }
  }

  public static <T> void reorder(EList<T> values, DependencyProvider<T> dependencyProvider)
  {
    for (int i = 0, size = values.size(), count = 0; i < size; ++i)
    {
      T value = values.get(i);
      if (count == size)
      {
        SetupCorePlugin.INSTANCE.log("Circular dependencies " + value, IStatus.WARNING);
        return;
      }

      boolean changed = false;

      // TODO Consider basing this on a provider that just returns a boolean based on "does v1 depend on v2".
      for (T dependency : dependencyProvider.getDependencies(value))
      {
        int index = values.indexOf(dependency);
        if (index > i)
        {
          values.move(i, index);
          changed = true;
        }
      }

      if (changed)
      {
        --i;
        ++count;
      }
      else
      {
        count = 0;
      }
    }
  }

  /**
   * Reads the content of the bundle entry with the given name within the given bundle.
   *
   * @param bundle The bundle containing the resource
   * @param name The name of the bundle entry
   *
   * @return The content of the given bundle entry
   */
  public static String readBundleResource(final Bundle bundle, final String name) throws IOException
  {
    InputStream stream = bundle.getEntry(name).openStream();
    Scanner scanner = null;
    try
    {
      scanner = new Scanner(stream, "UTF-8");

      // \\A is a 'trick' to read the whole contents of a stream with only one line
      return scanner.hasNext() ? scanner.useDelimiter("\\A").next() : null;
    }
    catch (Exception ex)
    {
      throw new IOExceptionWithCause(ex);
    }
    finally
    {
      if (scanner != null)
      {
        scanner.close();
      }
      if (stream != null)
      {
        IOUtil.close(stream);
      }
    }
  }

  public static void migrate(Resource resource, Collection<EObject> result)
  {
    new Migrator(resource).migrate(result);
  }

  /**
   * @author Eike Stepper
   */
  public interface DependencyProvider<T>
  {
    Collection<? extends T> getDependencies(T value);
  }

  /**
   * @author Ed Merks
   */
  public static class Migrator
  {
    /**
     * @author Ed Merks
     */
    private static class MigrationCopier extends EcoreUtil.Copier
    {
      private static final long serialVersionUID = 1L;

      private EPackage.Registry packageRegistry;

      public MigrationCopier(EPackage.Registry packageRegistry)
      {
        // super(true, false);

        this.packageRegistry = packageRegistry;
      }

      public EObject copyAsProxy(EObject eObject)
      {
        EObject copiedEObject = createCopy(eObject);
        if (copiedEObject != null)
        {
          put(eObject, copiedEObject);
          ((InternalEObject)copiedEObject).eSetProxyURI(URI.createURI("bogus:/" + BaseUtil.getRootURI(eObject)));
        }

        return copiedEObject;
      }

      @Override
      protected EClass getTarget(EClass eClass)
      {
        return getEClass(eClass, "name", "nsURI");
      }

      protected EClass getEClass(ENamedElement eNamedElement, String nameAnnotation, String nsURIAnnotation)
      {
        String nsURIs = getAnnotation(eNamedElement, nsURIAnnotation);
        if (nsURIs == null)
        {
          for (EObject eContainer = eNamedElement.eContainer(); eContainer != null; eContainer = eContainer.eContainer())
          {
            if (eContainer instanceof EPackage)
            {
              nsURIs = getAnnotation((EPackage)eContainer, "nsURIs");
            }
          }
        }

        if (nsURIs == null)
        {
          throw new IllegalStateException("Cannot find package URIs for " + EcoreUtil.getURI(eNamedElement));
        }

        String name = getAnnotation(eNamedElement, nameAnnotation);
        if (name == null)
        {
          name = eNamedElement.getName();
        }

        for (String nsURI : nsURIs.split(" "))
        {
          EPackage ePackage = packageRegistry.getEPackage(nsURI);
          if (ePackage != null)
          {
            EClassifier eClassifier = ePackage.getEClassifier(name);
            if (eClassifier instanceof EClass)
            {
              return (EClass)eClassifier;
            }
          }
        }

        throw new IllegalStateException("Cannot find class '" + name + "' for to " + EcoreUtil.getURI(eNamedElement));
      }

      @Override
      protected EStructuralFeature getTarget(EStructuralFeature eStructuralFeature)
      {
        String name = getAnnotation(eStructuralFeature, "name");
        if (name == null)
        {
          name = eStructuralFeature.getName();
        }

        EStructuralFeature result = getTarget(eStructuralFeature.getEContainingClass()).getEStructuralFeature(name);
        if (result != null)
        {
          return result;
        }

        throw new IllegalStateException("Cannot find feature corresponding to " + EcoreUtil.getURI(eStructuralFeature));
      }

      @Override
      protected Setting getTarget(EStructuralFeature eStructuralFeature, EObject eObject, EObject copyEObject)
      {
        String name = getAnnotation(eStructuralFeature, "name");

        // The blank name is used to discard features which are not intended to be migrated.
        if ("".equals(name))
        {
          return null;
        }

        if (name == null)
        {
          name = eStructuralFeature.getName();
        }

        String targetName = getAnnotation(eStructuralFeature, "targetName");
        if (targetName != null)
        {
          EClass targetEClass = getEClass(eStructuralFeature, "targetName", "targetNsURI");
          EClass eClass = copyEObject.eClass();
          for (EReference eReference : eClass.getEAllContainments())
          {
            EClass eReferenceType = eReference.getEReferenceType();
            if (eReferenceType == targetEClass)
            {
              EStructuralFeature.Setting setting = demandCreateContainer(copyEObject, targetEClass, eReferenceType, name, eStructuralFeature, eReference);
              if (setting != null && targetEClass == BasePackage.Literals.ANNOTATION)
              {
                // Handle mappings to annotations in a special way to record the original as a details entry.
                Annotation annotation = (Annotation)setting.getEObject();
                annotation.setSource(BaseAnnotationConstants.ANNOTATION_SOURCE);
                @SuppressWarnings("unchecked")
                Map.Entry<String, String> stringToStringMapEntry = (Map.Entry<String, String>)BaseFactory.eINSTANCE
                    .create(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY);
                InternalEObject mapEntry = (InternalEObject)stringToStringMapEntry;
                mapEntry.eSet(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY__KEY, EcoreUtil.getURI(eStructuralFeature).toString());
                annotation.getDetails().add(stringToStringMapEntry);
                return mapEntry.eSetting(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY__VALUE);
              }
              return setting;
            }
          }
        }

        EStructuralFeature.Setting setting = demandCreateContainer(copyEObject, name, eStructuralFeature);
        if (setting != null)
        {
          return setting;
        }

        throw new IllegalStateException("Cannot find feature corresponding to " + EcoreUtil.getURI(eStructuralFeature));
      }

      @Override
      protected void copyAttribute(EAttribute eAttribute, EObject eObject, EObject copyEObject)
      {
        // For attributes that are targeting an Annotation to be copied even if eIsSet is false.
        String targetName = getAnnotation(eAttribute, "targetName");
        if (targetName != null)
        {
          EClass targetEClass = getEClass(eAttribute, "targetName", "targetNsURI");
          if (targetEClass == BasePackage.Literals.ANNOTATION)
          {
            EStructuralFeature.Setting setting = getTarget(eAttribute, eObject, copyEObject);
            if (setting != null)
            {
              copyAttributeValue(eAttribute, eObject, eObject.eGet(eAttribute), setting);
              return;
            }
          }
        }

        super.copyAttribute(eAttribute, eObject, copyEObject);
      }

      protected EStructuralFeature.Setting demandCreateContainer(final EObject copyEObject, String name, EStructuralFeature eStructuralFeature)
      {
        return demandCreateContainer(new HashSet<EClass>(), copyEObject, name, eStructuralFeature);
      }

      protected EStructuralFeature.Setting demandCreateContainer(Set<EClass> tried, final EObject copyEObject, String name,
          EStructuralFeature eStructuralFeature)
      {
        EClass targetEClass = copyEObject.eClass();
        if (!tried.add(targetEClass))
        {
          return null;
        }

        EStructuralFeature targetEStructuralFeature = targetEClass.getEStructuralFeature(name);
        if (targetEStructuralFeature != null)
        {
          if (targetEStructuralFeature instanceof EReference && eStructuralFeature instanceof EReference)
          {
            EClass actualTargetEClass = ((EReference)targetEStructuralFeature).getEReferenceType();
            EClass expectedTargetEClass = getTarget(((EReference)eStructuralFeature).getEReferenceType());
            if (actualTargetEClass != expectedTargetEClass)
            {
              // Look for a containment feature that can hold the actual target instance.
              for (EReference eReference : targetEClass.getEAllContainments())
              {
                EClass eReferenceType = eReference.getEReferenceType();
                if (eReferenceType.isSuperTypeOf(actualTargetEClass))
                {
                  // Look for containment features of that reference's type to find one that can hold the expected target instance.
                  for (EReference childEReference : eReferenceType.getEAllContainments())
                  {
                    if (childEReference.getEReferenceType().isSuperTypeOf(expectedTargetEClass))
                    {
                      // Create a container and a setting for that child reference.
                      return demandCreateContainer(copyEObject, actualTargetEClass, eReference, childEReference, true);
                    }
                  }
                }
              }

              throw new IllegalStateException("Couldn't find a matching feature for " + EcoreUtil.getURI(eStructuralFeature));
            }
          }

          return ((InternalEObject)copyEObject).eSetting(targetEStructuralFeature);
        }

        EClass targetEContainingClass = getTarget(eStructuralFeature.getEContainingClass());
        if (targetEContainingClass != null)
        {
          for (EReference eReference : targetEClass.getEAllContainments())
          {
            EClass eReferenceType = eReference.getEReferenceType();
            if (targetEContainingClass.isSuperTypeOf(eReferenceType))
            {
              EStructuralFeature.Setting setting = demandCreateContainer(copyEObject, targetEContainingClass, eReferenceType, name, eStructuralFeature,
                  eReference);
              if (setting != null)
              {
                return setting;
              }
            }
          }

          // Try to create intermediate containers...
          for (final EReference eReference : targetEClass.getEAllContainments())
          {
            EClass eReferenceEType = eReference.getEReferenceType();
            if (!eReferenceEType.isAbstract())
            {
              EObject targetContainer;
              Runnable addToContainer = null;
              Object targetValue = copyEObject.eGet(eReference);
              if (eReference.isMany())
              {
                @SuppressWarnings("unchecked")
                final List<EObject> targetValues = (List<EObject>)targetValue;
                if (targetValues.isEmpty())
                {
                  final EObject demandCreatedTargetContainer = EcoreUtil.create(eReferenceEType);
                  targetContainer = demandCreatedTargetContainer;
                  addToContainer = new Runnable()
                  {
                    public void run()
                    {
                      targetValues.add(demandCreatedTargetContainer);
                    }
                  };
                }
                else
                {
                  targetContainer = targetValues.get(0);
                }
              }
              else if (targetValue == null)
              {
                final EObject demandCreatedTargetContainer = EcoreUtil.create(eReferenceEType);
                targetContainer = demandCreatedTargetContainer;
                addToContainer = new Runnable()
                {
                  public void run()
                  {
                    copyEObject.eSet(eReference, demandCreatedTargetContainer);
                  }
                };
              }
              else
              {
                targetContainer = (EObject)targetValue;
              }

              // If we can create a setting, add the container to really use it and return that setting.
              EStructuralFeature.Setting setting = demandCreateContainer(tried, targetContainer, name, eStructuralFeature);
              if (setting != null)
              {
                if (addToContainer != null)
                {
                  addToContainer.run();
                }

                return setting;
              }
            }
          }
        }

        return null;
      }

      protected EStructuralFeature.Setting demandCreateContainer(EObject copyEObject, EClass targetEClass, EClass eReferenceType, String name,
          EStructuralFeature eStructuralFeature, EReference eReference)
      {
        if (targetEClass.isSuperTypeOf(eReferenceType))
        {
          EStructuralFeature targetEStructuralFeature = targetEClass.getEStructuralFeature(name);
          if (targetEStructuralFeature == null)
          {
            throw new IllegalStateException("Cannot find feature corresponding to " + EcoreUtil.getURI(eStructuralFeature));
          }

          return demandCreateContainer(copyEObject, eReferenceType, eReference, targetEStructuralFeature, false);
        }

        return null;
      }

      private EStructuralFeature.Setting demandCreateContainer(EObject copyEObject, EClass eReferenceType, EReference eReference,
          EStructuralFeature settingFeature, boolean additional)
      {
        EObject targetContainer;
        Object targetValue = copyEObject.eGet(eReference);
        if (eReference.isMany())
        {
          @SuppressWarnings("unchecked")
          List<EObject> targetValues = (List<EObject>)targetValue;
          if (additional || targetValues.isEmpty())
          {
            targetContainer = EcoreUtil.create(eReferenceType);
            targetValues.add(targetContainer);
          }
          else
          {
            targetContainer = targetValues.get(0);
          }
        }
        else if (additional || targetValue == null)
        {
          targetContainer = EcoreUtil.create(eReferenceType);
          copyEObject.eSet(eReference, targetContainer);
        }
        else
        {
          targetContainer = (EObject)targetValue;
        }

        return ((InternalEObject)targetContainer).eSetting(settingFeature);
      }

      @Override
      protected void copyAttributeValue(EAttribute eAttribute, EObject eObject, Object value, Setting setting)
      {
        EDataType eDataType = eAttribute.getEAttributeType();
        EDataType targetEDataType = (EDataType)setting.getEStructuralFeature().getEType();
        if (eDataType.getInstanceClass() != targetEDataType.getInstanceClass() || eDataType.getInstanceClass() == null)
        {
          if (eAttribute.isMany())
          {
            @SuppressWarnings("unchecked")
            List<Object> values = (List<Object>)value;
            @SuppressWarnings("unchecked")
            List<Object> transformedValues = (List<Object>)setting.get(false);
            for (Object object : values)
            {
              transformedValues.add(convert(eDataType, targetEDataType, object));
            }
          }
          else
          {
            setting.set(convert(eDataType, targetEDataType, value));
          }
        }
        else
        {
          super.copyAttributeValue(eAttribute, eObject, value, setting);
        }
      }

      protected Object convert(EDataType sourceEDataType, EDataType targetEDataType, Object sourceValue)
      {
        return EcoreUtil.createFromString(targetEDataType, EcoreUtil.convertToString(sourceEDataType, sourceValue));
      }

      private String getAnnotation(EModelElement eModelElement, String key)
      {
        return EcoreUtil.getAnnotation(eModelElement, BaseAnnotationConstants.ANNOTATION_SOURCE, key);
      }
    }

    private Resource resource;

    public Migrator(Resource resource)
    {
      this.resource = resource;
    }

    public Collection<? extends EObject> migrate(Collection<EObject> result)
    {
      MigrationCopier migrationCopier = new MigrationCopier(resource.getResourceSet().getPackageRegistry());
      Set<EObject> allContainedObjects = new HashSet<EObject>();
      Set<EObject> allCrossReferencedObjects = new HashSet<EObject>();
      for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
      {
        EObject containedEObject = it.next();
        allContainedObjects.add(containedEObject);
        for (EObject eObject : containedEObject.eCrossReferences())
        {
          allCrossReferencedObjects.add(eObject);
          if (eObject.eIsProxy())
          {
            migrationCopier.copy(eObject);
          }
        }
      }

      allCrossReferencedObjects.removeAll(allContainedObjects);
      for (EObject eObject : allCrossReferencedObjects)
      {
        migrationCopier.copyAsProxy(eObject);
      }

      RuntimeException runtimeException = null;
      try
      {
        migrationCopier.copyAll(resource.getContents());
      }
      catch (RuntimeException ex)
      {
        runtimeException = ex;
      }

      try
      {
        migrationCopier.copyReferences();
      }
      catch (RuntimeException ex)
      {
        if (runtimeException == null)
        {
          runtimeException = ex;
        }
      }

      for (EObject eObject : resource.getContents())
      {
        EObject copiedEObject = migrationCopier.get(eObject);
        if (copiedEObject != null)
        {
          result.add(copiedEObject);
        }
      }

      if (runtimeException != null)
      {
        throw runtimeException;
      }

      Set<ModelElement> annotatedModelElements = new LinkedHashSet<ModelElement>();
      for (TreeIterator<Object> it = EcoreUtil.getAllContents(result); it.hasNext();)
      {
        Object object = it.next();
        if (object instanceof Annotation)
        {
          Annotation annotation = (Annotation)object;
          if (BaseAnnotationConstants.ANNOTATION_SOURCE.equals(annotation.getSource()))
          {
            annotatedModelElements.add(annotation.getModelElement());
          }
        }
      }

      for (ModelElement modelElement : annotatedModelElements)
      {
        try
        {
          ReflectUtil.invokeMethod(ReflectUtil.getMethod(modelElement.getClass(), "eMigrate"), modelElement);
        }
        catch (ReflectionException exception)
        {
          // Ignore.
        }
        catch (IllegalArgumentException ex)
        {
          throw new RuntimeException(ex);
        }
      }

      return result;
    }
  }
}
