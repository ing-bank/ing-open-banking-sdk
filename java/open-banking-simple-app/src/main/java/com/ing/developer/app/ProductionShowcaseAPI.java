package com.ing.developer.app;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiClient;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;

public class ProductionShowcaseAPI {
    public static void callShowcaseAPI() throws ApiException {
        // Set the BASE_URL=https://api.ing.com.
        // Use own client key.
        // Use certs uploaded to the developer portal
        // Use keystore password.
        String clientId = "3e19a706-511d-480c-ac3e-4ba135b154f4";
        String keyStoreFileName = "keystore-premium.jks";
        char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient clientAPI = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());
        GreetingsApi greetingsApi = new GreetingsApi(clientAPI);
        String greeting = greetingsApi.greetingsSingleGet(null).getMessage();
        System.out.println(greeting);
    }
}
