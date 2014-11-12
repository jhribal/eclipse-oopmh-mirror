/*
 * Copyright (c) 2014 Marco Descher and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marco Descher - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.providers;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormText;

public class HtmlTipSupport extends ToolTip
{
  private TreeViewer viewer;

  private String text;

  private IToolTipProvider tipProvider;

  private int style;

  private static final int DEFAULT_SHIFT_X = 10;

  private static final int DEFAULT_SHIFT_Y = 0;

  /**
   * Enable ToolTip support for the viewer by creating an instance from this
   * class.
   *
   * @param viewer
   *            the viewer the support is attached to
   * @param style
   *            style passed to control tool tip behavior
   *
   * @param manualActivation
   *            <code>true</code> if the activation is done manually using
   *            {@link #show(Point)}
   */
  public HtmlTipSupport(TreeViewer viewer, int style, boolean manualActivation)
  {
    super(viewer.getControl(), style, manualActivation);
    this.style = style;
    this.viewer = viewer;

    tipProvider = (IToolTipProvider)viewer.getLabelProvider();
    viewer.getControl().setToolTipText(""); //$NON-NLS-1$
  }

  @Override
  protected boolean shouldCreateToolTip(Event event)
  {
    if (!super.shouldCreateToolTip(event))
    {
      return false;
    }

    Point point = new Point(event.x, event.y);
    TreeItem item = viewer.getTree().getItem(point);
    if (item == null || item.getData() == null)
    {
      return false;
    }

    Object tip = tipProvider.getToolTip(item.getData());
    if (tip == null)
    {
      return false;
    }

    text = tip.toString();

    setShift(new Point(DEFAULT_SHIFT_X, DEFAULT_SHIFT_Y));

    return true;
  }

  @Override
  protected Composite createToolTipContentArea(Event event, Composite parent)
  {
    FormText formText = new FormText(parent, style)
    {
      @Override
      public Point computeSize(int wHint, int hHint, boolean changed)
      {
        return new Point(300, 200);
      }
    };
    if (text != null)
    {
      formText.setText("<form>" + text + "</form>", true, false);
    }
    return formText;
  }

  /**
   * Enable ToolTip support for the viewer by creating an instance from this
   * class.
   *
   * @param viewer
   *            the viewer the support is attached to
   */
  public static void enableFor(TreeViewer viewer)
  {
    new HtmlTipSupport(viewer, ToolTip.NO_RECREATE, false);
  }

  @Override
  protected Object getToolTipArea(Event event)
  {
    return viewer.getCell(new Point(event.x, event.y));
  }
}
