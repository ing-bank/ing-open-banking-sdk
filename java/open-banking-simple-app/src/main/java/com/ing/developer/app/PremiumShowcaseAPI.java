package com.ing.developer.app;
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiClient;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;

import java.security.PrivateKey;
import java.security.cert.Certificate;


public class PremiumShowcaseAPI {
    // Set the BASE_URL=https://api.sandbox.ing.com.
    // Use own client key.
    // Use certs uploaded to the developer portal
    // Use keystore password.
    static String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
    static String keyStoreFileName = "keystore-premium.jks";
    static char[] keyStorePassword = "secret".toCharArray();

    public static void callShowcaseAPI() throws ApiException {
        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
        System.out.println(greeting);
    }

    public static void callShowcaseAPIMTLSPinning() throws ApiException {
        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond()).setMTLSPinning(true);
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.mtlsOnlyGreetingsGet(null).getMessage();
        System.out.println(greeting);
    }

    public static void callShowcaseAPIJWS() throws ApiException {
        Utils.Pair<Certificate, PrivateKey> trustMaterial = Companion.Utils.getTrustMaterial(keyStoreFileName, keyStorePassword);
        Utils.Pair<PrivateKey, ClientBuilder> openBankingClient = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getSecond(), openBankingClient.getSecond(), null, trustMaterial.getFirst()).setMTLSPinning(true).setJwsSigning(true);
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.signedGreetingsGet(null,null).getMessage();
        System.out.println(greeting);
    }
}
