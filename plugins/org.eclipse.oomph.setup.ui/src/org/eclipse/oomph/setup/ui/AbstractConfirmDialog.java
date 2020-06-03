/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractConfirmDialog extends AbstractSetupDialog
{
  private final String rememberButtonText;

  private boolean remember;

  public AbstractConfirmDialog(Shell parentShell, String title, int width, int height, String rememberButtonText)
  {
    super(parentShell, title, width, height, SetupUIPlugin.INSTANCE, false);
    this.rememberButtonText = rememberButtonText;
  }

  public AbstractConfirmDialog(String title, int width, int height, String rememberButtonText)
  {
    this(null, title, width, height, rememberButtonText);
  }

  public boolean isRemember()
  {
    return remember;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    final Button rememberButton = createCheckbox(parent, rememberButtonText);
    rememberButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        remember = rememberButton.getSelection();
      }
    });

    String rememberButtonToolTip = getRememberButtonToolTip();
    if (!StringUtil.isEmpty(rememberButtonToolTip))
    {
      rememberButton.setToolTipText(rememberButtonToolTip);
    }

    doCreateButtons(parent);
  }

  protected void doCreateButtons(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, Messages.AbstractConfirmDialog_accept, false);
    createButton(parent, IDialogConstants.CANCEL_ID, Messages.AbstractConfirmDialog_decline, true);
  }

  protected String getRememberButtonToolTip()
  {
    return null;
  }
}
