/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ProjectContainer;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Ed Merks
 */
public class ConfigurationProcessor
{
  protected final SetupWizard setupWizard;

  protected final Configuration configuration;

  protected final Workspace workspace;

  protected Installation installation;

  private final ScopeStatus status = new ScopeStatus(null, Messages.ConfigurationProcessor_configurationStatus_error);

  private final ScopeStatus installationStatus;

  private final ScopeStatus workspaceStatus;

  public ConfigurationProcessor(SetupWizard setupWizard)
  {
    this.setupWizard = setupWizard;
    configuration = setupWizard.getConfiguration();
    if (configuration == null)
    {
      installation = null;
      workspace = null;
    }
    else
    {
      installation = configuration.getInstallation();
      workspace = configuration.getWorkspace();
    }

    installationStatus = new ScopeStatus(installation, Messages.ConfigurationProcessor_installationStatus_error);
    status.add(installationStatus);

    workspaceStatus = new ScopeStatus(workspace, Messages.ConfigurationProcessor_workspaceStatus_error);
    status.add(workspaceStatus);
  }

  public IStatus getStatus()
  {
    status.computeSeverity();

    int okCount = 0;
    IStatus result = null;
    for (IStatus status : status.getChildren())
    {
      if (status.isOK())
      {
        ++okCount;
      }
      else
      {
        result = status;
      }
    }

    if (okCount == 1)
    {
      return result;
    }

    return status;
  }

  public boolean processInstallation()
  {
    return installation == null ? handleNullInstallation() : handleInstallation();
  }

  protected MultiStatus getStatus(final Scope scope)
  {
    if (scope instanceof Installation)
    {
      return (MultiStatus)status.getChildren()[0];
    }

    MultiStatus containerStatus = (MultiStatus)status.getChildren()[scope instanceof ProductVersion ? 0 : 1];
    for (IStatus status : containerStatus.getChildren())
    {
      ScopeStatus scopeStatus = (ScopeStatus)status;
      if (scopeStatus.getScope() == scope)
      {
        return scopeStatus;
      }
    }

    ScopeStatus scopeStatus = new ScopeStatus(scope);
    containerStatus.add(scopeStatus);
    return scopeStatus;
  }

  public boolean processWorkspace()
  {
    return workspace == null ? handleNullWorkspace() : handleWorkspace();
  }

  protected boolean handleNullInstallation()
  {
    if (configuration == null && setupWizard.isSimple())
    {
      installationStatus.add(createResourceStatus(setupWizard.getUnappliedConfigurationResources(), SetupPackage.Literals.CONFIGURATION));
      return false;
    }

    if (installation == null && workspace == null && setupWizard.isSimple())
    {
      installationStatus.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, Messages.ConfigurationProcessor_installationStatus_configEmpty));
      return false;
    }

    return true;
  }

  protected boolean handleInstallation()
  {
    ProductVersion productVersion = installation.getProductVersion();
    return productVersion == null ? handleNullProductVersion() : handleProductVersion(productVersion);
  }

  protected boolean handleNullProductVersion()
  {
    addStatus(IStatus.ERROR, installation, Messages.ConfigurationProcessor_status_noProductVersion);
    return false;
  }

  protected boolean handleProductVersion(ProductVersion productVersion)
  {
    if (productVersion.eIsProxy())
    {
      return handleProxyProductVersion(productVersion);
    }

    Product product = productVersion.getProduct();
    if (product == null)
    {
      return handleNullProduct(productVersion);
    }

    ProductCatalog productCatalog = product.getProductCatalog();
    CatalogManager catalogManager = setupWizard.getCatalogManager();
    if (productCatalog == null)
    {
      boolean added = false;
      CatalogSelector catalogSelector = new CatalogSelector(catalogManager, true);
      for (Scope scope : catalogSelector.getCatalogs())
      {
        if (isUserProductCatalog(scope))
        {
          ProductCatalog userProductCatalog = (ProductCatalog)scope;
          userProductCatalog.getProducts().add(0, product);
          BaseUtil.saveEObject(userProductCatalog);
          catalogManager.selectCatalog(true, userProductCatalog, true);
          added = true;
          break;
        }
      }

      if (!added)
      {
        return handleNoUserProductCatalog(productVersion);
      }
    }
    else
    {
      Index index = catalogManager.getIndex();
      Index productCatalogIndex = productCatalog.getIndex();
      if (productCatalogIndex == index)
      {
        if (!"self".equals(productCatalog.getName())) //$NON-NLS-1$
        {
          catalogManager.selectCatalog(true, productCatalog, true);
        }
      }
      else if (!addCatalog(true, productCatalog, productVersion))
      {
        return handleNoRedirectableProductCatalog(productVersion);
      }
    }

    if ("self.empty.product.version".equals(productVersion.getQualifiedName())) //$NON-NLS-1$
    {
      return applyEmptyProductVersion();
    }

    return applyProductVersion(productVersion);
  }

  protected boolean applyEmptyProductVersion()
  {
    return false;
  }

  protected boolean applyProductVersion(ProductVersion productVersion)
  {
    return false;
  }

  protected void applyInstallation()
  {
    SetupContext setupContext = SetupContext.create(setupWizard.getResourceSet(), null);
    Installation setupInstallation = setupContext.getInstallation();

    applyAttributes(setupInstallation, installation);
    applySetupTasks(setupInstallation, installation);

    setupWizard.addAppliedConfigurationResource(installation.eResource());
  }

  protected void applyAttributes(Scope targetScope, Scope sourceScope)
  {
    String name = sourceScope.getName();
    if (!StringUtil.isEmpty(name))
    {
      targetScope.setName(name);
    }

    String label = sourceScope.getLabel();
    if (!StringUtil.isEmpty(label))
    {
      targetScope.setLabel(label);
    }

    String description = sourceScope.getDescription();
    if (!StringUtil.isEmpty(description))
    {
      targetScope.setDescription(description);
    }

    targetScope.getAnnotations().addAll(sourceScope.getAnnotations());
  }

  protected void applySetupTasks(SetupTaskContainer targetSetupTaskContainer, SetupTaskContainer sourceSetTaskContainer)
  {
    targetSetupTaskContainer.getSetupTasks().addAll(sourceSetTaskContainer.getSetupTasks());
  }

  protected boolean handleNoRedirectableProductCatalog(ProductVersion productVersion)
  {
    return false;
  }

  protected boolean handleNoUserProductCatalog(ProductVersion productVersion)
  {
    addStatus(IStatus.ERROR, productVersion, Messages.ConfigurationProcessor_status_productNotInIndex);
    return false;
  }

  protected boolean handleProxyProductVersion(ProductVersion productVersion)
  {
    addStatus(IStatus.ERROR, productVersion, Messages.ConfigurationProcessor_status_unresolvableVersion);
    return false;
  }

  protected boolean handleNullProduct(ProductVersion productVersion)
  {
    addStatus(IStatus.ERROR, productVersion, Messages.ConfigurationProcessor_status_versionNotInProduct);
    return false;
  }

  protected boolean handleNullWorkspace()
  {
    if (configuration == null && !setupWizard.isSimple())
    {
      workspaceStatus.add(createResourceStatus(setupWizard.getUnappliedConfigurationResources(), SetupPackage.Literals.CONFIGURATION));
      return false;
    }

    if (installation == null && workspace == null && !setupWizard.isSimple())
    {
      workspaceStatus.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, Messages.ConfigurationProcessor_workspaceStatus_configEmpty));
      return false;
    }

    return true;
  }

  protected IStatus createResourceStatus(Collection<? extends Resource> resources, EClass expectedEClass)
  {
    StringBuilder uris = new StringBuilder();
    List<IStatus> childStatuses = new ArrayList<IStatus>();
    for (Resource resource : resources)
    {
      if (uris.length() != 0)
      {
        uris.append(' ');
      }

      uris.append(resource.getURI());

      EList<Resource.Diagnostic> errors = resource.getErrors();
      if (errors.isEmpty())
      {
        EList<EObject> contents = resource.getContents();
        if (contents.isEmpty())
        {
          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, Messages.ConfigurationProcessor_status_emptyResource));
        }
        else
        {
          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID,
              NLS.bind(Messages.ConfigurationProcessor_status_resourceContains, contents.get(0).eClass().getName())));
        }
      }
      else
      {
        for (Resource.Diagnostic diagnostic : errors)
        {
          String message = diagnostic.getMessage();
          Throwable throwable = null;
          if (diagnostic instanceof Throwable)
          {
            throwable = (Throwable)diagnostic;
            if (throwable instanceof XMIException)
            {
              Throwable cause = throwable.getCause();
              if (cause != null)
              {
                XMIException xmiException = (XMIException)throwable;
                message = cause.getMessage();
                int line = xmiException.getLine();
                if (line != 0)
                {
                  message += " (" + line + ", " + xmiException.getColumn() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
              }
            }
          }

          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, message, throwable));
        }
      }
    }

    return new MultiStatus(SetupUIPlugin.PLUGIN_ID, 0, childStatuses.toArray(new IStatus[childStatuses.size()]),
        NLS.bind(Messages.ConfigurationProcessor_status_couldNotLoadFromUris, expectedEClass.getName(), uris), null);
  }

  protected boolean handleWorkspace()
  {
    if (setupWizard.isSimple())
    {
      int answer = new MessageDialog(setupWizard.getShell(), Messages.ConfigurationProcessor_configHandlingDialog_title, null,
          Messages.ConfigurationProcessor_configHandlingDialog_message, MessageDialog.WARNING, installation == null ? //
              new String[] { Messages.ConfigurationProcessor_configHandlingDialog_button_advancedMode,
                  Messages.ConfigurationProcessor_configHandlingDialog_button_cancel }
              : new String[] { Messages.ConfigurationProcessor_configHandlingDialog_button_advancedMode,
                  Messages.ConfigurationProcessor_configHandlingDialog_button_applyOnlyInstallation,
                  Messages.ConfigurationProcessor_configHandlingDialog_button_cancel },
          0).open();
      switch (answer)
      {
        case 0:
        {
          if (configuration != null)
          {
            setupWizard.addAppliedConfigurationResource(configuration.eResource());
          }

          Collection<? extends Resource> configurationResources = setupWizard.getAppliedConfigurationResources();
          handleSwitchToAdvancedMode();
          setupWizard.setConfigurationResources(configurationResources);
          return false;
        }

        case 1:
        {
          if (installation != null)
          {
            return true;
          }

          return false;
        }

        default:
        {
          return false;
        }
      }
    }

    List<Stream> projectStreams = new ArrayList<Stream>();
    for (Stream stream : workspace.getStreams())
    {
      if (stream.eIsProxy())
      {
        addStatus(IStatus.ERROR, stream, Messages.ConfigurationProcessor_status_unresolvableStream);
      }
      else
      {
        Project project = stream.getProject();
        if (project == null)
        {
          addStatus(IStatus.ERROR, stream, Messages.ConfigurationProcessor_status_streamNotInProject);
        }
        else
        {
          Project rootProject = project;
          ProjectCatalog projectCatalog = null;
          for (ProjectContainer projectContainer = project.getProjectContainer(); projectContainer != null; projectContainer = projectContainer
              .getProjectContainer())
          {
            if (projectContainer instanceof Project)
            {
              rootProject = (Project)projectContainer;
            }
            else if (projectContainer instanceof ProjectCatalog)
            {
              projectCatalog = (ProjectCatalog)projectContainer;
              break;
            }
          }

          if (projectCatalog == null)
          {
            ProjectContainer logicalProjectContainer = rootProject.getLogicalProjectContainer();
            if (logicalProjectContainer == null)
            {
              addStatus(IStatus.ERROR, stream,
                  Messages.ConfigurationProcessor_status_rootProjectNotInCatalog_noContainer + " " + EcoreUtil.getURI(rootProject)); //$NON-NLS-1$
            }
            else if (logicalProjectContainer.eIsProxy())
            {
              addStatus(IStatus.ERROR, stream,
                  Messages.ConfigurationProcessor_status_rootProjectNotInCatalog_unresolvableContainer + " " + EcoreUtil.getURI(logicalProjectContainer)); //$NON-NLS-1$
            }
            else if (logicalProjectContainer instanceof Project)
            {
              addStatus(IStatus.ERROR, stream,
                  Messages.ConfigurationProcessor_status_rootProjectNotInCatalog_notInContainer + " " + EcoreUtil.getURI(logicalProjectContainer)); //$NON-NLS-1$
            }
            else
            {
              // Try to add the catalog.
              ProjectCatalog logicalProjectCatalog = (ProjectCatalog)logicalProjectContainer;
              if (handleStream(logicalProjectCatalog, stream))
              {
                // If the project is in that catalog, we're all good.
                if (logicalProjectCatalog.getProjects().contains(rootProject))
                {
                  projectStreams.add(stream);
                }
                // Try to add the root project to the user extension project of the catalog.
                else if (handleUserProject(logicalProjectCatalog, rootProject))
                {
                  projectStreams.add(stream);
                }
                else
                {
                  addStatus(IStatus.ERROR, stream, Messages.ConfigurationProcessor_status_cannotAddRootProject + " " + EcoreUtil.getURI(rootProject)); //$NON-NLS-1$
                  addStatus(IStatus.ERROR, stream,
                      Messages.ConfigurationProcessor_status_noExtensibleProjectInCatalog + " " + EcoreUtil.getURI(logicalProjectCatalog)); //$NON-NLS-1$
                }
              }
              else
              {
                // Errors already handled.
              }
            }
          }
          else
          {
            if (handleStream(projectCatalog, stream))
            {
              projectStreams.add(stream);
            }
            else
            {
              // Errors already handled.
            }
          }
        }
      }
    }

    if (projectStreams.isEmpty() ? applyNoStreams() : applyStreams(projectStreams))
    {
      return applyWorkspace();
    }

    return false;
  }

  protected boolean applyWorkspace()
  {
    SetupContext setupContext = SetupContext.create(installation, Collections.<Stream> emptyList(), null);
    Workspace setupWorkspace = setupContext.getWorkspace();

    applyAttributes(setupWorkspace, workspace);
    applySetupTasks(setupWorkspace, workspace);

    if (setupWorkspace.getStreams().isEmpty() && !setupWorkspace.getSetupTasks().isEmpty())
    {
      WorkspaceTask workspaceTask = SetupFactory.eINSTANCE.createWorkspaceTask();
      workspaceTask.setID("workspace"); //$NON-NLS-1$
      setupWorkspace.getSetupTasks().add(0, workspaceTask);
    }

    setupWizard.addAppliedConfigurationResource(workspace.eResource());

    return true;
  }

  protected boolean applyStreams(List<Stream> streams)
  {
    return true;
  }

  protected boolean applyNoStreams()
  {
    return true;
  }

  protected boolean handleNoRedirectableProjectCatalog(Stream stream)
  {
    return false;
  }

  protected boolean handleUserProject(ProjectCatalog projectCatalog, Project project)
  {
    CatalogManager catalogManager = setupWizard.getCatalogManager();
    for (Project catalogProject : projectCatalog.getProjects())
    {
      if (isUserProject(catalogProject))
      {
        catalogProject.getProjects().add(0, project);
        BaseUtil.saveEObject(catalogProject);
        catalogManager.selectCatalog(false, projectCatalog, true);
        return true;
      }
    }

    // addStatus(IStatus.ERROR, , message);

    return false;
  }

  protected boolean handleStream(ProjectCatalog projectCatalog, Stream stream)
  {
    CatalogManager catalogManager = setupWizard.getCatalogManager();
    Index projectIndex = projectCatalog.getIndex();
    Index index = catalogManager.getIndex();
    if (projectIndex != index && !addCatalog(false, projectCatalog, stream))
    {
      return handleNoRedirectableProjectCatalog(stream);
    }

    catalogManager.selectCatalog(false, projectCatalog, true);

    return true;
  }

  protected boolean addCatalog(boolean product, Scope catalogScope, Scope originatingScope)
  {
    CatalogManager catalogManager = setupWizard.getCatalogManager();
    Scope catalog = catalogManager.getCatalog(product, "redirectable"); //$NON-NLS-1$
    String label = product ? "product" : "project"; //$NON-NLS-1$ //$NON-NLS-2$
    if (catalog == null)
    {
      addStatus(IStatus.ERROR, originatingScope, NLS.bind(Messages.ConfigurationProcessor_status_cannotAddCatalog, EcoreUtil.getURI(catalogScope)));

      ResourceSet resourceSet = setupWizard.getResourceSet();
      Resource resource = resourceSet.getResource(URI.createURI("index:/redirectable." + label + "s.setup"), false); //$NON-NLS-1$ //$NON-NLS-2$
      if (resource == null)
      {
        addStatus(IStatus.ERROR, originatingScope, product ? Messages.ConfigurationProcessor_status_noRedirectableProductCatalog
            : Messages.ConfigurationProcessor_status_noRedirectableProjectCatalog);
      }
      else
      {
        addStatus(IStatus.ERROR, originatingScope,
            NLS.bind(Messages.ConfigurationProcessor_status_catalogAlreadyRedirected, label, resourceSet.getURIConverter().normalize(resource.getURI())));
      }

      return false;
    }

    Resource catalogResource = catalog.eResource();
    URI sourceURI = catalogResource.getURI();
    URI targetURI = catalogScope.eResource().getURI();
    EcoreUtil.replace(catalog, catalogScope);
    catalogManager.selectCatalog(product, catalogScope, true);
    catalogResource.getResourceSet().getURIConverter().getURIMap().put(targetURI, sourceURI);

    EList<SetupTask> setupTasks = (product ? installation : workspace).getSetupTasks();
    EclipseIniTask redirectionEclipseIniTask = SetupFactory.eINSTANCE.createEclipseIniTask();
    redirectionEclipseIniTask.setVm(true);
    redirectionEclipseIniTask.setOption("-Doomph.redirection." + (product ? "products" : "projects") + "="); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    redirectionEclipseIniTask.setValue(sourceURI + "->" + targetURI); //$NON-NLS-1$
    setupTasks.add(0, redirectionEclipseIniTask);

    return true;
  }

  protected void addStatus(int severity, Scope scope, String message)
  {
    MultiStatus containerStatus = getStatus(scope);
    containerStatus.add(new Status(severity, SetupUIPlugin.PLUGIN_ID, message));
  }

  protected void handleSwitchToAdvancedMode()
  {
  }

  public static boolean isUserProject(Project project)
  {
    Resource resource = project.eResource();
    return resource != null && SetupContext.isUserScheme(resource.getURI().scheme());
  }

  public static boolean isUserProductCatalog(Scope scope)
  {
    if (scope instanceof ProductCatalog)
    {
      Resource resource = scope.eResource();
      return resource != null && SetupContext.isUserScheme(resource.getURI().scheme());
    }

    return false;
  }

  private static class ScopeStatus extends MultiStatus
  {
    private final Scope scope;

    public ScopeStatus(Scope scope)
    {
      super(SetupUIPlugin.PLUGIN_ID, 0, NLS.bind(Messages.ConfigurationProcessor_scopeStatus_message, EcoreUtil.getURI(scope)), null);
      this.scope = scope;
    }

    public ScopeStatus(Scope scope, String message)
    {
      super(SetupUIPlugin.PLUGIN_ID, 0, message, null);
      this.scope = scope;
    }

    public Scope getScope()
    {
      return scope;
    }

    public int computeSeverity()
    {
      for (IStatus status : getChildren())
      {
        int newSev = status instanceof ScopeStatus ? ((ScopeStatus)status).computeSeverity() : status.getSeverity();
        if (newSev > getSeverity())
        {
          setSeverity(newSev);
        }
      }

      return getSeverity();
    }
  }
}
