package com.ing.developer.app.configuration;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiClient;
import com.ing.developer.showcase.client.api.GreetingsApi;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Arrays;

public class GreetingsConfiguration {

    private final String clientId;
    private final String keyStoreFileName;
    private final char[] keyStorePassword;
    private final boolean logging;
    private final boolean useProxy;
    private final String proxyHost;
    private final int proxyPort;

    public GreetingsConfiguration(Environment env) {
        this.clientId = env.getRequiredProperty("premium.oauth.client-id");
        this.keyStoreFileName = env.getRequiredProperty("premium.keystore.name");
        this.keyStorePassword = env.getRequiredProperty("premium.keystore.password").toCharArray();
        this.logging = env.getRequiredProperty("logging", Boolean.class);
        this.useProxy = env.getRequiredProperty("proxy.use", Boolean.class);
        this.proxyHost = env.getRequiredProperty("proxy.host");
        this.proxyPort = env.getRequiredProperty("proxy.port", Integer.class);
    }

    @Bean
    public GreetingsApi greetingsApi() {
        return new GreetingsApi(buildClient());
    }

    private ApiClient buildClient() {
        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Utils.Pair<PrivateKey, ClientBuilder> pair = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, useProxy, proxyHost, proxyPort, false, logging);
        Arrays.fill(keyStorePassword, '0');
        return new ApiClient(clientId, trustMaterial.getSecond(), pair.getSecond(), null, trustMaterial.getFirst());
    }

}
