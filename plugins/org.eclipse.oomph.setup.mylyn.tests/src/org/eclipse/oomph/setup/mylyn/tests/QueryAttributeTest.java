/**
 */
package org.eclipse.oomph.setup.mylyn.tests;

import java.util.Map;

import junit.framework.TestCase;

import junit.textui.TestRunner;

import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynPackage;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Query Attribute</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class QueryAttributeTest extends TestCase
{

  /**
   * The fixture for this Query Attribute test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Map.Entry<String, String> fixture = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(QueryAttributeTest.class);
  }

  /**
   * Constructs a new Query Attribute test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QueryAttributeTest(String name)
  {
    super(name);
  }

  /**
   * Sets the fixture for this Query Attribute test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void setFixture(Map.Entry<String, String> fixture)
  {
    this.fixture = fixture;
  }

  /**
   * Returns the fixture for this Query Attribute test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Map.Entry<String, String> getFixture()
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
  @SuppressWarnings("unchecked")
  protected void setUp() throws Exception
  {
    setFixture((Map.Entry<String, String>)MylynFactory.eINSTANCE.create(MylynPackage.Literals.QUERY_ATTRIBUTE));
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

} //QueryAttributeTest
