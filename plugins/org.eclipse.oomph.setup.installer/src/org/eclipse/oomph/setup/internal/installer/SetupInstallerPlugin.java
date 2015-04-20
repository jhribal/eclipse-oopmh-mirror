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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.OomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author Eike Stepper
 */
public final class SetupInstallerPlugin extends OomphUIPlugin
{
  public static final SetupInstallerPlugin INSTANCE = new SetupInstallerPlugin();

  private static Implementation plugin;

  public static final Color COLOR_LIGHTEST_GRAY = SetupInstallerPlugin.getColor(245, 245, 245);

  public static final Color COLOR_LABEL_FOREGROUND = SetupInstallerPlugin.getColor(85, 85, 85);

  public static final String FONT_OPEN_SANS = "font-open-sans";

  public static final String FONT_LABEL_DEFAULT = FONT_OPEN_SANS + ".label-default";

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
      // FIXME: What's the best way to load a font from a bundle?
      // We have to remember, that the font might still be part
      // of the zipped bundle (rather than already being extracted).
      // URL fileURL = FileLocator.find(bundle, new Path("/fonts/OpenSans-Regular.ttf"), null);
      // BundleContext bundleContext = bundle.getBundleContext();
      // ServiceReference<URLConverter> converterRef = bundleContext.getServiceReference(org.eclipse.osgi.service.urlconversion.URLConverter.class);
      // URLConverter converter = bundleContext.getService(converterRef);
      // try
      // {
      // URL fontFileURL = converter.toFileURL(fontURL);
      // UIUtil.getDisplay().loadFont(fontFileURL.toString());
      // }
      // catch (IOException ex1)
      // {
      // ex1.printStackTrace();
      // }
      Bundle bundle = SetupInstallerPlugin.INSTANCE.getBundle();
      URL fontURL = bundle.getEntry(path);
      Path fontPath = new Path(path);
      File tmpFontFile = new File(PropertiesUtil.TEMP_DIR, fontPath.lastSegment());

      if (!tmpFontFile.exists())
      {
        InputStream in = null;
        OutputStream out = null;
        try
        {

          in = fontURL.openStream();
          out = new FileOutputStream(tmpFontFile);

          IOUtil.copy(in, out);

          in.close();

          out.flush();
          out.close();

          return UIUtil.getDisplay().loadFont(tmpFontFile.toString());
        }
        catch (IOException ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }
        finally
        {
          IOUtil.closeSilent(in);
          IOUtil.closeSilent(out);
        }
        return false;
      }

      return true;
    }
  }
}
