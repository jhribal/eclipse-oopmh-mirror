/*
 * Copyright (c) 2014 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.util.OfflineMode;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Ed Merks
 */
public class RemoteResourcesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  private static final String OFFLINE = "offline"; //$NON-NLS-1$

  public RemoteResourcesPreferencePage()
  {
  }

  public void init(IWorkbench workbench)
  {
    // Do nothing.
  }

  @Override
  protected IPreferenceStore doGetPreferenceStore()
  {
    return new DelegatingPreferenceStore(UIPlugin.INSTANCE.getPreferenceStore())
    {
      @Override
      public void setToDefault(String name)
      {
        if (OFFLINE.equals(name))
        {
          OfflineMode.setEnabled(false);
        }
        else
        {
          super.setToDefault(name);
        }
      }

      @Override
      public void setValue(String name, boolean value)
      {
        if (OFFLINE.equals(name))
        {
          OfflineMode.setEnabled(value);
        }
        else
        {
          super.setValue(name, value);
        }
      }

      @Override
      public boolean getBoolean(String name)
      {
        if (OFFLINE.equals(name))
        {
          return OfflineMode.isEnabled();
        }

        return super.getBoolean(name);
      }

      @Override
      public boolean getDefaultBoolean(String name)
      {
        if (OFFLINE.equals(name))
        {
          return false;
        }

        return super.getDefaultBoolean(name);
      }
    };
  }

  @Override
  protected void createFieldEditors()
  {
    Composite parent = getFieldEditorParent();

    BooleanFieldEditor offline = new BooleanFieldEditor(OFFLINE, Messages.RemoteResourcesPreferencePage_useOfflineCache_label, parent);
    offline.fillIntoGrid(parent, 2);
    addField(offline);
    offline.getDescriptionControl(parent).setToolTipText(Messages.RemoteResourcesPreferencePage_useOfflineCache_tooltip);

    BooleanFieldEditor showOffline = new BooleanFieldEditor(UIPropertyTester.SHOW_OFFLINE, Messages.RemoteResourcesPreferencePage_showOfflineToolbar_label, parent);
    showOffline.fillIntoGrid(parent, 2);
    addField(showOffline);
    showOffline.getDescriptionControl(parent).setToolTipText(Messages.RemoteResourcesPreferencePage_showOfflineToolbar_tooltip);

    Runnable runnable = null;
    try
    {
      @SuppressWarnings("unchecked")
      Class<? extends Runnable> refreshCacheHandlerClass = (Class<? extends Runnable>)CommonPlugin.loadClass("org.eclipse.oomph.setup.editor", //$NON-NLS-1$
          "org.eclipse.oomph.setup.presentation.handlers.RefreshCacheHandler"); //$NON-NLS-1$
      runnable = refreshCacheHandlerClass.newInstance();
    }
    catch (Exception ex)
    {
      // Ignore.
    }

    if (runnable != null)
    {
      final Runnable finalRunnable = runnable;
      Button refreshCache = new Button(parent, SWT.PUSH);
      refreshCache.setText(Messages.RemoteResourcesPreferencePage_refreshRemoteCache);
      refreshCache.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          finalRunnable.run();
        }
      });
    }
  }
}
