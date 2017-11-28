/*
 * Copyright (c) 2017 aprice and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    aprice - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.setup.PreferenceTask;

import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferenceNodeVisitor;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * A filtered tree for displaying preferences, with an additional filter to suppress preferences set to their default value.
 * @author Adrian Price <aprice@tibco.com>
 */
class PreferenceFilteredTree extends FilteredTree
{

  private static final class DummyPreferences implements IEclipsePreferences
  {
    public String absolutePath()
    {
      return null;
    }

    public void accept(IPreferenceNodeVisitor visitor) throws BackingStoreException
    {
    }

    public void addNodeChangeListener(INodeChangeListener listener)
    {
    }

    public void addPreferenceChangeListener(IPreferenceChangeListener listener)
    {
    }

    public String[] childrenNames() throws BackingStoreException
    {
      return null;
    }

    public void clear() throws BackingStoreException
    {
    }

    public void flush() throws BackingStoreException
    {
    }

    public String get(String key, String def)
    {
      return def;
    }

    public boolean getBoolean(String key, boolean def)
    {
      return def;
    }

    public byte[] getByteArray(String key, byte[] def)
    {
      return def;
    }

    public double getDouble(String key, double def)
    {
      return def;
    }

    public float getFloat(String key, float def)
    {
      return def;
    }

    public int getInt(String key, int def)
    {
      return def;
    }

    public long getLong(String key, long def)
    {
      return def;
    }

    public String[] keys() throws BackingStoreException
    {
      return null;
    }

    public String name()
    {
      return null;
    }

    public Preferences node(String path)
    {
      return this;
    }

    public boolean nodeExists(String pathName) throws BackingStoreException
    {
      return false;
    }

    public Preferences parent()
    {
      return null;
    }

    public void put(String key, String value)
    {
    }

    public void putBoolean(String key, boolean value)
    {
    }

    public void putByteArray(String key, byte[] value)
    {
    }

    public void putDouble(String key, double value)
    {
    }

    public void putFloat(String key, float value)
    {
    }

    public void putInt(String key, int value)
    {
    }

    public void putLong(String key, long value)
    {
    }

    public void remove(String key)
    {
    }

    public void removeNode() throws BackingStoreException
    {
    }

    public void removeNodeChangeListener(INodeChangeListener listener)
    {
    }

    public void removePreferenceChangeListener(IPreferenceChangeListener listener)
    {
    }

    public void sync() throws BackingStoreException
    {
    }
  }

  private static final class DummyScope implements IScopeContext
  {
    private final String name;

    DummyScope(String name)
    {
      this.name = name;
    }

    public IPath getLocation()
    {
      return null;
    }

    public String getName()
    {
      return name;
    }

    public IEclipsePreferences getNode(String qualifier)
    {
      return DUMMY_PREFERENCES;
    }
  }

  private static final IScopeContext DUMMY_CONFIGURATION_SCOPE = new DummyScope(ConfigurationScope.SCOPE);

  private static final IScopeContext DUMMY_DEFAULT_SCOPE = new DummyScope(DefaultScope.SCOPE);

  private static final IScopeContext DUMMY_INSTANCE_SCOPE = new DummyScope(InstanceScope.SCOPE);

  private static final IEclipsePreferences DUMMY_PREFERENCES = new DummyPreferences();

  private static final IScopeContext DUMMY_PROJECT_SCOPE = new DummyScope(ProjectScope.SCOPE);

  private ViewerFilter defaultsFilter;

  PreferenceFilteredTree(Composite parent, int treeStyle, PatternFilter filter)
  {
    super(parent, treeStyle, filter, true);
  }

  private void createDefaultsFilter(Composite parent)
  {
    ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.HORIZONTAL);
    final ToolItem defaultsToolItem = new ToolItem(toolBar, SWT.CHECK);
    defaultsToolItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("filter_advanced_properties"));
    defaultsToolItem.setToolTipText("Hide preferences set to their default value");
    defaultsToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        treeViewer.refresh();
      }
    });
    final IPreferencesService preferencesService = Platform.getPreferencesService();
    final IScopeContext[] defaultContexts = new IScopeContext[] { DUMMY_PROJECT_SCOPE, DUMMY_INSTANCE_SCOPE, DUMMY_CONFIGURATION_SCOPE, DefaultScope.INSTANCE };
    final IScopeContext[] prefContexts = new IScopeContext[] { DUMMY_PROJECT_SCOPE, DUMMY_INSTANCE_SCOPE, DUMMY_CONFIGURATION_SCOPE, DUMMY_DEFAULT_SCOPE };
    defaultsFilter = new ViewerFilter()
    {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element)
      {
        if (element instanceof PreferenceTask && defaultsToolItem.getSelection())
        {
          PreferenceTask pt = (PreferenceTask)element;
          String fqPrefName = pt.getKey();
          int idx = fqPrefName.indexOf('/', 1);
          String scope = fqPrefName.substring(1, idx);
          String keyWithoutScope = fqPrefName.substring(idx + 1);
          idx = keyWithoutScope.indexOf('/', 0);
          String qualifier = keyWithoutScope.substring(0, idx);
          String key = keyWithoutScope.substring(idx + 1);

          // NOTE: this hideous mess is necessary because IPreferencesService.getString(...) doesn't do what it says on the tin.
          // That is, it effectively IGNORES the contexts parameter and searches ALL scopes regardless of which ones were supplied.
          // So it's impossible to obtain the instance, configuration or default preference value by respectively passing just
          // InstanceScope.INSTANCE, ConfiguurationScope.INSTANCE or DefaultScope.INSTANCE - hence the need for the dummy scopes.
          IScopeContext instanceScope = DUMMY_INSTANCE_SCOPE;
          IScopeContext configurationScope = DUMMY_CONFIGURATION_SCOPE;
          if (InstanceScope.SCOPE.equals(scope))
          {
            instanceScope = InstanceScope.INSTANCE;
          }
          else if (ConfigurationScope.SCOPE.equals(scope))
          {
            configurationScope = ConfigurationScope.INSTANCE;
          }
          prefContexts[1] = instanceScope;
          prefContexts[2] = configurationScope;
          String defaultValue = preferencesService.getString(qualifier, key, null, defaultContexts);
          String prefValue = preferencesService.getString(qualifier, key, null, prefContexts);
          return !(prefValue == null || prefValue.equals(defaultValue));
        }
        return true;
      }
    };
  }

  @Override
  protected Composite createFilterControls(Composite parent)
  {
    ((GridLayout)parent.getLayout()).numColumns = 3;
    Composite filters = super.createFilterControls(parent);
    createDefaultsFilter(parent);
    return filters;
  }

  @Override
  protected Control createTreeControl(Composite parent, int style)
  {
    Control treeControl = super.createTreeControl(parent, style);
    treeViewer.addFilter(defaultsFilter);
    return treeControl;
  }

}
