/**
 */
package org.eclipse.oomph.setup.mylyn.tests;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.oomph.setup.mylyn.BuildPlan;
import org.eclipse.oomph.setup.mylyn.MylynFactory;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Build Plan</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class BuildPlanTest extends TestCase
{

  /**
   * The fixture for this Build Plan test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BuildPlan fixture = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(BuildPlanTest.class);
  }

  /**
   * Constructs a new Build Plan test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BuildPlanTest(String name)
  {
    super(name);
  }

  /**
   * Sets the fixture for this Build Plan test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void setFixture(BuildPlan fixture)
  {
    this.fixture = fixture;
  }

  /**
   * Returns the fixture for this Build Plan test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BuildPlan getFixture()
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
    setFixture(MylynFactory.eINSTANCE.createBuildPlan());
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

} //BuildPlanTest
