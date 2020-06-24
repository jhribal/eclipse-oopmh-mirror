/*
 * Copyright (c) 2015, 2016 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Yatta Solutions - [467209] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.core.util.SelfProductCatalogURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.IndexManagerDialog;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Andreas Scharf
 */
public class ProductCatalogsDialog extends AbstractSetupDialog
{
  public static final String TITLE = Messages.ProductCatalogsDialog_title;

  public static final String DESCRIPTION = Messages.ProductCatalogsDialog_description;

  private static final int APPLY_ID = IDialogConstants.CLIENT_ID + 1;

  private final CatalogManager catalogManager;

  private CheckboxTableViewer catalogViewer;

  private final IndexManager indexManager;

  private Installer installer;

  private Button applyButton;

  private URI originalIndex;

  private URI indexSelection;

  private List<Scope> originalSelectedCatalogs;

  private Map<URI, String> indexChoices;

  public ProductCatalogsDialog(Shell parentShell, Installer installer, CatalogManager catalogManager)
  {
    super(parentShell, TITLE, 700, 300, SetupInstallerPlugin.INSTANCE, false);
    this.installer = installer;
    this.catalogManager = catalogManager;
    indexManager = new IndexManager();

    Resource resource = catalogManager.getIndex().eResource();
    originalIndex = resource.getResourceSet().getURIConverter().normalize(resource.getURI());

    indexChoices = indexManager.getIndexLabels(true);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + "."; //$NON-NLS-1$
  }

  @Override
  protected void createUI(Composite parent)
  {
    catalogViewer = CheckboxTableViewer.newCheckList(parent, SWT.NONE);
    catalogViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    catalogViewer.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
        // Do nothing.
      }

      public void dispose()
      {
        // Do nothing.
      }

      public Object[] getElements(Object inputElement)
      {
        if (!(inputElement instanceof CatalogManager))
        {
          throw new IllegalArgumentException(Messages.ProductCatalogsDialog_BadInput_exception);
        }

        List<Scope> scopes = new ArrayList<Scope>();
        for (Scope scope : catalogManager.getCatalogs(true))
        {
          if (!scope.eIsProxy() && !"redirectable".equals(scope.getName())) //$NON-NLS-1$
          {
            scopes.add(scope);
          }
        }

        return scopes.toArray();
      }
    });

    catalogViewer.addFilter(new ViewerFilter()
    {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element)
      {
        return element instanceof Scope && !SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(((Scope)element).getName());
      }
    });

    catalogViewer.setLabelProvider(new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return SetupCoreUtil.getLabel((Scope)element);
      }
    });

    catalogViewer.setInput(catalogManager);
    originalSelectedCatalogs = new ArrayList<Scope>(catalogManager.getSelectedCatalogs(true));
    catalogViewer.setCheckedElements(originalSelectedCatalogs.toArray());

    catalogViewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        updateEnablement();
      }
    });

    final AdapterImpl selectionAdapter = new AdapterImpl()
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        if (notification.getFeature() == SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS)
        {
          catalogViewer.refresh();
          originalSelectedCatalogs = new ArrayList<Scope>(catalogManager.getSelectedCatalogs(true));
          catalogViewer.setCheckedElements(originalSelectedCatalogs.toArray());
          updateEnablement();
        }
      }
    };

    final CatalogSelection selection = catalogManager.getSelection();
    selection.eAdapters().add(selectionAdapter);
    parent.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        selection.eAdapters().remove(selectionAdapter);
      }
    });
  }

  @Override
  protected Control createButtonBar(Composite parent)
  {
    int size = indexChoices.size();
    if (size > 1 || !indexChoices.containsKey(originalIndex))
    {
      return createButtonBarWithControls(parent);
    }

    return super.createButtonBar(parent);
  }

  @Override
  protected void createControlsForButtonBar(Composite parent)
  {
    createLabel(parent, Messages.ProductCatalogsDialog_CatalogIndex_label);

    boolean indexUnmanaged = !indexChoices.containsKey(originalIndex);
    setMessage(DESCRIPTION + Messages.ProductCatalogsDialog_SwitchIndex_suffix_message);

    final Combo indexCombo = createCombo(parent);

    final List<String> values = new ArrayList<String>(indexChoices.values());
    addManagementValues(values);

    String[] items = values.toArray(new String[values.size()]);
    indexCombo.setItems(items);
    indexCombo.select(0);

    final List<URI> uris = new ArrayList<URI>(indexChoices.keySet());
    if (indexUnmanaged && uris.size() > 0)
    {
      indexSelection = uris.get(0);
    }

    indexCombo.setToolTipText(IndexManager.getUnderlyingLocation(indexSelection == null ? originalIndex : indexSelection).toString());

    indexCombo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        int selectionIndex = indexCombo.getSelectionIndex();
        if (selectionIndex >= uris.size())
        {
          URI currentSelection = indexSelection == null ? uris.isEmpty() ? null : uris.get(0) : indexSelection;
          indexCombo.select(indexSelection == null || currentSelection == null ? 0 : uris.indexOf(indexSelection));
          IndexManagerDialog indexManagerDialog = new IndexManagerDialog(getShell());
          indexManagerDialog.open();

          Map<URI, String> newIndexChoices = indexManager.getIndexLabels(true);

          values.clear();
          values.addAll(newIndexChoices.values());
          addManagementValues(values);

          uris.clear();
          uris.addAll(newIndexChoices.keySet());

          String[] items = values.toArray(new String[newIndexChoices.size()]);
          indexCombo.setItems(items);
          int index = uris.indexOf(currentSelection);
          if (index != -1)
          {
            indexCombo.select(index);
            indexSelection = currentSelection.equals(originalIndex) ? null : currentSelection;
            updateEnablement();
          }
          else if (uris.size() == 0)
          {
            close();
            return;
          }
          else
          {
            indexCombo.select(0);
            currentSelection = uris.get(0);
            indexSelection = currentSelection.equals(originalIndex) ? null : currentSelection;
            updateEnablement();
          }

        }
        else
        {
          URI indexLocationURI = uris.get(selectionIndex);
          indexSelection = indexLocationURI.equals(originalIndex) ? null : indexLocationURI;
          updateEnablement();
        }

        indexCombo.setToolTipText(IndexManager.getUnderlyingLocation(indexSelection == null ? originalIndex : indexSelection).toString());
      }
    });
  }

  private void addManagementValues(Collection<String> values)
  {
    // There are em dashes, which will form a line, rather than -- which has gaps.
    values.add("\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014"); //$NON-NLS-1$
    values.add(Messages.ProductCatalogsDialog_ManageIndices_label);
  }

  protected void updateEnablement()
  {
    List<Object> checkedElements = Arrays.asList(catalogViewer.getCheckedElements());
    boolean catalogSelectionChanged = !checkedElements.equals(originalSelectedCatalogs);
    boolean applyEnabled = catalogSelectionChanged || indexSelection != null;
    applyButton.setEnabled(applyEnabled);
    applyButton.setText(Messages.ProductCatalogsDialog_Apply_label);

    if (originalIndex == null)
    {
      if (catalogSelectionChanged)
      {
        setMessage(DESCRIPTION + Messages.ProductCatalogsDialog_ApplySelection_suffix_message);
      }
      else
      {
        setMessage(DESCRIPTION + "."); //$NON-NLS-1$
      }
    }
    else if (indexSelection != null)
    {
      setMessage(Messages.ProductCatalogsDialog_ApplyNewIndex_message, IMessageProvider.WARNING);
      catalogViewer.getControl().setEnabled(false);
    }
    else
    {
      catalogViewer.getControl().setEnabled(true);
      if (catalogSelectionChanged)
      {
        setMessage(DESCRIPTION + Messages.ProductCatalogsDialog_ApplySelection_suffix_message);
      }
      else if (indexChoices.size() > 1 || !indexChoices.containsKey(originalIndex))
      {
        setMessage(DESCRIPTION + Messages.ProductCatalogsDialog_SwitchCatalogIndex_suffix_message);
      }
      else
      {
        setMessage(DESCRIPTION + "."); //$NON-NLS-1$
      }
    }
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    applyButton = createButton(parent, APPLY_ID, Messages.ProductCatalogsDialog_Apply_label, false);
    applyButton.setEnabled(false);
    super.createButtonsForButtonBar(parent);

    updateEnablement();
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == APPLY_ID || buttonId == IDialogConstants.OK_ID)
    {
      if (indexSelection != null)
      {
        installer.reloadIndex(indexSelection);
        originalIndex = indexSelection;
        indexSelection = null;

        applyButton.setEnabled(false);

        Runnable buttonAnimator = new Runnable()
        {
          private String[] label = new String[] { Messages.ProductCatalogsDialog_Apply1_label, Messages.ProductCatalogsDialog_Apply2_label, Messages.ProductCatalogsDialog_Apply3_label };

          private int counter;

          public void run()
          {
            if (counter == 0 || !applyButton.isDisposed() && !Messages.ProductCatalogsDialog_Apply_label.equals(applyButton.getText()))
            {
              applyButton.setText(label[counter++ % 3]);
              UIUtil.timerExec(500, this);
            }
          }
        };

        buttonAnimator.run();
      }
      else
      {
        List<Object> checkedElements = Arrays.asList(catalogViewer.getCheckedElements());
        for (Scope scope : catalogManager.getCatalogs(true))
        {
          catalogManager.selectCatalog(true, scope, checkedElements.contains(scope));
        }
      }
    }

    super.buttonPressed(buttonId);
  }
}
