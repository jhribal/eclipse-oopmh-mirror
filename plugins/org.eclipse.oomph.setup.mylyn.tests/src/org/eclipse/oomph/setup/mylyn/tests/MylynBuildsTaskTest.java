/**
 */
package org.eclipse.oomph.setup.mylyn.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.oomph.setup.mylyn.MylynBuildsTask;
import org.eclipse.oomph.setup.mylyn.MylynFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Builds Task</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class MylynBuildsTaskTest extends TestCase
{

  /**
   * The fixture for this Builds Task test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MylynBuildsTask fixture = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(MylynBuildsTaskTest.class);
  }

  /**
   * Constructs a new Builds Task test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynBuildsTaskTest(String name)
  {
    super(name);
  }

  /**
   * Sets the fixture for this Builds Task test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void setFixture(MylynBuildsTask fixture)
  {
    this.fixture = fixture;
  }

  /**
   * Returns the fixture for this Builds Task test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MylynBuildsTask getFixture()
  {
    return fixture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#setUp()
   * @generated
   */
  @Override
  protected void setUp() throws Exception
  {
    setFixture(MylynFactory.eINSTANCE.createMylynBuildsTask());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#tearDown()
   * @generated
   */
  @Override
  protected void tearDown() throws Exception
  {
    setFixture(null);
  }

} //MylynBuildsTaskTest
