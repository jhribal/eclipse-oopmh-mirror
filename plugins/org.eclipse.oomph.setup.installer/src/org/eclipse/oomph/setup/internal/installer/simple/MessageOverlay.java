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
package org.eclipse.oomph.setup.internal.installer.simple;

import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.internal.ui.OomphButton;
import org.eclipse.oomph.setup.internal.installer.SetupInstallerPlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Andreas Scharf
 */
public class MessageOverlay extends Shell implements ControlListener
{
  public static interface ActionHandler
  {
    void actionPerformed();
  }

  public static interface Locator
  {
    void relocate(Control control);
  }

  private static final int AUTO_DISMISS_DEFAULT = 4 * 1000;

  private static final int MAX_MESSAGE_LENGTH = 175;

  private static final int MAX_TOOLTIP_LINE_LENGTH = 60;

  public static enum Type
  {
    ERROR(SetupInstallerPlugin.getColor(249, 54, 50), SimpleInstallerDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png")), SUCCESS(SetupInstallerPlugin.getColor(58, 195, 4),
        SimpleInstallerDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"), SetupInstallerPlugin.INSTANCE
            .getSWTImage("simple/close_message_hover.png"));

    public final Color backgroundColor;

    public final Color foregroundColor;

    public final Image closeImg;

    public final Image closeImgHover;

    private Type(Color backgroundColor, Color foregroundColor, Image closeImg, Image closeImgHover)
    {
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.closeImg = closeImg;
      this.closeImgHover = closeImgHover;
    }
  }

  private final Locator locator;

  private Link link;

  private Type type;

  private final boolean autoDismiss;

  private boolean firstShown = true;

  private final SimpleInstallerDialog dialog;

  private final ActionHandler handler;

  public MessageOverlay(SimpleInstallerDialog dialog, Type type, Locator locator, boolean autoDismiss)
  {
    this(dialog, type, locator, autoDismiss, null);
  }

  public MessageOverlay(SimpleInstallerDialog dialog, Type type, Locator locator, boolean autoDismiss, ActionHandler handler)
  {
    super(dialog, SWT.NO_TRIM);
    this.dialog = dialog;
    this.handler = handler;

    if (locator == null)
    {
      throw new IllegalArgumentException("Locator must not be null!");
    }
    if (type == null)
    {
      throw new IllegalArgumentException("Type must not be null!");
    }

    this.type = type;
    this.locator = locator;
    this.autoDismiss = autoDismiss;

    setBackground(this.type.backgroundColor);
    setBackgroundMode(SWT.INHERIT_FORCE);

    GridLayout layout = new GridLayout(2, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.marginLeft = 22;
    layout.marginRight = 18;
    layout.marginTop = 3;
    layout.marginBottom = 3;
    setLayout(layout);

    link = new Link(this, SWT.NONE);
    link.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).create());
    link.setFont(SimpleInstallerDialog.getDefaultFont());
    link.setForeground(this.type.foregroundColor);
    if (handler != null)
    {
      link.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          MessageOverlay.this.handler.actionPerformed();
          close();
        }
      });
    }

    dialog.addControlListener(this);

    OomphButton closeButton = new ImageHoverButton(this, SWT.PUSH, this.type.closeImg, this.type.closeImgHover);
    closeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        close();
      }
    });
    closeButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.BEGINNING).indent(0, 12).create());

    // Initial bounds
    this.locator.relocate(this);
  }

  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);
    if (firstShown && visible && autoDismiss)
    {
      firstShown = false;
      final Display display = getDisplay();
      Thread dismissThread = new Thread(new Runnable()
      {

        public void run()
        {
          try
          {
            Thread.sleep(AUTO_DISMISS_DEFAULT);
          }
          catch (InterruptedException ex)
          {
            // Nothing to do here
          }
          display.asyncExec(new Runnable()
          {

            public void run()
            {
              dialog.clearMessage();
            }
          });
        }
      });
      dismissThread.setDaemon(true);
      dismissThread.start();
    }
  }

  @Override
  public void dispose()
  {
    getParent().removeControlListener(this);
    super.dispose();
  }

  @Override
  protected void checkSubclass()
  {
    // Nothing to do
  }

  public void setMessage(String message)
  {
    String tmp = message;
    int maxMessageLength = MAX_MESSAGE_LENGTH;
    if (message.length() > maxMessageLength)
    {
      tmp = StringUtil.ellipsis(message, maxMessageLength, false);

      String wrapText = StringUtil.wrapText(message, MAX_TOOLTIP_LINE_LENGTH, true);
      wrapText = ensureMaxLineLength(message, wrapText, MAX_TOOLTIP_LINE_LENGTH);

      link.setToolTipText(wrapText);
    }
    else
    {
      link.setToolTipText(null);
    }
    link.setText(tmp);
    layout();
  }

  private String ensureMaxLineLength(String originalText, String wrappedText, int maxLineLength)
  {
    String[] lines = wrappedText.contains(StringUtil.NL) ? wrappedText.split(StringUtil.NL) : new String[] { wrappedText };

    for (String line : lines)
    {
      if (line.length() > maxLineLength)
      {
        wrappedText = StringUtil.wrapText(originalText, maxLineLength, false);
        break;
      }
    }
    return wrappedText;
  }

  public void controlResized(ControlEvent e)
  {
    if (!isDisposed())
    {
      MessageOverlay.this.locator.relocate(MessageOverlay.this);
    }
  }

  public void controlMoved(ControlEvent e)
  {
    if (!isDisposed())
    {
      MessageOverlay.this.locator.relocate(MessageOverlay.this);
    }
  }
}
