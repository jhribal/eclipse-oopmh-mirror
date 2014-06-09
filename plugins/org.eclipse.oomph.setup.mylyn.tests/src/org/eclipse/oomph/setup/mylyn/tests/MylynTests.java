/**
 */
package org.eclipse.oomph.setup.mylyn.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>mylyn</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class MylynTests extends TestSuite
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
    TestSuite suite = new MylynTests("mylyn Tests");
    suite.addTestSuite(MylynQueriesTaskTest.class);
    suite.addTestSuite(MylynBuildsTaskTest.class);
    return suite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynTests(String name)
  {
    super(name);
  }

} //MylynTests
