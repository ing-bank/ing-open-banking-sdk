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

        String merchant = paymentRequestAdapter.registerMerchant(request);
        merchant = merchant.replace("\r", "").replace("\n", "");
        String certificate = "-----BEGIN CERTIFICATE-----MIIDUjCCAjqgAwIBAgIEZyvdCTANBgkqhkiG9w0BAQsFADBrMRQwEgYDVQQDDAthcGkuaW5nLmNvbTEaMBgGA1UECwwRUGF5bWVudFJlcXVlc3RBUEkxFjAUBgNVBAoMDUlORyBCQU5LIE4uVi4xEjAQBgNVBAcMCUFtc3RlcmRhbTELMAkGA1UEBhMCTkwwHhcNMTkwODE4MDgwODAyWhcNMjkwOTE0MDgwODAyWjBrMRQwEgYDVQQDDAthcGkuaW5nLmNvbTEaMBgGA1UECwwRUGF5bWVudFJlcXVlc3RBUEkxFjAUBgNVBAoMDUlORyBCQU5LIE4uVi4xEjAQBgNVBAcMCUFtc3RlcmRhbTELMAkGA1UEBhMCTkwwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCavZYCg5w+tYhm++JWAwP7PPW1Nmuf4k3wCxvbYXmJkR2c8IxCZ2mzrxky2WHjucIACvjgoFKEIeU6HOwUilF5I3N+lJk4gmI0PmFRk6SLWR1AEVX+jad1xQj8oFNhUj8ecIB4GOsZPHdAgZzLZBDS8lLIvrWnoAZQMtO0TEmQBqo5wCeI1Mp6KCh1pAdbx1J82Xhd3h0weQ3hyeicKUFMcANlhxKBYtMDZG/Y3vOBZahW746E6JQo567iOELCIfoCvfio3c3WHms3TJfO6XcJDWNNGhd0q9HdqQR8+W5ed90n4G9h6obnTHi8U2LxDQr2iLwIPYgk37SmdgNGGA4HAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAGi7+k7rNumkLGc8Q4vUMQ5UOgCQhLifFQSnM2CSy4ERRz4rqRPAYbHGVhPOReYzRWkNqXEX+oPu5U2N9/6jJDtOmWILlu4Sr/nrlztvDKGLpMkQyK7M65dIr1fuK/JfcmgMW6rLZ2uuZNxukm7wkrLu6S+MPyt6ag9ouhR7ZEvObb4Q5JEJSF2waNN+2rE1bPX7dOTUQW+ILMALpqc8ufHM10FL2O322O5sr/NMvzOgp+hw73FqAQ9rKS8sjgcOtzWw+JxvwFMv3TKylTGKFhIzfJ5MmmJCbRV5M6eEYMUuOo8idZ6ctKlOqXDySWNwvhdj8nW5Ck/I13Ae9FSrz3Y=-----END CERTIFICATE-----";
        Assertions.assertEquals(certificate, merchant);
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
