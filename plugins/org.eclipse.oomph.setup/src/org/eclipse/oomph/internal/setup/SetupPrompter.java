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
package org.eclipse.oomph.internal.setup;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.UserCallback;

import java.util.List;

/**
 * @author Eike Stepper
 */
public interface SetupPrompter
{
  static final SetupPrompter OK = new Default(true);

  static final SetupPrompter CANCEL = new Default(false);

  OS getOS();

  String getVMPath();

  UserCallback getUserCallback();

  String getValue(VariableTask variable);

  boolean promptVariables(List<? extends SetupTaskContext> performers);

  /**
   * @author Eike Stepper
   */
  class Default implements SetupPrompter
  {
    private final boolean ok;

    public Default(boolean ok)
    {
      this.ok = ok;
    }

    public OS getOS()
    {
      return OS.INSTANCE;
    }

    public String getVMPath()
    {
      return null;
    }

    public UserCallback getUserCallback()
    {
      return null;
    }

    public String getValue(VariableTask variable)
    {
      return null;
    }

    public boolean promptVariables(List<? extends SetupTaskContext> performers)
    {
      return ok;
    }
  }
}
