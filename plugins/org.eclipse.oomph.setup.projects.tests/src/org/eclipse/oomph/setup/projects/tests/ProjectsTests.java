/**
 */
package org.eclipse.oomph.setup.projects.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test suite for the '<em><b>projects</b></em>' package.
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectsTests extends TestSuite
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
    TestSuite suite = new ProjectsTests("projects Tests");
    suite.addTestSuite(ProjectsImportTaskTest.class);
    return suite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectsTests(String name)
  {
    super(name);
  }

} //ProjectsTests
