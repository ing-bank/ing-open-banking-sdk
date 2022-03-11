package com.ing.developer.app;

import com.ing.developer.account.information.client.ApiClient;
import com.ing.developer.account.information.client.ApiException;
import com.ing.developer.account.information.client.api.AccountDetailsApi;
import com.ing.developer.account.information.client.model.Account;
import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import org.openapitools.client.model.TokenResponse;

import javax.ws.rs.client.ClientBuilder;
import java.security.PrivateKey;
import java.util.List;
import java.util.UUID;

public class Psd2AccountInfo {
    public static void getAccountInfo() throws ApiException {
        TokenResponse customerToken = Psd2CustomerToken.getCustomerToken();

        String keyId = "SN=5E4299BE";
        char[] keyStorePassword = "secret2".toCharArray();
        String keyStoreFileName = "keystore-psd2.jks";

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, true, false);
        ApiClient apiClient = new ApiClient(keyId, trustMaterial.getFirst(), trustMaterial.getSecond());
        AccountDetailsApi accountDetailsApi = new AccountDetailsApi(apiClient);

        String clientId = "5ca1ab1e-c0ca-c01a-cafe-154deadbea75";
        List<Account> accountDetails = accountDetailsApi.v3AccountsGet(UUID.fromString(clientId), "Bearer " + customerToken.getAccessToken()).getAccounts();
        System.out.println("accountInfo:" + accountDetails);

    }
}
