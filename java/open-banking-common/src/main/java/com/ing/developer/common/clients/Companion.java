package com.ing.developer.common.clients;

import com.ing.developer.common.Utils.Pair;
import com.ing.developer.common.exceptions.OpenBankingException;
import org.apache.http.ssl.SSLContextBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.ing.developer.common.Signing.base64encode;

public class Companion {

    public static class Utils {

        public static Pair<PrivateKey, ClientBuilder> createOpenBankingClient(String keyStoreFileName, char[] keyStorePassword, boolean useProxy, String proxyHost, Integer proxyPort, boolean psd2, boolean logging) {
            final KeyStore keyStore = getKeyStore(keyStoreFileName, keyStorePassword);
            final Pair<Certificate, PrivateKey> trustMaterial = getTrustMaterial(keyStore, keyStorePassword);

            final ClientConfig clientConfig = new ClientConfig();
            if (useProxy && proxyHost != null) {
                clientConfig.property(ClientProperties.PROXY_URI, proxyHost + ":" + (proxyPort == null ? 3128 : proxyPort));
            }

            SSLContext ctx = getSSLContext(keyStore, keyStorePassword);

            final ClientBuilder client = ClientBuilder.newBuilder()
                    .sslContext(ctx)
                    .hostnameVerifier(HttpsURLConnection.getDefaultHostnameVerifier())
                    .withConfig(clientConfig);

            if (psd2) {
                client.register((ClientRequestFilter) clientRequestContext -> clientRequestContext.getHeaders().addFirst("TPP-Signature-Certificate", base64encode(trustMaterial.getFirst())));
            }

            if (logging) {
                client.register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));
            }

            return new Pair<>(trustMaterial.getSecond(), client);
        }

        public static Pair<Certificate, PrivateKey> getTrustMaterial(String keyStoreFileName, char[] keyStorePassword) {
            return getTrustMaterial(getKeyStore(keyStoreFileName, keyStorePassword), keyStorePassword);
        }

        private static Pair<Certificate, PrivateKey> getTrustMaterial(KeyStore keyStore, char[] keyStorePassword) {
            String alias = "sign";
            try {
                Certificate certificate = keyStore.getCertificate(alias);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword);
                return new Pair<>(certificate, privateKey);
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                throw new OpenBankingException("Cannot retrieve trust material from keystore with alias: " + alias + ".", e);
            }
        }

        private static KeyStore getKeyStore(String keyStoreFileName, char[] keyStorePassword) {
            try {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStoreFileName), keyStorePassword);
                return keyStore;
            } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
                throw new OpenBankingException("KeyStore initialization and loading failed for: " + keyStoreFileName + " file.", e);
            }
        }

        private static SSLContext getSSLContext(KeyStore keyStore, char[] keyStorePassword) {
            try {
                return new SSLContextBuilder().loadKeyMaterial(keyStore, keyStorePassword, (one, two) -> "tls").build();
            } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
                throw new OpenBankingException(e);
            }
        }

    }

}
