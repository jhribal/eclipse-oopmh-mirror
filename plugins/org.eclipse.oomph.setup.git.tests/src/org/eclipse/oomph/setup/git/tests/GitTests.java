/**
 */
package org.eclipse.oomph.setup.git.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>git</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class GitTests extends TestSuite
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
    TestSuite suite = new GitTests("git Tests");
    suite.addTestSuite(GitCloneTaskTest.class);
    return suite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GitTests(String name)
  {
    super(name);
  }

} //GitTests
