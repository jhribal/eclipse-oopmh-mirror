/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LicensePrePrompter extends AbstractSetupDialog
{
  private static final String DEFAULT_LICENSE_UUID = "DEFAULT_LICENSE"; //$NON-NLS-1$

  @SuppressWarnings("nls")
  private static final Set<String> IMPLIED_LICENSE_UUIDS = Collections.unmodifiableSet(
      new HashSet<String>(Arrays.asList("6a3d083ad2bd7d3a80ee293235f8c5b1", "abc76a6cc9d06e4684ff61ed74a972c", "8d3137b6d090b6860d1b977a88ceb334",
          "d77cfd8b73fc71776727fcbb2605709", "6a3d083ad2bd7d3a80ee293235f8c5b1", "be4a7d94b4e1903e628a3001859739a8", "318bcab4617b336391ac7ab40514ccc3",
          "aa1a84ed6378c1cdba13a100c225aa57", "425529a69afe7129289ce9a30b4d2b6c", "84f0b7f0a433c033764dd28540d6d199", "4450aa10f21d146d6a6a80f84275f5b2")));

  private final String license;

  private Browser licenseBrowser;

  private String shellText;

  public LicensePrePrompter(Shell parentShell, String license)
  {
    super(parentShell, Messages.LicensePrePrompter_defaultLicense_name, 700, 700, SetupUIPlugin.INSTANCE, false);
    this.license = license;
    shellText = parentShell.getText();
  }

  @Override
  protected String getDefaultMessage()
  {
    return Messages.LicensePrePrompter_defaultMessage;
  }

  @Override
  protected void createUI(Composite parent)
  {
    licenseBrowser = new Browser(parent, SWT.NONE);
    licenseBrowser.setLayoutData(new GridData(GridData.FILL_BOTH));
    licenseBrowser.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    licenseBrowser.setText(license);
    licenseBrowser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changing(LocationEvent event)
      {
        String url = event.location;
        if (!"about:blank".equals(url)) //$NON-NLS-1$
        {
          OS.INSTANCE.openSystemBrowser(url);
          event.doit = false;
        }
      }
    });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, Messages.LicensePrePrompter_acceptButton_text, true);
    createButton(parent, IDialogConstants.CANCEL_ID, Messages.LicensePrePrompter_decideLaterButton_text, false);
  }

  public static EList<LicenseInfo> execute(Shell shell, User user)
  {
    Set<String> unacceptedLicenses = new HashSet<String>(IMPLIED_LICENSE_UUIDS);

    for (LicenseInfo licenseInfo : user.getAcceptedLicenses())
    {
      String uuid = licenseInfo.getUUID();
      if (DEFAULT_LICENSE_UUID.equals(uuid))
      {
        return null;
      }

      unacceptedLicenses.remove(uuid);
    }

    if (unacceptedLicenses.isEmpty())
    {
      return null;
    }

    String license = readLicense();
    if (StringUtil.isEmpty(license))
    {
      return null;
    }

    EList<LicenseInfo> acceptedLicenses = new BasicEList<LicenseInfo>();
    acceptedLicenses.add(new LicenseInfo(DEFAULT_LICENSE_UUID, Messages.LicensePrePrompter_defaultLicense_info));

    LicensePrePrompter prompter = new LicensePrePrompter(shell, license);
    if (prompter.open() == LicensePrePrompter.OK)
    {
      for (String uuid : unacceptedLicenses)
      {
        acceptedLicenses.add(new LicenseInfo(uuid, Messages.LicensePrePrompter_defaultLicense_name));
      }
    }

    return acceptedLicenses;
  }

  private static String readLicense()
  {
    StringBuilder builder = new StringBuilder();
    InputStream in = null;

    try
    {
      String path = LicensePrePrompter.class.getPackage().getName().replace('.', '/') + "/license.html"; //$NON-NLS-1$
      in = LicensePrePrompter.class.getClassLoader().getResourceAsStream(path);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));

      String line;
      while ((line = reader.readLine()) != null)
      {
        if (builder.length() != 0)
        {
          builder.append(StringUtil.NL);
        }

        builder.append(line);
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
    finally
    {
      IOUtil.close(in);
    }

    return builder.toString();
  }

  @Override
  protected String getShellText()
  {
    return shellText;
  }
}
