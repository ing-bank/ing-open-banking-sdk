package com.ing.developer.app;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.payment.request.client.ApiClient;
import com.ing.developer.payment.request.client.ApiException;
import com.ing.developer.payment.request.client.api.RegistrationApi;
import com.ing.developer.payment.request.client.model.CertificateResponse;
import com.ing.developer.payment.request.client.model.DailyReceivableLimit;
import com.ing.developer.payment.request.client.model.RegistrationRequest;

import javax.ws.rs.client.ClientBuilder;
import java.math.BigDecimal;
import java.security.PrivateKey;

public class PremiumRegisterMerchant {
    public static void registerMerchant() throws ApiException {
        String clientId = "e77d776b-90af-4684-bebc-521e5b2614dd";
        String keyStoreFileName = "keystore-premium.jks";
        char[] keyStorePassword = "secret".toCharArray();

        Utils.Pair<PrivateKey, ClientBuilder> trustMaterial = Companion.Utils.createOpenBankingClient(keyStoreFileName, keyStorePassword, false, null, null, false, false);
        ApiClient apiClient = new ApiClient(clientId, trustMaterial.getFirst(), trustMaterial.getSecond());

        RegistrationApi registrationApi = new RegistrationApi(apiClient);

        RegistrationRequest request = new RegistrationRequest();
        DailyReceivableLimit dailyReceivableLimit = new DailyReceivableLimit();
        dailyReceivableLimit.setValue(new BigDecimal(500000L));
        request.merchantId("001234567");
        request.merchantSubId("123456");
        request.merchantName("Company BV");
        request.merchantIBAN("NL26INGB0003275339");
        request.dailyReceivableLimit(dailyReceivableLimit);
        request.allowIngAppPayments("Y");

        CertificateResponse response = registrationApi.paymentRequestsRegistrationsPost(clientId, null, request);
        System.out.println("Premium registerMerchant response:" + response);
    }
}
