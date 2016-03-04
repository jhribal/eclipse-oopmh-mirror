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
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.projects.ProjectsFactory;
import org.eclipse.oomph.setup.projects.ProjectsImportTask;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class ProjectsImportTaskTest
{

  private static SetupTaskContext context;

  private static File tempDirectory;

  private static File projectsRoot;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    tempDirectory = File.createTempFile("oomph_testProjectsImportTask_", "_tmp");
    if (!tempDirectory.delete() || !tempDirectory.mkdir())
    {
      throw new IOException("Cannot create " + tempDirectory);
    }
    projectsRoot = createTempFile();
    createProject("select.project", projectsRoot);
    createProject("unselect.project", projectsRoot);
    createProject("select.nestedProject", new File(projectsRoot.getAbsolutePath(), "select.project"));
    context = SetupTaskPerformer.createForIDE(SetupCoreUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);

  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    IOUtil.deleteBestEffort(tempDirectory);
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
    cleanupWorkspace();
  }

  @Test
  public final void performSelectedNestedProjects() throws Exception
  {
    ProjectsImportTask task = ProjectsFactory.eINSTANCE.createProjectsImportTask();
    Predicate predicate = PredicatesFactory.eINSTANCE.createNamePredicate("select.*");
    SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
    source.setRootFolder(projectsRoot.getAbsolutePath());
    source.setLocateNestedProjects(true);
    source.getPredicates().add(predicate);
    task.getSourceLocators().add(source);

    try
    {
      assertThat(task.isNeeded(context), is(true));
      task.perform(context);
    }
    finally
    {
      task.dispose();
    }

    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("select.project").exists(), is(true));
    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("select.nestedProject").exists(), is(true));
    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("unselect.project").exists(), is(false));
  }

  @Test
  public final void performAllNotNestedProjects() throws Exception
  {
    ProjectsImportTask task = ProjectsFactory.eINSTANCE.createProjectsImportTask();
    SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
    source.setRootFolder(projectsRoot.getAbsolutePath());
    source.setLocateNestedProjects(false);
    task.getSourceLocators().add(source);

    try
    {
      assertThat(task.isNeeded(context), is(true));
      task.perform(context);
    }
    finally
    {
      task.dispose();
    }

    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("select.project").exists(), is(true));
    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("select.nestedProject").exists(), is(false));
    assertThat(ResourcesPlugin.getWorkspace().getRoot().getProject("unselect.project").exists(), is(true));
  }

  @Test
  public final void performNoProjects() throws Exception
  {
    File emptyRoot = createTempFile();

    ProjectsImportTask task = ProjectsFactory.eINSTANCE.createProjectsImportTask();
    SourceLocator source = ResourcesFactory.eINSTANCE.createSourceLocator();
    source.setRootFolder(emptyRoot.getAbsolutePath());
    task.getSourceLocators().add(source);

    // TODO implement ProjectsImportTaskImpl.isNeeded so it won't always return true
    // assertThat(task.isNeeded(context), is(false));
  }

  private static File createTempFile() throws IOException
  {
    File p = File.createTempFile("tmp_", "", tempDirectory);
    if (!p.delete())
    {
      throw new IOException("Cannot obtain unique path " + tempDirectory);
    }
    return p;
  }

  private static void createProject(String name, File rootFolder) throws CoreException
  {
    IProject target = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(target.getName());
    File location = new File(rootFolder.getAbsolutePath(), name);
    description.setLocationURI(location.toURI());
    target.create(description, new NullProgressMonitor());
    target.delete(IResource.NONE, new NullProgressMonitor());
  }

  private void cleanupWorkspace() throws CoreException
  {
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    IProject target;
    for (IProject project : projects)
    {
      target = ResourcesPlugin.getWorkspace().getRoot().getProject(project.getName());
      target.delete(IResource.NEVER_DELETE_PROJECT_CONTENT, new NullProgressMonitor());
    }
  }
}
