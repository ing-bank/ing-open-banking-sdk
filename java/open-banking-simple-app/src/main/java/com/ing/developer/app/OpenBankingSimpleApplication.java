package com.ing.developer.app;

import static com.ing.developer.app.Premium.callPremium;
import static com.ing.developer.app.Production.callProducation;
import static com.ing.developer.app.Psd2.callPsd2;

public class OpenBankingSimpleApplication {

    public static void main(String[] args) {
        String baseUrl = System.getenv("BASE_URL");
        System.out.println("OpenBankingSimpleApplication " + baseUrl);
        if (baseUrl.equals("https://api.sandbox.ing.com")) {
            try {
                callPsd2();
                callPremium();
            } catch (Exception e) {}
        } else if (baseUrl.equals("https://api.ing.com")) {
            try {
                callProducation();
            } catch (Exception e) {}
        }
    }
}

