/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ericsson AB (Julian Enoch) - initial API and implementation
 */
package org.eclipse.oomph.setup.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynQueriesTask;
import org.eclipse.oomph.setup.mylyn.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MylynQueriesTaskTest
{

  protected static SetupTaskContext context;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    context = SetupTaskPerformer.createForIDE(EMFUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testPerform() throws Exception
  {
    MylynQueriesTask task = MylynFactory.eINSTANCE.createMylynQueriesTask();
    task.setConnectorKind("bugzilla");
    task.setRepositoryURL("https://bugs.eclipse.org/bugs");
    Query query = MylynFactory.eINSTANCE.createQuery();
    query.setSummary("Bugs");
    query
        .setURL("https://bugs.eclipse.org/bugs/buglist.cgi?classification=Modeling&product=EMF&component=cdo.releng&bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED");
    task.getQueries().add(query);

    try
    {
      assertThat(task.isNeeded(context), is(true));
      task.perform(context);
      assertThat(task.isNeeded(context), is(false));
    }
    finally
    {
      task.dispose();
    }
  }

}
