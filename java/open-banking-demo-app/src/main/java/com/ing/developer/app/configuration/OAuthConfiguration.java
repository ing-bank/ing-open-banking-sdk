package com.ing.developer.app.configuration;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.ws.rs.client.Client;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Arrays;

public class OAuthConfiguration {

    private final Environment env;

    public OAuthConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public OpenBankingOAuthApi openBankingOAuthApi() {
        String keyId = env.getRequiredProperty("psd2.key-id");
        String keyStoreFileName = env.getRequiredProperty("psd2.keystore.name");
        char[] keyStorePassword = env.getRequiredProperty("psd2.keystore.password").toCharArray();
        Boolean logging = env.getRequiredProperty("logging", Boolean.class);
        Boolean useProxy = env.getRequiredProperty("proxy.use", Boolean.class);
        String proxyHost = env.getRequiredProperty("proxy.host");
        Integer proxyPort = env.getRequiredProperty("proxy.port", Integer.class);
        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, useProxy, proxyHost, proxyPort, false, logging).getSecond().build();
        Arrays.fill(keyStorePassword, '0');
        return new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);
    }

}
