package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.UIPlugin;
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
public class SimpleInstallerButton extends Canvas implements Listener, PaintListener
{
  private static final int DEFAULT_ICON_TEXT_GAP = 5;

  private static final Color COLOR_DEFAULT_HOVER = SimpleInstallerDialog.COLOR_PURPLE;

  private static final Color COLOR_DEFAULT_DISABLED = SetupInstallerPlugin.getColor(210, 210, 210);

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

  public SimpleInstallerButton(Composite parent, int buttonStyle)
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
    if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
    {
      return new Point(wHint, hHint);
    }

    Point totalSize = getTotalSize();
    if (wHint != SWT.DEFAULT)
    {
      totalSize.x = wHint;
    }
    if (hHint != SWT.DEFAULT)
    {
      totalSize.y = hHint;
    }
    return totalSize;
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

    addPaintListener(this);
  }

  protected void unhookListeners()
  {
    removeListener(SWT.MouseEnter, (Listener)this);
    removeListener(SWT.MouseExit, (Listener)this);
    removeListener(SWT.MouseDown, (Listener)this);
    removeListener(SWT.MouseUp, (Listener)this);

    removePaintListener(this);
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

    if (image != null)
    {
      Rectangle imgBounds = image.getBounds();
      int imgY = (clientArea.y + clientArea.height - imgBounds.height) / 2;

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

  public void handleEvent(Event event)
  {
    switch (event.type)
    {
      case SWT.MouseEnter:
        hover = true;
        onHover();
        break;
      case SWT.MouseUp:
        hover = true;
        onHover();
        notifySelectionListeners(new SelectionEvent(event));
        break;
      case SWT.MouseExit:
      case SWT.MouseDown:
        hover = false;
        onHover();
        break;
    }

    if (!isDisposed())
    {
      redraw();
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
    drawBackground(gc, clientBounds.x, clientBounds.y, clientBounds.width, clientBounds.height, 0, 0);
    drawContent(e);
    if (isHover())
    {
      drawHoverState(gc, clientBounds.x, clientBounds.y, clientBounds.width, clientBounds.height);
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
