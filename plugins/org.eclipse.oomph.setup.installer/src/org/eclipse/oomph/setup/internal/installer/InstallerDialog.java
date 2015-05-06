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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.ConfirmationPage;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardDialog;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.OomphPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * @author Eike Stepper
 */
public final class InstallerDialog extends SetupWizardDialog implements InstallerUI
{
  private final IPageChangedListener pageChangedListener = new PageChangedListener();

  private final boolean restarted;

  private ToolItem updateToolItem;

  private boolean updateSearching;

  private Resolution updateResolution;

  private IStatus updateError;

  private String version;

  private Link versionLink;

  public InstallerDialog(Shell parentShell, boolean restarted)
  {
    this(parentShell, new SetupWizard.Installer(), restarted);
  }

  public InstallerDialog(Shell parentShell, Installer installer, boolean restarted)
  {
    super(parentShell, installer);
    this.restarted = restarted;
    addPageChangedListener(pageChangedListener);
  }

  public Installer getInstaller()
  {
    return (Installer)getWizard();
  }

  public boolean refreshJREs()
  {
    ProductPage page = (ProductPage)getWizard().getPage(ProductPage.PAGE_NAME);
    if (page != null)
    {
      return page.refreshJREs();
    }

    return false;
  }

  @Override
  public void create()
  {
    super.create();

    final Shell shell = getShell();
    shell.addTraverseListener(new TraverseListener()
    {
      public void keyTraversed(TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_ESCAPE)
        {
          shell.close();
          e.detail = SWT.TRAVERSE_NONE;
          e.doit = false;
        }
      }
    });

    shell.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        final Runnable checkIndex = this;
        final Installer installer = getInstaller();
        installer.getIndexLoader().awaitIndexLoad();
        shell.getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            if (installer.getCatalogManager().getIndex() == null)
            {
              int answer = new MessageDialog(shell, "Network Problem", null,
                  "The catalog could not be loaded. Please ensure that you have network access and, if needed, have configured your network proxy.",
                  MessageDialog.ERROR, new String[] { "Retry", "Configure Network Proxy...", "Exit" }, 0).open();
              switch (answer)
              {
                case 0:
                  installer.reloadIndex();
                  shell.getDisplay().asyncExec(checkIndex);
                  return;

                case 1:
                  new NetworkConnectionsDialog(getShell()).open();
                  installer.reloadIndex();
                  shell.getDisplay().asyncExec(checkIndex);
                  return;

                default:
                  close();
                  return;
              }
            }
          }
        });
      }
    });
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    Control helpControl = super.createHelpControl(parent);
    setProductVersionLink(parent);
    return helpControl;
  }

  @Override
  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
    ToolItem networkProxySettingsToolItem = createToolItem(toolBar, "install_prefs_proxy", "Network proxy settings");
    networkProxySettingsToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new NetworkConnectionsDialog(getShell());
        dialog.open();
      }
    });
    AccessUtil.setKey(networkProxySettingsToolItem, "proxy");

    ToolItem sshSettingsToolItem = createToolItem(toolBar, "install_prefs_ssh2", "SSH2 settings");
    sshSettingsToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new NetworkSSH2Dialog(getShell());
        dialog.open();
      }
    });
    AccessUtil.setKey(sshSettingsToolItem, "ssh");

    ToolItem simpleToolItem = createToolItem(toolBar, "simple", "Switch to simple mode");
    simpleToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        close();
        setReturnCode(RETURN_SIMPLE);
      }
    });
    AccessUtil.setKey(simpleToolItem, "simple");

    updateToolItem = createToolItem(toolBar, "install_update0", "Update");
    updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_searching0"));
    updateToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        selfUpdate();
      }
    });
    AccessUtil.setKey(updateToolItem, "update");
  }

  protected final ToolItem createToolItem(ToolBar toolBar, String iconPath, String toolTip)
  {
    ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
    if (iconPath == null)
    {
      toolItem.setText(toolTip);
    }
    else
    {
      Image image = SetupInstallerPlugin.INSTANCE.getSWTImage(iconPath);
      toolItem.setImage(image);
      toolItem.setToolTipText(toolTip);
    }

    return toolItem;
  }

  private void selfUpdate()
  {
    updateError = null;
    setUpdateIcon(0);

    if (updateResolution == null)
    {
      initUpdateSearch();
    }
    else
    {
      Runnable successRunnable = new Runnable()
      {
        public void run()
        {
          close();
          setReturnCode(RETURN_RESTART);
        }
      };

      ExceptionHandler<CoreException> exceptionHandler = new ExceptionHandler<CoreException>()
      {
        public void handleException(CoreException ex)
        {
          updateError = ex.getStatus();
        }
      };

      Runnable finalRunnable = new Runnable()
      {
        public void run()
        {
          updateResolution = null;
          setUpdateIcon(0);
        }
      };

      SelfUpdate.update(getShell(), updateResolution, successRunnable, exceptionHandler, finalRunnable);
    }
  }

  private void initUpdateSearch()
  {
    updateSearching = true;

    Thread updateIconSetter = new UpdateIconAnimator();
    updateIconSetter.start();

    Thread updateSearcher = new UpdateSearcher();
    updateSearcher.start();
  }

  private void setUpdateIcon(final int icon)
  {
    updateToolItem.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        if (updateToolItem == null || updateToolItem.isDisposed())
        {
          return;
        }

        try
        {
          if (updateSearching)
          {
            updateToolItem.setToolTipText("Checking for updates...");
            updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_searching" + icon + ""));
            updateToolItem.setEnabled(false);
          }
          else if (updateError != null)
          {
            StringBuilder builder = new StringBuilder();
            formatStatus(builder, "", updateError);
            updateToolItem.setToolTipText(builder.toString());
            updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_error"));
            updateToolItem.setEnabled(true);
          }
          else if (updateResolution != null)
          {
            updateToolItem.setToolTipText("Install available updates");
            updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update" + icon + ""));
            updateToolItem.setEnabled(true);
          }
          else
          {
            updateToolItem.setToolTipText("No updates available");
            updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update_disabled"));
            updateToolItem.setEnabled(false);
          }
        }
        catch (Exception ex)
        {
          // Ignore
        }
      }

      private void formatStatus(StringBuilder builder, String indent, IStatus status)
      {
        if (builder.length() != 0)
        {
          builder.append('\n');
        }

        builder.append(indent);
        builder.append(status.getMessage());

        for (IStatus child : status.getChildren())
        {
          formatStatus(builder, indent + "   ", child);
        }
      }
    });
  }

  private void setProductVersionLink(Composite parent)
  {
    GridLayout parentLayout = (GridLayout)parent.getLayout();
    parentLayout.numColumns++;
    parentLayout.horizontalSpacing = 10;

    versionLink = new Link(parent, SWT.NO_FOCUS);
    versionLink.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER));
    versionLink.setToolTipText("About");
    AccessUtil.setKey(versionLink, "version");

    Thread thread = new ProductVersionInitializer();
    thread.start();
  }

  public int show()
  {
    return open();
  }

  public void showAbout()
  {
    new AboutDialog(getShell(), version).open();
  }

  /**
   * @author Eike Stepper
   */
  private final class PageChangedListener implements IPageChangedListener
  {
    public void pageChanged(PageChangedEvent event)
    {
      if (event.getSelectedPage() instanceof ConfirmationPage)
      {
        updateSearching = false;
        updateResolution = null;
        updateError = null;
        setUpdateIcon(0);

        SetupTaskPerformer performer = getInstaller().getPerformer();
        performer.getBundles().add(SetupInstallerPlugin.INSTANCE.getBundle());
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ProductVersionInitializer extends Thread
  {
    private boolean selfHosting;

    public ProductVersionInitializer()
    {
      super("Product Version Initializer");
    }

    @Override
    public void run()
    {
      try
      {
        version = getProductVersion();
        if (version != null)
        {
          if (selfHosting)
          {
            updateSearching = false;
            setUpdateIcon(0);
          }
          else if (!restarted)
          {
            initUpdateSearch();
          }

          versionLink.getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                versionLink.addSelectionListener(new SelectionAdapter()
                {
                  @Override
                  public void widgetSelected(SelectionEvent e)
                  {
                    showAbout();
                  }
                });

                versionLink.setText("<a>" + version + "</a>");
                versionLink.getParent().layout();
              }
              catch (Exception ex)
              {
                SetupInstallerPlugin.INSTANCE.log(ex);
              }
            }
          });
        }
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    private String getProductVersion()
    {
      Agent agent = P2Util.getAgentManager().getCurrentAgent();

      IProfile profile = agent.getProfileRegistry().getProfile(IProfileRegistry.SELF);
      if (profile == null || "SelfHostingProfile".equals(profile.getProfileId()))
      {
        selfHosting = true;
        return "Self Hosting";
      }

      String firstBuildID = null;
      int highestBuildID = 0;

      BundleContext bundleContext = SetupInstallerPlugin.INSTANCE.getBundleContext();
      for (Bundle bundle : bundleContext.getBundles())
      {
        String symbolicName = bundle.getSymbolicName();
        if (symbolicName.startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
        {
          String buildID = OomphPlugin.getBuildID(bundle);
          if (buildID != null)
          {
            if (firstBuildID == null)
            {
              firstBuildID = buildID;
            }

            try
            {
              int id = Integer.parseInt(buildID);
              if (id > highestBuildID)
              {
                highestBuildID = id;
              }
            }
            catch (NumberFormatException ex)
            {
              //$FALL-THROUGH$
            }
          }
        }
      }

      String buildID = highestBuildID != 0 ? Integer.toString(highestBuildID) : firstBuildID;

      for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(SetupUIPlugin.PRODUCT_ID), null)))
      {
        String label;

        Version version = iu.getVersion();
        if (buildID != null && version.getSegmentCount() > 3)
        {
          label = version.getSegment(0) + "." + version.getSegment(1) + "." + version.getSegment(2);
        }
        else
        {
          label = version.toString();
        }

        if (buildID != null)
        {
          label += " Build " + buildID;
        }

        return label;
      }

      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateIconAnimator extends Thread
  {
    public UpdateIconAnimator()
    {
      super("Update Icon Animator");
    }

    @Override
    public void run()
    {
      try
      {
        for (int i = 0; updateSearching || updateResolution != null; i = ++i % 20)
        {
          if (updateToolItem == null || updateToolItem.isDisposed())
          {
            return;
          }

          int icon = i > 7 ? 0 : i;
          setUpdateIcon(icon);
          sleep(80);
        }

        setUpdateIcon(0);
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateSearcher extends Thread
  {
    public UpdateSearcher()
    {
      super("Update Searcher");
    }

    @Override
    public void run()
    {
      try
      {
        User user = getInstaller().getUser();
        updateResolution = SelfUpdate.resolve(user, null);
      }
      catch (CoreException ex)
      {
        updateError = ex.getStatus();
      }
      finally
      {
        updateSearching = false;
      }
    }
  }
}
