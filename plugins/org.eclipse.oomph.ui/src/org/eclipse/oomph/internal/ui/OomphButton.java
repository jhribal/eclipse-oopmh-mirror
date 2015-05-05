/*
 * Copyright (c) 2014 Andreas Scharf (Kassel, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andreas Scharf - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Special button that does not draw any borders on hover/down coming from the OS.
 *
 * @author ascharf
 */
public class OomphButton extends Canvas implements Listener, PaintListener
{
  private static final int DEFAULT_ICON_TEXT_GAP = 5;

  private static final Color COLOR_DEFAULT_HOVER = UIPlugin.getColor(44, 34, 85);

  private static final Color COLOR_DEFAULT_DISABLED = UIPlugin.getColor(210, 210, 210);

  private List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

  private int buttonStyle;

  private boolean hover;

  private Color hoverColor = COLOR_DEFAULT_HOVER;

  private Color disabledColor = COLOR_DEFAULT_DISABLED;

  private Image image;

  private String text;

  private int cornerWidth = 0;

  private int iconTextGap = DEFAULT_ICON_TEXT_GAP;

  private int alignment = SWT.LEFT;

  private boolean listenersPaused;

  private boolean mouseDown;

  private boolean mouseLockedInBounds;

  private boolean showButtonDownState = true;

  public OomphButton(Composite parent, int buttonStyle)
  {
    super(parent, SWT.TRANSPARENT);
    this.buttonStyle = buttonStyle;

    setLayout(new GridLayout(1, false));
    setBackground(null);
    setCursor(UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND));

    hookListeners();
  }

  public void setText(String text)
  {
    if (this.text != text)
    {
      this.text = text;
      redraw();
    }
  }

  public void setImage(Image image)
  {
    if (this.image != image)
    {
      this.image = image;
      redraw();
    }
  }

  public Image getImage()
  {
    return image;
  }

  public void setAlignment(int alignment)
  {
    switch (alignment)
    {
      case SWT.LEFT:
      case SWT.CENTER:
      case SWT.RIGHT:
        // ok - fall through
        break;
      default:
        throw new IllegalArgumentException("Alignment must be one of SWT.LEFT, SWT.CENTER, SWT.RIGHT");
    }
    if (this.alignment != alignment)
    {
      this.alignment = alignment;
      redraw();
    }
  }

  public int getAlignment()
  {
    return alignment;
  }

  @Override
  public Point computeSize(int wHint, int hHint, boolean changed)
  {
    Point size = null;

    if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
    {
      size = new Point(wHint, hHint);
    }
    else
    {
      size = getTotalSize();
    }

    if (wHint != SWT.DEFAULT)
    {
      size.x = wHint;
    }
    if (hHint != SWT.DEFAULT)
    {
      size.y = hHint;
    }

    // extend the size to be able to visualize button down state
    size.x += 1;
    size.y += 1;

    return size;
  }

  protected Point getTotalSize()
  {
    int width = 0;
    int height = 0;
    if (text != null)
    {
      GC gc = new GC(this);
      Point textSize = gc.textExtent(text);
      width += textSize.x;

      if (image != null)
      {
        width += iconTextGap;
      }

      height += textSize.y;
    }

    if (image != null)
    {
      Rectangle imgBounds = image.getBounds();
      width += imgBounds.width;

      int heightDiff = imgBounds.height - height;
      if (heightDiff > 0)
      {
        height += heightDiff;
      }
    }

    return new Point(width, height);
  }

  protected void hookListeners()
  {
    addListener(SWT.MouseEnter, this);
    addListener(SWT.MouseExit, this);
    addListener(SWT.MouseDown, this);
    addListener(SWT.MouseUp, this);
    addListener(SWT.MouseMove, this);

    addPaintListener(this);
  }

  protected void unhookListeners()
  {
    removePaintListener(this);

    removeListener(SWT.MouseMove, (Listener)this);
    removeListener(SWT.MouseUp, (Listener)this);
    removeListener(SWT.MouseDown, (Listener)this);
    removeListener(SWT.MouseExit, (Listener)this);
    removeListener(SWT.MouseEnter, (Listener)this);
  }

  @Override
  public void dispose()
  {
    unhookListeners();
    super.dispose();
  }

  public void setIconTextGap(int iconTextGap)
  {
    if (this.iconTextGap != iconTextGap)
    {
      this.iconTextGap = iconTextGap;
      if (getParent() != null)
      {
        getParent().layout();
      }
    }
  }

  /**
   * Called when this button should paint itself.
   *
   * Subclasses may implement.
   */
  protected void drawContent(PaintEvent e)
  {
    GC gc = e.gc;

    Point totalSize = getTotalSize();

    Rectangle clientArea = getClientArea();

    int startX = 0;

    switch (getAlignment())
    {
      case SWT.CENTER:
        startX = (clientArea.x + clientArea.width - totalSize.x) / 2;
        break;
      case SWT.RIGHT:
        startX = clientArea.x + clientArea.width - totalSize.x;
        break;
    }

    if (showButtonDownState && mouseLockedInBounds)
    {
      startX++;
    }

    if (image != null)
    {
      Rectangle imgBounds = image.getBounds();
      int imgY = (clientArea.y + clientArea.height - imgBounds.height) / 2;

      if (showButtonDownState && mouseLockedInBounds)
      {
        imgY++;
      }

      drawImage(gc, startX, imgY);

      startX += imgBounds.width;
      if (text != null)
      {
        startX += iconTextGap;
      }
    }

    if (text != null)
    {
      Point textExtent = gc.textExtent(text);
      int textY = (clientArea.y + clientArea.height - textExtent.y) / 2;

      if (showButtonDownState && mouseLockedInBounds)
      {
        textY += 1;
      }
      drawText(gc, startX, textY);
    }
  }

  protected void drawText(GC gc, int x, int y)
  {
    if (isEnabled())
    {
      if (isHover() && !listenersPaused && hoverColor != null)
      {
        gc.setForeground(hoverColor);
      }
    }
    else if (disabledColor != null)
    {
      gc.setForeground(disabledColor);
    }
    gc.drawText(text, x, y, true);
  }

  protected void drawImage(GC gc, int x, int y)
  {
    gc.drawImage(image, x, y);
  }

  @Override
  public int getStyle()
  {
    return buttonStyle;
  }

  @Override
  protected void checkSubclass()
  {
    // Nothing to do
  }

  public boolean isHover()
  {
    return hover;
  }

  public void setShowButtonDownState(boolean showButtonDownState)
  {
    if (this.showButtonDownState != showButtonDownState)
    {
      this.showButtonDownState = showButtonDownState;
      redraw();
    }
  }

  public void handleEvent(Event event)
  {
    switch (event.type)
    {
      case SWT.MouseEnter:
        onMouseEnter(event);
        break;
      case SWT.MouseUp:
        onMouseUp(event);
        break;
      case SWT.MouseExit:
        onMouseExit(event);
        break;
      case SWT.MouseDown:
        onMouseDown(event);
        break;
      case SWT.MouseMove:
        mouseMoveInternal(event);
    }

    if (!isDisposed())
    {
      redraw();
    }
  }

  private void mouseMoveInternal(Event event)
  {
    if (mouseDown)
    {
      Rectangle bounds = getBounds();
      boolean inBounds = event.x >= 0 && event.x <= bounds.width && event.y >= 0 && event.y <= bounds.height;
      if (inBounds != mouseLockedInBounds)
      {
        mouseLockedInBounds = inBounds;
        if (inBounds)
        {
          onMouseEnter(event);
        }
        else
        {
          onMouseExit(event);
        }
      }
    }
  }

  protected void onMouseDown(Event event)
  {
    mouseDown = true;
    mouseLockedInBounds = true;
  }

  protected void onMouseExit(Event event)
  {
    setHover(false);
  }

  protected void onMouseUp(Event event)
  {
    mouseLockedInBounds = false;
    mouseDown = false;
    setHover(true);
    notifySelectionListeners(new SelectionEvent(event));
  }

  protected void onMouseEnter(Event event)
  {
    setHover(true);
  }

  private void setHover(boolean hover)
  {
    if (this.hover != hover)
    {
      this.hover = hover;
      onHover();
    }
  }

  /**
   * Called when the hover status of this button changes
   */
  protected void onHover()
  {
    // Subclasses may implement
  }

  private void notifySelectionListeners(SelectionEvent event)
  {
    if (!isEnabled() || listenersPaused)
    {
      return;
    }

    Rectangle r = getBounds();
    if (event.x >= 0 && event.x <= r.width && event.y >= 0 && event.y <= r.height && !selectionListeners.isEmpty())
    {
      for (SelectionListener listener : selectionListeners)
      {
        try
        {
          listener.widgetSelected(event);
        }
        catch (Exception ex)
        {
          UIPlugin.INSTANCE.log(ex);
        }
      }
    }
  }

  protected boolean isListenersPaused()
  {
    return listenersPaused;
  }

  protected void setListenersPaused(boolean pause)
  {
    listenersPaused = pause;
  }

  public void setCornerWidth(int cornerWidth)
  {
    if (this.cornerWidth != cornerWidth)
    {
      this.cornerWidth = cornerWidth;
      redraw();
    }
  }

  public int getCornerWidth()
  {
    return cornerWidth;
  }

  public void addSelectionListener(SelectionListener listener)
  {
    selectionListeners.add(listener);
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    selectionListeners.remove(selectionListeners);
  }

  public final void paintControl(PaintEvent e)
  {
    Rectangle clientBounds = getClientArea();
    GC gc = e.gc;
    gc.setAntialias(SWT.ON);
    int clientX = clientBounds.x;
    int clientY = clientBounds.y;

    int clientWidth = clientBounds.width - 1;
    int clientHeight = clientBounds.height - 1;

    if (showButtonDownState && mouseLockedInBounds)
    {
      clientX++;
      clientY++;
    }

    drawBackground(gc, clientX, clientY, clientWidth, clientHeight, 0, 0);
    drawContent(e);
    if (isHover())
    {
      drawHoverState(gc, clientX, clientY, clientWidth, clientHeight);
    }
  }

  protected void drawHoverState(GC gc, int x, int y, int width, int height)
  {
    // Subclasses may implement to draw a hover state.
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    gc.fillRoundRectangle(x, y, width, height, cornerWidth, cornerWidth);
  }

  public Color getHoverColor()
  {
    return hoverColor;
  }

  public void setHoverColor(Color hoverColor)
  {
    if (this.hoverColor != hoverColor)
    {
      this.hoverColor = hoverColor;
      redraw();
    }
  }

  public Color getDisabledColor()
  {
    return disabledColor;
  }

  public void setDisabledColor(Color disabledColor)
  {
    if (this.disabledColor != disabledColor)
    {
      this.disabledColor = disabledColor;
      redraw();
    }
  }

}
