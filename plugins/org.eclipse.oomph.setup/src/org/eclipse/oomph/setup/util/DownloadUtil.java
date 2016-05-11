/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ContentHandler;
import org.eclipse.emf.ecore.resource.URIConverter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class DownloadUtil
{

  private static final int RETRY_DELAY = 2000;

  private static final int HTTP_ERROR_BASE_NUMBER = 400;

  private static final int DOWNLOAD_TIMEOUT = 30000;
  
  private static final int CONNECT_TIMEOUT = 10000;

  private static final int READ_TIMEOUT = 30000;

  private static final int BUFFER_SIZE = 4096;

  private DownloadUtil()
  {
  }


  public static File downloadURL(String url, ProgressLog progress)
  {
    String downloadUrl;
    
    /* Remove trailing "/" */
    if (url.endsWith("/"))
    {
      downloadUrl = url.substring(0, url.length() - 1);
    }
    else
    {
      downloadUrl = url;
    }

    try
    {
      String name = encodeFilename(url);
      File tmp = File.createTempFile(name + "-", ".part");
      File file = new File(tmp.getParentFile(), name + ".zip");
      if (!file.exists())
      {
        try
        {
          downloadURL(downloadUrl, tmp, progress);
        }
        catch (Exception ex)
        {
          if (tmp.exists() && !tmp.delete())
          {
            tmp.deleteOnExit();
          }

          throw ex;
        }

        tmp.renameTo(file);
      }

      return file;
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  @SuppressWarnings("resource")
  private static void downloadURL(String url, File file, ProgressLog progress)
  {
    final byte data[] = new byte[BUFFER_SIZE];
    BufferedInputStream in = null;
    FileOutputStream out = null;

    try
    {
      SocketTimeoutException exception = null;
      String fileName = file.getName();
      float factor = 0;

      long start = System.currentTimeMillis();
      while (System.currentTimeMillis() < start + DOWNLOAD_TIMEOUT)
      {
        exception = null;

        try
        {
          URLConnection connection = new URL(url).openConnection();
          connection.setConnectTimeout(CONNECT_TIMEOUT - (int)(System.currentTimeMillis() - start));
          connection.setReadTimeout(READ_TIMEOUT);

          if (connection instanceof HttpURLConnection)
          {
            connection.connect();

            int result = ((HttpURLConnection)connection).getResponseCode();
            if (result >= HTTP_ERROR_BASE_NUMBER)
            {
              throw new IOException("HTTP error " + result);
            }
          }

          int length = connection.getContentLength();

          /* Percentage factor */
          factor = 100f / length;

          fileName = new File(connection.getURL().getFile()).getName();

          in = new BufferedInputStream(connection.getInputStream());
          break;
        }
        catch (SocketTimeoutException ex)
        {
          exception = ex;
          progress.log("Connection timed out. Retrying in 2 seconds...");

          try
          {
            Thread.sleep(RETRY_DELAY);
          }
          catch (InterruptedException ex1)
          {
            throw ex;
          }
        }
      }

      if (exception != null)
      {
        throw exception;
      }

      out = new FileOutputStream(file);

      int lastPercent = 0;
      int read = 0;
      for (;;)
      {
        long startRead = System.currentTimeMillis();
        int n;

        try
        {
          n = in.read(data, 0, BUFFER_SIZE);
          if (n == -1)
          {
            break;
          }
        }
        catch (SocketTimeoutException ex)
        {
          progress.log("Timeout during read after " + (System.currentTimeMillis() - startRead) + " millis");
          throw ex;
        }

        out.write(data, 0, n);
        read += n;

        int percent = Math.round(factor * read);
        if (percent != lastPercent)
        {
          progress.log("Downloading " + fileName + " (" + percent + "%)");
        }
      }
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
    finally
    {
      OS.close(out);
      OS.close(in);
    }
  }

  private static String encodeFilename(String url)
  {
    StringBuilder builder = new StringBuilder(url);
    for (int i = 0; i < builder.length(); i++)
    {
      char c = builder.charAt(i);
      if (!(Character.isLetter(c) || Character.isDigit(c)))
      {
        builder.setCharAt(i, '_');
      }
    }

    return builder.toString();
  }

  public static String load(URIConverter uriConverter, URI uri, String encoding) throws IOException
  {
    BufferedInputStream bufferedInputStream = null;
    try
    {
      bufferedInputStream = new BufferedInputStream(uriConverter.createInputStream(uri));
      byte[] input = new byte[bufferedInputStream.available()];
      bufferedInputStream.read(input);

      if (encoding == null)
      {
        Map<String, ?> contentDescription = uriConverter.contentDescription(uri,
            Collections.singletonMap(ContentHandler.OPTION_REQUESTED_PROPERTIES, Collections.singleton(ContentHandler.CONTENT_TYPE_PROPERTY)));
        encoding = (String)contentDescription.get(ContentHandler.CHARSET_PROPERTY);
      }

      return encoding == null ? new String(input) : new String(input, encoding);
    }
    finally
    {
      IOUtil.close(bufferedInputStream);
    }
  }
}
