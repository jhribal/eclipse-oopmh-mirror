/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import org.osgi.framework.Bundle;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class AboutDialog extends AbstractSetupDialog
{
  private static final int ECLIPSE_VERSION_COLUMN_INDEX = 1;

  private static final String SHOW_ALL_PLUGINS = "SHOW_ALL_PLUGINS"; //$NON-NLS-1$

  private static final int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;

  private static final List<? extends OomphTransferDelegate> DND_DELEGATES = Collections.singletonList(new OomphTransferDelegate.TextTransferDelegate());

  private static final Transfer[] DND_TRANSFERS = new Transfer[] { DND_DELEGATES.get(0).getTransfer() };

  private static final int VERSION_COLUMN_PADDING = 10;

  private final IDialogSettings dialogSettings = getDialogSettings();

  private final String version;

  private boolean showAllPlugins;

  private Profile profile;

  private Table table;

  private TableColumn idColumn;

  private TableColumn versionColumn;

  private ControlAdapter columnResizer = new ControlAdapter()
  {
    @Override
    public void controlResized(ControlEvent e)
    {
      Point size = table.getSize();
      ScrollBar bar = table.getVerticalBar();
      if (bar != null && bar.isVisible())
      {
        size.x -= bar.getSize().x;
      }

      versionColumn.pack();

      idColumn.setWidth(size.x - versionColumn.getWidth() - VERSION_COLUMN_PADDING);
    }
  };

  private Color gray;

  public AboutDialog(Shell parentShell, String theVersion)
  {
    super(parentShell, MessageFormat.format(Messages.AboutDialog_title, parentShell.getText()), 700, 500, SetupInstallerPlugin.INSTANCE, false);
    version = theVersion;
    showAllPlugins = dialogSettings.getBoolean(SHOW_ALL_PLUGINS);
  }

  @Override
  protected String getDefaultMessage()
  {
    return MessageFormat.format(Messages.AboutDialog_InstallerVersion_message, version, SetupUtil.INSTALLER_UPDATE_URL);
  }

  @Override
  protected void createUI(Composite parent)
  {
    table = new Table(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.NO_SCROLL | SWT.V_SCROLL);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.setLayoutData(new GridData(GridData.FILL_BOTH));
    table.addControlListener(columnResizer);

    idColumn = new TableColumn(table, SWT.NONE);
    idColumn.setText(Messages.AboutDialog_PluginColumn_label);
    idColumn.setResizable(false);
    idColumn.setMoveable(false);

    versionColumn = new TableColumn(table, SWT.NONE);
    versionColumn.setText(Messages.AboutDialog_VersionColumn_label);
    versionColumn.setResizable(false);
    versionColumn.setMoveable(false);

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    profile = agent.getCurrentProfile();

    gray = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);

    fillTable();

    TableViewer tableViewer = new TableViewer(table);
    GeneralDragAdapter generalDragAdapter = new GeneralDragAdapter(tableViewer, new GeneralDragAdapter.DraggedObjectsFactory()
    {
      public List<Object> createDraggedObjects(ISelection selection) throws Exception
      {
        List<Object> result = new ArrayList<Object>();
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          if (object instanceof IInstallableUnit)
          {
            IInstallableUnit iu = (IInstallableUnit)object;
            result.add(P2Factory.eINSTANCE.createRequirement(iu.getId(), new VersionRange(iu.getVersion().toString())));
          }
        }

        return result;
      }
    }, DND_DELEGATES);

    tableViewer.addDragSupport(DND_OPERATIONS, DND_TRANSFERS, generalDragAdapter);
  }

  private void fillTable()
  {
    List<IInstallableUnit> plugins = getPlugins();
    Collections.sort(plugins);

    for (IInstallableUnit plugin : plugins)
    {
      TableItem item = new TableItem(table, SWT.NONE);
      item.setData(plugin);

      String id = plugin.getId();
      item.setText(0, id);

      String version = plugin.getVersion().toString();

      if (id.startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
      {
        try
        {
          Bundle[] bundles = Platform.getBundles(id, version);
          if (bundles != null)
          {
            for (Bundle bundle : bundles)
            {
              String buildID = OomphPlugin.getBuildID(bundle);
              if (buildID != null)
              {
                version += MessageFormat.format(Messages.AboutDialog_Build_label, buildID);
                break;
              }
            }
          }
        }
        catch (Exception ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }
      }
      else
      {
        item.setForeground(gray);
      }

      item.setText(ECLIPSE_VERSION_COLUMN_INDEX, version);
    }

    versionColumn.pack();
    versionColumn.setWidth(versionColumn.getWidth() + VERSION_COLUMN_PADDING);

    table.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });
  }

  private List<IInstallableUnit> getPlugins()
  {
    if (profile == null)
    {
      return Collections.emptyList();
    }

    List<IInstallableUnit> plugins = new ArrayList<IInstallableUnit>();
    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), null)))
    {
      if (showAllPlugins || iu.getId().startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
      {
        for (IProvidedCapability capability : iu.getProvidedCapabilities())
        {
          if ("osgi.bundle".equals(capability.getNamespace())) //$NON-NLS-1$
          {
            plugins.add(iu);
            break;
          }
        }
      }
    }

    return plugins;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    final Button showAllPluginsButton = createCheckbox(parent, Messages.AboutDialog_ShowAllPlugins_label);
    showAllPluginsButton.setSelection(showAllPlugins);
    showAllPluginsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        showAllPlugins = showAllPluginsButton.getSelection();
        dialogSettings.put(SHOW_ALL_PLUGINS, showAllPlugins);

        table.removeAll();
        fillTable();
      }
    });

    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
  }

  @Override
  protected String getShellText()
  {
    return PropertiesUtil.getProductName();
  }
}
