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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Messages;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.provisional.p2.repository.IStateful;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingTransport extends Transport
{
  private static final ThreadLocal<Stack<String>> REPOSITORY_LOCATIONS = new InheritableThreadLocal<Stack<String>>()
  {
    @Override
    protected Stack<String> initialValue()
    {
      return new Stack<String>();
    }
  };

  private static final long REFRESH_INTERVAL = 1000 * 60 * 60 * 24; // 24 hours

  private static boolean DEBUG = true;

  private final Transport delegate;

  private final File cacheFolder;

  public CachingTransport(Transport delegate)
  {
    if (delegate instanceof CachingTransport)
    {
      throw new IllegalArgumentException("CachingTransport should not be chained.");
    }

    this.delegate = delegate;

    cacheFolder = cacheFolder();
    cacheFolder.mkdirs();
  }

  /**
   * Attention when overriding this method - is is called from the constructor.
   */
  protected File cacheFolder()
  {
    return new File(P2CorePlugin.getUserStateFolder(new File(PropertiesUtil.USER_HOME)), "cache");
  }

  public File getCacheFile(URI uri)
  {
    return new File(cacheFolder, IOUtil.encodeFileName(uri.toString()));
  }

  private static final Map<String, Thread> downloadingThreads = new HashMap<String, Thread>();

  @Override
  public IStatus download(URI uri, OutputStream target, long startPos, IProgressMonitor monitor)
  {
    if (DEBUG)
    {
      log("  ! " + uri);
    }

    File cacheFile = getCacheFile(uri);
    String key = cacheFile.getName();
    Thread currentThread = Thread.currentThread();
    synchronized (downloadingThreads)
    {
      Thread thread = null;
      do
      {
        thread = downloadingThreads.get(key);
        if (thread == null)
        {
          downloadingThreads.put(key, currentThread);
        }
        else
        {
          try
          {
            downloadingThreads.wait();
          }
          catch (InterruptedException ex)
          {
            currentThread.interrupt();
            return Status.CANCEL_STATUS;
          }
        }
      } while (thread != null);
    }
    try
    {

      boolean loadingRepository = isLoadingRepository(uri);
      if (loadingRepository)
      {
        Boolean fresh = isFresh(cacheFile);
        if (fresh != null && !fresh)
        {
          try
          {
            if (DEBUG)
            {
              log("Check if cached version is outdated by " + uri);
            }
            if (getLastModified(uri, monitor) == 0)
            {
              fresh = true;
            }
            else
            {
              fresh = isFresh(cacheFile);
            }
          }
          catch (FileNotFoundException e)
          {
            fresh = true;
          }
          catch (Exception e)
          {
            // failed to refresh - already is set to false, just for readability and debugging:
            fresh = null;
          }
        }
        if (Boolean.TRUE.equals(fresh))
        {
          try
          {
            if (DEBUG)
            {
              log("Using cached version of " + uri);
            }
            if (!cacheFile.exists())
            {
              // we know the file does not exist, as it is freshly checked
              return new DownloadStatus(IStatus.ERROR, P2CorePlugin.INSTANCE.getSymbolicName(), ProvisionException.ARTIFACT_NOT_FOUND, NLS.bind(
                  Messages.artifact_not_found, uri), new FileNotFoundException("Refresh interval was not reached but file could not be found the last time."));
            }
            FileInputStream inputStream = new FileInputStream(cacheFile);
            try
            {
              IOUtil.copy(inputStream, target);
            }
            finally
            {
              inputStream.close();
            }
            return Status.OK_STATUS;
          }
          catch (Exception ex)
          {
            //$FALL-THROUGH$
          }
        }
        else if (cacheFile.exists())
        {
          if (DEBUG)
          {
            log("Cached version of " + uri + " needs refresh.");
          }
        }
      }

      if (loadingRepository)
      {

        File tmpFile = new File(cacheFile.getAbsolutePath() + ".tmp");
        StatefulFileOutputStream statefulTarget;
        try
        {
          statefulTarget = new StatefulFileOutputStream(tmpFile);

          try
          {
            long start = System.currentTimeMillis();
            IStatus status = delegate.download(uri, statefulTarget, startPos, monitor);
            IOUtil.closeSilent(statefulTarget);
            if (DEBUG)
            {
              log("Downloading from remote repository took " + (System.currentTimeMillis() - start) + " ms for " + uri + " (" + cacheFile.length() + " byte). "
                  + currentThread);
            }
            if (status.isOK())
            {
              tmpFile.renameTo(cacheFile);
              // Files can be many megabytes large, so download them directly to a file.
              FileInputStream input = new FileInputStream(cacheFile);
              try
              {
                IOUtil.copy(input, target);
              }
              finally
              {
                IOUtil.closeSilent(input);
              }

              DownloadStatus downloadStatus = (DownloadStatus)status;
              long lastModified = downloadStatus.getLastModified();
              if (lastModified > 0)
              {
                cacheFile.setLastModified(lastModified);
              }
              refreshed(cacheFile);
            }
            else
            {
              if (DEBUG)
              {
                log("Download failed removing cached version of " + uri);
              }
              if (!tmpFile.delete())
              {
                if (DEBUG)
                {
                  log("Deleting " + cacheFile.getAbsolutePath() + " failed. Trying to delete on exit.");
                }
                tmpFile.deleteOnExit();
              }
              if (status.getException() instanceof FileNotFoundException)
              {
                if (DEBUG)
                {
                  log("Mark as non-existent: " + uri);
                }
                refreshed(cacheFile);
              }
            }

            return status;
          }
          finally
          {
            IOUtil.closeSilent(statefulTarget);
            if (target instanceof IStateful)
            {
              ((IStateful)target).setStatus(statefulTarget.getStatus());
            }
          }
        }
        catch (IOException ex)
        {
          throw new IORuntimeException(ex);
        }
      }
      long start = System.currentTimeMillis();
      IStatus result = delegate.download(uri, target, startPos, monitor);
      if (DEBUG)
      {
        DownloadStatus downloadStatus = (DownloadStatus)result;
        log("Downloading from remote repository took " + (System.currentTimeMillis() - start) + "ms for " + uri + " (" + downloadStatus.getFileSize()
            + " bytes, not stored). " + currentThread);
      }
      return result;
    }
    finally
    {
      synchronized (downloadingThreads)
      {
        Thread thread = downloadingThreads.remove(key);
        if (thread != currentThread)
        {
          throw new IllegalStateException("The current thread should have been downloading " + key);
        }
        downloadingThreads.notifyAll();
      }
    }
  }

  /**
   * @return TRUE if file was checked/loaded within REFRESH_INTERVAL, FALSE if it should be refreshed, null if unknown
   */
  Boolean isFresh(File cacheFile)
  {
    // FIXME: migration problem with old file layout vs. new one?
    long refreshedDate = getRefreshedDate(cacheFile);
    if (refreshedDate > 0)
    {
      boolean fresh = OfflineMode.isEnabled() || refreshedDate > now() - REFRESH_INTERVAL;
      return fresh;
    }
    return null;
  }

  protected long now()
  {
    return System.currentTimeMillis();
  }

  private void refreshed(File cacheFile) throws IORuntimeException
  {
    try
    {
      File refreshedMarker = getRefreshedFileFor(cacheFile);
      refreshedMarker.createNewFile();
      refreshedMarker.setLastModified(now());
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  private long getRefreshedDate(File cacheFile)
  {
    File marker = getRefreshedFileFor(cacheFile);
    return marker.exists() ? marker.lastModified() : -1;
  }

  private File getRefreshedFileFor(File cacheFile)
  {
    File refreshedMarker = new File(cacheFile.getAbsolutePath() + ".refreshed");
    return refreshedMarker;
  }

  @Override
  public IStatus download(URI uri, OutputStream target, IProgressMonitor monitor)
  {
    return download(uri, target, 0, monitor);
  }

  @Override
  public InputStream stream(URI uri, IProgressMonitor monitor) throws FileNotFoundException, CoreException, AuthenticationFailedException
  {
    return delegate.stream(uri, monitor);
  }

  @Override
  public long getLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    if (DEBUG)
    {
      log("  ? " + uri);
    }

    if (isLoadingRepository(uri))
    {
      File cacheFile = getCacheFile(uri);
      if (Boolean.TRUE.equals(isFresh(cacheFile)))
      {
        if (!cacheFile.exists())
        {
          throw new FileNotFoundException("Was not found the last time and is not checked again.");
        }
        return cacheFile.lastModified();
      }

      try
      {
        return delegateGetLastModified(uri, monitor);
      }
      catch (FileNotFoundException ex)
      {
        if (DEBUG)
        {
          log("Mark as non-existent: " + uri);
        }
        refreshed(cacheFile);
        cacheFile.delete();
        throw ex;
      }
      catch (Exception ex)
      {
        // When being physically disconnected it's likely that DNS problems pop up in the form of CoreExceptions.
        // Since we are in offline mode just pretend the file is not found.
        FileNotFoundException exception = new FileNotFoundException(ex.getMessage());
        exception.initCause(ex);
        throw exception;
      }
    }

    return delegateGetLastModified(uri, monitor);
  }

  private long delegateGetLastModified(URI uri, IProgressMonitor monitor) throws CoreException, FileNotFoundException, AuthenticationFailedException
  {
    long lastModified;
    try
    {
      long start = System.currentTimeMillis();
      lastModified = delegate.getLastModified(uri, monitor);
      if (DEBUG)
      {
        log("Head request on remote repository took " + (System.currentTimeMillis() - start) + "ms. " + Thread.currentThread());
      }
    }
    catch (CoreException ex)
    {
      lastModified = 0;
    }

    if (lastModified != 0)
    {
      File cacheFile = getCacheFile(uri);
      if (!cacheFile.exists() || cacheFile.lastModified() != lastModified)
      {
        if (DEBUG)
        {
          log("Detected changed version of " + uri);
        }
        cacheFile.delete();
        getRefreshedFileFor(cacheFile).delete();
        return lastModified - 1; // this tricks p2 cache
      }

      refreshed(cacheFile);
    }
    return lastModified;
  }

  protected boolean isLoadingRepository(URI uri)
  {
    String location = org.eclipse.emf.common.util.URI.createURI(uri.toString()).trimSegments(1).toString();

    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    return !stack.isEmpty() && stack.peek().equals(location);
  }

  private static void log(String message)
  {
    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    for (int i = 1; i < stack.size(); i++)
    {
      message = "   " + message;
    }

    System.out.println(message);
  }

  static void startLoadingRepository(URI location)
  {
    String uri = location.toString();
    if (uri.endsWith("/"))
    {
      uri = uri.substring(0, uri.length() - 1);
    }

    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    stack.push(uri);

    if (DEBUG && !uri.startsWith("file:"))
    {
      log("--> " + location);
    }
  }

  static void stopLoadingRepository()
  {
    Stack<String> stack = REPOSITORY_LOCATIONS.get();
    if (DEBUG && !stack.isEmpty())
    {
      String location = stack.peek();
      if (!location.startsWith("file:"))
      {
        log("<-- " + location);
      }
    }

    stack.pop();
  }

  /**
   * @author Eike Stepper
   */
  private static final class StatefulFileOutputStream extends FileOutputStream implements IStateful
  {
    private IStatus status;

    public StatefulFileOutputStream(File file) throws FileNotFoundException
    {
      super(file);
    }

    public IStatus getStatus()
    {
      return status;
    }

    public void setStatus(IStatus status)
    {
      this.status = status;
    }
  }
}
