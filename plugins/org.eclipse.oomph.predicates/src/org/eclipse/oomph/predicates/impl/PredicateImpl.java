/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.predicates.PredicatesPackage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.resources.IResource;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class PredicateImpl extends ModelElementImpl implements Predicate
{
  private static final Pattern MATCH_NOTHING_PATTERN = Pattern.compile("$^"); //$NON-NLS-1$

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PredicateImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return PredicatesPackage.Literals.PREDICATE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public abstract boolean matches(IResource resource);

  protected Pattern getPattern(String pattern)
  {
    if (pattern != null)
    {
      try
      {
        return Pattern.compile(pattern);
      }
      catch (PatternSyntaxException ex)
      {
        // Ignore.
      }
    }

    return MATCH_NOTHING_PATTERN;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case PredicatesPackage.PREDICATE___MATCHES__IRESOURCE:
        return matches((IResource)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

} // PredicateImpl
