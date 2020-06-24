/*
 * Copyright (c) 2014, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core.variables;

import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptorManager;
import org.eclipse.oomph.util.internal.pde.TargetPlatformClasspathFile;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class TargletContainerClasspath implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String containerID) throws CoreException
  {
    return getClasspath(containerID);
  }

  public static String getClasspath(String containerID) throws CoreException
  {
    StringBuilder builder = new StringBuilder();

    ITargletContainerDescriptor descriptor = TargletContainerDescriptorManager.getInstance().getDescriptor(containerID, new NullProgressMonitor());
    if (descriptor != null)
    {
      Profile profile = descriptor.getWorkingProfile();
      if (profile != null)
      {
        IFileArtifactRepository artifactRepository = profile.getBundlePool().getFileArtifactRepository();

        for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), new NullProgressMonitor())))
        {
          for (IArtifactKey artifactKey : iu.getArtifacts())
          {
            if ("osgi.bundle".equals(artifactKey.getClassifier()) && !artifactKey.getId().endsWith(".source")) //$NON-NLS-1$ //$NON-NLS-2$
            {
              File file = artifactRepository.getArtifactFile(artifactKey);
              TargetPlatformClasspathFile.appendBundleClasspath(builder, file);
            }
          }
        }
      }
    }

    return builder.toString();
  }
}
