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
package org.eclipse.oomph.setup.mylyn.tests;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynQueriesTask;
import org.eclipse.oomph.setup.mylyn.Query;

/**
 * <!-- begin-user-doc --> A test case for the model object '
 * <em><b>Queries Task</b></em>'. <!-- end-user-doc -->
 * 
 * @generated
 */
@SuppressWarnings("restriction")
public class MylynQueriesTaskTest extends TestCase {

	/**
	 * The fixture for this Queries Task test case. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MylynQueriesTask fixture = null;

	protected SetupTaskContext context;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(MylynQueriesTaskTest.class);
	}

	/**
	 * Constructs a new Queries Task test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MylynQueriesTaskTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Queries Task test case. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(MylynQueriesTask fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Queries Task test case. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MylynQueriesTask getFixture() {
		return fixture;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see junit.framework.TestCase#setUp()
	 * @generated
	 */
	@Override
	protected void setUp() throws Exception {
		context = SetupTaskPerformer.createForIDE(EMFUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);
		setFixture(MylynFactory.eINSTANCE.createMylynQueriesTask());
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 * @generated
	 */
	@Override
	protected void tearDown() throws Exception {
		setFixture(null);
	}

	public final void testPerform() throws Exception {
		MylynQueriesTask task = getFixture();
		task.setConnectorKind("bugzilla");
		task.setRepositoryURL("https://bugs.eclipse.org/bugs");
		Query query = MylynFactory.eINSTANCE.createQuery();
		query.setSummary("Bugs");
		query.setURL("https://bugs.eclipse.org/bugs/buglist.cgi?classification=Modeling&product=EMF&component=cdo.releng&bug_status=UNCONFIRMED&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED");
		task.getQueries().add(query);

		try {
			assertTrue(task.isNeeded(context));
			task.perform(context);
			assertFalse(task.isNeeded(context));
		} finally {
			task.dispose();
		}
	}

} // MylynQueriesTaskTest
