/*
 * Copyright (c) 2014, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.FormHandler;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ecf.core.util.Proxy;
import org.eclipse.ecf.provider.filetransfer.util.ProxySetupHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Ed Merks
 */
public abstract class Authenticator
{
  public static Set<? extends Authenticator> create(VariableTask variable, final StringExpander stringExpander, URIConverter uriConverter)
  {
    Set<Authenticator> result = null;
    if (variable.getType() == VariableType.PASSWORD)
    {
      for (Annotation annotation : variable.getAnnotations())
      {
        if (AnnotationConstants.ANNOTATION_PASSWORD_VERIFICATION.equals(annotation.getSource()))
        {
          EMap<String, String> details = annotation.getDetails();
          String type = details.get("type"); //$NON-NLS-1$
          if ("form".equals(type)) //$NON-NLS-1$
          {
            String cookie = details.get("form.cookie"); //$NON-NLS-1$
            String url = details.get("form.url"); //$NON-NLS-1$
            if (url != null)
            {
              String formFields = details.get("form.parameters"); //$NON-NLS-1$
              if (formFields != null)
              {
                if (result == null)
                {
                  result = new LinkedHashSet<Authenticator>();
                }

                Set<String> keys = new HashSet<String>();
                final Map<String, String> map = new HashMap<String, String>();
                String variableValue = variable.getValue();
                map.put("value", variableValue); //$NON-NLS-1$
                map.put("form.url", url); //$NON-NLS-1$
                StringExpander localExpander = new StringExpander()
                {
                  @Override
                  protected String resolve(String key)
                  {
                    return map.containsKey(key) ? map.get(key) : resolve(stringExpander, key);
                  }

                  @Override
                  protected boolean isUnexpanded(String key)
                  {
                    return !map.containsKey(key) && isUnexpanded(stringExpander, key);
                  }

                  @Override
                  protected String filter(String value, String filterName)
                  {
                    return filter(stringExpander, value, filterName);
                  }
                };

                String formSecurity = details.get("form.secure.parameters"); //$NON-NLS-1$
                List<String> secureKeys = formSecurity == null ? null : SegmentSequence.create(" ", formSecurity).segmentsList(); //$NON-NLS-1$
                Map<String, String> parameters = new LinkedHashMap<String, String>();
                for (String key : SegmentSequence.create(" ", formFields).segments()) //$NON-NLS-1$
                {
                  String detailKey = "form.parameter." + key; //$NON-NLS-1$
                  String unexpandedValue = details.get(detailKey);
                  String value = localExpander.expandString(unexpandedValue, keys);
                  if (value == null)
                  {
                    if (secureKeys.contains(key))
                    {
                      value = PreferencesUtil.encrypt(" "); //$NON-NLS-1$
                    }
                    else
                    {
                      value = unexpandedValue;
                    }
                  }

                  map.put(detailKey, value);
                  parameters.put(key, value);
                }

                String formString = Form.getForm(parameters, secureKeys, true);
                String formFilter = details.get("form.filter"); //$NON-NLS-1$
                boolean isFiltered = formFilter != null && formString.matches(formFilter);

                String ok = localExpander.expandString(details.get("form.ok"), keys); //$NON-NLS-1$
                if (ok == null)
                {
                  continue;
                }

                String info = localExpander.expandString(details.get("form.info"), keys); //$NON-NLS-1$
                if (info == null)
                {
                  continue;
                }

                String warning = localExpander.expandString(details.get("form.warning"), keys); //$NON-NLS-1$
                if (warning == null)
                {
                  continue;
                }

                String error = localExpander.expandString(details.get("form.error"), keys); //$NON-NLS-1$
                if (error == null)
                {
                  continue;
                }

                String verificationURL = localExpander.expandString(details.get("form.verification.url")); //$NON-NLS-1$
                Pattern verificationPattern = null;
                String verificationMatches = localExpander.expandString(details.get("form.verification.matches")); //$NON-NLS-1$
                if (verificationMatches != null)
                {
                  try
                  {
                    verificationPattern = Pattern.compile(verificationMatches, Pattern.DOTALL);
                  }
                  catch (Exception ex)
                  {
                    continue;
                  }
                }

                result.add(new Form(isFiltered, url, parameters, secureKeys, cookie, ok, info, warning, error, verificationURL, verificationPattern));
              }
            }
          }
          else if ("form-post".equals(type)) //$NON-NLS-1$
          {
            String url = details.get("form.url"); //$NON-NLS-1$
            String user = details.get("form.user"); //$NON-NLS-1$
            String password = details.get("form.password"); //$NON-NLS-1$
            String info = details.get("form.info"); //$NON-NLS-1$
            String ok = details.get("form.ok"); //$NON-NLS-1$
            String warning = details.get("form.warning"); //$NON-NLS-1$
            String error = details.get("form.error"); //$NON-NLS-1$
            String responseLocation = details.get("form.response.location.matches"); //$NON-NLS-1$
            if (url != null && user != null && password != null && responseLocation != null && info != null && ok != null && warning != null && error != null)
            {
              if (result == null)
              {
                result = new LinkedHashSet<Authenticator>();
              }

              Set<String> keys = new HashSet<String>();
              final Map<String, String> map = new HashMap<String, String>();
              String variableValue = variable.getValue();
              map.put("value", variableValue); //$NON-NLS-1$
              map.put("form.url", url); //$NON-NLS-1$
              StringExpander localExpander = new StringExpander()
              {
                @Override
                protected String resolve(String key)
                {
                  return map.containsKey(key) ? map.get(key) : resolve(stringExpander, key);
                }

                @Override
                protected boolean isUnexpanded(String key)
                {
                  return !map.containsKey(key) && isUnexpanded(stringExpander, key);
                }

                @Override
                protected String filter(String value, String filterName)
                {
                  return filter(stringExpander, value, filterName);
                }
              };

              String expandedUser = localExpander.expandString(user, keys);
              if (expandedUser == null)
              {
                continue;
              }

              map.put("form.user", expandedUser); //$NON-NLS-1$

              String expandedPassword = localExpander.expandString(password, keys);
              if (expandedPassword == null)
              {
                expandedPassword = PreferencesUtil.encrypt(" "); //$NON-NLS-1$
              }
              map.put("form.password", expandedUser); //$NON-NLS-1$

              String formFilter = details.get("form.filter"); //$NON-NLS-1$
              boolean isFiltered = formFilter != null && expandedUser.matches(formFilter);

              String expandedResponseLocation = localExpander.expandString(responseLocation, keys);
              if (expandedResponseLocation == null)
              {
                continue;
              }

              Pattern responseLocationPattern = null;
              try
              {
                responseLocationPattern = Pattern.compile(expandedResponseLocation);
              }
              catch (PatternSyntaxException ex)
              {
                continue;
              }

              result.add(new FormPost(uriConverter, isFiltered, url, expandedUser, expandedPassword, localExpander.expandString(ok, keys),
                  localExpander.expandString(info, keys), localExpander.expandString(warning, keys), localExpander.expandString(error, keys),
                  responseLocationPattern));
            }
          }
        }
      }
    }

    if (result != null)
    {
      // Clean up old obsolete forms if there are new form-posts.
      for (Authenticator authenticator : result)
      {
        if (authenticator instanceof FormPost)
        {
          for (Iterator<Authenticator> it = result.iterator(); it.hasNext();)
          {
            Authenticator otherAuthenticator = it.next();
            if (otherAuthenticator instanceof Form)
            {
              it.remove();
            }
          }

          break;
        }
      }
    }

    return result;
  }

  /**
   * @author Ed Merks
   */
  public static class Form extends Authenticator
  {
    private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=([A-Za-z0-9-_]+)"); //$NON-NLS-1$

    private boolean isFiltered;

    private String uri;

    private String cookie;

    private Map<String, String> parameters;

    private Collection<String> secureKeys;

    private String verificationURI;

    private Pattern verificationPattern;

    public Form(boolean isFiltered, String uri, Map<String, String> parameters, Collection<String> secureKeys, String cookie, String ok, String info,
        String warning, String error)
    {
      super(ok, info, warning, error);
      this.isFiltered = isFiltered;
      this.uri = uri;
      this.parameters = parameters;
      this.secureKeys = secureKeys;
      this.cookie = cookie;
    }

    public Form(boolean isFiltered, String uri, Map<String, String> parameters, Collection<String> secureKeys, String cookie, String ok, String info,
        String warning, String error, String verificationURI, Pattern verificationPattern)
    {
      this(isFiltered, uri, parameters, secureKeys, cookie, ok, info, warning, error);
      this.verificationURI = verificationURI;
      this.verificationPattern = verificationPattern;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + (cookie == null ? 0 : cookie.hashCode());
      result = prime * result + (isFiltered ? 1231 : 1237);
      result = prime * result + (parameters == null ? 0 : parameters.hashCode());
      result = prime * result + (secureKeys == null ? 0 : secureKeys.hashCode());
      result = prime * result + (uri == null ? 0 : uri.hashCode());
      result = prime * result + (verificationPattern == null ? 0 : verificationPattern.toString().hashCode());
      result = prime * result + (verificationURI == null ? 0 : verificationURI.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      Form other = (Form)obj;
      if (cookie == null)
      {
        if (other.cookie != null)
        {
          return false;
        }
      }
      else if (!cookie.equals(other.cookie))
      {
        return false;
      }

      if (isFiltered != other.isFiltered)
      {
        return false;
      }

      if (parameters == null)
      {
        if (other.parameters != null)
        {
          return false;
        }
      }
      else if (!parameters.equals(other.parameters))
      {
        return false;
      }

      if (secureKeys == null)
      {
        if (other.secureKeys != null)
        {
          return false;
        }
      }
      else if (!secureKeys.equals(other.secureKeys))
      {
        return false;
      }

      if (uri == null)
      {
        if (other.uri != null)
        {
          return false;
        }
      }
      else if (!uri.equals(other.uri))
      {
        return false;
      }

      if (verificationPattern == null)
      {
        if (other.verificationPattern != null)
        {
          return false;
        }
      }
      else if (!verificationPattern.toString().equals(other.verificationPattern.toString()))
      {
        return false;
      }

      if (verificationURI == null)
      {
        if (other.verificationURI != null)
        {
          return false;
        }
      }
      else if (!verificationURI.equals(other.verificationURI))
      {
        return false;
      }

      return true;
    }

    @Override
    public boolean isFiltered()
    {
      return isFiltered;
    }

    @Override
    public int validate()
    {
      int status = IStatus.WARNING;
      for (int i = 0; i < 3; ++i)
      {
        try
        {
          status = sendPost() ? IStatus.OK : IStatus.ERROR;
          break;
        }
        catch (Exception ex)
        {
          // Retry
        }
      }

      if (verificationURI != null && status == IStatus.OK)
      {
        try
        {
          status = verify() ? IStatus.OK : IStatus.ERROR;
        }
        catch (Exception ex)
        {
          status = IStatus.WARNING;
        }
      }

      return status;
    }

    protected boolean sendPost() throws Exception
    {
      URL url = new URL(uri.toString());

      // Establish a connection that looks just like a browser connection to post the form data.
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setUseCaches(false);
      connection.setConnectTimeout(5000);
      connection.setRequestMethod("POST"); //$NON-NLS-1$
      connection.setRequestProperty("User-Agent", "Mozilla/5.0"); //$NON-NLS-1$ //$NON-NLS-2$
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
      connection.setDoOutput(true);

      handleProxy(connection);

      // Write the form data to connection.
      DataOutputStream data = new DataOutputStream(connection.getOutputStream());
      data.writeBytes(getForm(parameters, secureKeys, true));
      data.close();

      // If we receive a valid response...
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK)
      {
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        if (cookie == null)
        {
          // If there is no verification URI but at verification pattern...
          if (verificationURI == null && verificationPattern != null)
          {
            // Use it to match the links returned in the response.
            List<String> links = headerFields.get("Link"); //$NON-NLS-1$
            if (links != null)
            {
              for (String link : links)
              {
                // Return true if any link matches.
                if (verificationPattern.matcher(link).matches())
                {
                  return true;
                }
              }
            }

            return false;
          }

          // We're only checking that we get an HTTP_OK, and not HTTP_UNAUTHORIZED.
          return true;
        }
        else
        {
          // Look for the cookies...
          List<String> list = headerFields.get("Set-Cookie"); //$NON-NLS-1$
          if (list != null)
          {
            String prefix = cookie + "="; //$NON-NLS-1$
            for (String value : list)
            {
              // If the expected cookie is present, return true.
              if (value.startsWith(prefix))
              {
                return true;
              }
            }
          }
        }
      }

      // Otherwise, there is no session cookie, so the form submission did not successfully create a session.
      return false;
    }

    protected boolean verify() throws Exception
    {
      URL url = new URL(verificationURI.toString());

      // Establish a connection that looks just like a browser connection to post the form data.
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setUseCaches(false);
      connection.setConnectTimeout(5000);
      connection.setRequestMethod("GET"); //$NON-NLS-1$
      connection.setRequestProperty("User-Agent", "Mozilla/5.0"); //$NON-NLS-1$ //$NON-NLS-2$

      handleProxy(connection);

      // If we receive a valid response...
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK)
      {
        InputStream inputStream = null;
        try
        {
          inputStream = connection.getInputStream();
          String contentType = connection.getHeaderField("Content-Type"); //$NON-NLS-1$
          if (contentType != null)
          {
            Matcher matcher = CHARSET_PATTERN.matcher(contentType);
            if (matcher.find())
            {
              String charset = matcher.group(1);
              ByteArrayOutputStream bytes = new ByteArrayOutputStream();
              IOUtil.copy(inputStream, bytes);
              String content = new String(bytes.toByteArray(), charset);
              return verificationPattern.matcher(content).matches();
            }
          }
        }
        finally
        {
          IOUtil.close(inputStream);
        }
      }

      // Otherwise, there is no session cookie, so the form submission did not successfully create a session.
      return false;
    }

    protected static String getForm(Map<String, String> parameters, Collection<String> secureKeys, boolean secure)
    {
      StringBuilder form = new StringBuilder();
      for (Map.Entry<String, String> entry : parameters.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();
        if (secure && secureKeys != null && secureKeys.contains(key))
        {
          value = PreferencesUtil.decrypt(value);
        }

        if (form.length() != 0)
        {
          form.append('&');
        }

        form.append(key);
        form.append('=');
        try
        {
          form.append(URLEncoder.encode(value, "UTF-8")); //$NON-NLS-1$
        }
        catch (UnsupportedEncodingException ex)
        {
          // UTF-8 is always supported.
        }
      }

      return form.toString();
    }

    @Override
    public String toString()
    {
      return getForm(parameters, secureKeys, false);
    }

    private void handleProxy(HttpURLConnection connection)
    {
      try
      {
        // If there are proxy settings, ensure that the proper proxy authorization is established.
        Proxy proxy = ProxySetupHelper.getProxy(uri);
        if (proxy != null)
        {
          Authorization authorization = new ECFURIHandlerImpl.AuthorizationHandler.Authorization(proxy.getUsername(), proxy.getPassword());
          if (authorization.isAuthorized())
          {
            connection.setRequestProperty("Proxy-Authorization", authorization.getAuthorization()); //$NON-NLS-1$
          }
        }
      }
      catch (NoClassDefFoundError ex)
      {
        // Ignore.
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public static class FormPost extends Authenticator
  {
    private final URIConverter uriConverter;

    private final boolean isFiltered;

    private final String uri;

    private final String user;

    private final String password;

    private final Pattern responseLocation;

    public FormPost(URIConverter uriConverter, boolean isFiltered, String uri, String user, String password, String ok, String info, String warning,
        String error, Pattern responseLocation)
    {
      super(ok, info, warning, error);
      this.uriConverter = uriConverter;
      this.isFiltered = isFiltered;
      this.uri = uri;
      this.user = user;
      this.password = password;
      this.responseLocation = responseLocation;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + (isFiltered ? 1231 : 1237);
      result = prime * result + (user == null ? 0 : user.hashCode());
      result = prime * result + (password == null ? 0 : password.hashCode());
      result = prime * result + (uri == null ? 0 : uri.hashCode());
      result = prime * result + (responseLocation == null ? 0 : responseLocation.toString().hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      FormPost other = (FormPost)obj;
      if (isFiltered != other.isFiltered)
      {
        return false;
      }

      if (user == null)
      {
        if (other.user != null)
        {
          return false;
        }
      }
      else if (!user.equals(other.user))
      {
        return false;
      }

      if (password == null)
      {
        if (other.password != null)
        {
          return false;
        }
      }
      else if (!password.equals(other.password))
      {
        return false;
      }

      if (uri == null)
      {
        if (other.uri != null)
        {
          return false;
        }
      }
      else if (!uri.equals(other.uri))
      {
        return false;
      }

      if (responseLocation == null)
      {
        if (other.responseLocation != null)
        {
          return false;
        }
      }
      else if (!responseLocation.toString().equals(other.responseLocation.toString()))
      {
        return false;
      }

      return true;
    }

    @Override
    public boolean isFiltered()
    {
      return isFiltered;
    }

    @Override
    public int validate()
    {
      int status = IStatus.WARNING;
      for (int i = 0; i < 3; ++i)
      {
        try
        {
          status = sendPost() ? IStatus.OK : IStatus.ERROR;
          break;
        }
        catch (IOException ex)
        {
          // Retry
          ex.printStackTrace();
        }
      }

      return status;
    }

    protected boolean sendPost() throws IOException
    {
      final Authorization authorization = new Authorization(user, PreferencesUtil.decrypt(password));
      FormHandler formHandler = new ECFURIHandlerImpl.FormHandler(org.eclipse.emf.common.util.URI.createURI(uri), uriConverter,
          new ECFURIHandlerImpl.AuthorizationHandler()
          {
            public Authorization authorize(org.eclipse.emf.common.util.URI uri)
            {
              return authorization;
            }

            public Authorization reauthorize(org.eclipse.emf.common.util.URI uri, Authorization authorization)
            {
              return Authorization.UNAUTHORIZEABLE;
            }
          })
      {
        @Override
        protected boolean isRedo()
        {
          return true;
        }

        @Override
        protected boolean validHeaders(Map<String, List<String>> headers)
        {
          if (super.validHeaders(headers))
          {
            List<String> locations = headers.get("Location"); //$NON-NLS-1$
            if (locations != null)
            {
              for (String location : locations)
              {
                if (responseLocation.matcher(location).matches())
                {
                  return true;
                }
              }
            }
          }

          return false;
        }
      };

      return formHandler.process();
    }

    protected static String getForm(Map<String, String> parameters, Collection<String> secureKeys, boolean secure)
    {
      StringBuilder form = new StringBuilder();
      for (Map.Entry<String, String> entry : parameters.entrySet())
      {
        String key = entry.getKey();
        String value = entry.getValue();
        if (secure && secureKeys != null && secureKeys.contains(key))
        {
          value = PreferencesUtil.decrypt(value);
        }

        if (form.length() != 0)
        {
          form.append('&');
        }

        form.append(key);
        form.append('=');
        try
        {
          form.append(URLEncoder.encode(value, "UTF-8")); //$NON-NLS-1$
        }
        catch (UnsupportedEncodingException ex)
        {
          // UTF-8 is always supported.
        }
      }

      return form.toString();
    }

    @Override
    public String toString()
    {
      return "PostForm: uri=" + uri + " user=" + user + " password=" + (password == null ? null : PreferencesUtil.decrypt(password)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
  }

  private String ok;

  private String info;

  private String warning;

  private String error;

  protected Authenticator(String ok, String info, String warning, String error)
  {
    this.ok = ok;
    this.info = info;
    this.warning = warning;
    this.error = error;
  }

  public abstract boolean isFiltered();

  public abstract int validate();

  public String getMessage(int severity)
  {
    switch (severity)
    {
      case IStatus.OK:
      {
        return ok;
      }
      case IStatus.INFO:
      {
        return info;
      }

      case IStatus.WARNING:
      {
        return warning;
      }
      case IStatus.ERROR:
      {
        return error;
      }
    }

    return null;
  }
}
