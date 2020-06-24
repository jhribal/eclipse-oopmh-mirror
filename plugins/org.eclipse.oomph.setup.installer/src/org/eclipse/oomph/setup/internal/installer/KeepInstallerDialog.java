/*
 * Copyright (c) 2015, 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 *    Christoph Laeubrich - [494735] - Eclipse Installer does not create .desktop file for the menu
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

/**
 * @author Eike Stepper
 */
public final class KeepInstallerDialog extends AbstractSetupDialog
{
  private final boolean startPermanentInstaller;

  private String location;

  private Button startMenuButton;

  private Button desktopButton;

  public KeepInstallerDialog(Shell parentShell, boolean startPermanentInstaller)
  {
    super(parentShell, PropertiesUtil.getProductName(), 610, 380, SetupInstallerPlugin.INSTANCE, false);
    this.startPermanentInstaller = startPermanentInstaller;
  }

  @Override
  protected String getDefaultMessage()
  {
    return KeepInstallerUtil.KEEP_INSTALLER_DESCRIPTION + "."; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    final Shell shell = getShell();
    setTitle(Messages.KeepInstallerDialog__title);

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = getContainerMargin();
    layout.marginHeight = getContainerMargin();
    layout.verticalSpacing = 5;
    parent.setLayout(layout);

    Label locationLabel = new Label(parent, SWT.NONE);
    locationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    locationLabel.setText(Messages.KeepInstallerDialog_CopyTo_label);

    final Text locationText = new Text(parent, SWT.BORDER);
    locationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    locationText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        location = locationText.getText();
        String error = validate();

        setErrorMessage(error);

        Button okButton = getButton(IDialogConstants.OK_ID);
        okButton.setEnabled(error == null && location.length() != 0);
      }

      private String validate()
      {
        if (location.length() == 0)
        {
          return null;
        }

        File folder = new File(location);
        if (!folder.exists())
        {
          return null;
        }

        if (!folder.isDirectory())
        {
          return Messages.KeepInstallerDialog_PathNoDirectory_message;
        }

        if (!isEmpty(folder))
        {
          return Messages.KeepInstallerDialog_DirectoryNoEmpty_message;
        }

        return null;
      }

      private boolean isEmpty(File folder)
      {
        File[] children = folder.listFiles();
        return children == null || children.length == 0;
      }
    });

    Button browseButton = new Button(parent, SWT.NONE);
    browseButton.setText(Messages.KeepInstallerDialog_Browse_label + StringUtil.HORIZONTAL_ELLIPSIS);
    browseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        FileDialog dialog = new FileDialog(shell, SWT.APPLICATION_MODAL | SWT.SAVE);
        dialog.setText(Messages.KeepInstallerDialog__title);

        if (!StringUtil.isEmpty(location))
        {
          final File file = new File(location).getAbsoluteFile();
          dialog.setFilterPath(file.getParent());
          dialog.setFileName(file.getName());
        }

        String dir = dialog.open();
        if (dir != null)
        {
          locationText.setText(dir);
        }
      }
    });

    if (KeepInstallerUtil.getDesktopSupport() != null)
    {
      new Label(parent, SWT.NONE);
      startMenuButton = new Button(parent, SWT.CHECK);
      startMenuButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
      startMenuButton.setText(Messages.KeepInstallerDialog_CreateStartMenu_label);
      startMenuButton.setSelection(true);

      new Label(parent, SWT.NONE);
      desktopButton = new Button(parent, SWT.CHECK);
      desktopButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
      desktopButton.setText(Messages.KeepInstallerDialog_CreateDesktopShortcut_label);
      desktopButton.setSelection(true);
    }

    setDefaultLocation(locationText);
  }

  @Override
  protected void okPressed()
  {
    final String launcher = OS.getCurrentLauncher(false);
    if (launcher != null)
    {
      final boolean startMenu = startMenuButton == null ? false : startMenuButton.getSelection();
      final boolean desktop = desktopButton == null ? false : desktopButton.getSelection();

      ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog((Shell)getShell().getParent());

      try
      {
        progressMonitorDialog.run(true, false, new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            monitor.beginTask(MessageFormat.format(Messages.KeepInstallerDialog_CopyingInstaller_task, location), IProgressMonitor.UNKNOWN);
            KeepInstallerUtil.keepInstaller(location, startPermanentInstaller, launcher, startMenu, desktop, false);
            monitor.done();
          }
        });
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    super.okPressed();
  }

  @Override
  protected String getShellText()
  {
    return PropertiesUtil.getProductName();
  }

  public static void setDefaultLocation(final Text locationText)
  {
    UIUtil.asyncExec(locationText, new Runnable()
    {
      public void run()
      {
        File home = new File(PropertiesUtil.getUserHome());
        String folderName = PropertiesUtil.getProductName().toLowerCase().replace('\u00A0', '-').replace(' ', '-');
        for (int i = 1; i < Integer.MAX_VALUE; i++)
        {
          File folder = new File(home, folderName + (i > 1 ? i : "")); //$NON-NLS-1$
          if (!folder.exists())
          {
            String path = folder.getAbsolutePath();
            locationText.setText(path);
            locationText.setSelection(path.length());
            return;
          }
        }
      }
    });
  }
}
