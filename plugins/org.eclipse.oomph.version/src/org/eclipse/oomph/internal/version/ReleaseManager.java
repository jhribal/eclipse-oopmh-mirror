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
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.internal.version.Activator.ReleaseCheckMode;
import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IElement.Type;
import org.eclipse.oomph.version.IRelease;
import org.eclipse.oomph.version.IReleaseManager;
import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

import org.osgi.framework.Version;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author Eike Stepper
 */
public class ReleaseManager implements IReleaseManager
{
  private Map<IRelease, Long> releases = new WeakHashMap<IRelease, Long>();

  private SAXParserFactory parserFactory;

  public ReleaseManager()
  {
  }

  private SAXParser getParser() throws ParserConfigurationException, SAXException
  {
    if (parserFactory == null)
    {
      parserFactory = SAXParserFactory.newInstance();
    }

    return parserFactory.newSAXParser();
  }

  public synchronized IRelease getRelease(IFile file) throws CoreException
  {
    try
    {
      for (Entry<IRelease, Long> entry : releases.entrySet())
      {
        IRelease release = entry.getKey();
        if (release.getFile().equals(file))
        {
          long timeStamp = entry.getValue();
          if (file.getLocalTimeStamp() == timeStamp)
          {
            return release;
          }

          releases.remove(release);
          break;
        }
      }

      String releasePath = file.getFullPath().toString();
      ReleaseCheckMode releaseCheckMode = Activator.getReleaseCheckMode(releasePath);
      if (releaseCheckMode == null)
      {
        Activator.setReleaseCheckMode(releasePath, ReleaseCheckMode.FULL);
      }

      if (!file.exists())
      {
        throw new FileNotFoundException(releasePath);
      }

      IRelease release = new Release(getParser(), file);
      releases.put(release, file.getLocalTimeStamp());
      return release;
    }
    catch (CoreException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, ex.getLocalizedMessage(), ex));
    }
  }

  public Map<IElement, IElement> createElements(String path, boolean resolve)
  {
    Map<IElement, IElement> elements = new HashMap<IElement, IElement>();
    for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
    {
      if (project.isOpen())
      {
        VersionBuilderArguments args = new VersionBuilderArguments(project);
        String releasePath = args.getReleasePath();
        if (path.equals(releasePath))
        {
          IModel componentModel = VersionUtil.getComponentModel(project);
          IElement element = createElement(componentModel, true, resolve);
          elements.put(element, element);
        }
      }
    }

    Set<IElement> keySet = elements.keySet();
    ArrayList<IElement> queue = new ArrayList<IElement>(keySet);
    for (int i = 0; i < queue.size(); i++)
    {
      IElement element = queue.get(i);
      for (IElement child : element.getChildren())
      {
        if (!elements.containsKey(child))
        {
          IModel childModel = getComponentModel(child.trimVersion());
          if (childModel != null)
          {
            IElement topElement = createElement(childModel, true, resolve);
            queue.add(topElement);
            elements.put(topElement, topElement);
          }
          else
          {
            elements.put(child, child);
          }
        }
      }
    }
    return elements;
  }

  public synchronized IRelease createRelease(IFile file) throws CoreException, IOException, NoSuchAlgorithmException
  {
    Release release = new Release(file);
    String path = file.getFullPath().toString();

    release.getElements().putAll(createElements(path, true));

    release.write();
    releases.put(release, file.getLocalTimeStamp());
    return release;
  }

  public IElement createElement(IModel componentModel, boolean withFeatureContent, boolean resolve)
  {
    if (componentModel instanceof IPluginModelBase)
    {
      IPluginModelBase pluginModel = (IPluginModelBase)componentModel;
      BundleDescription description = pluginModel.getBundleDescription();
      if (description == null)
      {
        throw new IllegalStateException("No bundle description for " + pluginModel.getInstallLocation());
      }

      String name = description.getSymbolicName();
      Version version = description.getVersion();
      return new Element(Type.PLUGIN, name, version);
    }

    return createFeatureElement(componentModel, withFeatureContent, resolve);
  }

  @SuppressWarnings("restriction")
  private IElement createFeatureElement(IModel componentModel, boolean withContent, boolean resolve)
  {
    org.eclipse.pde.internal.core.ifeature.IFeatureModel featureModel = (org.eclipse.pde.internal.core.ifeature.IFeatureModel)componentModel;
    org.eclipse.pde.internal.core.ifeature.IFeature feature = featureModel.getFeature();

    String name = feature.getId();
    Version version = new Version(feature.getVersion());
    IElement element = new Element(Type.FEATURE, name, version);

    if (withContent)
    {
      String licenseFeatureID = feature.getLicenseFeatureID();
      if (licenseFeatureID.length() != 0)
      {
        Element child = new Element(IElement.Type.FEATURE, licenseFeatureID, feature.getLicenseFeatureVersion());
        if (resolve)
        {
          child.resolveVersion();
        }

        child.setLicenseFeature(true);
        element.getChildren().add(child);
      }

      for (org.eclipse.pde.internal.core.ifeature.IFeatureChild versionable : feature.getIncludedFeatures())
      {
        Element child = new Element(IElement.Type.FEATURE, versionable.getId(), versionable.getVersion());
        if (resolve)
        {
          child.resolveVersion();
        }

        element.getChildren().add(child);
      }

      for (org.eclipse.pde.internal.core.ifeature.IFeaturePlugin versionable : feature.getPlugins())
      {
        Element child = new Element(IElement.Type.PLUGIN, versionable.getId(), versionable.getVersion());
        if (resolve)
        {
          child.resolveVersion();
        }

        element.getChildren().add(child);
      }
    }

    return element;
  }

  @SuppressWarnings("restriction")
  public IModel getComponentModel(IElement element)
  {
    String name = element.getName();
    if (element.getType() == IElement.Type.PLUGIN)
    {
      IPluginModelBase model = PluginRegistry.findModel(name);
      if (name.endsWith(".source") && model != null && model.getUnderlyingResource() == null)
      {
        return null;
      }

      if (!element.isVersionUnresolved())
      {
        Version pluginVersion = VersionUtil.normalize(model.getBundleDescription().getVersion());
        if (!element.getVersion().equals(pluginVersion))
        {
          return null;
        }
      }

      return model;
    }

    org.eclipse.pde.internal.core.FeatureModelManager manager = org.eclipse.pde.internal.core.PDECore.getDefault().getFeatureModelManager();
    org.eclipse.pde.internal.core.ifeature.IFeatureModel[] featureModels = manager.getWorkspaceModels();

    org.eclipse.pde.internal.core.ifeature.IFeatureModel featureModel = getFeatureModel(name, featureModels);
    if (featureModel == null)
    {
      featureModels = manager.getExternalModels();
      featureModel = getFeatureModel(name, featureModels);
    }

    if (name.endsWith(".source") && featureModel != null && featureModel.getUnderlyingResource() == null)
    {
      return null;
    }

    if (!element.isVersionUnresolved())
    {
      org.eclipse.pde.internal.core.ifeature.IFeature feature = featureModel.getFeature();
      Version featureVersion = VersionUtil.normalize(new Version(feature.getVersion()));
      if (!element.getVersion().equals(featureVersion))
      {
        return null;
      }
    }

    return featureModel;
  }

  @SuppressWarnings("restriction")
  private org.eclipse.pde.internal.core.ifeature.IFeatureModel getFeatureModel(String name, org.eclipse.pde.internal.core.ifeature.IFeatureModel[] featureModels)
  {
    Version highestVersion = null;
    org.eclipse.pde.internal.core.ifeature.IFeatureModel highestModel = null;

    for (org.eclipse.pde.internal.core.ifeature.IFeatureModel featureModel : featureModels)
    {
      org.eclipse.pde.internal.core.ifeature.IFeature feature = featureModel.getFeature();
      String id = feature.getId();
      if (id.equals(name))
      {
        Version newVersion = new Version(feature.getVersion());
        if (highestVersion == null || highestVersion.compareTo(newVersion) < 0)
        {
          highestVersion = newVersion;
          highestModel = featureModel;
        }
      }
    }

    if (highestModel == null)
    {
      return null;
    }

    return highestModel;
  }
}
