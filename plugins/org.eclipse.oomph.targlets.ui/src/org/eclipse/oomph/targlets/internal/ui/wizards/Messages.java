/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.internal.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.internal.ui.wizards.messages"; //$NON-NLS-1$

  public static String TargetDefinitionExportWizard_title; // Export Target Definition
  public static String TargetDefinitionExportWizard_exportTargetJob_name; // Export Target Definition

  public static String TargetDefinitionExportWizardPage_defaultMessage; // Select a target definition and enter a folder to which to export.
  public static String TargetDefinitionExportWizardPage_targetPlatform; // Target platform:
  public static String TargetDefinitionExportWizardPage_exportFolder; // Export folder:
  public static String TargetDefinitionExportWizardPage_browseButton_text; // Browse...
  public static String TargetDefinitionExportWizardPage_exportFolderDialog_text; // Export Folder
  public static String TargetDefinitionExportWizardPage_exportFolderDialog_message; // Select a folder to which to export:
  public static String TargetDefinitionExportWizardPage_determineTargetsJob_name; // Determine Target Definitions
  public static String TargetDefinitionExportWizardPage_selectTargetToExport; // Select a target definition to export.
  public static String TargetDefinitionExportWizardPage_enterFolderToExportTo; // Enter a folder to which to export.
  public static String TargetDefinitionExportWizardPage_active; // (Active)

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
