/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.internal.ui.ToggleSwitchButton;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Andreas Scharf
 */
public class SimpleInstallerMenu extends Shell implements Listener
{
  private static final int MENU_MIN_WIDTH = 340;

  private static final int MENU_MIN_HEIGHT = 553;

  public SimpleInstallerMenu(Shell parent)
  {
    super(parent, SWT.NO_TRIM);
    setSize(MENU_MIN_WIDTH, MENU_MIN_HEIGHT);
    setBackground(SetupInstallerPlugin.getColor(247, 148, 31));
    setBackgroundMode(SWT.INHERIT_FORCE);

    GridLayout layout = UIUtil.createGridLayout(1);
    layout.marginHeight = 19;
    layout.marginWidth = 28;
    layout.verticalSpacing = 0;
    setLayout(layout);

    FlatButton closeButton = new ImageHoverButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_hover.png"));
    closeButton.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).create());
    closeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        close();
      }
    });

    hookListeners();
  }

  public InstallerMenuItem findMenuItemByName(String name)
  {
    if (name == null)
    {
      throw new IllegalArgumentException("name must not be null");
    }

    for (Control child : getChildren())
    {
      if (child instanceof InstallerMenuItem && name.equals(((InstallerMenuItem)child).getText()))
      {
        return (InstallerMenuItem)child;
      }
    }

    return null;
  }

  @Override
  public void setVisible(boolean visible)
  {
    if (visible)
    {
      adjustPosition();
    }

    super.setVisible(visible);

    if (visible)
    {
      setFocus();
      forceFocus();
    }
  }

  @Override
  public void close()
  {
    setVisible(false);
  }

  @Override
  protected void checkSubclass()
  {
    // Subclassing is allowed.
  }

  private void hookListeners()
  {
    Composite parent = getParent();
    parent.addListener(SWT.Move, this);
    parent.addListener(SWT.Resize, this);

    Display display = getDisplay();
    display.addFilter(SWT.FocusOut, this);
    display.addFilter(SWT.FocusIn, this);
    display.addFilter(SWT.MouseDown, this);

    addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        removeDisposeListener(this);
        unhookListeners();
      }
    });
  }

  private void unhookListeners()
  {
    Display display = getDisplay();
    display.removeFilter(SWT.MouseDown, this);
    display.removeFilter(SWT.FocusIn, this);
    display.removeFilter(SWT.FocusOut, this);

    Composite parent = getParent();
    parent.removeListener(SWT.Resize, this);
    parent.removeListener(SWT.Move, this);
  }

  private void adjustPosition()
  {
    Composite parent = getParent();
    Rectangle bounds = parent.getBounds();

    Point menuStartLocation = new Point(bounds.x + bounds.width, bounds.y + 75);
    parent.toDisplay(menuStartLocation);

    Point prefSize = computeSize(SWT.DEFAULT, SWT.DEFAULT);
    Point size = new Point(Math.max(prefSize.x, MENU_MIN_WIDTH), Math.max(prefSize.y, bounds.height - 80) - 5);

    setBounds(menuStartLocation.x, menuStartLocation.y, size.x, size.y);
  }

  public void handleEvent(Event event)
  {
    switch (event.type)
    {
      case SWT.Move:
      case SWT.Resize:
        adjustPosition();
        break;
      case SWT.FocusIn:
      case SWT.FocusOut:
      case SWT.MouseDown:
        if (closeMenu(event))
        {
          close();
        }
        break;
    }
  }

  private boolean closeMenu(Event event)
  {
    Display display = getDisplay();
    Control focusControl = display.getFocusControl();
    Control cursorControl = display.getCursorControl();

    if (cursorControl == null)
    {
      return true;
    }

    if (focusControl == this)
    {
      return false;
    }

    boolean menuButtonPressed = SimpleInstallerMenuButton.ACCESS_KEY.equals(AccessUtil.getKey(cursorControl));
    if (!menuButtonPressed && event.type == SWT.FocusOut && focusControl != null)
    {
      menuButtonPressed = SimpleInstallerMenuButton.ACCESS_KEY.equals(AccessUtil.getKey(focusControl));
    }

    if (menuButtonPressed)
    {
      return false;
    }

    if (focusControl != null && UIUtil.isParent(this, focusControl))
    {
      return false;
    }

    return true;
  }

  /**
   * @author Andreas Scharf
   */
  public static class InstallerMenuItem extends Composite
  {
    private static final Font FONT = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///13/bold"));

    private ImageHoverButton button;

    private Divider divider;

    public InstallerMenuItem(final SimpleInstallerMenu menu)
    {
      super(menu, SWT.NONE);

      GridLayout layout = UIUtil.createGridLayout(1);
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      layout.verticalSpacing = 0;

      setLayout(layout);
      setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 36).create());

      Composite content = new Composite(this, SWT.NONE);
      content.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

      createContent(content);

      divider = new Divider(this, 1);
      divider.setBackground(AbstractSimpleDialog.COLOR_WHITE);
      divider.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.END).grab(true, false).create());

      addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          menu.close();
        }
      });
    }

    public void createContent(Composite content)
    {
      GridLayout layout = UIUtil.createGridLayout(1);
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      content.setLayout(layout);

      button = new ImageHoverButton(content, SWT.PUSH);
      button.setAlignment(SWT.LEFT);
      button.setForeground(AbstractSimpleDialog.COLOR_WHITE);
      button.setFont(FONT);
      button.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).create());
    }

    public void setDefaultImage(Image defaultImage)
    {
      button.setDefaultImage(defaultImage);
    }

    public void setHoverImage(Image hoverImage)
    {
      button.setHoverImage(hoverImage);
    }

    public void addSelectionListener(SelectionListener listener)
    {
      button.addSelectionListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener)
    {
      button.removeSelectionListener(listener);
    }

    public String getText()
    {
      return button.getText();
    }

    public void setText(String text)
    {
      button.setText(text);
    }

    @Override
    public void setVisible(boolean visible)
    {
      super.setVisible(visible);

      Object data = getLayoutData();
      if (data instanceof GridData)
      {
        ((GridData)data).exclude = !visible;

        Composite parent = getParent();
        if (parent != null)
        {
          parent.layout();
        }
      }
    }

    public boolean isdDividerVisible()
    {
      return !((GridData)divider.getLayoutData()).exclude;
    }

    public void setDividerVisible(boolean visible)
    {
      ((GridData)divider.getLayoutData()).exclude = !visible;
      layout();
    }

    /**
     * @author Andreas Scharf
     */
    private static final class Divider extends Composite implements PaintListener
    {
      private final int height;

      public Divider(Composite parent, int height)
      {
        super(parent, SWT.NONE);
        this.height = height;
        addPaintListener(this);
      }

      @Override
      public Point computeSize(int wHint, int hHint, boolean changed)
      {
        return new Point(wHint > 0 ? wHint : 0, height);
      }

      public void paintControl(PaintEvent e)
      {
        Rectangle clientArea = getClientArea();
        e.gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
      }
    }
  }

  /**
   * @author Andreas Scharf
   */
  public static class InstallerMenuItemWithToggle extends InstallerMenuItem
  {

    private ToggleSwitchButton toggleSwitch;

    public InstallerMenuItemWithToggle(SimpleInstallerMenu menu)
    {
      super(menu);
    }

    @Override
    public void createContent(Composite content)
    {
      super.createContent(content);

      GridLayout layout = UIUtil.createGridLayout(2);
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      content.setLayout(layout);

      toggleSwitch = new ToggleSwitchButton(content);
      toggleSwitch.setLayoutData(GridDataFactory.swtDefaults().create());
    }

    public ToggleSwitchButton getToggleSwitch()
    {
      return toggleSwitch;
    }
  }
}
