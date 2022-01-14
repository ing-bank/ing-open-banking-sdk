package com.ing.developer.app;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.common.clients.OpenBankingOAuthApi;

import javax.ws.rs.client.Client;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class Psd2AuthUrl {
    public static void getAuthUrl() {
        String keyId = "SN=5E4299BE";
        String keyStoreFileName = "keystore-psd2.jks";
        char[] keyStorePassword = "secret2".toCharArray();

        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Client openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false).getSecond().build();

        OpenBankingOAuthApi openBankingOAuthApi = new OpenBankingOAuthApi(keyId, trustMaterial, openBankingClient);

        String redirectUri = "https://www.example.com";
        String countryCode = "NL";
        String scope = "payment-accounts%3Abalances%3Aview%20payment-accounts%3Atransactions%3Aview";

        String consentUri = openBankingOAuthApi.getConsentUri(redirectUri, scope, countryCode);
        System.out.println("consentUri:" + consentUri);
    }
}
