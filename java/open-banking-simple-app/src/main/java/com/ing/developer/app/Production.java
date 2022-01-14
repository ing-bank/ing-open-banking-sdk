package com.ing.developer.app;

import static com.ing.developer.app.ProductionShowcaseAPI.callShowcaseAPI;

public class Production {
    public static void callProducation() throws Exception {
        System.out.println("PRODUCTION");
        callShowcaseAPI();
    }
}
