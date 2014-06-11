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
import static org.junit.Assert.fail;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.jdt.JDTFactory;
import org.eclipse.oomph.setup.maven.MavenFactory;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.pde.PDEFactory;
import org.eclipse.oomph.setup.projects.ProjectsFactory;
import org.eclipse.oomph.setup.projectset.ProjectSetFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.workbench.WorkbenchFactory;
import org.eclipse.oomph.setup.workingsets.SetupWorkingSetsFactory;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import org.junit.Test;

public class SetupTaskPerformerTest
{

  @Test
  public final void testReorderSetupTasks()
  {
    try
    {
      SetupTaskPerformer setupTaskPerformer = SetupTaskPerformer.createForIDE(EMFUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);
      EList<SetupTask> setupTasks = new BasicEList<SetupTask>();

      SetupTask apiBaselineTask = PDEFactory.eINSTANCE.createAPIBaselineTask();
      SetupTask variableTask = SetupFactory.eINSTANCE.createVariableTask();
      SetupTask eclipseIniTask = SetupFactory.eINSTANCE.createEclipseIniTask();
      // A project preference has a specific priority
      SetupTask preferenceTask = SetupFactory.eINSTANCE.createPreferenceTask();
      ((PreferenceTask)preferenceTask).setKey("/project/editor_save_participant_org.eclipse.jdt.ui.postsavelistener.cleanup");
      SetupTask instancePreferenceTask = SetupFactory.eINSTANCE.createPreferenceTask();
      ((PreferenceTask)instancePreferenceTask).setKey("/instance/org.eclipse.ui.ide/WORKSPACE_NAME");
      SetupTask fileAssociationsTask = WorkbenchFactory.eINSTANCE.createFileAssociationsTask();
      SetupTask gitCloneTask = GitFactory.eINSTANCE.createGitCloneTask();
      SetupTask jreTask = JDTFactory.eINSTANCE.createJRETask();
      SetupTask keyBindingTask = WorkbenchFactory.eINSTANCE.createKeyBindingTask();
      SetupTask linkLocationTask = SetupFactory.eINSTANCE.createLinkLocationTask();
      SetupTask mavenImportTask = MavenFactory.eINSTANCE.createMavenImportTask();
      SetupTask mylynBuildsTask = MylynFactory.eINSTANCE.createMylynBuildsTask();
      SetupTask mylynQueriesTask = MylynFactory.eINSTANCE.createMylynQueriesTask();
      SetupTask p2Task = SetupP2Factory.eINSTANCE.createP2Task();
      SetupTask projectSetImportTask = ProjectSetFactory.eINSTANCE.createProjectSetImportTask();
      SetupTask projectsImportTask = ProjectsFactory.eINSTANCE.createProjectsImportTask();
      SetupTask redirectionTask = SetupFactory.eINSTANCE.createRedirectionTask();
      SetupTask resourceCopyTask = SetupFactory.eINSTANCE.createResourceCopyTask();
      SetupTask resourceCreationTask = SetupFactory.eINSTANCE.createResourceCreationTask();
      SetupTask targetPlatformTask = PDEFactory.eINSTANCE.createTargetPlatformTask();
      SetupTask targletTask = SetupTargletsFactory.eINSTANCE.createTargletTask();
      SetupTask textModifyTask = SetupFactory.eINSTANCE.createTextModifyTask();
      SetupTask workingSetTask = SetupWorkingSetsFactory.eINSTANCE.createWorkingSetTask();

      // Requirements
      apiBaselineTask.getPredecessors().add(eclipseIniTask);
      projectsImportTask.getPredecessors().add(targetPlatformTask);
      mylynBuildsTask.getPredecessors().add(projectsImportTask);
      mylynBuildsTask.getPredecessors().add(resourceCopyTask);
      keyBindingTask.getPredecessors().add(p2Task);

      setupTasks.add(apiBaselineTask);
      setupTasks.add(variableTask);
      setupTasks.add(eclipseIniTask);
      setupTasks.add(preferenceTask);
      setupTasks.add(instancePreferenceTask);
      setupTasks.add(fileAssociationsTask);
      setupTasks.add(gitCloneTask);
      setupTasks.add(jreTask);
      setupTasks.add(keyBindingTask);
      setupTasks.add(linkLocationTask);
      setupTasks.add(mavenImportTask);
      setupTasks.add(mylynBuildsTask);
      setupTasks.add(mylynQueriesTask);
      setupTasks.add(p2Task);
      setupTasks.add(projectSetImportTask);
      setupTasks.add(projectsImportTask);
      setupTasks.add(redirectionTask);
      setupTasks.add(resourceCopyTask);
      setupTasks.add(resourceCreationTask);
      setupTasks.add(targetPlatformTask);
      setupTasks.add(targletTask);
      setupTasks.add(textModifyTask);
      setupTasks.add(workingSetTask);

      // Execution
      setupTaskPerformer.reorderSetupTasks(setupTasks);

      // Validation
      assertThat(setupTasks.indexOf(apiBaselineTask), is(5));
      assertThat(setupTasks.indexOf(variableTask), is(6));
      assertThat(setupTasks.indexOf(eclipseIniTask), is(3));
      assertThat(setupTasks.indexOf(preferenceTask), is(21));
      assertThat(setupTasks.indexOf(instancePreferenceTask), is(4));
      assertThat(setupTasks.indexOf(fileAssociationsTask), is(7));
      assertThat(setupTasks.indexOf(gitCloneTask), is(8));
      assertThat(setupTasks.indexOf(jreTask), is(9));
      assertThat(setupTasks.indexOf(keyBindingTask), is(10));
      assertThat(setupTasks.indexOf(linkLocationTask), is(1));
      assertThat(setupTasks.indexOf(mavenImportTask), is(11));
      assertThat(setupTasks.indexOf(mylynBuildsTask), is(15));
      assertThat(setupTasks.indexOf(mylynQueriesTask), is(16));
      assertThat(setupTasks.indexOf(p2Task), is(2));
      assertThat(setupTasks.indexOf(projectSetImportTask), is(17));
      assertThat(setupTasks.indexOf(projectsImportTask), is(14));
      assertThat(setupTasks.indexOf(redirectionTask), is(0));
      assertThat(setupTasks.indexOf(resourceCopyTask), is(12));
      assertThat(setupTasks.indexOf(resourceCreationTask), is(18));
      assertThat(setupTasks.indexOf(targetPlatformTask), is(13));
      assertThat(setupTasks.indexOf(targletTask), is(19));
      assertThat(setupTasks.indexOf(textModifyTask), is(20));
      assertThat(setupTasks.indexOf(workingSetTask), is(22));
    }
    catch (Exception e)
    {
      fail();
    }
  }

}
