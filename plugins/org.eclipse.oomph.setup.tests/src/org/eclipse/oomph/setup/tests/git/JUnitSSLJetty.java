/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.tests.git;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * @author Joerg Reichert
 */
public class JUnitSSLJetty extends org.junit.rules.ExternalResource
{

  private Server server = null;

  private int port = -1;

  public JUnitSSLJetty(int port)
  {
    this.port = port;
  }

  @Override
  protected void before() throws Throwable
  {
    super.before();
    server = new Server();

    SslSelectChannelConnector ssl_connector = new SslSelectChannelConnector();
    ssl_connector.setPort(port);
    SslContextFactory cf = ssl_connector.getSslContextFactory();
    cf.setKeyStorePath(getClass().getResource("keystore").toString());
    cf.setKeyStorePassword("123456");
    cf.setKeyManagerPassword("123456");

    server.setConnectors(new Connector[] { ssl_connector });

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setResourceBase("resources/git_repo");

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
    server.setHandler(handlers);

    server.start();
  }

  @Override
  protected void after()
  {
    if (server != null)
    {
      try
      {
        server.stop();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    super.after();
  }
}
