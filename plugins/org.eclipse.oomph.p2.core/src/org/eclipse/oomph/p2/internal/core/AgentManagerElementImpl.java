/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.util.StringUtil;

import java.text.MessageFormat;

/**
 * @author Eike Stepper
 */
public abstract class AgentManagerElementImpl implements AgentManagerElement
{
  public AgentManagerElementImpl()
  {
  }

  public abstract String getElementType();

  public final void delete()
  {
    delete(false);
  }

  public final void delete(boolean force)
  {
    if (!force && isUsed())
    {
      throw new P2Exception(MessageFormat.format(Messages.AgentManagerElementImpl_Used_exception, StringUtil.cap(getElementType()), this));
    }

    doDelete();
  }

  protected abstract void doDelete();
}
