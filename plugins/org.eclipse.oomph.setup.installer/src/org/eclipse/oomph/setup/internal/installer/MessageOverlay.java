package org.eclipse.oomph.setup.internal.installer;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MessageOverlay extends Shell implements ControlListener
{
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

  private Label text;

  private Type type;

  private final boolean autoDismiss;

  private boolean firstShown = true;

  private final SimpleInstallerDialog dialog;

  public MessageOverlay(SimpleInstallerDialog dialog, Type type, Locator locator, boolean autoDismiss)
  {
    super(dialog, SWT.NO_TRIM);
    this.dialog = dialog;

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

    text = new Label(this, SWT.WRAP);
    text.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).create());
    text.setFont(SimpleInstallerDialog.getDefaultFont());
    text.setForeground(this.type.foregroundColor);

    dialog.addControlListener(this);

    SimpleInstallerButton closeButton = new ImageHoverButton(this, SWT.PUSH, this.type.closeImg, this.type.closeImgHover);
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

      text.setToolTipText(wrapText);
    }
    else
    {
      text.setToolTipText(null);
    }
    text.setText(tmp);
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
