package com.ing.developer.app.configuration;

import com.ing.developer.account.information.client.ApiClient;
import com.ing.developer.account.information.client.api.AccountDetailsApi;
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;
import java.util.Arrays;

public class AccountInformationConfiguration {

    private final String clientId;
    private final String keyStoreFileName;
    private final char[] keyStorePassword;
    private final boolean logging;
    private final boolean useProxy;
    private final String proxyHost;
    private final int proxyPort;

    public AccountInformationConfiguration(Environment env) {
        this.clientId = env.getRequiredProperty("psd2.key-id");
        this.keyStoreFileName = env.getRequiredProperty("psd2.keystore.name");
        this.keyStorePassword = env.getRequiredProperty("psd2.keystore.password").toCharArray();
        this.logging = env.getRequiredProperty("logging", Boolean.class);
        this.useProxy = env.getRequiredProperty("proxy.use", Boolean.class);
        this.proxyHost = env.getRequiredProperty("proxy.host");
        this.proxyPort = env.getRequiredProperty("proxy.port", Integer.class);
    }

    @Bean
    public AccountDetailsApi accountDetailsApi() {
        return new AccountDetailsApi(buildClient());
    }

    private ApiClient buildClient() {
        Utils.Pair<PrivateKey, ClientBuilder> pair = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, useProxy, proxyHost, proxyPort, true, logging);
        Arrays.fill(keyStorePassword, '0');
        return new ApiClient(clientId, pair.getFirst(), pair.getSecond());
    }

}
