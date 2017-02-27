/*
 * Copyright (c) 2017 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * @author Eike Stepper
 */
public final class OAuthConstants
{
  public static final String PROP_SERVICE = "oomph.oauth.service";

  public static final String PROP_EXPECTED_CALLBACK = "oomph.oauth.expected.callback";

  public static final String PROP_SCOPES = "oomph.oauth.scopes";

  public static final String PROP_CLIENT_ID = "oomph.oauth.client.id";

  public static final String PROP_CLIENT_SECRET = "oomph.oauth.client.secret";

  public static final String PROP_CLIENT_KEY = "oomph.oauth.client.key";

  public static final String DEFAULT_SERVICE = "https://accounts.eclipse.org/";

  public static final String DEFAULT_EXPECTED_CALLBACK = "http://localhost/";

  public static final String[] DEFAULT_SCOPES = { "profile", "uss_retrieve", "uss_update" };

  static final String SERVICE = PropertiesUtil.getProperty(PROP_SERVICE, DEFAULT_SERVICE);

  static final String EXPECTED_CALLBACK = PropertiesUtil.getProperty(PROP_EXPECTED_CALLBACK, DEFAULT_EXPECTED_CALLBACK);

  static final String[] SCOPES = initScopes();

  static String getClientID() throws UnsupportedEncodingException
  {
    return getClientValue(PROP_CLIENT_ID, CLIENT_ID);
  }

  static String getClientSecret() throws UnsupportedEncodingException
  {
    return getClientValue(PROP_CLIENT_SECRET, CLIENT_SECRET);
  }

  private static String getClientValue(String propName, String defaultValue) throws UnsupportedEncodingException
  {
    try
    {
      String property = PropertiesUtil.getProperty(propName);
      if (!StringUtil.isEmpty(property))
      {
        String key = PropertiesUtil.getProperty(PROP_CLIENT_KEY);
        if (!StringUtil.isEmpty(key))
        {
          return decrypt(property, key);
        }

        return property;
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return decrypt(defaultValue, CLIENT_KEY);
  }

  private static String decrypt(String str, String key) throws UnsupportedEncodingException
  {
    byte[] keyBytes = HexUtil.hexToBytes(key);
    byte[] bytes = HexUtil.hexToBytes(str);
    byte[] result = new byte[bytes.length - 1];

    int j = bytes[result.length] - Byte.MIN_VALUE;
    crypt(bytes, result, keyBytes, result.length, j);
    return new String(result, "UTF-8");
  }

  private static byte[] encrypt(String str, byte[] key, Random random) throws UnsupportedEncodingException
  {
    byte[] bytes = str.getBytes("UTF-8");
    byte[] result = new byte[bytes.length + 1];

    int j = random.nextInt(key.length);
    result[bytes.length] = (byte)(j + Byte.MIN_VALUE);
    crypt(bytes, result, key, bytes.length, j);
    return result;
  }

  private static void crypt(byte[] bytes, byte[] result, byte[] key, int length, int j)
  {
    for (int i = 0; i < length; i++)
    {
      result[i] = (byte)(bytes[i] ^ key[j++ % key.length]);
    }
  }

  private static String[] initScopes()
  {
    try
    {
      String property = PropertiesUtil.getProperty(PROP_SCOPES);
      if (!StringUtil.isEmpty(property))
      {
        List<String> list = StringUtil.explode(property, ",");
        return list.toArray(new String[list.size()]);
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return DEFAULT_SCOPES;
  }

  private OAuthConstants()
  {
  }

  public static void main(String[] args) throws Exception
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.print("Client ID: ");
    String clientID = reader.readLine();

    System.out.print("Client Secret: ");
    String clientSecret = reader.readLine();

    int keyLength = Math.max(clientID.length(), clientSecret.length());
    byte[] key = new byte[keyLength];

    Random random = new Random(System.currentTimeMillis());
    random.nextBytes(key);

    clientID = HexUtil.bytesToHex(encrypt(clientID, key, random));
    clientSecret = HexUtil.bytesToHex(encrypt(clientSecret, key, random));
    String clientKey = HexUtil.bytesToHex(key);

    System.out.println();
    System.out.println("private static final String CLIENT_ID = \"" + clientID + "\";");
    System.out.println("private static final String CLIENT_SECRET = \"" + clientSecret + "\";");
    System.out.println("private static final String CLIENT_KEY = \"" + clientKey + "\";");
    System.out.println();
    System.out.println("-D" + PROP_CLIENT_ID + "=" + clientID);
    System.out.println("-D" + PROP_CLIENT_SECRET + "=" + clientSecret);
    System.out.println("-D" + PROP_CLIENT_KEY + "=" + clientKey);
  }

  private static final String CLIENT_ID = "4e890e9b113d2470e7a206ef59414cb38a98fbfaaf79760fe8374e96";

  private static final String CLIENT_SECRET = "870a5352c5046346e43d9c19601b74eca332ea62537d80df84d0f28f";

  private static final String CLIENT_KEY = "0c49048fc87fdf2e1207e5edeaa39cc63b233ba346341fb05ad976";
}
