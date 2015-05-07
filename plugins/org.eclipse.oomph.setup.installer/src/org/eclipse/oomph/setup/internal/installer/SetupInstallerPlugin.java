/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

import org.osgi.framework.BundleContext;

import java.io.File;

/**
 * @author Eike Stepper
 */
public final class SetupInstallerPlugin extends OomphUIPlugin
{
  public static final SetupInstallerPlugin INSTANCE = new SetupInstallerPlugin();

  public static final Color COLOR_WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

  public static final Color COLOR_LIGHTEST_GRAY = getColor(245, 245, 245);

  public static final Color COLOR_LABEL_FOREGROUND = getColor(85, 85, 85);

  public static final String FONT_OPEN_SANS = "font-open-sans";

  public static final String FONT_LABEL_DEFAULT = FONT_OPEN_SANS + ".label-default";

  private static Implementation plugin;

  public SetupInstallerPlugin()
  {
    super(new ResourceLocator[] { SetupUIPlugin.INSTANCE });
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      initializeFonts();
    }

    private void initializeFonts()
    {
      loadFont("/fonts/OpenSans-Regular.ttf");
      JFaceResources.getFontRegistry().put(SetupInstallerPlugin.FONT_LABEL_DEFAULT, new FontData[] { new FontData("Open Sans", 9, SWT.BOLD) });
    }

    private boolean loadFont(String path)
    {
      File exportedFont = new File(PropertiesUtil.TEMP_DIR, new Path(path).lastSegment());
      SetupInstallerPlugin.INSTANCE.exportResources(path, exportedFont);
      return UIUtil.getDisplay().loadFont(exportedFont.toString());
    }
  }
}
