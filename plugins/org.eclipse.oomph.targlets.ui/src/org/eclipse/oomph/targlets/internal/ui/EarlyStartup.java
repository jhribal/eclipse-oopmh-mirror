/*
 * Copyright (c) 2017 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eike Stepper
 */
public class EarlyStartup implements IStartup
{
  private static final boolean MANIFEST_DISCOVERY = !PropertiesUtil.isProperty("org.eclipse.oomph.targlets.ui.SKIP_MANIFEST_DISCOVERY");

  public void earlyStartup()
  {
    if (MANIFEST_DISCOVERY)
    {
      final IWorkbench workbench = PlatformUI.getWorkbench();
      workbench.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
          if (window != null)
          {
            ManifestDiscovery.INSTANCE.start();
          }
        }
      });
    }
  }
}
