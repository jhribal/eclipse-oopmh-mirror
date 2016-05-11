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
package org.eclipse.oomph.setup.log;

import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface ProgressLogRunnable
{
  /**
   * @return a non-empty set of reasons if a restart is needed, an empty set or <code>null</code> otherwise.
   */
  Set<String> run(ProgressLog log) throws Exception;
}
