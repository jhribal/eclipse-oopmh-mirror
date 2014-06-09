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
package org.eclipse.oomph.setup.projects.tests;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.projects.ProjectsFactory;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;

/**
 * <!-- begin-user-doc --> A test case for the model object '
 * <em><b>Import Task</b></em>'. <!-- end-user-doc -->
 * 
 * @generated
 */
@SuppressWarnings("restriction")
public class ProjectsImportTaskTest extends TestCase {

	private SetupTaskContext context;
	private File tmp;
	private File folder;
	/**
	 * The fixture for this Import Task test case. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected ProjectsImportTask fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(ProjectsImportTaskTest.class);
	}

	/**
	 * Constructs a new Import Task test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @throws IOException
	 * 
	 * @generated
	 */
	public ProjectsImportTaskTest(String name) throws IOException {
		super(name);
	}

	/**
	 * Sets the fixture for this Import Task test case. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(ProjectsImportTask fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Import Task test case. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected ProjectsImportTask getFixture() {
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
		tmp = File.createTempFile("oomph_test_", "_tmp");
		if (!tmp.delete() || !tmp.mkdir())
			throw new IOException("Cannot create " + tmp);
		folder = createTempFile();
		context = SetupTaskPerformer.createForIDE(EMFUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);
		setFixture(ProjectsFactory.eINSTANCE.createProjectsImportTask());
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
		cleanupWorkspace();
		if (tmp != null)
			FileUtils.delete(tmp, FileUtils.RECURSIVE | FileUtils.RETRY | FileUtils.SKIP_MISSING);
	}

	public final void testPerformSelectedNestedProjects() throws Exception {

		File projectsRoot = createDirectory(folder, "testPerformSelectedNestedProjects");
		createProject("select.project", projectsRoot);
		createProject("unselect.project", projectsRoot);
		createProject("select.nestedProject", new File(projectsRoot.getAbsolutePath(), "select.project"));

		ProjectsImportTask task = getFixture();

		Predicate predicate = PredicatesFactory.eINSTANCE.createNamePredicate("select.*");

		SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
		source.setRootFolder(projectsRoot.getAbsolutePath());
		source.setLocateNestedProjects(true);
		source.getPredicates().add(predicate);

		task.getSourceLocators().add(source);

		try {
			assertTrue(task.isNeeded(context));
			task.perform(context);
		} finally {
			task.dispose();
		}

		assertTrue(ResourcesPlugin.getWorkspace().getRoot().getProject("select.project").exists());
		assertTrue(ResourcesPlugin.getWorkspace().getRoot().getProject("select.nestedProject").exists());
		assertFalse(ResourcesPlugin.getWorkspace().getRoot().getProject("unselect.project").exists());
	}

	public final void testPerformAllNotNestedProjects() throws Exception {
		File projectsRoot = createDirectory(folder, "testPerformAllNotNestedProjects");
		createProject("select.project", projectsRoot);
		createProject("unselect.project", projectsRoot);
		createProject("select.nestedProject", new File(projectsRoot.getAbsolutePath(), "select.project"));

		ProjectsImportTask task = getFixture();

		SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
		source.setRootFolder(projectsRoot.getAbsolutePath());
		source.setLocateNestedProjects(false);

		task.getSourceLocators().add(source);

		try {
			assertTrue(task.isNeeded(context));
			task.perform(context);
		} finally {
			task.dispose();
		}

		assertTrue(ResourcesPlugin.getWorkspace().getRoot().getProject("select.project").exists());
		assertFalse(ResourcesPlugin.getWorkspace().getRoot().getProject("select.nestedProject").exists());
		assertTrue(ResourcesPlugin.getWorkspace().getRoot().getProject("unselect.project").exists());
	}

	public final void testPerformNoProjects() throws Exception {
		File emptyRoot = createDirectory(folder, "emptyRoot");

		ProjectsImportTask task = getFixture();

		SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
		source.setRootFolder(emptyRoot.getAbsolutePath());
		task.getSourceLocators().add(source);

		// TODO implement isNeeded so it won't always return true
		// assertFalse(task.isNeeded(context));
	}

	private File createTempFile() throws IOException {
		File p = File.createTempFile("tmp_", "", tmp);
		if (!p.delete()) {
			throw new IOException("Cannot obtain unique path " + tmp);
		}
		return p;
	}

	private void createProject(String name, File rootFolder) throws CoreException {
		IProject target = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(target.getName());
		File location = new File(rootFolder.getAbsolutePath(), name);
		description.setLocationURI(location.toURI());
		target.create(description, new NullProgressMonitor());
		target.delete(IResource.NONE, new NullProgressMonitor());
	}

	private File createDirectory(File location, String name) throws IOException {
		File directory = new File(location, name);
		FileUtils.mkdirs(directory);
		return directory.getCanonicalFile();
	}

	private void cleanupWorkspace() throws CoreException {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		IProject target;
		for (IProject project : projects) {
			target = ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());
			target.delete(IResource.NONE, new NullProgressMonitor());
		}
	}
} // ProjectsImportTaskTest
