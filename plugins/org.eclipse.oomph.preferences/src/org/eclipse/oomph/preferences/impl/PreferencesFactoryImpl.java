/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.preferences.impl;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferencesFactoryImpl extends EFactoryImpl implements PreferencesFactory
{
  private Map<String, URI> PREFERENCE_URIS = Collections.synchronizedMap(new WeakHashMap<String, URI>());

  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static PreferencesFactory init()
  {
    try
    {
      PreferencesFactory thePreferencesFactory = (PreferencesFactory)EPackage.Registry.INSTANCE.getEFactory(PreferencesPackage.eNS_URI);
      if (thePreferencesFactory != null)
      {
        return thePreferencesFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new PreferencesFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferencesFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case PreferencesPackage.PREFERENCE_NODE:
        return createPreferenceNode();
      case PreferencesPackage.PROPERTY:
        return createProperty();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case PreferencesPackage.ESCAPED_STRING:
        return createEscapedStringFromString(eDataType, initialValue);
      case PreferencesPackage.URI:
        return createURIFromString(eDataType, initialValue);
      case PreferencesPackage.PREFERENCE_NODE_NAME:
        return createPreferenceNodeNameFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case PreferencesPackage.ESCAPED_STRING:
        return convertEscapedStringToString(eDataType, instanceValue);
      case PreferencesPackage.URI:
        return convertURIToString(eDataType, instanceValue);
      case PreferencesPackage.PREFERENCE_NODE_NAME:
        return convertPreferenceNodeNameToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceNode createPreferenceNode()
  {
    PreferenceNodeImpl preferenceNode = new PreferenceNodeImpl();
    return preferenceNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Property createProperty()
  {
    PropertyImpl property = new PropertyImpl();
    return property;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String createEscapedString(String literal)
  {
    return StringUtil.unescape(literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createEscapedStringFromString(EDataType eDataType, String initialValue)
  {
    return createEscapedString(initialValue);
  }

  private static final String[] ESCAPES = { //
      "\\000", //$NON-NLS-1$
      "\\001", //$NON-NLS-1$
      "\\002", //$NON-NLS-1$
      "\\003", //$NON-NLS-1$
      "\\004", //$NON-NLS-1$
      "\\005", //$NON-NLS-1$
      "\\006", //$NON-NLS-1$
      "\\007", //$NON-NLS-1$
      "\\010", //$NON-NLS-1$
      "\t", //$NON-NLS-1$
      "\n", //$NON-NLS-1$
      "\\013", //$NON-NLS-1$
      "\\014", //$NON-NLS-1$
      "\r", //$NON-NLS-1$
      "\\016", //$NON-NLS-1$
      "\\017", //$NON-NLS-1$
      "\\020", //$NON-NLS-1$
      "\\021", //$NON-NLS-1$
      "\\022", //$NON-NLS-1$
      "\\023", //$NON-NLS-1$
      "\\024", //$NON-NLS-1$
      "\\025", //$NON-NLS-1$
      "\\026", //$NON-NLS-1$
      "\\027", //$NON-NLS-1$
      "\\030", //$NON-NLS-1$
      "\\031", //$NON-NLS-1$
      "\\032", //$NON-NLS-1$
      "\\033", //$NON-NLS-1$
      "\\034", //$NON-NLS-1$
      "\\035", //$NON-NLS-1$
      "\\036", //$NON-NLS-1$
      "\\037" //$NON-NLS-1$
  };

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertEscapedString(String instanceValue)
  {
    if (instanceValue == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0, length = instanceValue.length(); i < length; ++i)
    {
      char c = instanceValue.charAt(i);
      if (c < ESCAPES.length)
      {
        result.append(ESCAPES[c]);
      }
      else if (c == '\\')
      {
        result.append("\\\\"); //$NON-NLS-1$
      }
      else if (c == '\177')
      {
        result.append("\\177"); //$NON-NLS-1$
      }
      else
      {
        result.append(c);
      }
    }

    return result.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertEscapedStringToString(EDataType eDataType, Object instanceValue)
  {
    return convertEscapedString((String)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public URI createURI(String literal)
  {
    if (literal == null)
    {
      return null;
    }

    URI result = PREFERENCE_URIS.get(literal);
    if (result == null)
    {
      String[] segments = literal.split("/"); //$NON-NLS-1$
      int length = segments.length;
      if (length == 0)
      {
        result = URI.createHierarchicalURI(null, "", null, null, null); //$NON-NLS-1$
      }
      else
      {
        result = URI.createURI(""); //$NON-NLS-1$
        StringBuilder property = null;
        boolean startProperty = false;
        int start = -1;
        for (int i = 0; i < length; ++i)
        {
          String segment = segments[i];
          if (property != null)
          {
            if (startProperty)
            {
              property.append('/');
            }
            else
            {
              startProperty = true;
            }

            property.append(segment);
          }
          else if (segment.length() == 0)
          {
            if (i == 0)
            {
              if (i != length - 1)
              {
                result = URI.createHierarchicalURI(null, "", null, null, null); //$NON-NLS-1$
              }

              start = 1;
            }
            else
            {
              property = new StringBuilder();
            }
          }
          else if (i == start)
          {
            result = URI.createHierarchicalURI(null, URI.encodeAuthority(segment, false), null, null, null);
          }
          else
          {
            result = result.appendSegment(URI.encodeSegment(segment, false));
          }
        }

        if (property != null)
        {
          result = result.appendSegment(URI.encodeSegment(property.toString(), false));
        }
      }

      PREFERENCE_URIS.put(literal, result);
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public URI createURIFromString(EDataType eDataType, String initialValue)
  {
    return createURI(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String convertURI(URI instanceValue)
  {
    if (instanceValue == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    String authority = instanceValue.authority();
    if (authority != null)
    {
      result.append('/');
      result.append(URI.decode(authority));
    }

    for (String segment : instanceValue.segments())
    {
      if (result.length() != 0)
      {
        result.append('/');
      }

      String decodedSegment = URI.decode(segment);
      if (decodedSegment.contains("/")) //$NON-NLS-1$
      {
        result.append('/');
      }

      result.append(decodedSegment);
    }

    return result.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertURIToString(EDataType eDataType, Object instanceValue)
  {
    return convertURI((URI)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createPreferenceNodeName(String literal)
  {
    return (String)super.createFromString(PreferencesPackage.Literals.PREFERENCE_NODE_NAME, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String createPreferenceNodeNameFromString(EDataType eDataType, String initialValue)
  {
    return createPreferenceNodeName(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPreferenceNodeName(String instanceValue)
  {
    return super.convertToString(PreferencesPackage.Literals.PREFERENCE_NODE_NAME, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertPreferenceNodeNameToString(EDataType eDataType, Object instanceValue)
  {
    return convertPreferenceNodeName((String)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferencesPackage getPreferencesPackage()
  {
    return (PreferencesPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static PreferencesPackage getPackage()
  {
    return PreferencesPackage.eINSTANCE;
  }

} // PreferencesFactoryImpl
