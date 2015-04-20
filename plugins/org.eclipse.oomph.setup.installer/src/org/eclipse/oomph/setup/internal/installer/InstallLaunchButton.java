package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class InstallLaunchButton extends SimpleInstallerButton
{
  private static final Color COLOR_FOREGROUND_DEFAULT = SimpleInstallerDialog.COLOR_WHITE;

  private static final Color COLOR_INSTALL = SetupInstallerPlugin.getColor(250, 148, 0);

  private static final Color COLOR_INSTALLING = SetupInstallerPlugin.getColor(50, 196, 0);

  private static final Color COLOR_INSTALLING_FOREGROUND = SetupInstallerPlugin.COLOR_LABEL_FOREGROUND;

  private static final Color COLOR_LAUNCH = COLOR_INSTALLING;

  public static enum State
  {
    INSTALL("INSTALL", COLOR_INSTALL, COLOR_FOREGROUND_DEFAULT, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install-button-install.png")), INSTALLING(
        "INSTALLING", COLOR_INSTALLING, COLOR_INSTALLING_FOREGROUND), INSTALLED("LAUNCH", COLOR_LAUNCH, COLOR_FOREGROUND_DEFAULT, SetupInstallerPlugin.INSTANCE
        .getSWTImage("simple/install-button-launch.png"));

    public final Image icon;

    public final String label;

    public final Color backgroundColor;

    public final Color foregroundColor;

    State(final String label, final Color backgroundColor, final Color foregroundColor)
    {
      this(label, backgroundColor, foregroundColor, null);
    }

    State(final String label, final Color backgroundColor, final Color foregroundColor, final Image icon)
    {
      this.label = label;
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.icon = icon;
    }

  }

  private State currentState;

  private float progress;

  public InstallLaunchButton(Composite parent)
  {
    super(parent, SWT.PUSH);

    setForeground(UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    setFont(SetupInstallerPlugin.getFont(getFont(), URI.createURI("font:///14/bold")));
    setCornerWidth(10);
    setCurrentState(State.INSTALL);
    setAlignment(SWT.CENTER);
  }

  public float getProgress()
  {
    return progress;
  }

  public void setProgress(float progress)
  {
    if (progress < 0 || progress > 1)
    {
      throw new IllegalArgumentException("Progress must be between 0 <= progress >= 1");
    }
    this.progress = progress;
    redraw();
  }

  public void setCurrentState(State newState)
  {
    if (newState == null)
    {
      throw new IllegalArgumentException("New state cannot be null!");
    }

    if (currentState != newState)
    {
      State oldState = currentState;
      currentState = newState;
      stateChanged(oldState, currentState);
    }
  }

  private void stateChanged(State oldState, State newState)
  {
    setImage(newState.icon);
    setText(newState.label);
    setBackground(newState.backgroundColor);
    setForeground(newState.foregroundColor);

    switch (newState)
    {
      case INSTALLING:
        setListenersPaused(true);
        setCursor(null);
        break;
      default:
        setCursor(UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
        setListenersPaused(false);
        break;
    }
  }

  public State getCurrentState()
  {
    return currentState;
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    if (currentState == State.INSTALLING)
    {
      gc.setBackground(SetupInstallerPlugin.COLOR_LIGHTEST_GRAY);
      gc.fillRoundRectangle(x, y, width, height, getCornerWidth(), getCornerWidth());
      int progressWidth = (int)(width * progress);
      gc.setBackground(currentState.backgroundColor);
      gc.fillRoundRectangle(x, y, progressWidth, height, getCornerWidth(), getCornerWidth());

      // Check if we should draw a hard edge
      if (progressWidth <= width - getCornerWidth() / 2)
      {
        gc.fillRectangle(progressWidth - getCornerWidth(), y, getCornerWidth(), height);
      }
    }
    else
    {
      super.drawBackground(gc, x, y, width, height, offsetX, offsetY);
    }
  }

}
