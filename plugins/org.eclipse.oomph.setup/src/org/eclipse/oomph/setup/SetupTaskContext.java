/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;

import java.io.File;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface SetupTaskContext extends ProgressLog
{
  IProgressMonitor getProgressMonitor(boolean working);

  SetupPrompter getPrompter();

  Trigger getTrigger();

  void checkCancelation();

  boolean isSelfHosting();

  boolean isPerforming();

  boolean isOffline();

  boolean isMirrors();

  boolean isRestartNeeded();

  void setRestartNeeded(String reason);

  User getUser();

  Workspace getWorkspace();

  Installation getInstallation();

  File getInstallationLocation();

  File getProductLocation();

  File getProductConfigurationLocation();

  File getWorkspaceLocation();

  String getRelativeProductFolder();

  OS getOS();

  URIConverter getURIConverter();

  URI redirect(URI uri);

  String redirect(String uri);

  Object get(Object key);

  Object put(Object key, Object value);

  Set<Object> keySet();

  String getLauncherName();

  boolean matchesFilterContext(String filter);
}
