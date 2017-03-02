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
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.internal.base.BasePlugin;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.core.runtime.IStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileLock;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class BaseUtil
{
  private static final String BOGUS_SCHEME = URI.createURI("bogus:").scheme();

  private static final Adapter REDUCED = new Adapter()
  {
    public void setTarget(Notifier newTarget)
    {
    }

    public void notifyChanged(Notification notification)
    {
    }

    public boolean isAdapterForType(Object type)
    {
      return false;
    }

    public Notifier getTarget()
    {
      return null;
    }
  };

  private BaseUtil()
  {
  }

  public static BaseResource loadResourceSafely(ResourceSet resourceSet, URI uri)
  {

    try
    {
      return (BaseResource)resourceSet.getResource(uri, true);
    }
    catch (RuntimeException ex)
    {
      // This method is always expected to return a non-null result.
      BaseResource resource = (BaseResource)resourceSet.getResource(uri, false);
      if (resource == null)
      {
        // Failure to even create a resource in the first place will cause null pointer exceptions in downstream clients.
        // Throwing this exception will make it easier to track down why a resource cannot be created.
        throw new IORuntimeException(ex);
      }

      BasePlugin.INSTANCE.log(ex, IStatus.WARNING);
      return resource;
    }
  }

  public static void saveEObject(EObject eObject)
  {
    try
    {
      XMLResource xmlResource = (XMLResource)eObject.eResource();
      xmlResource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
    }
    catch (IOException ex)
    {
      BasePlugin.INSTANCE.log(ex);
    }
  }

  public static URI getRootURI(EObject eObject)
  {
    if (eObject.eIsProxy())
    {
      return ((InternalEObject)eObject).eProxyURI();
    }

    EObject rootContainer = EcoreUtil.getRootContainer(eObject);
    URI uri = EcoreUtil.getURI(rootContainer);
    String relativeURIFragmentPath = EcoreUtil.getRelativeURIFragmentPath(rootContainer, eObject);
    if (relativeURIFragmentPath.length() != 0)
    {
      uri = uri.trimFragment().appendFragment(uri.fragment() + "/" + relativeURIFragmentPath);
    }

    return uri;
  }

  public static EStructuralFeature getFeature(EClass eClass, String xmlName)
  {
    for (EStructuralFeature eStructuralFeature : eClass.getEAllStructuralFeatures())
    {
      if (xmlName.equals(ExtendedMetaData.INSTANCE.getName(eStructuralFeature)))
      {
        return eStructuralFeature;
      }
    }

    return null;
  }

  private static InputStream openInputStream(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      return uriConverter.createInputStream(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException();
    }
  }

  private static OutputStream openOutputStream(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      return uriConverter.createOutputStream(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  public static void copyFile(URIConverter uriConverter, Map<?, ?> options, URI source, URI target) throws IORuntimeException
  {
    InputStream input = null;
    OutputStream output = null;

    try
    {
      input = openInputStream(uriConverter, options, source);
      output = openOutputStream(uriConverter, options, target);
      IOUtil.copy(input, output);
    }
    finally
    {
      IOUtil.closeSilent(output);
      IOUtil.closeSilent(input);
    }
  }

  public static byte[] readFile(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    InputStream input = openInputStream(uriConverter, options, uri);

    try
    {
      ByteArrayOutputStream output = new ByteArrayOutputStream(input.available());
      IOUtil.copy(input, output);
      return output.toByteArray();
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.closeSilent(input);
    }
  }

  public static void writeFile(URIConverter uriConverter, Map<?, ?> options, URI uri, byte[] bytes) throws IORuntimeException
  {
    OutputStream output = openOutputStream(uriConverter, options, uri);

    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(bytes);
      IOUtil.copy(input, output);
    }
    finally
    {
      IOUtil.closeSilent(output);
    }
  }

  public static void deleteFile(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      uriConverter.delete(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T getObjectByType(Collection<?> objects, EClassifier type)
  {
    return (T)EcoreUtil.getObjectByType(objects, type);
  }

  public static boolean execute(long timeout, Runnable runnable, URIConverter uriConverter, URI... uris)
  {
    // Create URIs for the locking the given URIs.
    // Each will be in the same folder
    URI[] lockLocations = new URI[uris.length];
    for (int i = 0; i < uris.length; ++i)
    {
      URI uri = uris[i];
      if (uri != null)
      {
        lockLocations[i] = uri.trimSegments(1).appendSegment(uri.lastSegment() + ".lock");
      }
    }

    FileLock[] fileLocks = new FileLock[uris.length];
    for (long start = System.currentTimeMillis(); System.currentTimeMillis() - start < timeout;)
    {
      OutputStream[] outputStreams = new OutputStream[uris.length];
      try
      {
        // Try to acquire the lock for each URI, i.e., create an output stream for each lock URI.
        for (int i = 0; i < uris.length; ++i)
        {
          URI uri = lockLocations[i];
          if (uri != null)
          {
            outputStreams[i] = uriConverter.createOutputStream(uri);
            if (outputStreams[i] instanceof FileOutputStream)
            {
              FileOutputStream fileOutputStream = (FileOutputStream)outputStreams[i];
              fileLocks[i] = fileOutputStream.getChannel().tryLock();
            }
          }
        }

        // If successful, run the runnable and return success.
        runnable.run();
        return true;
      }
      catch (IOException ex)
      {
        // If a lock could not be acquired, sleep for a short time before trying again.
        try
        {
          Thread.sleep(100);
        }
        catch (InterruptedException ex1)
        {
          throw new RuntimeException(ex1);
        }
      }
      finally
      {
        // Unlock the the lock for each URI, i.e., close each output stream, and delete the lock file.
        for (int i = 0; i < uris.length; ++i)
        {
          OutputStream outputStream = outputStreams[i];
          if (outputStream != null)
          {
            // If there is an output stream, try to close it.
            try
            {
              outputStream.close();
            }
            catch (Throwable throwable)
            {
              // We don't expect this ever to happen, but if it does, we ignore it.
              // Ignore.
            }

            // Try to delete the lock file.
            try
            {
              uriConverter.delete(lockLocations[i], null);
            }
            catch (Throwable throwable)
            {
              // It's possible for this to fail because some other process has already acquired the lock, i.e., opened an output stream.
              // In this case, the other process will delete the file when its done, or perhaps has already deleted it.
            }

            FileLock fileLock = fileLocks[i];
            if (fileLock != null)
            {
              try
              {
                fileLock.release();
              }
              catch (Throwable throwable)
              {
                // We don't expect this ever to happen, but if it does, we ignore it.
                // Ignore.
              }
            }
          }
        }
      }
    }

    return false;
  }

  public static String getAnnotation(ModelElement modelElement, String sourceURI, String key)
  {
    Annotation annotation = modelElement.getAnnotation(sourceURI);
    return annotation == null ? null : (String)annotation.getDetails().get(key);
  }

  public static void setAnnotation(ModelElement modelElement, String sourceURI, String key, String value)
  {
    Annotation annotation = modelElement.getAnnotation(sourceURI);
    if (value == null)
    {
      if (annotation != null)
      {
        annotation.getDetails().removeKey(key);
      }
    }
    else
    {
      if (annotation == null)
      {
        annotation = BaseFactory.eINSTANCE.createAnnotation();
        annotation.setSource(sourceURI);
        modelElement.getAnnotations().add(annotation);
      }

      annotation.getDetails().put(key, value);
    }
  }

  public static boolean isReduced(ModelElement modelElement)
  {
    return modelElement.eAdapters().contains(REDUCED);
  }

  public static void setReduced(ModelElement modelElement, boolean reduced)
  {
    EList<Adapter> eAdapters = modelElement.eAdapters();
    if (reduced)
    {
      if (!eAdapters.contains(REDUCED))
      {
        eAdapters.add(REDUCED);
      }
    }
    else
    {
      eAdapters.remove(REDUCED);
    }
  }

  public static URI createBogusURI(URI uri)
  {
    return URI.createURI("bogus:" + uri);
  }

  public static URI resolveBogusURI(URI uri)
  {
    return isBogusURI(uri) ? URI.createURI(uri.opaquePart()).appendQuery(uri.query()).appendFragment(uri.fragment()) : uri;
  }

  public static boolean isBogusURI(URI uri)
  {
    return uri != null && uri.scheme() == BOGUS_SCHEME && uri.hasOpaquePart();
  }
}
