/*
 * Copyright (c) 2015 Yatta Solutions GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.OfflineMode;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Messages;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.osgi.util.NLS;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;

@SuppressWarnings("restriction")
public class CachingTransportTest
{
  /**
   * @author Eike Stepper
   */
  private final class CachingTransportForTests extends CachingTransport
  {
    private CachingTransportForTests(Transport delegate)
    {
      super(delegate);
    }

    @Override
    protected File cacheFolder()
    {
      return cacheFolder;
    }

    @Override
    protected boolean isLoadingRepository(URI uri)
    {
      return true;
    }

    @Override
    protected long now()
    {
      return now;
    }
  }

  private final class RepositoryTransportMock extends Transport
  {
    long lastModified;

    int downloadsPerformed = 0;

    int headRequestsPerformed = 0;

    boolean found = true;

    boolean error = false;

    @Override
    public IStatus download(URI toDownload, OutputStream target, long startPos, IProgressMonitor monitor)
    {
      String id = P2CorePlugin.INSTANCE.getSymbolicName();
      if (error)
      {
        headRequestsPerformed++;
        return new DownloadStatus(IStatus.ERROR, id, ProvisionException.REPOSITORY_FAILED_READ, NLS.bind(Messages.io_failedRead, toDownload),
            new UnknownHostException());
      }
      if (!found)
      {
        headRequestsPerformed++;
        return new DownloadStatus(IStatus.ERROR, id, ProvisionException.ARTIFACT_NOT_FOUND, NLS.bind(Messages.artifact_not_found, toDownload),
            new FileNotFoundException());
      }
      downloadsPerformed++;
      try
      {
        target.write(5);
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      DownloadStatus okStatus = new DownloadStatus(IStatus.OK, id, "OK");
      okStatus.setLastModified(lastModified);
      return okStatus;
    }

    @Override
    public IStatus download(URI toDownload, OutputStream target, IProgressMonitor monitor)
    {
      return download(toDownload, target, 0, monitor);
    }

    @Override
    public InputStream stream(URI toDownload, IProgressMonitor monitor) throws FileNotFoundException, CoreException, AuthenticationFailedException
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified(URI toDownload, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
    {
      headRequestsPerformed++;
      if (error)
      {
        return 0;
      }
      return lastModified;
    }
  }

  private RepositoryTransportMock mock = new RepositoryTransportMock();

  long now;

  File cacheFolder;

  private CachingTransport cachingTransport;

  private OutputStream devNull = new OutputStream()
  {

    @Override
    public void write(int b) throws IOException
    {
    }
  };

  @Before
  public void clear() throws Exception
  {
    OfflineMode.setEnabled(true);
    cacheFolder = File.createTempFile("oomph-cache", "");
    cacheFolder.delete(); // we don't want a file but a folder
    cachingTransport = new CachingTransportForTests(mock);

    mock.downloadsPerformed = 0;
    mock.headRequestsPerformed = 0;
    mock.found = true;
    mock.error = false;
    uri1 = new URI("http://example.com/something");
    uri2 = new URI("http://example.com/somethingElse");
    now = 2;
    mock.lastModified = 1;
  }

  @After
  public void tidyUp() throws Exception
  {
    for (File file : cacheFolder.listFiles())
    {
      file.delete();
    }
    cacheFolder.delete();
  }

  URI uri1, uri2;

  @Test
  public void testFilesAreDownloadedOnlyOnce() throws Exception
  {
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri2, devNull, null);
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);

    now += HOURS * 2;

    cachingTransport.download(uri2, devNull, null);
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
  }

  private static final int HOURS = 1000 * 60 * 60;

  @Test
  public void testFilesAreOnlyChecked() throws Exception
  {
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);

    now += HOURS * 25;

    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
  }

  @Test
  public void testFilesAreCheckedAndDownloadedIfModified() throws Exception
  {
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);

    mock.lastModified += HOURS * 25;
    now += HOURS * 25;

    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
    // but only once
    cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
  }

  @Test
  public void testNotFoundIsCached() throws Exception
  {
    mock.found = false;
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    IStatus status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.ERROR, status.getSeverity());
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);

    status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.ERROR, status.getSeverity());
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);
  }

  @Test
  public void testNetworkProblemIsNotCached() throws Exception
  {
    mock.error = true;
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    IStatus status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.ERROR, status.getSeverity());
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);

    status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.ERROR, status.getSeverity());
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 2, mock.headRequestsPerformed);
  }

  @Test
  public void testCacheUsedOnNetworkProblem() throws Exception
  {
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    IStatus status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.OK, status.getSeverity());
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);

    now += HOURS * 25;
    mock.lastModified += 1;
    mock.error = true;
    status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.OK, status.getSeverity());
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);

    mock.error = false;
    // when error is gone file should be refreshed

    status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.OK, status.getSeverity());
    Assert.assertEquals("downloads", 2, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 2, mock.headRequestsPerformed);
  }

  @Test
  public void testCacheNotSpoiledAfterHeadRequest() throws Exception
  {
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 0, mock.headRequestsPerformed);
    long lastModified = cachingTransport.getLastModified(uri1, null);
    Assert.assertTrue("lastModifed > 0", lastModified > 0);
    Assert.assertEquals("downloads", 0, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);

    IStatus status = cachingTransport.download(uri1, devNull, null);
    Assert.assertEquals("status", IStatus.OK, status.getSeverity());
    Assert.assertEquals("downloads", 1, mock.downloadsPerformed);
    Assert.assertEquals("head requests", 1, mock.headRequestsPerformed);
  }

}
