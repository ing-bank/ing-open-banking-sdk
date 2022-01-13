package com.ing.developer.app;

import com.ing.developer.account.information.client.ApiException;

import static com.ing.developer.app.Psd2AccountInfo.getAccountInfo;
import static com.ing.developer.app.Psd2AuthUrl.getAuthUrl;
import static com.ing.developer.app.Psd2CustomerToken.getCustomerToken;

public class Psd2 {

    public static void callPsd2() throws ApiException {
        System.out.println("PSD2");
        getAuthUrl();
        getCustomerToken();
        getAccountInfo();
    }
}
