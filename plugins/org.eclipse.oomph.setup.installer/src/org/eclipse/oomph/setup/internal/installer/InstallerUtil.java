/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Andreas Scharf - Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Andreas Scharf
 */
public class InstallerUtil
{
  private static final Preference PREF_KEPT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("kept");

  private static String powerShell;

  private InstallerUtil()
  {
  }

  public static void createShortCut(String specialFolder, String target)
  {
    try
    {
      String powerShell = InstallerUtil.getPowerShell();
      if (powerShell != null)
      {
        Runtime.getRuntime().exec(
            new String[] {
                powerShell,
                "-command",
                "& {$linkPath = Join-Path ([Environment]::GetFolderPath('" + specialFolder + "')) 'Eclipse Installer.lnk'; $targetPath = '" + target
                    + "'; $link = (New-Object -ComObject WScript.Shell).CreateShortcut( $linkpath ); $link.TargetPath = $targetPath; $link.Save()}" });
      }
    }
    catch (IOException ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
  }

  public static void pinToTaskBar(String location, String launcherName)
  {
    try
    {
      String powerShell = InstallerUtil.getPowerShell();
      if (powerShell != null)
      {
        Runtime.getRuntime().exec(
            new String[] { powerShell, "-command",
                "& { (new-object -c shell.application).namespace('" + location + "').parsename('" + launcherName + "').invokeverb('taskbarpin') }" });
      }
    }
    catch (IOException ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
  }

  protected void keepInstaller(String launcher, boolean startMenu, boolean desktop, boolean quickLaunch)
  {

  }

  public static boolean canKeepInstaller()
  {
    if (!isInstallerKept() && OS.INSTANCE.isWin())
    {
      String launcher = InstallerApplication.getLauncher();
      return launcher != null && launcher.startsWith(PropertiesUtil.TEMP_DIR);
    }

    return false;
  }

  public static String getPowerShell()
  {
    if (powerShell == null)
    {
      try
      {
        String systemRoot = System.getenv("SystemRoot");
        if (systemRoot != null)
        {
          File system32 = new File(systemRoot, "system32");
          if (system32.isDirectory())
          {
            File powerShellFolder = new File(system32, "WindowsPowerShell");
            if (powerShellFolder.isDirectory())
            {
              File[] versions = powerShellFolder.listFiles();
              if (versions != null)
              {
                for (File version : versions)
                {
                  try
                  {
                    File executable = new File(version, "powershell.exe");
                    if (executable.isFile())
                    {
                      powerShell = executable.getAbsolutePath();
                      break;
                    }
                  }
                  catch (Exception ex)
                  {
                    //$FALL-THROUGH$
                  }
                }
              }
            }
          }
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }

    return powerShell;
  }

  public static void keepInstaller(String targetLocation, boolean startPermanentInstaller, String launcher, boolean startMenu, boolean desktop,
      boolean quickLaunch)
  {
    File source = new File(launcher).getParentFile();
    File target = new File(targetLocation);
    IOUtil.copyTree(source, target, true);

    String launcherName = new File(launcher).getName();
    String permanentLauncher = new File(target, launcherName).getAbsolutePath();

    if (startPermanentInstaller)
    {
      try
      {
        Runtime.getRuntime().exec(permanentLauncher);
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
    else
    {
      String url = target.toURI().toString();
      OS.INSTANCE.openSystemBrowser(url);
    }

    if (startMenu)
    {
      createShortCut("Programs", permanentLauncher);
    }

    if (desktop)
    {
      createShortCut("Desktop", permanentLauncher);
    }

    if (quickLaunch)
    {
      pinToTaskBar(targetLocation, launcherName);
    }

    setKeepInstaller(true);
  }

  public static boolean isInstallerKept()
  {
    return PREF_KEPT.get(false);
  }

  public static void setKeepInstaller(boolean keep)
  {
    PREF_KEPT.set(keep);
  }
}
