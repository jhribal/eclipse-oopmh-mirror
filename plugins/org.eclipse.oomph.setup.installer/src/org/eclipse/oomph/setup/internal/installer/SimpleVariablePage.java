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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.jreinfo.ui.JREController;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.installer.InstallLaunchButton.State;
import org.eclipse.oomph.setup.internal.installer.MessageOverlay.Type;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.JREDownloadHandler;
import org.eclipse.oomph.setup.ui.LicensePrePrompter;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProductPage;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SimpleVariablePage extends SimpleInstallerPage
{
  private static final Preference PREF_INSTALL_ROOT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("installRoot");

  private static final File FILE_INSTALL_ROOT = new File(SetupInstallerPlugin.INSTANCE.getUserLocation().toFile(), PREF_INSTALL_ROOT.key() + ".txt");

  private static final String TEXT_README = "show readme file";

  private static final String TEXT_LOG = "show installation log";

  private static final String TEXT_KEEP = "keep installer";

  private final Map<String, ProductVersion> productVersions = new HashMap<String, ProductVersion>();

  private Product product;

  private ProductVersion selectedProductVersion;

  private String readmePath;

  private Browser browser;

  private CCombo versionCombo;

  private ImageCheckbox bitness32Button;

  private ImageCheckbox bitness64Button;

  private JREController javaController;

  private Label javaLabel;

  private ComboViewer javaViewer;

  private SimpleInstallerButton javaButton;

  private Text folderText;

  private SimpleInstallerButton folderButton;

  private String installRoot;

  private String installFolder;

  private Thread installThread;

  private StackComposite installStack;

  private InstallLaunchButton installButton;

  private String installError;

  private boolean installed;

  private SetupTaskPerformer performer;

  private SimpleProgress progress;

  private SimpleInstallerButton cancelButton;

  private SimpleInstallerButton showReadmeButton;

  private SimpleInstallerButton keepInstallerButton;

  private Composite afterInstallComposite;

  private SimpleInstallerButton showInstallLogButton;

  private Composite errorComposite;

  public SimpleVariablePage(final Composite parent, final SimpleInstallerDialog dialog)
  {
    super(parent, dialog, true);
  }

  @Override
  protected void createContent(Composite container)
  {
    container.setBackgroundMode(SWT.INHERIT_FORCE);
    container.setBackground(SimpleInstallerDialog.COLOR_WHITE);

    // Row 1
    GridData browserLayoutData = GridDataFactory.fillDefaults().grab(true, false).create();
    browserLayoutData.heightHint = OS.INSTANCE.isLinux() ? 120 : 216;

    Composite browserComposite = new Composite(container, SWT.NONE);
    browserComposite.setLayoutData(browserLayoutData);
    browserComposite.setLayout(new FillLayout());

    browser = new Browser(browserComposite, SWT.NO_SCROLL);
    browser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        String url = event.location;
        if (!"about:blank".equals(url))
        {
          OS.INSTANCE.openSystemBrowser(url);
          event.doit = false;
        }
      }
    });

    Composite variablesComposite = new Composite(container, SWT.NONE);
    GridLayout variablesLayout = new GridLayout(4, false);
    variablesLayout.horizontalSpacing = 8;
    variablesLayout.verticalSpacing = 3;
    variablesLayout.marginLeft = 14;
    variablesLayout.marginRight = 30;
    variablesLayout.marginTop = 40;
    variablesLayout.marginBottom = 0;
    variablesLayout.marginHeight = 0;
    variablesComposite.setLayout(variablesLayout);
    variablesComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

    // Row 3
    Label prodVersionLabel = createLabel(variablesComposite, "Product Version");
    prodVersionLabel.setLayoutData(GridDataFactory.swtDefaults().hint(135, SWT.DEFAULT).create());

    versionCombo = createComboBox(variablesComposite, SWT.READ_ONLY);
    versionCombo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String label = versionCombo.getItem(versionCombo.getSelectionIndex());
        productVersionSelected(productVersions.get(label));
        UIUtil.clearTextSelection(versionCombo);
      }
    });

    bitness32Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/32bit_hover.png"));
    bitness32Button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).indent(4, 0).hint(SWT.DEFAULT, 30).create());
    bitness32Button.setChecked(false);
    bitness32Button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        bitness32Button.setChecked(true);
        bitness64Button.setChecked(false);
        javaController.setBitness(32);
      }
    });
    bitness32Button.setVisible(JREManager.BITNESS_CHANGEABLE);

    bitness64Button = new ImageCheckbox(variablesComposite, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/64bit_hover.png"));
    bitness64Button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).hint(SWT.DEFAULT, 30).create());
    bitness64Button.setChecked(true);
    bitness64Button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        bitness32Button.setChecked(false);
        bitness64Button.setChecked(true);
        javaController.setBitness(64);
      }
    });
    bitness64Button.setVisible(JREManager.BITNESS_CHANGEABLE);

    // Row 4
    javaLabel = createLabel(variablesComposite, "Java VM");

    CCombo javaCombo = createComboBox(variablesComposite, SWT.READ_ONLY);
    applyComboOrTextStyle(javaCombo);

    javaViewer = new ComboViewer(javaCombo);
    javaViewer.setContentProvider(new ArrayContentProvider());
    javaViewer.setLabelProvider(new LabelProvider());

    javaButton = new ImageHoverButton(variablesComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_hover.png"));
    javaButton.setLayoutData(GridDataFactory.swtDefaults().indent(4, 0).create());
    javaButton.setToolTipText("Select Java VM...");
    javaButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        javaController.configureJREs();
      }
    });

    JREDownloadHandler downloadHandler = new JREDownloadHandler()
    {
      @Override
      protected Product getProduct()
      {
        return product;
      }
    };

    javaController = new JREController(javaLabel, javaViewer, downloadHandler)
    {
      @Override
      protected void modelEmpty(boolean empty)
      {
        super.modelEmpty(empty);
        installButton.setEnabled(!empty);
      }

      @Override
      protected void setLabel(String text)
      {
        super.setLabel(text + " ");
      }
    };

    new Label(variablesComposite, SWT.NONE);

    // Row 5
    createLabel(variablesComposite, "Installation Folder");

    folderText = createTextField(variablesComposite);
    folderText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        String dir = folderText.getText();
        validateFolderText(dir);
      }
    });

    folderButton = new ImageHoverButton(variablesComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/folder_hover.png"));
    folderButton.setLayoutData(GridDataFactory.swtDefaults().indent(4, 0).create());
    folderButton.setToolTipText("Select installation folder...");
    folderButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText(AbstractSetupDialog.SHELL_TEXT);
        dialog.setMessage("Select installation folder...");

        if (!StringUtil.isEmpty(installRoot))
        {
          dialog.setFilterPath(installRoot);
        }

        String dir = dialog.open();
        if (dir != null)
        {
          validateFolderText(dir);
          setFolderText(dir);
        }
      }
    });

    new Label(variablesComposite, SWT.NONE);
    new Label(variablesComposite, SWT.NONE);

    installButton = new InstallLaunchButton(variablesComposite);
    installButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.BEGINNING).hint(SWT.DEFAULT, 36).indent(0, 32).create());
    installButton.setCurrentState(InstallLaunchButton.State.INSTALL);

    new Label(variablesComposite, SWT.NONE);
    new Label(variablesComposite, SWT.NONE);

    installStack = new StackComposite(variablesComposite, SWT.NONE);
    installStack.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BEGINNING).span(4, 1).indent(60, 0).hint(SWT.DEFAULT, 72).create());

    final Composite duringInstallContainer = new Composite(installStack, SWT.NONE);
    duringInstallContainer.setLayout(UIUtil.createGridLayout(1));

    // during installation
    cancelButton = createButton(duringInstallContainer, "Cancel Installation", "Cancel", SetupInstallerPlugin.INSTANCE.getSWTImage("simple/delete.png"));
    cancelButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        installCancel();
      }
    });

    afterInstallComposite = new Composite(installStack, SWT.NONE);
    GridLayout afterInstallLayoutData = UIUtil.createGridLayout(1);
    afterInstallLayoutData.verticalSpacing = 3;
    afterInstallComposite.setLayout(afterInstallLayoutData);

    showReadmeButton = createButton(afterInstallComposite, TEXT_README, null, null);
    showReadmeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (readmePath != null)
        {
          java.net.URI readmeURI = new File(installFolder, OS.INSTANCE.getEclipseDir() + "/" + readmePath).toURI();
          dialog.showReadme(readmeURI);
        }
      }
    });

    showInstallLogButton = createButton(afterInstallComposite, TEXT_LOG, null, null);
    showInstallLogButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        File installationLogFile = new File(installFolder, OS.INSTANCE.getEclipseDir() + "/configuration/org.eclipse.oomph.setup/setup.log");
        dialog.showInstallationLog(installationLogFile);
      }
    });

    keepInstallerButton = createButton(afterInstallComposite, TEXT_KEEP, null, null);
    keepInstallerButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        dialog.showKeepInstaller();
      }
    });

    installButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (installed)
        {
          launchProduct();
        }
        else
        {
          dialog.clearMessage();
          dialog.setButtonsEnabled(false);

          setEnabled(false);

          installButton.setCurrentState(State.INSTALLING);
          installButton.setProgress(0);

          installStack.setTopControl(duringInstallContainer);
          installStack.setVisible(true);
          layout(true, true);

          install();
        }
      }
    });

    errorComposite = new Composite(installStack, SWT.NONE);
    GridLayout errorLayout = UIUtil.createGridLayout(1);
    errorLayout.verticalSpacing = 0;
    errorComposite.setLayout(errorLayout);

    // Just for debugging
    // installStack.setVisible(true);
    // installStack.setTopControl(errorComposite);
    // installButton.setProgress(0.98f);
    // installButton.setCurrentState(InstallLaunchButton.State.INSTALLING);
    // installButton.setEnabled(false);

  }

  private SimpleInstallerButton createButton(Composite parent, String text, String toolTip, Image icon)
  {
    SimpleInstallerButton button = new SimpleInstallerButton(parent, SWT.PUSH);
    button.setBackground(SetupInstallerPlugin.COLOR_LIGHTEST_GRAY);
    button.setText(text);
    button.setCornerWidth(10);
    button.setAlignment(SWT.CENTER);
    button.setFont(SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///10/normal")));
    button.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BEGINNING).grab(false, false).hint(232, 22).create());
    button.setForeground(SetupInstallerPlugin.COLOR_LABEL_FOREGROUND);

    if (icon != null)
    {
      button.setImage(icon);
    }

    if (toolTip != null)
    {
      button.setToolTipText(toolTip);
    }
    return button;
  }

  protected void productVersionSelected(ProductVersion productVersion)
  {
    if (selectedProductVersion != productVersion)
    {
      selectedProductVersion = productVersion;

      String requiredJavaVersion = productVersion.getRequiredJavaVersion();
      javaController.setJavaVersion(requiredJavaVersion);

      ProductPage.saveProductVersionSelection(installer.getCatalogManager(), selectedProductVersion);
    }
  }

  @Override
  public boolean setFocus()
  {
    return folderText.setFocus();
  }

  public void setProduct(Product product)
  {
    this.product = product;

    String simpleInstallerHtml = SimpleInstallerDialog.getSimpleInstallerHtmlTemplate();
    simpleInstallerHtml = simpleInstallerHtml.replace("%CONTENT%", SimpleProductPage.renderProduct(product, true, false));

    browser.setText(simpleInstallerHtml, true);

    productVersions.clear();
    versionCombo.removeAll();

    ProductVersion defaultProductVersion = ProductPage.getDefaultProductVersion(installer.getCatalogManager(), product);
    int i = 0;
    int selection = 0;

    for (ProductVersion productVersion : ProductPage.getValidProductVersions(product))
    {
      String label = productVersion.getLabel();
      if (label == null)
      {
        label = productVersion.getName();
      }

      if (defaultProductVersion == null)
      {
        defaultProductVersion = productVersion;
      }

      productVersions.put(label, productVersion);
      versionCombo.add(label);

      if (productVersion == defaultProductVersion)
      {
        selection = i;
      }

      ++i;
    }

    versionCombo.select(selection);
    versionCombo.setSelection(new Point(0, 0));
    productVersionSelected(defaultProductVersion);

    installFolder = getDefaultInstallationFolder();
    setFolderText(installFolder);

    installButton.setCurrentState(State.INSTALL);

    installStack.setVisible(false);
    installed = false;

    setEnabled(true);
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    versionCombo.setEnabled(enabled);

    if (JREManager.BITNESS_CHANGEABLE)
    {
      int bitness = javaController.getBitness();
      bitness32Button.setEnabled(enabled);
      bitness32Button.setVisible(enabled || bitness == 32);
      bitness64Button.setEnabled(enabled);
      bitness64Button.setVisible(enabled || bitness == 64);
    }

    javaViewer.getControl().setEnabled(enabled);
    javaButton.setEnabled(enabled);

    folderText.setEnabled(enabled);
    folderButton.setEnabled(enabled);
  }

  public boolean refreshJREs()
  {
    if (javaController != null)
    {
      javaController.refresh();
      return true;
    }

    return false;
  }

  private String getDefaultInstallationFolder()
  {
    String name = product.getName();
    int lastDot = name.lastIndexOf('.');
    if (lastDot != -1)
    {
      name = name.substring(lastDot + 1);
    }

    name += "-" + selectedProductVersion.getName().replace('.', '-');

    if (installRoot == null)
    {
      if (FILE_INSTALL_ROOT.isFile())
      {
        List<String> lines = IOUtil.readLines(FILE_INSTALL_ROOT, "UTF-8");
        if (lines != null && !lines.isEmpty())
        {
          installRoot = lines.get(0);
          if (installRoot.length() == 0)
          {
            installRoot = null;
          }
        }
      }

      if (installRoot == null)
      {
        installRoot = PREF_INSTALL_ROOT.get(PropertiesUtil.USER_HOME);
      }
    }

    for (int i = 1; i < 1000; i++)
    {
      String filename = name;
      if (i != 1)
      {
        filename += i;
      }

      File folder = new File(installRoot, filename);
      if (!folder.exists())
      {
        return folder.getAbsolutePath();
      }
    }

    throw new IllegalStateException("User home is full");
  }

  private void install()
  {
    installThread = new Thread()
    {
      @Override
      public void run()
      {
        installError = null;
        performer = null;
        progress = new SimpleProgress();

        try
        {
          installPerform();
        }
        catch (InterruptedException ex)
        {
          progress.setCanceled(true);
        }
        catch (OperationCanceledException ex)
        {
          progress.setCanceled(true);
        }
        catch (Exception ex)
        {
          if (!progress.isCanceled())
          {
            SetupInstallerPlugin.INSTANCE.log(ex);
            installError = ex.getMessage();
            if (StringUtil.isEmpty(installError))
            {
              installError = ex.getClass().getName();
            }
          }
        }
        finally
        {
          if (performer != null)
          {
            IOUtil.close(performer.getLogStream());
          }

          if (!progress.isCanceled())
          {
            UIUtil.syncExec(new Runnable()
            {
              public void run()
              {
                try
                {
                  installFinished();
                }
                catch (SWTException ex)
                {
                  //$FALL-THROUGH$
                }
              }
            });
          }
        }
      }
    };

    installThread.setDaemon(true);
    installThread.start();
  }

  private void installPerform() throws Exception
  {
    if (dialog.getPool() != null)
    {
      P2Util.getAgentManager().setDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName(), dialog.getPool());
      System.setProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION, dialog.getPool().getLocation().getAbsolutePath());
    }
    else
    {
      System.clearProperty(AgentManager.PROP_BUNDLE_POOL_LOCATION);
    }

    JRE jre = javaController.getJRE();
    String vmPath = new File(jre.getJavaHome(), "bin").getAbsolutePath();

    ResourceSet resourceSet = installer.getResourceSet();
    URIConverter uriConverter = resourceSet.getURIConverter();

    SetupContext setupContext = SetupContext.create(resourceSet, selectedProductVersion);
    Installation installation = setupContext.getInstallation();
    final User user = setupContext.getUser();

    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        EList<LicenseInfo> licenses = LicensePrePrompter.execute(getShell(), user);
        if (licenses != null)
        {
          user.getAcceptedLicenses().addAll(licenses);
          BaseUtil.saveEObject(user);
        }
      }
    });

    UserAdjuster userAdjuster = new UserAdjuster();
    userAdjuster.adjust(user, installFolder);
    IOUtil.writeLines(FILE_INSTALL_ROOT, "UTF-8", Collections.singletonList(installRoot));

    SimplePrompter prompter = new SimplePrompter();

    performer = SetupTaskPerformer.create(uriConverter, prompter, Trigger.BOOTSTRAP, setupContext, false);
    performer.getUnresolvedVariables().clear();
    performer.put(ILicense.class, ProgressPage.LICENSE_CONFIRMER);
    performer.put(Certificate.class, UnsignedContentDialog.createUnsignedContentConfirmer(user, false));
    performer.setOffline(false);
    performer.setMirrors(true);
    performer.setVMPath(vmPath);
    performer.setProgress(progress);
    performer.log("Executing " + performer.getTrigger().toString().toLowerCase() + " tasks");
    performer.perform(progress);
    performer.recordVariables(installation, null, user);
    performer.savePasswords();

    File configurationLocation = performer.getProductConfigurationLocation();
    installation.eResource().setURI(URI.createFileURI(new File(configurationLocation, "org.eclipse.oomph.setup/installation.setup").toString()));
    BaseUtil.saveEObject(installation);

    userAdjuster.undo();
    BaseUtil.saveEObject(user);
  }

  private void installCancel()
  {
    if (progress != null)
    {
      progress.setCanceled(true);
    }

    if (installThread != null)
    {
      installThread.interrupt();
    }

    dialog.setButtonsEnabled(true);
    setEnabled(true);

    installButton.setCurrentState(State.INSTALL);
    installStack.setVisible(false);
  }

  private void installFinished()
  {
    readmePath = null;

    if (installError == null)
    {
      installed = true;

      dialog.showMessage("Installation complete", Type.SUCCESS, true);

      showInstallLogButton.setParent(afterInstallComposite);
      installStack.setTopControl(afterInstallComposite);
      installStack.setVisible(true);
      layout(true, true);

      installButton.setCurrentState(State.INSTALLED);
      installButton.setToolTipText("Launch");

      Scope scope = selectedProductVersion;
      while (scope != null)
      {
        Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
        if (annotation != null)
        {
          readmePath = annotation.getDetails().get(AnnotationConstants.KEY_README_PATH);
          if (readmePath != null)
          {
            showReadmeButton.setEnabled(true);
            break;
          }
        }

        scope = scope.getParentScope();
      }

      keepInstallerButton.setVisible(InstallerUtil.canKeepInstaller());
    }
    else
    {
      setEnabled(true);
      installButton.setCurrentState(State.INSTALL);
      showInstallError(installError);
    }

    backButton.setEnabled(true);

    dialog.setButtonsEnabled(true);
  }

  private void showInstallError(String message)
  {
    dialog.showMessage(message, Type.ERROR, false);

    installStack.setTopControl(errorComposite);
    installStack.setVisible(true);
    layout(true, true);
  }

  private void launchProduct()
  {
    try
    {
      ProgressPage.launchProduct(performer);
    }
    catch (Exception ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }

    dialog.exitSelected();
  }

  private void setFolderText(String dir)
  {
    folderText.setText(dir);
  }

  private void validateFolderText(String dir)
  {
    installFolder = dir;
    // TODO validate dir?

    try
    {
      File folder = new File(installFolder);

      File parentFolder = folder.getParentFile();
      if (parentFolder != null)
      {
        installRoot = parentFolder.getAbsolutePath();
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class UserAdjuster
  {
    private static final URI INSTALLATION_LOCATION_ATTRIBUTE_URI = SetupTaskPerformer.getAttributeURI(SetupPackage.Literals.INSTALLATION_TASK__LOCATION);

    private EList<AttributeRule> attributeRules;

    private String oldValue;

    private void adjust(User user, String installFolder)
    {
      attributeRules = user.getAttributeRules();
      for (AttributeRule attributeRule : attributeRules)
      {
        if (INSTALLATION_LOCATION_ATTRIBUTE_URI.equals(attributeRule.getAttributeURI()))
        {
          oldValue = attributeRule.getValue();
          attributeRule.setValue(installFolder);
          return;
        }
      }

      AttributeRule attributeRule = SetupFactory.eINSTANCE.createAttributeRule();
      attributeRule.setAttributeURI(INSTALLATION_LOCATION_ATTRIBUTE_URI);
      attributeRule.setValue(installFolder);
      attributeRules.add(attributeRule);
    }

    public void undo()
    {
      for (Iterator<AttributeRule> it = attributeRules.iterator(); it.hasNext();)
      {
        AttributeRule attributeRule = it.next();
        if (INSTALLATION_LOCATION_ATTRIBUTE_URI.equals(attributeRule.getAttributeURI()))
        {
          if (oldValue == null)
          {
            it.remove();
          }
          else
          {
            attributeRule.setValue(oldValue);
          }

          return;
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SimplePrompter extends HashMap<String, String>implements SetupPrompter
  {
    private static final long serialVersionUID = 1L;

    public SimplePrompter()
    {
    }

    public boolean promptVariables(List<? extends SetupTaskContext> performers)
    {
      for (SetupTaskContext performer : performers)
      {
        List<VariableTask> unresolvedVariables = ((SetupTaskPerformer)performer).getUnresolvedVariables();
        for (VariableTask variable : unresolvedVariables)
        {
          String name = variable.getName();
          // System.out.println(name);

          String value = get(name);
          if (value == null)
          {
            return false;
          }
        }
      }

      return true;
    }

    public String getValue(VariableTask variable)
    {
      String name = variable.getName();
      // System.out.println(name);

      return get(name);
    }

    public UserCallback getUserCallback()
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SimpleProgress implements ProgressLog, IProgressMonitor, Runnable
  {
    private volatile String name;

    private volatile double totalWork;

    private volatile double work;

    private volatile boolean canceled;

    private volatile boolean done;

    private String lastName;

    public void setTerminating()
    {
    }

    public void log(String line)
    {
      log(line, true, Severity.OK);
    }

    public void log(String line, Severity severity)
    {
      log(line, true, severity);
    }

    public void log(String line, boolean filter)
    {
      log(line, filter, Severity.OK);
    }

    public synchronized void log(String line, boolean filter, Severity severity)
    {
      name = line;
    }

    public void log(IStatus status)
    {
      String string = SetupInstallerPlugin.toString(status);
      log(string, false);
    }

    public void log(Throwable t)
    {
      String string = SetupInstallerPlugin.toString(t);
      log(string, false);
    }

    public synchronized void task(SetupTask setupTask)
    {
      if (setupTask != null)
      {
        name = "Performing setup task " + setupTask.eClass().getName();
      }
      else
      {
        name = null;
      }
    }

    public synchronized boolean isCanceled()
    {
      return canceled;
    }

    public synchronized void setCanceled(boolean canceled)
    {
      this.canceled = canceled;
    }

    public synchronized void beginTask(String name, int totalWork)
    {
      performer.log(name);
      if (this.totalWork == 0)
      {
        this.totalWork = totalWork;
        schedule();
      }
    }

    public synchronized void done()
    {
      work = totalWork;
      done = true;
    }

    public synchronized void setTaskName(String name)
    {
      performer.log(name);
    }

    public synchronized void subTask(String name)
    {
      performer.log(name);
    }

    public synchronized void internalWorked(double work)
    {
      this.work += work;
    }

    public void worked(int work)
    {
      internalWorked(work);
    }

    public void run()
    {
      String name;
      double totalWork;
      double work;
      boolean canceled;
      boolean done;

      synchronized (this)
      {
        name = this.name;
        totalWork = this.totalWork;
        work = this.work;
        canceled = this.canceled;
        done = this.done;
      }

      if (!canceled)
      {
        double progress = work / totalWork;

        try
        {
          installButton.setProgress((float)progress);

          if (!ObjectUtil.equals(name, lastName))
          {
            lastName = name;
            if (!done)
            {
              installButton.setToolTipText(StringUtil.safe(name));
            }
          }
        }
        catch (SWTException ex)
        {
          return;
        }

        if (!done)
        {
          schedule();
        }
      }
    }

    private void schedule()
    {
      UIUtil.asyncExec(getDisplay(), this);
    }
  }
}
