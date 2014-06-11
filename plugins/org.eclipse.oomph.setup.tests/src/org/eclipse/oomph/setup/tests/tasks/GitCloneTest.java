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
package org.eclipse.oomph.setup.tests.tasks;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl;
import org.eclipse.oomph.setup.util.OS;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ericsson AB (Julian Enoch)
 */
public class GitCloneTest
{
  private static final boolean USE_MMAP = PropertiesUtil.isProperty("jgit.junit.usemmap");

  private static SetupTaskContext context;

  private static File tempDirectory;

  private final List<Repository> toClose = new ArrayList<Repository>();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    tempDirectory = File.createTempFile("oomph_testGitCloneTask_", "_tmp");
    if (!tempDirectory.delete() || !tempDirectory.mkdir())
    {
      throw new IOException("Cannot create " + tempDirectory);
    }

    context = mock(SetupTaskContext.class);
    OS os = mock(OS.class);
    when(os.isLineEndingConversionNeeded()).thenReturn(false);
    when(context.redirect(anyString())).then(returnsFirstArg());
    when(context.getOS()).thenReturn(os);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    if (USE_MMAP)
    {
      System.gc();
    }

    if (tempDirectory != null)
    {
      IOUtil.deleteBestEffort(tempDirectory);
    }

    SystemReader.setInstance(null);
  }

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
    RepositoryCache.clear();
    for (Repository repo : toClose)
    {
      repo.close();
    }

    toClose.clear();
  }

  @Test
  public final void testCloneLocalRepository() throws Exception
  {
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
    GitCloneTask task = GitFactory.eINSTANCE.createGitCloneTask();
    File directory = createTempDirectory();
    task.setLocation(directory.getCanonicalPath());
    task.setCheckoutBranch("branch");
    task.setRemoteURI("file://" + git.getRepository().getWorkTree().getAbsolutePath());

    // Execution
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

    // Verification
    File gitDir = new File(directory.getCanonicalPath() + "/.git");
    Repository repo = new FileRepository(gitDir);
    Git clone = new Git(repo);
    assertThat(clone, is(notNullValue()));
    toClose.add(clone.getRepository());
    assertThat(clone.getRepository().getFullBranch(), is("refs/heads/branch"));
    assertThat(clone.getRepository().getConfig().getString(ConfigConstants.CONFIG_BRANCH_SECTION, "branch", ConfigConstants.CONFIG_KEY_REMOTE), is("origin"));
    assertThat(clone.getRepository().getConfig().getString(ConfigConstants.CONFIG_BRANCH_SECTION, "branch", ConfigConstants.CONFIG_KEY_MERGE),
        is("refs/heads/branch"));
    assertThat(getFetchRefSpec(clone.getRepository()), is(new RefSpec("+refs/heads/*:refs/remotes/origin/*")));
  }

  @Test
  public final void testCloneRemoteRepository() throws Exception
  {
    GitCloneTask task = GitFactory.eINSTANCE.createGitCloneTask();

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
    ((GitCloneTaskImpl)task).setCloneCommand(cloneCommand);
    File directory = createTempDirectory();
    task.setLocation(directory.getCanonicalPath());
    task.setCheckoutBranch("branch");
    task.setRemoteURI("git://git.eclipse.org/gitroot/oomph/org.eclipse.oomph");
    task.setPushURI("ssh://jenoch@git.eclipse.org:29418/cdo/org.eclipse.oomph.git");

    // Execution
    try
    {
      assertThat(task.isNeeded(context), is(true));
      task.perform(context);
    }
    finally
    {
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

  private File createTempFile() throws IOException
  {
    File tmp = File.createTempFile("tmp_", "", tempDirectory);
    if (!tmp.delete())
    {
      throw new IOException("Cannot obtain unique path " + tempDirectory);
    }

    return tmp;
  }

  private File createTempDirectory() throws IOException
  {
    File directory = createTempFile();
    FileUtils.mkdirs(directory);
    return directory.getCanonicalFile();
  }

  private static File writeTrashFile(final Repository repository, final String name, final String data) throws IOException
  {
    File path = new File(repository.getWorkTree(), name);
    FileUtils.mkdirs(path.getParentFile(), true);
    Writer w = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");

    try
    {
      w.write(data);
    }
    finally
    {
      w.close();
    }

    return path;
  }

  private FileRepository createWorkRepository() throws IOException
  {
    String gitdirName = createTempFile().getPath();
    File gitdir = new File(gitdirName + "/" + Constants.DOT_GIT);
    FileRepository repository = new FileRepository(gitdir);
    assertThat(gitdir.exists(), is(false));
    repository.create();
    toClose.add(repository);
    return repository;
  }

  private static RefSpec getFetchRefSpec(Repository repository) throws URISyntaxException
  {
    RemoteConfig remoteConfig = new RemoteConfig(repository.getConfig(), Constants.DEFAULT_REMOTE_NAME);
    return remoteConfig.getFetchRefSpecs().get(0);
  }
}
