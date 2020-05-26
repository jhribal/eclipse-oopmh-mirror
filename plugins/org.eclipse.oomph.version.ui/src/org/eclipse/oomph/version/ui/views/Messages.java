/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.ui.views.messages"; //$NON-NLS-1$

  public static String VersionsView_action1_executedMessage; // Action 1 executed
  public static String VersionsView_action1_text; // Action 1
  public static String VersionsView_action1_tooltip; // Action 1 tooltip
  public static String VersionsView_action2_executedMessage; // Action 2 executed
  public static String VersionsView_action2_text; // Action 2
  public static String VersionsView_action2_tooltip; // Action 2 tooltip
  public static String VersionsView_doubleClickAction_detectedMessage; // Double-click detected on {0}
  public static String VersionsView_messageDialog_versions; // Versions

  static
  {
    // initialize resource bundles
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
