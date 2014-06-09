/**
 */
package org.eclipse.oomph.setup.mylyn.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Mylyn</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class MylynAllTests extends TestSuite
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(suite());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static Test suite()
  {
    TestSuite suite = new MylynAllTests("Mylyn Tests");
    suite.addTest(MylynTests.suite());
    return suite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynAllTests(String name)
  {
    super(name);
  }

} //MylynAllTests
