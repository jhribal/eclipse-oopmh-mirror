/*
 * Copyright (c) 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.internal.core.P2Index;
import org.eclipse.oomph.p2.internal.core.P2Index.Repository;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.SearchField;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class RepositoryFinderDialog extends DockableDialog implements FilterHandler, LocationListener
{
  private static final Object INPUT = new Object();

  private static final Object LOADING = new Object()
  {
    @Override
    public String toString()
    {
      return Messages.RepositoryFinderDialog_stringRepresentation;
    }
  };

  private static final int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;

  private static final List<? extends OomphTransferDelegate> DND_DELEGATES = Collections.singletonList(new OomphTransferDelegate.TextTransferDelegate());

  private static final Transfer[] DND_TRANSFERS = new Transfer[] { DND_DELEGATES.get(0).getTransfer() };

  private SearchField searchField;

  private Label statsLabel;

  private TableViewer viewer;

  private Repository[] repositories;

  private List<Repository> filteredRepositories;

  private Repository selectedRepository;

  public RepositoryFinderDialog(IWorkbenchWindow workbenchWindow)
  {
    super(workbenchWindow);
  }

  public Repository getSelectedRepository()
  {
    return selectedRepository;
  }

  @Override
  protected IDialogSettings getDialogBoundsSettings()
  {
    return P2UIPlugin.INSTANCE.getDialogSettings("RepositoryFinder"); //$NON-NLS-1$
  }

  @Override
  protected Control createContents(Composite parent)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("full/obj16/RepositoryList")); //$NON-NLS-1$
    getShell().setText(Messages.RepositoryFinderDialog_title);

    Composite composite = new Composite(parent, SWT.NONE);
    FillLayout layout = new FillLayout();
    composite.setLayout(layout);
    GridData layoutData = new GridData(GridData.FILL_BOTH);
    layoutData.widthHint = 800;
    layoutData.heightHint = 600;
    composite.setLayoutData(layoutData);
    applyDialogFont(composite);

    initializeDialogUnits(composite);

    int columns = 3;

    Composite content = new Composite(composite, SWT.NONE);
    content.setLayout(GridLayoutFactory.fillDefaults().extendedMargins(0, 0, LayoutConstants.getSpacing().y, 0).numColumns(columns).create());

    Label filterLabel = new Label(content, SWT.NONE);
    filterLabel.setLayoutData(GridDataFactory.fillDefaults().indent(LayoutConstants.getSpacing().x, 0).align(SWT.BEGINNING, SWT.CENTER).create());
    filterLabel.setText(Messages.RepositoryFinderDialog_filterLabel_text);

    searchField = new SearchField(content, this)
    {
      @Override
      protected void finishFilter()
      {
        if (repositories == null)
        {
          viewer.setSelection(new StructuredSelection(LOADING), true);
        }
        else if (filteredRepositories != null)
        {
          if (filteredRepositories.isEmpty())
          {
            return;
          }

          viewer.setSelection(new StructuredSelection(filteredRepositories.get(0)), true);
        }
        else if (filteredRepositories != null)
        {
          if (repositories.length == 0)
          {
            return;
          }

          viewer.setSelection(new StructuredSelection(repositories[0]), true);
        }

        viewer.getControl().setFocus();
      }
    };

    searchField.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
    searchField.getFilterControl().setToolTipText(Messages.RepositoryFinderDialog_searchField_tooltip);
    searchField.setFocus();
    searchField.setInitialText(""); //$NON-NLS-1$

    statsLabel = new Label(content, SWT.NONE);
    statsLabel.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.CENTER).create());
    statsLabel.setText(NLS.bind(Messages.RepositoryFinderDialog_statsLabel_repositories, "000000000")); // Make label big enough. //$NON-NLS-1$
    statsLabel.setVisible(false); // Don't render until proper text is set.

    Composite viewerComposite = new Composite(content, SWT.NONE);
    viewerComposite.setLayoutData(GridDataFactory.fillDefaults().span(columns, 1).grab(true, true).create());
    viewerComposite.setLayout(GridLayoutFactory.fillDefaults().spacing(0, 0).create());

    Label ruler = new Label(viewerComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
    ruler.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

    viewer = new TableViewer(viewerComposite, SWT.MULTI | SWT.VIRTUAL);
    viewer.getControl().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    viewer.getControl().setEnabled(false); // Loading...
    viewer.setUseHashlookup(true);
    viewer.setContentProvider(new FinderContentProvider());
    viewer.setLabelProvider(new FinderLabelProvider());
    viewer.setInput(INPUT);

    viewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = event.getStructuredSelection();
        Object element = selection.getFirstElement();
        if (element instanceof Repository)
        {
          selectedRepository = (Repository)element;
        }
        else
        {
          selectedRepository = null;
        }
      }
    });

    GeneralDragAdapter dragAdapter = new GeneralDragAdapter(viewer, new GeneralDragAdapter.DraggedObjectsFactory()
    {
      public List<Object> createDraggedObjects(ISelection selection) throws Exception
      {
        List<Object> result = new ArrayList<Object>();
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          if (object instanceof Repository)
          {
            Repository repository = (Repository)object;
            result.add(P2Factory.eINSTANCE.createRepository(repository.getLocation().toString()));
          }
        }

        return result;
      }
    }, DND_DELEGATES);

    viewer.addDragSupport(DND_OPERATIONS, DND_TRANSFERS, dragAdapter);

    dragAdapter.getContextMenu().addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        String url = getURL(viewer.getSelection());
        if (url != null)
        {
          manager.add(new ExplorerAction(url));
        }
      }
    });

    viewer.addOpenListener(new IOpenListener()
    {
      public void open(OpenEvent event)
      {
        String url = getURL(event.getSelection());
        if (url != null)
        {
          RepositoryExplorer.explore(url);
        }
      }
    });

    new ColumnViewerInformationControlToolTipSupport(viewer, this);

    loadRepositories();

    return composite;
  }

  public void handleFilter(final String filter)
  {
    if (repositories == null)
    {
      viewer.setItemCount(1); // Loading...
    }
    else if (filter == null || filter.length() == 0)
    {
      filteredRepositories = null;
      setStats(repositories.length);
      viewer.setItemCount(repositories.length);
    }
    else
    {
      Pattern pattern = StringUtil.globPattern(filter);
      List<Repository> list = new ArrayList<Repository>();

      for (Repository repository : repositories)
      {
        String url = repository.getLocation().toString();
        if (pattern.matcher(url).find())
        {
          list.add(repository);
        }
      }

      filteredRepositories = list;
      setStats(filteredRepositories.size());
      statsLabel.setText(NLS.bind(Messages.RepositoryFinderDialog_statsLabel_repositories, filteredRepositories.size()));
      viewer.setItemCount(filteredRepositories.size());
    }

    viewer.refresh();
  }

  @Override
  public boolean handleWorkbenchPart(IWorkbenchPart part)
  {
    return true;
  }

  public void changing(LocationEvent event)
  {
    if (repositories != null)
    {
      for (Repository repository : repositories)
      {
        if (repository.getLocation().toString().equals(event.location))
        {
          viewer.setSelection(new StructuredSelection(repository), true);
          viewer.getControl().setFocus();
          break;
        }
      }
    }
  }

  public void changed(LocationEvent event)
  {
    // Do nothing.
  }

  private void loadRepositories()
  {
    new Job(Messages.RepositoryFinderDialog_loadRepositoriesJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        repositories = P2Index.INSTANCE.getRepositories();
        if (repositories == null)
        {
          repositories = new Repository[0];
        }

        Arrays.sort(repositories);

        UIUtil.asyncExec(statsLabel, new Runnable()
        {
          public void run()
          {
            setStats(repositories.length);
            viewer.setItemCount(repositories.length);
            viewer.refresh();
            viewer.getControl().setEnabled(true);
          }
        });

        return Status.OK_STATUS;
      }
    }.schedule();
  }

  private void setStats(int repositories)
  {
    statsLabel.setText(NLS.bind(Messages.RepositoryFinderDialog_statsLabel_repositories, repositories));
    statsLabel.setVisible(true);
  }

  private void appendToolTipText(Repository repository, StringBuilder builder)
  {
    builder.append("<h3>"); //$NON-NLS-1$
    builder.append(repository.isComposed() ? Messages.RepositoryFinderDialog_tooltip_composite : Messages.RepositoryFinderDialog_tooltip_simple);
    builder.append("&nbsp;" + Messages.RepositoryFinderDialog_tooltip_repository + "&nbsp;<span style=\"white-space: nowrap;\">"); //$NON-NLS-1$ //$NON-NLS-2$
    builder.append(repository);
    builder.append("</span></h3><ul>"); //$NON-NLS-1$

    builder.append("<li><span style=\"white-space: nowrap;\">"); //$NON-NLS-1$
    builder.append(new Date(repository.getTimestamp()));
    builder.append("</span>&nbsp;("); //$NON-NLS-1$
    builder.append(repository.getTimestamp());
    builder.append(")"); //$NON-NLS-1$

    builder.append("<li>"); //$NON-NLS-1$
    builder.append(repository.getCapabilityCount());
    builder.append("&nbsp;" //$NON-NLS-1$
        + (repository.getCapabilityCount() == 1 ? Messages.RepositoryFinderDialog_tooltip_capability : Messages.RepositoryFinderDialog_tooltip_capabilities));

    if (repository.getUnresolvedChildren() != 0)
    {
      builder.append("<li><font color=\"#ff0000\"><b>"); //$NON-NLS-1$
      builder.append(repository.getUnresolvedChildren());
      builder.append("&nbsp;"); //$NON-NLS-1$
      builder.append(repository.getUnresolvedChildren() == 1 ? Messages.RepositoryFinderDialog_tooltip_unresolvedChild
          : Messages.RepositoryFinderDialog_tooltip_unresolvedChildren);
      builder.append("!</b></font>"); //$NON-NLS-1$
    }

    if (repository.isCompressed())
    {
      builder.append("<li>"); //$NON-NLS-1$
      builder.append(Messages.RepositoryFinderDialog_tooltip_compressed);
    }

    builder.append("</ul>"); //$NON-NLS-1$

    Repository[] children = repository.getChildren();
    if (children != null && children.length != 0)
    {
      builder.append("<h3>"); //$NON-NLS-1$
      builder.append(Messages.RepositoryFinderDialog_tooltip_children);
      builder.append("<h3>"); //$NON-NLS-1$
      builder.append("<ul>"); //$NON-NLS-1$

      for (Repository child : children)
      {
        builder.append("<li><a href=\""); //$NON-NLS-1$
        builder.append(child);
        builder.append("\"><span style=\"white-space: nowrap;\">"); //$NON-NLS-1$
        builder.append(child);
        builder.append("</span></a>"); //$NON-NLS-1$
      }

      builder.append("</ul>"); //$NON-NLS-1$
    }

    Repository[] composites = repository.getComposites();
    if (composites != null && composites.length != 0)
    {
      builder.append("<h3>"); //$NON-NLS-1$
      builder.append(Messages.RepositoryFinderDialog_tooltip_composites);
      builder.append("<h3>"); //$NON-NLS-1$
      builder.append("<ul>"); //$NON-NLS-1$

      for (Repository composite : composites)
      {
        builder.append("<li><a href=\""); //$NON-NLS-1$
        builder.append(composite);
        builder.append("\"><span style=\"white-space: nowrap;\">"); //$NON-NLS-1$
        builder.append(composite);
        builder.append("</span></a>"); //$NON-NLS-1$
      }

      builder.append("</ul>"); //$NON-NLS-1$
    }
  }

  private static String getURL(ISelection selection)
  {
    for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
    {
      Object object = it.next();
      if (object instanceof Repository)
      {
        Repository repository = (Repository)object;
        return repository.getLocation().toString();
      }
    }

    return null;
  }

  /**
   * Return the instance for this workbench window, if there is one.
   */
  public static RepositoryFinderDialog getFor(IWorkbenchWindow workbenchWindow)
  {
    return DockableDialog.getFor(RepositoryFinderDialog.class, workbenchWindow);
  }

  /**
   * Close the instance for this workbench window, if there is one.
   */
  public static void closeFor(IWorkbenchWindow workbenchWindow)
  {
    DockableDialog.closeFor(RepositoryFinderDialog.class, workbenchWindow);
  }

  /**
   * Reopen or create the instance for this workbench window.
   */
  public static RepositoryFinderDialog openFor(final IWorkbenchWindow workbenchWindow)
  {
    Factory<RepositoryFinderDialog> factory = new Factory<RepositoryFinderDialog>()
    {
      public RepositoryFinderDialog create(IWorkbenchWindow workbenchWindow)
      {
        return new RepositoryFinderDialog(workbenchWindow);
      }
    };

    return DockableDialog.openFor(RepositoryFinderDialog.class, factory, workbenchWindow);
  }

  /**
   * @author Eike Stepper
   */
  private final class FinderContentProvider implements ILazyContentProvider
  {
    public FinderContentProvider()
    {
      viewer.setItemCount(1); // Loading...
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      // Do nothing.
    }

    public void updateElement(int index)
    {
      if (filteredRepositories != null)
      {
        viewer.replace(filteredRepositories.get(index), index);
      }
      else if (repositories != null)
      {
        viewer.replace(repositories[index], index);
      }
      else
      {
        viewer.replace(LOADING, index);
      }
    }

    public void dispose()
    {
      // Do nothing.
    }
  }

  /**
   * @author Stepper
   */
  private final class FinderLabelProvider extends ColumnLabelProvider
  {
    private final Font baseFont = viewer.getControl().getFont();

    private final Font boldFont = P2UIPlugin.getBoldFont(baseFont);

    private final Color redColor = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_RED);

    private final Color grayColor = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_GRAY);

    @Override
    public Image getImage(Object element)
    {
      if (element instanceof Repository)
      {
        Repository repository = (Repository)element;
        if (repository.isComposed())
        {
          return P2UIPlugin.INSTANCE.getSWTImage("obj16/compositeRepository.png"); //$NON-NLS-1$
        }

        return P2UIPlugin.INSTANCE.getSWTImage("full/obj16/Repository"); //$NON-NLS-1$
      }

      return super.getImage(element);
    }

    @Override
    public Font getFont(Object element)
    {
      if (element instanceof Repository)
      {
        Repository repository = (Repository)element;
        if (repository.isComposed())
        {
          return boldFont;
        }
      }

      return super.getFont(element);
    }

    @Override
    public Color getForeground(Object element)
    {
      if (element instanceof Repository)
      {
        Repository repository = (Repository)element;
        if (repository.getUnresolvedChildren() != 0)
        {
          return redColor;
        }

        if (repository.getCapabilityCount() == 0)
        {
          return grayColor;
        }
      }

      return super.getForeground(element);
    }

    @Override
    public String getToolTipText(Object element)
    {
      if (element instanceof Repository)
      {
        Repository repository = (Repository)element;

        StringBuilder builder = new StringBuilder();
        appendToolTipText(repository, builder);
        return builder.toString();
      }

      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ExplorerAction extends Action
  {
    private final String url;

    public ExplorerAction(String url)
    {
      super(Messages.RepositoryFinderDialog_exploreAction_text, P2UIPlugin.INSTANCE.getImageDescriptor("full/obj16/Repository")); //$NON-NLS-1$
      this.url = url;
    }

    @Override
    public void run()
    {
      RepositoryExplorer.explore(url);
    }
  }
}
