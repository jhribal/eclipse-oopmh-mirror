package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class SimpleInstallerMenu extends Shell
{
  public static class InstallerMenuItem extends SimpleInstallerButton
  {
    private static final Font FONT = SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///13/bold"));

    private static final Color WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

    private Divider divider;

    public InstallerMenuItem(final SimpleInstallerMenu menu)
    {
      super(menu, SWT.PUSH);
      GridLayout layout = UIUtil.createGridLayout(1);
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      setLayout(layout);
      setAlignment(SWT.LEFT);
      setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 36).create());

      setFont(FONT);
      setForeground(WHITE);

      divider = new Divider(this, 1);
      divider.setBackground(WHITE);
      divider.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.END).grab(true, true).create());

      addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          menu.close();
        }
      });
    }

    @Override
    public void setVisible(boolean visible)
    {
      super.setVisible(visible);

      Object data = getLayoutData();
      if (data instanceof GridData)
      {
        ((GridData)data).exclude = !visible;
        if (getParent() != null)
        {
          getParent().layout();
        }
      }
    }

    public void setDividerVisible(boolean visible)
    {
      ((GridData)divider.getLayoutData()).exclude = !visible;
      layout();
    }
  }

  private static class Divider extends Composite
  {
    private int height;

    public Divider(Composite parent, int height)
    {
      super(parent, SWT.NONE);
      this.height = height;
      addPaintListener(new PaintListener()
      {

        public void paintControl(PaintEvent e)
        {
          Rectangle clientArea = Divider.this.getClientArea();
          e.gc.fillRectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height);
        }
      });
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed)
    {
      return new Point(wHint > 0 ? wHint : 0, height);
    }
  }

  private static final int MENU_WIDTH = 340;

  private static final int MENU_HEIGHT = 553;

  public SimpleInstallerMenu(Shell parent)
  {
    super(parent, SWT.NO_TRIM);
    setSize(MENU_WIDTH, MENU_HEIGHT);
    setBackground(SetupInstallerPlugin.getColor(247, 148, 31));
    setBackgroundMode(SWT.INHERIT_FORCE);
    GridLayout layout = UIUtil.createGridLayout(1);
    layout.marginHeight = 19;
    layout.marginWidth = 28;
    layout.verticalSpacing = 0;
    setLayout(layout);

    SimpleInstallerButton closeButton = new ImageHoverButton(this, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close.png"),
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

  private void hookListeners()
  {
    getParent().addControlListener(new ControlAdapter()
    {
      @Override
      public void controlMoved(ControlEvent e)
      {
        adjustPosition();
      }
    });
  }

  @Override
  protected void checkSubclass()
  {
    /* Do nothing - Subclassing is allowed */
  }

  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    if (visible)
    {
      adjustPosition();
    }
  }

  private void adjustPosition()
  {
    Rectangle bounds = getParent().getBounds();
    Point bottomRight = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
    getParent().toDisplay(bottomRight);

    Point size = getSize();
    setBounds(bottomRight.x, bottomRight.y - size.y - 5, size.x, size.y);
  }

  @Override
  public void close()
  {
    setVisible(false);
  }
}
