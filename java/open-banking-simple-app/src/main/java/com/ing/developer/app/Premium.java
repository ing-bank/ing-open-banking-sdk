package com.ing.developer.app;

import static com.ing.developer.app.PremiumRegisterMerchant.registerMerchant;
import static com.ing.developer.app.PremiumShowcaseAPI.*;


public class Premium {
    public static void callPremium() throws Exception {
        System.out.println("PREMIUM");
        registerMerchant();
        callShowcaseAPI();
        callShowcaseAPIMTLSPinning();
        callShowcaseAPIJWS();
    }
}
