package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class BackButton extends ImageHoverButton
{
  private static Image arrowLeft = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left.png");

  private static Image arrowLeftDisabled = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/arrow_left_disabled.png");

  public BackButton(Composite parent)
  {
    super(parent, SWT.PUSH, arrowLeft, arrowLeft);
    setDisabledImage(arrowLeftDisabled);
    setIconTextGap(16);
    setText("BACK");
    setForeground(SetupInstallerPlugin.COLOR_LABEL_FOREGROUND);
    setFont(SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///10/bold")));
  }

}
