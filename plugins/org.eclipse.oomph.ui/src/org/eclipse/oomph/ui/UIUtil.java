/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public final class UIUtil
{
  public static final IWorkbench WORKBENCH;

  private static Image ERROR_IMAGE;

  private static Image WARNING_IMAGE;

  private static Image INFO_IMAGE;

  static
  {
    IWorkbench workbench = null;

    try
    {
      workbench = PlatformUI.getWorkbench();
    }
    catch (Throwable ex)
    {
      // Workbench has not been created.
    }

    WORKBENCH = workbench;
  }

  private UIUtil()
  {
  }

  public static Display getDisplay()
  {
    Display display = Display.getCurrent();
    if (display == null)
    {
      try
      {
        display = PlatformUI.getWorkbench().getDisplay();
      }
      catch (Throwable ignore)
      {
        //$FALL-THROUGH$
      }
    }

    if (display == null)
    {
      display = Display.getDefault();
    }

    if (display == null)
    {
      display = new Display();
    }

    return display;
  }

  public static Shell getShell()
  {
    final Shell[] shell = { null };

    final Display display = getDisplay();
    display.syncExec(new Runnable()
    {
      public void run()
      {
        shell[0] = display.getActiveShell();

        if (shell[0] == null)
        {
          try
          {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window == null)
            {
              IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
              if (windows.length != 0)
              {
                window = windows[0];
              }
            }

            if (window != null)
            {
              shell[0] = window.getShell();
            }
          }
          catch (Throwable ignore)
          {
            //$FALL-THROUGH$
          }
        }

        if (shell[0] == null)
        {
          Shell[] shells = display.getShells();
          if (shells.length > 0)
          {
            shell[0] = shells[0];
          }
        }
      }
    });

    return shell[0];
  }

  public static GridLayout createGridLayout(int numColumns)
  {
    GridLayout layout = new GridLayout(numColumns, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    return layout;
  }

  public static GridData applyGridData(Control control)
  {
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    control.setLayoutData(data);
    return data;
  }

  public static GridData grabVertical(GridData data)
  {
    data.grabExcessVerticalSpace = true;
    data.verticalAlignment = GridData.FILL;
    return data;
  }

  public static void clearTextSelection(Object control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      text.clearSelection();
    }
  }

  public static void setSelectionToEnd(Widget control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      String content = text.getText();
      text.setSelection(content.length() + 1);
    }
  }

  public static void selectAllText(Widget control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      text.setSelection(0);
    }
  }

  private static Text findTextControl(Object control)
  {
    if (control instanceof Viewer)
    {
      control = ((Viewer)control).getControl();
    }

    if (control instanceof CCombo)
    {
      CCombo combo = (CCombo)control;
      try
      {
        control = ReflectUtil.getValue("text", combo);
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }
    }

    if (control instanceof Text)
    {
      return (Text)control;
    }

    return null;
  }

  public static void runInProgressDialog(Shell shell, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell)
    {
      @Override
      protected Point getInitialSize()
      {
        Point calculatedSize = super.getInitialSize();
        if (calculatedSize.x < 800)
        {
          calculatedSize.x = 800;
        }

        return calculatedSize;
      }
    };

    try
    {
      dialog.run(true, true, runnable);
    }
    catch (OperationCanceledException ex)
    {
      // Ignore.
    }
    catch (InvocationTargetException ex)
    {
      if (!(ex.getCause() instanceof OperationCanceledException))
      {
        throw ex;
      }
    }
  }

  public static void handleException(Throwable ex)
  {
    UIPlugin.INSTANCE.log(ex);
    ErrorDialog.open(ex);
  }

  public static Color getEclipseThemeColor()
  {
    return UIPlugin.getColor(44, 34, 85);
  }

  public static Image getStatusImage(int severity)
  {
    if (severity == IStatus.ERROR)
    {
      if (ERROR_IMAGE == null)
      {
        ERROR_IMAGE = UIPlugin.INSTANCE.getSWTImage("error");
      }

      return ERROR_IMAGE;
    }

    if (severity == IStatus.WARNING)
    {
      if (WARNING_IMAGE == null)
      {
        WARNING_IMAGE = UIPlugin.INSTANCE.getSWTImage("warning");
      }

      return WARNING_IMAGE;
    }

    if (INFO_IMAGE == null)
    {
      INFO_IMAGE = UIPlugin.INSTANCE.getSWTImage("info");
    }

    return INFO_IMAGE;
  }

  public static void exec(Display display, boolean async, Runnable runnable)
  {
    if (async)
    {
      asyncExec(display, runnable);
    }
    else
    {
      syncExec(display, runnable);
    }
  }

  public static void asyncExec(Runnable runnable)
  {
    final Display display = getDisplay();
    if (display != null)
    {
      asyncExec(display, runnable);
    }
  }

  public static void asyncExec(final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.asyncExec(new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void syncExec(final Runnable runnable)
  {
    final Display display = getDisplay();
    if (Display.getCurrent() == display || display == null)
    {
      runnable.run();
    }
    else
    {
      syncExec(display, runnable);
    }
  }

  public static void syncExec(final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.syncExec(new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void timerExec(int milliseconds, final Runnable runnable)
  {
    final Display display = getDisplay();
    if (display != null)
    {
      timerExec(milliseconds, display, runnable);
    }
  }

  public static void timerExec(int milliseconds, final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.timerExec(milliseconds, new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static IDialogSettings getOrCreateSection(IDialogSettings settings, String sectionName)
  {
    IDialogSettings section = settings.getSection(sectionName);
    if (section == null)
    {
      section = settings.addNewSection(sectionName);
    }

    return section;
  }

  public static void dispose(Resource... resources)
  {
    for (int i = 0; i < resources.length; i++)
    {
      Resource resource = resources[i];
      if (resource != null && !resource.isDisposed())
      {
        resource.dispose();
      }
    }
  }

  public static void simulateKey(char character)
  {
    Display display = getDisplay();

    Event event = new Event();
    event.type = SWT.KeyDown;
    event.character = character;
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
    }

    event.type = SWT.KeyUp;
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
    }
  }

  /**
   * Checks if the given {@link Control} is a child of the given
   * parent.
   *
   * @param parent The parent, not null.
   * @param controlToCheck The control to check, not null.
   *
   * @return <code>true</code> if the given control is a child of the given
   * parent, <code>false</code> otherwise.
   */
  public static boolean isParent(Composite parent, Control controlToCheck)
  {
    if (parent == null || controlToCheck == null)
    {
      throw new IllegalArgumentException("Neither parent nor controlToCheck must be null");
    }

    if (controlToCheck == parent)
    {
      return true;
    }

    Composite tmpParent = controlToCheck.getParent();

    while (tmpParent != parent && tmpParent != null)
    {
      tmpParent = tmpParent.getParent();
    }

    return tmpParent == parent;
  }
}
