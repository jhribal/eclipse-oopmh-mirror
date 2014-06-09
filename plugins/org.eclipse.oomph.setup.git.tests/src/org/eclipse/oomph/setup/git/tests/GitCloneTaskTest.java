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
package org.eclipse.oomph.setup.git.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.jgit.util.SystemReader;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl;
import org.mockito.Mockito;

/**
 * <!-- begin-user-doc --> A test case for the model object '
 * <em><b>Clone Task</b></em>'. <!-- end-user-doc -->
 * 
 * @generated
 */
@SuppressWarnings("restriction")
public class GitCloneTaskTest extends TestCase {

	private static final boolean useMMAP = "true".equals(System.getProperty("jgit.junit.usemmap"));
	private SetupTaskContext context;
	private File tmp;
	private final List<Repository> toClose = new ArrayList<Repository>();
	/**
	 * The fixture for this Clone Task test case. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected GitCloneTask fixture = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static void main(String[] args) {
		TestRunner.run(GitCloneTaskTest.class);
	}

	/**
	 * Constructs a new Clone Task test case with the given name. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GitCloneTaskTest(String name) {
		super(name);
	}

	/**
	 * Sets the fixture for this Clone Task test case. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void setFixture(GitCloneTask fixture) {
		this.fixture = fixture;
	}

	/**
	 * Returns the fixture for this Clone Task test case. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected GitCloneTask getFixture() {
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
		context = SetupTaskPerformer.createForIDE(EMFUtil.createResourceSet(), SetupPrompter.CANCEL, Trigger.STARTUP);
		setFixture(GitFactory.eINSTANCE.createGitCloneTask());
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
		RepositoryCache.clear();
		for (Repository repo : toClose) {
			repo.close();
		}
		toClose.clear();
		if (useMMAP)
			System.gc();
		if (tmp != null)
			FileUtils.delete(tmp, FileUtils.RECURSIVE | FileUtils.RETRY | FileUtils.SKIP_MISSING);
		SystemReader.setInstance(null);
	}

	public final void testCloneLocalRepository() throws Exception {
		// Preparation
		FileRepository repository = createWorkRepository();
		Git git = new Git(repository);
		// commit something
		writeTrashFile(repository, "Test.txt", "Hello world");
		git.add().addFilepattern("Test.txt").call();
		git.commit().setMessage("Initial commit").call();
		// create a test branch and switch to it
		git.checkout().setCreateBranch(true).setName("branch").call();

		// Initialization
		GitCloneTask task = getFixture();
		File directory = createTempDirectory("testCloneLocalRepository");
		task.setLocation(directory.getCanonicalPath());
		task.setCheckoutBranch("branch");
		task.setRemoteURI("file://" + git.getRepository().getWorkTree().getAbsolutePath());

		// Execution
		try {
			assertTrue(task.isNeeded(context));
			task.perform(context);
			assertFalse(task.isNeeded(context));
		} finally {
			task.dispose();
		}

		// Verification
		File gitDir = new File(directory.getCanonicalPath() + "/.git");
		Repository repo = new FileRepository(gitDir);
		Git git2 = new Git(repo);
		assertNotNull(git2);
		toClose.add(git2.getRepository());
		assertEquals(git2.getRepository().getFullBranch(), "refs/heads/branch");
		assertEquals("origin", git2.getRepository().getConfig().getString(ConfigConstants.CONFIG_BRANCH_SECTION, "branch", ConfigConstants.CONFIG_KEY_REMOTE));
		assertEquals("refs/heads/branch", git2.getRepository().getConfig().getString(ConfigConstants.CONFIG_BRANCH_SECTION, "branch", ConfigConstants.CONFIG_KEY_MERGE));
		assertEquals(new RefSpec("+refs/heads/*:refs/remotes/origin/*"), fetchRefSpec(git2.getRepository()));
	}

	public final void testCloneRemoteRepository() throws Exception {
		GitCloneTaskImpl task = (GitCloneTaskImpl) getFixture();

		// Mocking
		CloneCommand cloneCommand = Mockito.mock(CloneCommand.class);
		CheckoutCommand checkoutCommand = Mockito.mock(CheckoutCommand.class);
		CreateBranchCommand createBranchCommand = Mockito.mock(CreateBranchCommand.class);
		ResetCommand resetCommand = Mockito.mock(ResetCommand.class);
		StatusCommand statusCommand = Mockito.mock(StatusCommand.class);
		Repository repository = Mockito.mock(Repository.class);
		StoredConfig config = Mockito.mock(StoredConfig.class);
		Git git = Mockito.mock(Git.class);

		// Stubbing
		Mockito.when(cloneCommand.call()).thenReturn(git);
		Mockito.when(git.branchCreate()).thenReturn(createBranchCommand);
		Mockito.when(git.checkout()).thenReturn(checkoutCommand);
		Mockito.when(git.reset()).thenReturn(resetCommand);
		Mockito.when(git.status()).thenReturn(statusCommand);
		Mockito.when(git.getRepository()).thenReturn(repository);
		Mockito.when(repository.getConfig()).thenReturn(config);

		// Initialization
		task.setCloneCommand(cloneCommand);
		File directory = createTempDirectory("testCloneRemoteRepository");
		task.setLocation(directory.getCanonicalPath());
		task.setCheckoutBranch("branch");
		task.setRemoteURI("git://git.eclipse.org/gitroot/oomph/org.eclipse.oomph");
		task.setPushURI("ssh://jenoch@git.eclipse.org:29418/cdo/org.eclipse.oomph.git");

		// Execution
		try {
			assertTrue(task.isNeeded(context));
			task.perform(context);
		} finally {
			task.dispose();
		}

		// Verification
		Mockito.verify(cloneCommand).setURI("git://git.eclipse.org/gitroot/oomph/org.eclipse.oomph");
		Mockito.verify(cloneCommand).setBranchesToClone(Collections.singleton("branch"));
		Mockito.verify(cloneCommand).setDirectory(directory);
		Mockito.verify(cloneCommand).call();
		Mockito.verify(checkoutCommand).setName("branch");
		Mockito.verify(createBranchCommand).setName("branch");
		Mockito.verify(createBranchCommand).setStartPoint("refs/remotes/origin/branch");
	}

	private File createTempFile() throws IOException {
		File p = File.createTempFile("tmp_", "", tmp);
		if (!p.delete()) {
			throw new IOException("Cannot obtain unique path " + tmp);
		}
		return p;
	}

	private File createTempDirectory(String name) throws IOException {
		File directory = new File(createTempFile(), name);
		FileUtils.mkdirs(directory);
		return directory.getCanonicalFile();
	}

	private static File writeTrashFile(final Repository repo, final String name, final String data) throws IOException {
		File path = new File(repo.getWorkTree(), name);
		FileUtils.mkdirs(path.getParentFile(), true);
		Writer w = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
		try {
			w.write(data);
		} finally {
			w.close();
		}
		return path;
	}

	private FileRepository createWorkRepository() throws IOException {
		String gitdirName = createTempFile().getPath();
		File gitdir = new File(gitdirName + "/" + Constants.DOT_GIT);
		FileRepository repo = new FileRepository(gitdir);
		assertFalse(gitdir.exists());
		repo.create();
		toClose.add(repo);
		return repo;
	}

	private static RefSpec fetchRefSpec(Repository r) throws URISyntaxException {
		RemoteConfig remoteConfig = new RemoteConfig(r.getConfig(), Constants.DEFAULT_REMOTE_NAME);
		return remoteConfig.getFetchRefSpecs().get(0);
	}
} // GitCloneTaskTest
