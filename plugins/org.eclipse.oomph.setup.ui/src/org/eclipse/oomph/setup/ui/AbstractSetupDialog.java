/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Christian W. Damus - bug 506031
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.OomphUIPlugin;

import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSetupDialog extends OomphDialog
{
  /**
   * @deprecated Use the {@link #getDefaultShellText()} API, instead.
   */
  @Deprecated
  public static final String SHELL_TEXT = "Eclipse Installer";

  private static String defaultShellText = SHELL_TEXT;

  public AbstractSetupDialog(Shell parentShell, String title, int width, int height, OomphUIPlugin plugin, boolean helpAvailable)
  {
    super(parentShell, title, width, height, plugin, helpAvailable);
  }

  @Override
  protected String getImagePath()
  {
    return "install_wiz.png";
  }

  @Override
  protected String getShellText()
  {
    return getDefaultShellText();
  }

  /**
   * Queries the default text for new shells.
   * The default-default, if not set, is {@code "Eclipse Installer"}.
   *
   * @return the default text for new shells
   */
  public static String getDefaultShellText()
  {
    return defaultShellText;
  }

  /**
   * Assigns the default text for new shells.
   *
   * @param text the new default shell text (required)
   * @throws IllegalArgumentException if {@code text} is {@code null}
   */
  public static void setDefaultShellText(String text)
  {
    if (text == null)
    {
      throw new IllegalArgumentException("null text"); //$NON-NLS-1$
    }

    defaultShellText = text;
  }
}
