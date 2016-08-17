package main;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.jboss.resteasy.plugins.server.sun.http.SunHttpJaxrsServer;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;


public class Main {

    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        // Create sun server
        HttpServer sunServer = createHttpsServer(Config.getInstance().getResourcePath("/key.jks"), Config.getInstance().getPort());

        // Wrap sun server in a JAX-RS interface
        SunHttpJaxrsServer jaxrsServer = new SunHttpJaxrsServer();
        jaxrsServer.setHttpServer(sunServer);

        // Add resource classes
        jaxrsServer.getDeployment().getActualResourceClasses().add(Resource.class);

        // Start server
        jaxrsServer.start();
        System.out.println("server started");
    }

    /**
     * Create an HTTPS server.
     * Adapted from http://stackoverflow.com/a/34483734/1559886.
     *
     * @param keyPath Path to the key file.
     * @param port    Port on which to start the web server.
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    private static HttpsServer createHttpsServer(String keyPath, int port) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        // setup the socket address
        InetSocketAddress address = new InetSocketAddress(port);

        // initialize the HTTPS server
        HttpsServer httpsServer = HttpsServer.create(address, 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // initialize the keystore
        char[] password = "password".toCharArray();
        KeyStore javaKeyStore = KeyStore.getInstance("JKS");
        FileInputStream fileInputStream = new FileInputStream(keyPath);
        javaKeyStore.load(fileInputStream, password);

        // setup the key manager factory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(javaKeyStore, password);

        // setup the trust manager factory
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(javaKeyStore);

        // setup the HTTPS context and parameters
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure(HttpsParameters httpsParameters) {
                try {
                    // initialize the SSL context
                    SSLContext sslContext = SSLContext.getDefault();
                    SSLEngine sslEngine = sslContext.createSSLEngine();
                    httpsParameters.setNeedClientAuth(false);
                    httpsParameters.setCipherSuites(sslEngine.getEnabledCipherSuites());
                    httpsParameters.setProtocols(sslEngine.getEnabledProtocols());

                    // get the default parameters
                    SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                    httpsParameters.setSSLParameters(defaultSSLParameters);
                } catch (Exception e) {
                    System.out.println("Failed to create HTTPS port");
                    e.printStackTrace();
                }
            }
        });
        return httpsServer;
    }
}
