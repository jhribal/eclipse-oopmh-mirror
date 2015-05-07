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

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class NetworkSSH2Dialog extends AbstractPreferenceDialog
{
  public static final String TITLE = "SSH2 Keys";

  public static final String DESCRIPTION = "Adjust your SSH2 key settings";

  public NetworkSSH2Dialog(Shell parentShell)
  {
    super(parentShell, TITLE);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + ".";
  }

  @Override
  protected PreferencePage createPreferencePage()
  {
    return new SSH2PreferencePageWithoutHelp();
  }

  /**
   * @author Eike Stepper
   */
  private final class SSH2PreferencePageWithoutHelp extends org.eclipse.jsch.internal.ui.preference.PreferencePage
  {
    public SSH2PreferencePageWithoutHelp()
    {
      noDefaultAndApplyButton();
    }

    @Override
    protected Control createContents(Composite parent)
    {
      try
      {
        return super.createContents(parent);
      }
      catch (Exception ex)
      {
        // We expect an IllegalStateException here: Workbench has not been created yet.
        // But it's the last call, so keep going...
        return parent.getChildren()[0];
      }
    }

    @Override
    protected Label createDescriptionLabel(Composite parent)
    {
      return null;
    }
  }
}
