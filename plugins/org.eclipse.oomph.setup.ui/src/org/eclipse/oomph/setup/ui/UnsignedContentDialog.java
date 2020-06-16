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

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.UnsignedPolicy;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.io.File;

public class UnsignedContentDialog extends AbstractConfirmDialog
{
  private final String[] unsignedContent;

  public UnsignedContentDialog(String[] unsignedContent)
  {
    super(Messages.UnsignedContentDialog_title, 600, 400, Messages.UnsignedContentDialog_rememberButton_text);
    this.unsignedContent = unsignedContent;
  }

  @Override
  protected String getShellText()
  {
    return Messages.UnsignedContentDialog_shellText;
  }

  @Override
  protected String getDefaultMessage()
  {
    return Messages.UnsignedContentDialog_defaultMessage;
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    TreeViewer viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
    viewer.setContentProvider(new ITreeContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object getParent(Object element)
      {
        return null;
      }

      public Object[] getElements(Object element)
      {
        return getChildren(element);
      }

      public Object[] getChildren(Object element)
      {
        if (element == UnsignedContentDialog.this)
        {
          return unsignedContent;
        }

        return new Object[0];
      }

      public boolean hasChildren(Object element)
      {
        return element == UnsignedContentDialog.this;
      }
    });

    viewer.setLabelProvider(new UnsignedContentLabelProvider());
    viewer.setComparator(new ViewerComparator());
    viewer.setInput(this);

    Control control = viewer.getControl();
    control.setLayoutData(new GridData(GridData.FILL_BOTH));
    Dialog.applyDialogFont(control);
  }

  public static Confirmer createUnsignedContentConfirmer(final User user, final boolean saveChangedUser)
  {
    Boolean propPolicy = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_UNSIGNED_POLICY);
    if (propPolicy != null)
    {
      return propPolicy ? Confirmer.ACCEPT : Confirmer.DECLINE;
    }

    if (user != null)
    {
      UnsignedPolicy userPolicy = user.getUnsignedPolicy();
      if (userPolicy == UnsignedPolicy.ACCEPT)
      {
        return Confirmer.ACCEPT;
      }
    }

    return new AbstractDialogConfirmer()
    {
      @Override
      public Confirmation confirm(boolean defaultConfirmed, Object info)
      {
        Confirmation confirmation = super.confirm(defaultConfirmed, info);
        if (user != null && confirmation.isRemember())
        {
          UnsignedPolicy unsignedPolicy = confirmation.isConfirmed() ? UnsignedPolicy.ACCEPT : UnsignedPolicy.DECLINE;
          user.setUnsignedPolicy(unsignedPolicy);

          if (saveChangedUser)
          {
            BaseUtil.saveEObject(user);
          }
        }

        return confirmation;
      }

      @Override
      protected AbstractConfirmDialog createDialog(boolean defaultConfirmed, Object info)
      {
        String[] unsignedContent = (String[])info;
        return new UnsignedContentDialog(unsignedContent);
      }
    };
  }

  /**
   * @author Eike Stepper
   */
  private final class UnsignedContentLabelProvider extends LabelProvider
  {
    @Override
    public Image getImage(Object element)
    {
      File file = new File((String)element);
      if (file.isDirectory())
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("unsigned-directory"); //$NON-NLS-1$
      }

      if (file.isFile())
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("unsigned-file"); //$NON-NLS-1$
      }

      return null;
    }

    @Override
    public String getText(Object element)
    {
      return (String)element;
    }
  }
}
