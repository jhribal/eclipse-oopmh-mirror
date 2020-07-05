/*
 * Copyright (c) 2015, 2020 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 *    Christoph Laeubrich - [494735] - Eclipse Installer does not create .desktop file for the menu
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.internal.installer.DesktopSupport.ShortcutType;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.Platform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class KeepInstallerUtil
{
  public static final String KEEP_INSTALLER_DESCRIPTION = Messages.KeepInstallerUtil_Keep_message;

  private static final Preference PREF_KEPT = SetupInstallerPlugin.INSTANCE.getConfigurationPreference("kept"); //$NON-NLS-1$

  private static DesktopSupport desktopSupport;

  private KeepInstallerUtil()
  {
  }

  private static void createShortCut(ShortcutType specialFolder, File target)
  {
    createShortCut(specialFolder, null, target, PropertiesUtil.getProductName(), null, PropertiesUtil.getProductID());
  }

  public static void createShortCut(ShortcutType specialFolder, String groupName, File target, String shortcutName, String description, String id)
  {
    try
    {
      DesktopSupport desktopSupport = KeepInstallerUtil.getDesktopSupport();
      if (desktopSupport != null)
      {
        desktopSupport.createShortCut(specialFolder, groupName, target, shortcutName, description, id);
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
      DesktopSupport desktopSupport = KeepInstallerUtil.getDesktopSupport();
      if (desktopSupport != null)
      {
        desktopSupport.pinToTaskBar(location, launcherName);
      }
    }
    catch (IOException ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
    }
  }

  public static boolean canKeepInstaller()
  {
    return !isInstallerKept() && isTransientInstaller();
  }

  public static boolean isTransientInstaller()
  {
    if (OS.INSTANCE.isWin())
    {
      String launcher = OS.getCurrentLauncher(false);
      return launcher != null && launcher.startsWith(PropertiesUtil.getTmpDir());
    }

    return false;
  }

  public static DesktopSupport getDesktopSupport()
  {
    if (desktopSupport == null)
    {
      try
      {
        if (OS.INSTANCE.isWin())
        {
          String systemRoot = System.getenv("SystemRoot"); //$NON-NLS-1$
          if (systemRoot != null)
          {
            File system32 = new File(systemRoot, "system32"); //$NON-NLS-1$
            if (system32.isDirectory())
            {
              File powerShellFolder = new File(system32, "WindowsPowerShell"); //$NON-NLS-1$
              if (powerShellFolder.isDirectory())
              {
                File[] versions = powerShellFolder.listFiles();
                if (versions != null)
                {
                  for (File version : versions)
                  {
                    try
                    {
                      File executable = new File(version, "powershell.exe"); //$NON-NLS-1$
                      if (executable.isFile())
                      {
                        desktopSupport = new WindowsPowerShell(executable.getAbsolutePath());
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
        else if (OS.INSTANCE.isLinux())
        {
          desktopSupport = new FreeDestkopDektopSupport();
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }

    return desktopSupport;
  }

  public static void keepInstaller(String targetLocation, boolean startPermanentInstaller, String launcher, boolean startMenu, boolean desktop,
      boolean quickLaunch)
  {
    File source = new File(launcher).getParentFile();
    File target = new File(targetLocation);
    IOUtil.copyTree(source, target, true);

    String launcherName = new File(launcher).getName();
    File permanentLauncher = new File(target, launcherName);

    if (startPermanentInstaller)
    {
      // Include the application arguments in this launch.
      List<String> command = new ArrayList<String>();
      command.add(permanentLauncher.getAbsolutePath());
      command.addAll(Arrays.asList(Platform.getApplicationArgs()));
      try
      {
        Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
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
      createShortCut(ShortcutType.START_MENU, permanentLauncher);
    }

    if (desktop)
    {
      createShortCut(ShortcutType.DESKTOP, permanentLauncher);
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
