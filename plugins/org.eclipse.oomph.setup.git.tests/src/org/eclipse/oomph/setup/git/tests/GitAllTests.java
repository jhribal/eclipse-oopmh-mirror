/**
 */
package org.eclipse.oomph.setup.git.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>Git</b></em>' model.
 * <!-- end-user-doc -->
 * @generated
 */
public class GitAllTests extends TestSuite
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
    TestSuite suite = new GitAllTests("Git Tests");
    suite.addTest(GitTests.suite());
    return suite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GitAllTests(String name)
  {
    super(name);
  }

} //GitAllTests
