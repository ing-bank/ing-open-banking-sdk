package com.ing.developer.app;

import com.ing.developer.app.apis.account.information.AccountInformationService;
import com.ing.developer.app.apis.greetings.GreetingsAdapter;
import com.ing.developer.app.apis.payment.initiation.PaymentInitiationAdapter;
import com.ing.developer.app.apis.payment.initiation.model.CreditorAccount;
import com.ing.developer.app.apis.payment.initiation.model.Currency;
import com.ing.developer.app.apis.payment.initiation.model.InstructedAmount;
import com.ing.developer.app.apis.payment.initiation.model.PaymentInitiation;
import com.ing.developer.app.apis.payment.request.PaymentRequestAdapter;
import com.ing.developer.common.exceptions.http.OpenBankingHttpNotFoundException;
import com.ing.developer.common.exceptions.http.OpenBankingHttpUnauthorizedException;
import com.ing.developer.payment.request.client.model.DailyReceivableLimit;
import com.ing.developer.payment.request.client.model.RegistrationRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenBankingDemoApplicationTestIntegration {

    @Autowired
    private PaymentRequestAdapter paymentRequestAdapter;

    @Autowired
    private PaymentInitiationAdapter paymentInitiationAdapter;

    @Autowired
    private AccountInformationService accountInformationService;

    @Autowired
    private GreetingsAdapter greetingsAdapter;

    @Test
    void contextLoads() {
    }

    @Test
    void registerMerchant() {
        RegistrationRequest request = new RegistrationRequest();
        DailyReceivableLimit dailyReceivableLimit = new DailyReceivableLimit();
        dailyReceivableLimit.setValue(new BigDecimal(500000L));
        request.merchantId("001234567");
        request.merchantSubId("123456");
        request.merchantName("Company BV");
        request.merchantIBAN("NL26INGB0003275339");
        request.dailyReceivableLimit(dailyReceivableLimit);
        request.allowIngAppPayments("Y");

        String merchantCertificate = paymentRequestAdapter.registerMerchant(request).getAcquiringMtlsCertificate();
        merchantCertificate = merchantCertificate.replace("\r", "").replace("\n", "");
        String certificate = "-----BEGIN CERTIFICATE-----MIIDSjCCAjKgAwIBAgIGAZCmwJ27MA0GCSqGSIb3DQEBCwUAMGYxCzAJBgNVBAYTAk5MMRYwFAYDVQQIDA1Ob29yZC1Ib2xsYW5kMRIwEAYDVQQHDAlBbXN0ZXJkYW0xFzAVBgNVBAoMDklORyBHcm9lcCBOLlYuMRIwEAYDVQQDDAkwMDUxNTc5ODYwHhcNMjQwNzEyMTE0MjM5WhcNMjUwODEyMTE0MjM5WjBmMQswCQYDVQQGEwJOTDEWMBQGA1UECAwNTm9vcmQtSG9sbGFuZDESMBAGA1UEBwwJQW1zdGVyZGFtMRcwFQYDVQQKDA5JTkcgR3JvZXAgTi5WLjESMBAGA1UEAwwJMDA1MTU3OTg2MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAujyo1+M6xq1pMXA3b7dCHpUmyOo0MiSYe2hHellRFbS+4uKvPvjAMlkwcG4Egj+BOy6NyfbRaagvkeANJRmxZxqcwNXmIRrJZN8KA3+sswPRgAroJEYJR/iZuXilQH5SXOx4gpjwQDYV+51dqCDGSEWZGCXgePj1pE4p3miTioQDCGZuoZ47yHx39pKgXpPhJybSfyWOKs9REv/yznfZFXrkhsi46i7ht0Dxj8Hx6JV1qq5mrQ52hxidYlHk/+9/hHOBDILhcEBpgLxVcAfBMun/JFJRj89myZbX5ITIoTY7AVt+Vc5Rzyo0x79y3MzmL9cuGPPDUylpaW1RJY1jfQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQAkSLjlzf6Zhupr1kTPICRCBqew1DwQK4Jgn8bxsnFq6HS0OIRgMXamFxbc9PaNyx8vjqjAXOyBPIAiSdvc6kgVgPq+55A72SRoB9wlodubh8Tw4ZpFQdUGwpntYm9V1sGtzuufuiXjBddIvZzFlj7NELuyuDIKjpLIJqHzx1qA7KTiALmeAPOandICYybU3BPxBYHuanzuMIui7N9GZPofxC3NVmFPp0vXTf4l+M6kMo0wJPh1uW23euJefOynqIcXh7HEgvlCB/CNdfKi9D2A9Dmdd3mA7my6kR/Nb7jQBupTdexmi21tG10OpAmc83Q8/R7paGdGm5ylmCH4rmVO-----END CERTIFICATE-----";
        Assertions.assertEquals(certificate, merchantCertificate);
    }

    @Test
    void initiatePayment() {
        PaymentInitiation initiation = new PaymentInitiation();
        initiation.instructedAmount = new InstructedAmount();
        initiation.instructedAmount.amountInCents = 100L;
        initiation.instructedAmount.currency = Currency.EUR;
        initiation.creditorAccount = new CreditorAccount();
        initiation.creditorAccount.iban = "AT861921125678901234";
        initiation.creditorName = "Laura Musterfrau";

        Assertions.assertThrows(OpenBankingHttpNotFoundException.class, () -> paymentInitiationAdapter.initiatePayment(initiation));
    }

    @Test
    void getCustomerConsentUri() {
        String consentUri = accountInformationService.getConsentUri();
        System.out.println("\n********** ********** ********** BEGIN URI ********** ********** **********\n" +
                consentUri + "\n********** ********** **********  END URI  ********** ********** **********\n");
        Assertions.assertTrue(consentUri.length() > 10);
    }

    @Test
    void getGreeting() {
        String greetings = greetingsAdapter.getGreeting();
        Assertions.assertEquals("Welcome to ING!", greetings);
    }

    @Test
    void getGreetingMtlsPinning() {
        String greetings = greetingsAdapter.getGreetingMtlsPinning();
        Assertions.assertEquals("Welcome to ING!", greetings);
    }

    @Test
    void getGreetingJWS() {
            String greetings = greetingsAdapter.getGreetingJWS();
        Assertions.assertEquals("Welcome to ING!", greetings);
    }
}
