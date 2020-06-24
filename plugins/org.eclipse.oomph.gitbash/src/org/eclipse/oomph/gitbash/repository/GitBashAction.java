/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.GitBash;

import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class GitBashAction extends AbstractRepositoryAction
{
  @Override
  protected void run(Shell shell, File workTree) throws Exception
  {
    String gitBash = GitBash.getExecutable(shell);
    if (gitBash != null)
    {
      Runtime.getRuntime().exec("cmd /c cd \"" + workTree.getAbsolutePath() + "\" && start cmd.exe /c \"" + gitBash + "\" --login -i"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
  }
}
