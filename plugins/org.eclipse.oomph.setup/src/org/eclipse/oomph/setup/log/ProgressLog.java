/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.log;

import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.core.runtime.IStatus;

/**
 * @author Eike Stepper
 */
public interface ProgressLog
{
  boolean isCanceled();

  void log(String line);

  void log(String line, Severity severity);

  void log(String line, boolean filter);

  void log(String line, boolean filter, Severity severity);

  void log(IStatus status);

  void log(Throwable t);

  void task(SetupTask setupTask);

  void setTerminating();

  /**
   * @author Eike Stepper
   */
  public enum Severity
  {
    OK, INFO, WARNING, ERROR;

    public static Severity fromStatus(IStatus status)
    {
      if (status != null)
      {
        switch (status.getSeverity())
        {
          case IStatus.INFO:
            return INFO;

          case IStatus.WARNING:
            return WARNING;

          case IStatus.ERROR:
            return ERROR;
            
          default:
            break;
        }
      }

      return OK;
    }
  }
}
