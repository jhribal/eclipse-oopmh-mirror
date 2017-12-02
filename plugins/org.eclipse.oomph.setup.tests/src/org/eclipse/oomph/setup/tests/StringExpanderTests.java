/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.tests;

import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.setup.internal.core.StringFilterRegistry;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.tests.AbstractTest;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Test with Unix file separator.
 *
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.JVM)
public class StringExpanderTests extends AbstractTest
{
  private final StringExpander expander = new WindowsStringExpander();

  @Test
  public void testNull() throws Exception
  {
    assertThat(expander.expandString(null), isNull());
  }

  @Test
  public void testVariable() throws Exception
  {
    assertThat(expander.expandString("START${windows.path}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariableSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariableSlashSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path//}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariableSlashPipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/|}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariablePipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePipePipe() throws Exception
  {
    assertThat(expander.expandString("START${windows.path||}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePipeSlash() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|/}END"), is("STARTC:\\develop\\java-latest\\END"));
  }

  @Test
  public void testVariableFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|property}END"), is("STARTC:\\\\develop\\\\java-latestEND"));
  }

  @Test
  public void testVariableUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|unknown}END"), is("STARTC:\\develop\\java-latestEND"));
  }

  @Test
  public void testVariablePath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testVariableFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|property/ws}END"), is("STARTC:\\\\develop\\\\java-latest\\wsEND"));
  }

  @Test
  public void testVariableUnresolvedFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${windows.path|unknown/ws}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testVariablePathFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}END"), is("STARTC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testVariablePathUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|unknown}END"), is("STARTC:\\develop\\java-latest\\wsEND"));
  }

  @Test
  public void testUnresolvedVariable() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path}END"), is("START${unknown.path}END"));
  }

  @Test
  public void testUnresolvedVariableFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|property}END"), is("START${unknown.path|property}END"));
  }

  @Test
  public void testUnresolvedVariableUnresolvedFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|unknown}END"), is("START${unknown.path|unknown}END"));
  }

  @Test
  public void testUnresolvedVariablePath() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path/ws}END"), is("START${unknown.path/ws}END"));
  }

  @Test
  public void testUnresolvedVariableFilterPath() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path|property/ws}END"), is("START${unknown.path|property/ws}END"));
  }

  @Test
  public void testUnresolvedVariablePathFilter() throws Exception
  {
    assertThat(expander.expandString("START${unknown.path/ws|property}END"), is("START${unknown.path/ws|property}END"));
  }

  @Test
  public void testTwice() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}${windows.path/ws|property}END"),
        is("STARTC:\\\\develop\\\\java-latest\\\\wsC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testTwiceWithText() throws Exception
  {
    assertThat(expander.expandString("START${windows.path/ws|property}MIDDLE${windows.path/ws|property}END"),
        is("STARTC:\\\\develop\\\\java-latest\\\\wsMIDDLEC:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testDollar() throws Exception
  {
    assertThat(expander.expandString("START$END"), is("START$END"));
    assertThat(expander.expandString("START$$END"), is("START$END"));
  }

  @Test
  public void testEscaped() throws Exception
  {
    assertThat(expander.expandString("START$${windows.path}END"), is("START${windows.path}END"));
  }

  @Test
  public void testEscaped_Bug473706() throws Exception
  {
    assertThat(expander.expandString("<timestamp>${maven.build.timestamp}</timestamp>"), is("<timestamp>${maven.build.timestamp}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$${maven.build.timestamp}</timestamp>"), is("<timestamp>${maven.build.timestamp}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$${maven.build.timestamp}</timestamp>"), is("<timestamp>$${maven.build.timestamp}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$$${maven.build.timestamp}</timestamp>"), is("<timestamp>$${maven.build.timestamp}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$$$${maven.build.timestamp}</timestamp>"), is("<timestamp>$$${maven.build.timestamp}</timestamp>"));

    assertThat(expander.expandString("<timestamp>${windows.path}</timestamp>"), is("<timestamp>C:\\develop\\java-latest</timestamp>"));
    assertThat(expander.expandString("<timestamp>$${windows.path}</timestamp>"), is("<timestamp>${windows.path}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$${windows.path}</timestamp>"), is("<timestamp>$C:\\develop\\java-latest</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$$${windows.path}</timestamp>"), is("<timestamp>$${windows.path}</timestamp>"));
    assertThat(expander.expandString("<timestamp>$$$$${windows.path}</timestamp>"), is("<timestamp>$$C:\\develop\\java-latest</timestamp>"));

    assertThat(SetupUtil.escape("<timestamp>$</timestamp>"), is("<timestamp>$</timestamp>"));
    assertThat(SetupUtil.escape("<timestamp>${}</timestamp>"), is("<timestamp>${}</timestamp>"));
    assertThat(SetupUtil.escape("<timestamp>${maven.build.timestamp}</timestamp>"), is("<timestamp>$${maven.build.timestamp}</timestamp>"));
    assertThat(SetupUtil.escape("<timestamp>$${maven.build.timestamp}</timestamp>"), is("<timestamp>$$$${maven.build.timestamp}</timestamp>"));
    assertThat(SetupUtil.escape("<timestamp>$$${maven.build.timestamp}</timestamp>"), is("<timestamp>$$$$$${maven.build.timestamp}</timestamp>"));
  }

  @Test
  public void testEscapedPathFilter() throws Exception
  {
    assertThat(expander.expandString("START$${windows.path/ws|property}END"), is("START${windows.path/ws|property}END"));
  }

  @Test
  public void testEscapedPathFilterUnescaped() throws Exception
  {
    assertThat(expander.expandString("START$${windows.path/ws|property}${windows.path/ws|property}END"),
        is("START${windows.path/ws|property}C:\\\\develop\\\\java-latest\\\\wsEND"));
  }

  @Test
  public void testBase64FilterTextWithDefaultEncoding() throws Exception
  {
    // NOTE: unparameterised base64 filter uses the platform default encoding.
    String base64 = XMLTypeFactory.eINSTANCE.convertBase64Binary("CAFEBABE".getBytes(Charset.defaultCharset()));
    assertThat(expander.expandString("START${text.ascii|base64}END"), is("START" + base64 + "END"));
  }

  @Test
  public void testBase64FilterTextWithExplicitEncoding() throws Exception
  {
    // NOTE: test portability: every JRE is required to support UTF-8 and ISO-8859-1.
    assertThat(expander.expandString("START${text.extended|base64.iso-8859-1}END"), is("STARTx8Teyd/F38s=END"));
    assertThat(expander.expandString("START${text.extended|base64.utf-8}END"), is("STARTw4fDhMOew4nDn8OFw5/Diw==END"));
  }

  @Test
  public void testBase64FilterXmlWithDefaultEncoding() throws Exception
  {
    String expected = "STARTPD94bWwgdmVyc2lvbj0iMS4wIj8+DQo8cm9vdCBpZD0idTEyMyI+DQoJPGNoaWxkPkNBRkVCQUJFPC9jaGlsZD4NCjwvcm9vdD4=END";
    assertThat(expander.expandString("START${xml.default|base64}END"), is(expected));
    // NOTE: A filter argument cannot override the default XML UTF-8 encoding.
    assertThat(expander.expandString("START${xml.default|base64.iso-8859-1}END"), is(expected));
  }

  @Test
  public void testBase64FilterXmlWithExplicitEncoding() throws Exception
  {
    // NOTE: A filter argument cannot override the explicit or implicit XML encoding.
    String expected = "STARTPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iSVNPLTg4NTktMSI/Pg0KPHJvb3QgaWQ9IrW5srMiPg0KCTxjaGlsZD7HxN7J38XfyzwvY2hpbGQ+DQo8L3Jvb3Q+END";
    assertThat(expander.expandString("START${xml.iso-8859-1|base64}END"), is(expected));
    assertThat(expander.expandString("START${xml.iso-8859-1|base64.utf-8}END"), is(expected));
  }

  /**
   * @author Eike Stepper
   */
  private static final class WindowsStringExpander extends StringExpander
  {
    private final Map<String, String> variables = new HashMap<String, String>();

    public WindowsStringExpander()
    {
      variables.put("windows.path", "C:\\develop\\java-latest");
      variables.put("unix.path", "/develop/java-latest");
      variables.put("user.name", "stepper");
      variables.put("text.ascii", "CAFEBABE");
      variables.put("text.extended", /* "ÇÄÞÉßÅßË" */"\u00C7\u00C4\u00DE\u00C9\u00DF\u00C5\u00DF\u00CB");
      variables.put("xml.default", "<?xml version=\"1.0\"?>\r\n<root id=\"u123\">\r\n\t<child>CAFEBABE</child>\r\n</root>");
      variables.put("xml.iso-8859-1",
          /* "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n<root id=\"µ¹²³\">\r\n\t<child>ÇÄÞÉßÅßË</child>\r\n</root>" */
          "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\r\n<root id=\"\u00B5\u00B9\u00B2\u00B3\">\r\n\t<child>\u00C7\u00C4\u00DE\u00C9\u00DF\u00C5\u00DF\u00CB</child>\r\n</root>");
    }

    @Override
    protected String getFileSeparator()
    {
      return "\\";
    }

    @Override
    protected String resolve(String key)
    {
      return variables.get(key);
    }

    @Override
    protected boolean isUnexpanded(String key)
    {
      return !variables.containsKey(key);
    }

    @Override
    protected String filter(String value, String filterName)
    {
      return StringFilterRegistry.INSTANCE.filter(value, filterName);
    }
  }
}
