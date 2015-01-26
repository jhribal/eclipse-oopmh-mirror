package org.eclipse.oomph.setup.tests.git;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.impl.GitCloneTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class GitCloneTaskImplTest
{
  private GitCloneTaskImpl cloneTask = (GitCloneTaskImpl)GitFactory.eINSTANCE.createGitCloneTask();

  private int port = 8888;

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Rule
  public JUnitSSLJetty server = new JUnitSSLJetty(port);

  @Test
  public void testGitCloneTask() throws Exception
  {
    disableCertificateCheck();

    String newRemoteURI = "https://my@mail.com@localhost:" + port + "/.git";
    cloneTask.setCheckoutBranch("master");
    cloneTask.setLocation(folder.getRoot().getAbsolutePath() + "/local_clone");
    cloneTask.setRemoteURI(newRemoteURI);
    ResourceSetImpl rs = new ResourceSetImpl();
    URIConverter uriConverter = rs.getURIConverter();
    SetupPrompter prompter = SetupPrompter.CANCEL;
    Trigger trigger = Trigger.MANUAL;
    SetupContext setupContext = SetupContext.create();
    Stream stream = SetupFactory.eINSTANCE.createStream();
    stream.setName("master");
    SetupTaskContext context = new SetupTaskPerformer(uriConverter, prompter, trigger, setupContext, stream);
    cloneTask.isNeeded(context);
    cloneTask.perform(context);
  }

  private void disableCertificateCheck() throws NoSuchAlgorithmException, KeyManagementException
  {
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
    {
      public java.security.cert.X509Certificate[] getAcceptedIssuers()
      {
        return null;
      }

      public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
      {
      }

      public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
      {
      }

    } };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier()
    {

      public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession)
      {
        if (hostname.equals("localhost"))
        {
          return true;
        }
        return false;
      }
    });
  }
}
