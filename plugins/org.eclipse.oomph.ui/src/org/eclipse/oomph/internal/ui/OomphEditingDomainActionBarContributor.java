/*
 * Copyright (c) 2015-2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.texteditor.FindReplaceAction;

import java.lang.reflect.Field;
import java.util.ResourceBundle;

/**
 * @author Ed Merks
 */
public class OomphEditingDomainActionBarContributor extends EditingDomainActionBarContributor
{
  protected FindAction findAction;

  protected CollapseAllAction collapseAllAction;

  private RevertAction revertAction;

  public OomphEditingDomainActionBarContributor()
  {
    super();
  }

  public OomphEditingDomainActionBarContributor(int style)
  {
    super(style);
  }

  @Override
  public void init(IActionBars actionBars)
  {
    findAction = createFindAction();
    if (findAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
    }

    // Conditionally create EMF 2.14's expand-all and collapse-all actions.
    // Avoiding creating our own collapse-all action when EMF's is available ensures that the two appear in the same group on the tool bar.
    try
    {
      // Try to create EMF's expand-all action, which is new to EMF 2.14.
      Field expandAllActionField = ReflectUtil.getField(EditingDomainActionBarContributor.class, "expandAllAction"); //$NON-NLS-1$
      Class<?> expandAllActionClass = CommonPlugin.loadClass("org.eclipse.emf.edit.ui", "org.eclipse.emf.edit.ui.action.ExpandAllAction"); //$NON-NLS-1$ //$NON-NLS-2$
      Object expandAllAction = expandAllActionClass.newInstance();
      expandAllActionField.set(this, expandAllAction);

      // Try to create EMF's collapse-all action, which is new to EMF 2.14.
      Field collapseAllActionField = ReflectUtil.getField(EditingDomainActionBarContributor.class, "collapseAllAction"); //$NON-NLS-1$
      Class<?> collapseAllActionClass = CommonPlugin.loadClass("org.eclipse.emf.edit.ui", "org.eclipse.emf.edit.ui.action.CollapseAllAction"); //$NON-NLS-1$ //$NON-NLS-2$
      Object collapseAllAction = collapseAllActionClass.newInstance();
      collapseAllActionField.set(this, collapseAllAction);
    }
    catch (Exception ex)
    {
      // Failing that, create our own collapse all action.
      collapseAllAction = createCollapseAllAction();
    }

    revertAction = createRevertAction();
    if (revertAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.REVERT.getId(), revertAction);
    }

    super.init(actionBars);
  }

  protected FindAction createFindAction()
  {
    return new FindAction();
  }

  private CollapseAllAction createCollapseAllAction()
  {
    return new CollapseAllAction();
  }

  private RevertAction createRevertAction()
  {
    return new RevertAction();
  }

  @Override
  protected DeleteAction createDeleteAction()
  {
    // Specialize this so we can add the find action relative to the delete action.
    DeleteAction deleteAction = super.createDeleteAction();
    deleteAction.setId("delete"); //$NON-NLS-1$
    return deleteAction;
  }

  @Override
  public void contributeToToolBar(IToolBarManager toolBarManager)
  {
    super.contributeToToolBar(toolBarManager);

    if (collapseAllAction != null)
    {
      toolBarManager.add(collapseAllAction);
    }
  }

  @Override
  public void shareGlobalActions(IPage page, IActionBars actionBars)
  {
    super.shareGlobalActions(page, actionBars);

    if (findAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
    }
  }

  @Override
  public void deactivate()
  {
    super.deactivate();

    if (findAction != null)
    {
      findAction.setActiveWorkbenchPart(null);
    }

    if (collapseAllAction != null)
    {
      collapseAllAction.setActiveWorkbenchPart(null);
    }

    if (revertAction != null)
    {
      revertAction.setActiveWorkbenchPart(null);
    }
  }

  @Override
  public void activate()
  {
    super.activate();

    if (findAction != null)
    {
      findAction.setActiveWorkbenchPart(activeEditor);
    }

    if (collapseAllAction != null)
    {
      collapseAllAction.setActiveWorkbenchPart(activeEditor);
    }

    if (revertAction != null)
    {
      revertAction.setActiveWorkbenchPart(activeEditor);
    }
  }

  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    super.menuAboutToShow(menuManager);

    menuManager.insertAfter("delete", new ActionContributionItem(findAction)); //$NON-NLS-1$
    menuManager.insertAfter("delete", new Separator()); //$NON-NLS-1$
  }

  public static class FindAction extends Action
  {
    private IWorkbenchPart workbenchPart;

    public FindAction()
    {
      super(Messages.OomphEditingDomainActionBarContributor_findOrReplace, UIPlugin.INSTANCE.getImageDescriptor("search")); //$NON-NLS-1$
    }

    @Override
    public void run()
    {
      IFindReplaceTarget adapter = ObjectUtil.adapt(workbenchPart, IFindReplaceTarget.class);
      if (adapter instanceof FindAndReplaceTarget)
      {
        FindAndReplaceTarget findAndReplaceTarget = (FindAndReplaceTarget)adapter;
        findAndReplaceTarget.initialize(workbenchPart);
      }

      // Reuse the platform's text editor's find and replace action.
      new FindReplaceAction(null, null, workbenchPart)
      {
        @Override
        protected void initialize(ResourceBundle bundle, String prefix)
        {
        }
      }.run();
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      this.workbenchPart = workbenchPart;
      if (workbenchPart != null)
      {
        setEnabled(workbenchPart.getAdapter(IFindReplaceTarget.class) != null);
      }
    }
  }

  public static final class CollapseAllAction extends Action
  {
    private IViewerProvider viewerProvider;

    public CollapseAllAction()
    {
      super(Messages.OomphEditingDomainActionBarContributor_collapseAll_action, IAction.AS_PUSH_BUTTON);
      setImageDescriptor(UIPlugin.INSTANCE.getImageDescriptor("collapse-all")); //$NON-NLS-1$
      setToolTipText(Messages.OomphEditingDomainActionBarContributor_collapseAll_tooltip);
    }

    @Override
    public void run()
    {
      Viewer viewer = viewerProvider.getViewer();
      if (viewer instanceof TreeViewer)
      {
        TreeViewer treeViewer = (TreeViewer)viewer;
        treeViewer.collapseAll();
      }
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof IViewerProvider)
      {
        viewerProvider = (IViewerProvider)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        viewerProvider = null;
      }
    }
  }

  private static final class RevertAction extends Action
  {
    private IRevertablePart revertableEditor;

    @Override
    public void run()
    {
      revertableEditor.doRevert();
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof IRevertablePart)
      {
        revertableEditor = (IRevertablePart)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        revertableEditor = null;
      }
    }
  }
}
