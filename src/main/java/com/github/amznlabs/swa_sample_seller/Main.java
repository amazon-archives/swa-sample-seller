// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.

package com.github.amznlabs.swa_sample_seller;

import com.github.amznlabs.swa_sample_seller.controllers.RequestController;
import com.github.amznlabs.swa_sample_seller.controllers.AssetsController;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;
import org.jboss.resteasy.plugins.server.sun.http.SunHttpJaxrsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.KeyStore;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws CertificateException, UnrecoverableKeyException,
            NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException {
        // Create sun server
        HttpServer sunServer = createHttpsServer(
                Config.getInstance().getResourcePath("/key.jks"), Config.getInstance().getPort());

        // Wrap sun server in a JAX-RS interface
        SunHttpJaxrsServer jaxrsServer = new SunHttpJaxrsServer();
        jaxrsServer.setHttpServer(sunServer);

        // Add resource classes
        jaxrsServer.getDeployment().getActualResourceClasses().add(RequestController.class);
        jaxrsServer.getDeployment().getActualResourceClasses().add(AssetsController.class);

        // Start server
        jaxrsServer.start();
        LOGGER.info("Server started at https://127.0.0.1:{}", Config.getInstance().getPort());
    }

    /**
     * Create an HTTPS server with the key found at keyPath and port number.
     * Adapted from http://stackoverflow.com/a/34483734/1559886.
     *
     * @param keyPath Path to the key file.
     * @param port    Port on which to start the web server.
     * @return HTTPS server
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    private static HttpsServer createHttpsServer(String keyPath, int port) throws NoSuchAlgorithmException,
            IOException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        // setup the socket address
        InetSocketAddress address = new InetSocketAddress(port);

        // initialize the HTTPS server
        HttpsServer httpsServer = HttpsServer.create(address, 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // initialize the keystore
        char[] password = "password".toCharArray();
        KeyStore javaKeyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fileInputStream = new FileInputStream(keyPath)) {
            javaKeyStore.load(fileInputStream, password);
        }

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
                    LOGGER.error("Failed to configure the HTTPS parameters for an incoming connection: {}", e);
                }
            }
        });
        return httpsServer;
    }
}
