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
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.ui.AgentManagerDialog;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.installer.MessageOverlay.Type;
import org.eclipse.oomph.setup.internal.installer.SimpleInstallerMenu.InstallerMenuItem;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Stack;

/**
 * @author Eike Stepper
 */
public final class SimpleInstallerDialog extends AbstractSimpleDialog implements InstallerUI
{
  private static final int INSTALLER_WIDTH = 523;

  private static final int INSTALLER_HEIGHT = 632;

  public static final Color COLOR_PURPLE = SetupInstallerPlugin.getColor(44, 34, 85);

  private static final Preference PREF_POOL_ENABLED = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("poolEnabled");

  private static final boolean CAPTURE = false;

  private final Installer installer;

  private Composite stack;

  private StackLayout stackLayout;

  private SimpleProductPage productPage;

  private SimpleVariablePage variablePage;

  private SimpleReadmePage readmePage;

  private SimpleInstallationLogPage installationLogPage;

  private SimpleKeepInstallerPage keepInstallerPage;

  private Stack<SimpleInstallerPage> pageStack = new Stack<SimpleInstallerPage>();

  private Resolution updateResolution;

  private static String simpleInstallerCss;

  private static String simpleInstallerPage;

  private static Font defaultFont;

  private SimpleInstallerMenu installerMenu;

  private boolean poolEnabled;

  private BundlePool pool;

  private InstallerMenuItem updateLauncherItem;

  private SimpleInstallerMenuButton menuButton;

  private Label updateLauncherSpacer;

  private MessageOverlay currentMessage;

  public SimpleInstallerDialog(Display display, final Installer installer)
  {
    super(display, OS.INSTANCE.isMac() ? SWT.TOOL : SWT.NO_TRIM, INSTALLER_WIDTH, INSTALLER_HEIGHT);
    this.installer = installer;
  }

  @Override
  protected void createUI(Composite titleComposite)
  {
    if (CAPTURE)
    {
      captureDownloadButton();
    }

    poolEnabled = PREF_POOL_ENABLED.get(true);
    enablePool(poolEnabled);

    Composite exitMenuButtonContainer = new Composite(titleComposite, SWT.NONE);
    exitMenuButtonContainer.setLayout(UIUtil.createGridLayout(1));
    exitMenuButtonContainer.setLayoutData(GridDataFactory.swtDefaults().grab(false, true).align(SWT.CENTER, SWT.FILL).create());

    SimpleInstallerButton exitButton = new ImageHoverButton(exitMenuButtonContainer, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exit.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exit_hover.png"));
    exitButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.BEGINNING).create());
    exitButton.setToolTipText("Exit");
    exitButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        exitSelected();
      }
    });

    menuButton = new SimpleInstallerMenuButton(exitMenuButtonContainer);
    menuButton.setLayoutData(GridDataFactory.swtDefaults().grab(false, true).align(SWT.CENTER, SWT.BEGINNING).indent(11, 0).create());
    menuButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        toggleMenu();
      }
    });

    stackLayout = new StackLayout();

    stack = new Composite(this, SWT.NONE);
    stack.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
    stack.setLayout(stackLayout);

    productPage = new SimpleProductPage(stack, this);
    variablePage = new SimpleVariablePage(stack, this);
    readmePage = new SimpleReadmePage(stack, this);
    installationLogPage = new SimpleInstallationLogPage(stack, this);
    keepInstallerPage = new SimpleKeepInstallerPage(stack, this);

    switchToPage(productPage);

    Display display = getDisplay();

    Thread updateSearcher = new UpdateSearcher(display);
    updateSearcher.start();

    display.timerExec(500, new Runnable()
    {
      public void run()
      {
        installer.getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
        installer.loadIndex();
      }
    });

    // Initialize menu
    getInstallerMenu();

    updateAvailable(false);
  }

  private void toggleMenu()
  {
    getInstallerMenu().setVisible(!getInstallerMenu().isVisible());
  }

  public Installer getInstaller()
  {
    return installer;
  }

  public static Font getDefaultFont()
  {
    if (defaultFont == null)
    {
      defaultFont = JFaceResources.getFont(SetupInstallerPlugin.FONT_LABEL_DEFAULT);
    }

    return defaultFont != null ? defaultFont : UIUtil.getDisplay().getSystemFont();
  }

  public void setButtonsEnabled(boolean enabled)
  {
    menuButton.setEnabled(enabled);
  }

  private void enablePool(boolean poolEnabled)
  {
    if (this.poolEnabled != poolEnabled)
    {
      this.poolEnabled = poolEnabled;
      PREF_POOL_ENABLED.set(poolEnabled);
    }

    if (poolEnabled)
    {
      pool = P2Util.getAgentManager().getDefaultBundlePool(SetupUIPlugin.INSTANCE.getSymbolicName());
    }
    else
    {
      pool = null;
    }

    // FIXME: Enabled/Disabled state for bundle pooling?
    // if (poolButton != null)
    // {
    // poolButton.setImage(getBundlePoolImage());
    // }
  }

  public BundlePool getPool()
  {
    return pool;
  }

  private SimpleInstallerMenu getInstallerMenu()
  {
    if (installerMenu == null)
    {
      installerMenu = createIntallerMenu();

    }
    return installerMenu;
  }

  private SimpleInstallerMenu createIntallerMenu()
  {
    SimpleInstallerMenu menu = new SimpleInstallerMenu(this);
    updateLauncherItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    updateLauncherItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/exclamation_circle.png"));
    updateLauncherItem.setText("UPDATE LAUNCHER");
    updateLauncherItem.setToolTipText("Install available updates");
    updateLauncherItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Runnable successRunnable = new Runnable()
        {
          public void run()
          {
            setReturnCode(RETURN_RESTART);
            exitSelected();
          }
        };

        ExceptionHandler<CoreException> exceptionHandler = new ExceptionHandler<CoreException>()
        {
          public void handleException(CoreException ex)
          {
            ErrorDialog.open(ex);
          }
        };

        SelfUpdate.update(getShell(), updateResolution, successRunnable, exceptionHandler, null);

      }
    });

    updateLauncherSpacer = new Label(menu, SWT.NONE);
    updateLauncherSpacer.setLayoutData(GridDataFactory.swtDefaults().hint(SWT.DEFAULT, 46).create());

    InstallerMenuItem advancedModeItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    advancedModeItem.setText("ADVANCED MODE");
    advancedModeItem.setToolTipText("Switch to advanced mode");
    advancedModeItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setReturnCode(RETURN_ADVANCED);
        exitSelected();
      }
    });

    InstallerMenuItem networkSettingsItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    networkSettingsItem.setText("NETWORK SETTINGS");
    networkSettingsItem.setToolTipText("Network proxy settings");
    networkSettingsItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new ProxyPreferenceDialog(SimpleInstallerDialog.this);
        dialog.open();
      }
    });

    InstallerMenuItem bundlePoolingItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    bundlePoolingItem.setText("BUNDLE POOLING");
    bundlePoolingItem.setToolTipText("Configure bundle pool...");
    bundlePoolingItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        manageBundlePools();
      }
    });

    InstallerMenuItem aboutItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    aboutItem.setText("ABOUT");
    aboutItem.setToolTipText("About");
    aboutItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        showAbout();
      }
    });

    InstallerMenuItem quitItem = new SimpleInstallerMenu.InstallerMenuItem(menu);
    quitItem.setText("QUIT");
    quitItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        exitSelected();
      }
    });
    quitItem.setDividerVisible(false);

    return menu;
  }

  private void updateAvailable(boolean available)
  {
    menuButton.setNotificationVisible(available);
    updateLauncherItem.setVisible(available);
    ((GridData)updateLauncherSpacer.getLayoutData()).exclude = !available;

    installerMenu.layout();
  }

  private void manageBundlePools()
  {
    final boolean[] enabled = { poolEnabled };

    AgentManagerDialog dialog = new AgentManagerDialog(getShell())
    {
      @Override
      protected void createUI(Composite parent)
      {
        final Button enabledButton = new Button(parent, SWT.CHECK);
        enabledButton.setText("Enable shared bundle pool");
        enabledButton.setSelection(poolEnabled);
        enabledButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            enabled[0] = enabledButton.getSelection();
            getComposite().setEnabled(enabled[0]);
          }
        });

        new Label(parent, SWT.NONE);
        super.createUI(parent);
        getComposite().setEnabled(poolEnabled);
      }

      @Override
      protected void createButtonsForButtonBar(Composite parent)
      {
        super.createButtonsForButtonBar(parent);
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(false);
        }
      }

      @Override
      protected void elementChanged(Object element)
      {
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(element instanceof BundlePool);
        }
      }
    };

    if (pool != null)
    {
      dialog.setSelectedElement(pool);
    }

    if (dialog.open() == AgentManagerDialog.OK)
    {
      enablePool(enabled[0]);
      pool = (BundlePool)dialog.getSelectedElement();
    }
  }

  public boolean refreshJREs()
  {
    if (variablePage != null)
    {
      return variablePage.refreshJREs();
    }

    return false;
  }

  public void showAbout()
  {
    int xxx;
    // TODO Fix version in about dialog

    String version = "he.ll.o";
    new AboutDialog(getShell(), version).open();
  }

  public void productSelected(Product product)
  {
    variablePage.setProduct(product);
    switchToPage(variablePage);
  }

  public void backSelected()
  {
    if (pageStack.size() <= 1)
    {
      return;
    }

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {

        SimpleInstallerPage currentPage = pageStack.pop();
        try
        {
          currentPage.aboutToHide();
        }
        catch (Exception e)
        {
          SetupInstallerPlugin.INSTANCE.log(e);
        }

        SimpleInstallerPage previousPage = pageStack.peek();

        stackLayout.topControl = previousPage;
        stack.layout();

        previousPage.aboutToShow();
        previousPage.setFocus();
      }
    });
  }

  private void captureDownloadButton()
  {
    final Shell captureShell = new Shell(this, SWT.NO_TRIM | SWT.MODELESS);
    captureShell.setLayout(new FillLayout());

    Image image = SetupInstallerPlugin.INSTANCE.getSWTImage("/download.png");

    final ToolButton downloadActiveButton = new ToolButton(captureShell, SWT.RADIO, image, false);
    downloadActiveButton.setBackground(COLOR_WHITE);
    downloadActiveButton.setSelection(true);

    final ToolButton downloadHoverButton = new ToolButton(captureShell, SWT.PUSH, image, false);
    downloadHoverButton.setBackground(COLOR_WHITE);
    downloadHoverButton.addMouseMoveListener(new MouseMoveListener()
    {
      public void mouseMove(MouseEvent e)
      {
        try
        {
          AccessUtil.save(new File("/develop/download_hover.png"), downloadHoverButton);
          AccessUtil.save(new File("/develop/download_active.png"), downloadActiveButton);
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        finally
        {
          // captureShell.dispose();
        }
      }
    });

    captureShell.pack();
    captureShell.open();

    Point pt = getDisplay().map(downloadHoverButton, null, 10, 10);
    downloadHoverButton.setFocus();

    Event event = new Event();
    event.type = SWT.MouseMove;
    event.x = pt.x;
    event.y = pt.y;
    getDisplay().post(event);
  }

  public void showMessage(String message, Type type, boolean dismissAutomatically)
  {
    clearMessage();

    currentMessage = new MessageOverlay(this, type, new Locator()
    {
      public void relocate(Control control)
      {
        Rectangle bounds = SimpleInstallerDialog.this.getBounds();
        int x = bounds.x + 5;
        int y = bounds.y + 24;

        int width = bounds.width - 9;

        // Depending on the current page, the height varies
        int height = pageStack.peek() instanceof SimpleProductPage ? 87 : 70;
        control.setBounds(x, y, width, height);
      }
    }, dismissAutomatically);
    currentMessage.setMessage(message);
    currentMessage.setVisible(true);
  }

  public void clearMessage()
  {
    if (currentMessage != null && !currentMessage.isDisposed())
    {
    	if(!currentMessage.isDisposed())
        {
          currentMessage.close();
        }
        currentMessage = null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateSearcher extends Thread
  {
    private Display display;

    public UpdateSearcher(Display display)
    {
      super("Simple Update Searcher");
      this.display = display;
    }

    @Override
    public void run()
    {
      try
      {
        User user = getInstaller().getUser();
        updateResolution = SelfUpdate.resolve(user, null);
        if (updateResolution != null && !display.isDisposed())
        {
          display.asyncExec(new Runnable()
          {
            public void run()
            {
              updateAvailable(true);
            }
          });
        }
      }
      catch (CoreException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

  private static String getSimpleInstallerCSS()
  {
    if (simpleInstallerCss == null)
    {
      try
      {
        simpleInstallerCss = SetupCoreUtil.readBundleResource(SetupInstallerPlugin.INSTANCE.getBundle(), "/html/css/simpleInstaller.css");
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
    return simpleInstallerCss;
  }

  static String getSimpleInstallerHtmlTemplate()
  {
    if (simpleInstallerPage == null)
    {
      try
      {
        simpleInstallerPage = SetupCoreUtil.readBundleResource(SetupInstallerPlugin.INSTANCE.getBundle(), "/html/SimpleInstallerPage.html");

        // Embed CSS
        simpleInstallerPage = simpleInstallerPage.replace("%INSTALLER_CSS%", SimpleInstallerDialog.getSimpleInstallerCSS());
      }
      catch (IOException ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
    return simpleInstallerPage;
  }

  private void switchToPage(final SimpleInstallerPage page)
  {
    if (page != null)
    {
      final SimpleInstallerPage currentPage = !pageStack.isEmpty() ? pageStack.peek() : null;
      if (currentPage == null || currentPage != page)
      {
        pageStack.push(page);

        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            if (currentPage != null)
            {
              currentPage.aboutToHide();
            }

            stackLayout.topControl = page;
            stack.layout();

            page.aboutToShow();
            page.setFocus();
          }
        });
      }
    }
  }

  void showReadme(URI readmeURI)
  {
    readmePage.setReadmeURI(readmeURI);
    switchToPage(readmePage);
  }

  void showInstallationLog(File installationLogFile)
  {
    installationLogPage.setInstallationLogFile(installationLogFile);
    switchToPage(installationLogPage);
  }

  void showKeepInstaller()
  {
    switchToPage(keepInstallerPage);
  }
}
