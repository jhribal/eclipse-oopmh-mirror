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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 *
 * @author Andreas Scharf
 */
public class SimpleInstallerMenuButton extends Composite
{
  // TODO: We have some issues with transparency of the overlay
  // if we move the overlay a bit down (e.g. by using a y-offset of -7)
  // for example.
  private static final int NOTIFICATION_X_OFFSET = 15;

  private static final int NOTIFICATION_Y_OFFSET = -9;

  private Control notificationOverlay;

  private OomphButton button;

  public SimpleInstallerMenuButton(Composite parent)
  {
    super(parent, SWT.NONE);
    setLayout(new FillLayout());

    Composite container = new Composite(this, SWT.NONE);

    notificationOverlay = new Composite(container, SWT.NONE);
    Image overlayImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/notification_overlay.png");
    Rectangle overlayImageBounds = overlayImage.getBounds();
    notificationOverlay.setBackgroundImage(overlayImage);

    @SuppressWarnings("unused")
    int overlayX = NOTIFICATION_X_OFFSET >= 0 ? NOTIFICATION_X_OFFSET : 0;
    @SuppressWarnings("unused")
    int overlayY = NOTIFICATION_Y_OFFSET >= 0 ? NOTIFICATION_Y_OFFSET : 0;

    notificationOverlay.setBounds(overlayX, overlayY, overlayImageBounds.width, overlayImageBounds.height);
    notificationOverlay.setVisible(false);

    Image buttonImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/menu.png");
    Image buttonHoverImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/menu_hover.png");
    button = new ImageHoverButton(container, SWT.PUSH, buttonImage, buttonHoverImage);

    // TODO: As soon as the transparancy issues with the overlay are solved,
    // we can re-enable the visualization of the button down state
    button.setShowButtonDownState(false);

    @SuppressWarnings("unused")
    int baseX = NOTIFICATION_X_OFFSET >= 0 ? 0 : -NOTIFICATION_X_OFFSET;
    @SuppressWarnings("unused")
    int baseY = NOTIFICATION_Y_OFFSET >= 0 ? 0 : -NOTIFICATION_Y_OFFSET;

    Rectangle baseBounds = buttonHoverImage.getBounds();
    button.setBounds(baseX, baseY, baseBounds.width, baseBounds.height);

    Rectangle unionBounds = notificationOverlay.getBounds().union(button.getBounds());
    container.setSize(unionBounds.width, unionBounds.height);
    setNotificationVisible(false);
  }

  public void setNotificationVisible(boolean visible)
  {
    notificationOverlay.setVisible(visible);
  }

  public void addSelectionListener(SelectionAdapter selectionAdapter)
  {
    button.addSelectionListener(selectionAdapter);
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    button.removeSelectionListener(listener);
  }

}
