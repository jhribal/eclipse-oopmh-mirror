/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.projectcopy;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.projectcopy.messages"; //$NON-NLS-1$

  public static String ProjectCopyAction_dialog_title;
  public static String ProjectCopyAction_dialog_message;
  public static String ProjectCopyAction_dialog_nameValidator_projectAlreadyExists;
  public static String ProjectCopyAction_dialog_nameValidator_locationAlreadyExists;
  public static String ProjectCopyAction_copyProjectJob_name;

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
