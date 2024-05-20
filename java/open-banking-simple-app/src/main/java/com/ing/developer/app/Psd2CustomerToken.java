package com.ing.developer.app;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;
import org.openapitools.client.model.TokenResponse;

import javax.ws.rs.client.Client;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class Psd2CustomerToken {
    public static TokenResponse getCustomerToken() {
        String keyId = "SN=546212FB";
        String keyStoreFileName = "keystore-psd2.jks";
        char[] keyStorePassword = "secret2".toCharArray();

        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false).getSecond().build();

        OpenBankingOAuthApi openBankingOAuthApi = new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);

        String authorizationCode = "8b6cd77a-aa44-4527-ab08-a58d70cca286";

        TokenResponse customerToken = openBankingOAuthApi.getCustomerToken(authorizationCode);
        System.out.println("customerToken:" + customerToken.getAccessToken());
        return customerToken;
    }
}
