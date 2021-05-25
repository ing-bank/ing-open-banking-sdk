package com.ing.developer.app.apis.payment.initiation;

import com.ing.developer.app.apis.payment.initiation.model.PaymentInitiation;
import com.ing.developer.payment.initiation.client.ApiException;
import com.ing.developer.payment.initiation.client.api.SinglePaymentInitiationApi;
import com.ing.developer.payment.initiation.client.model.PaymentInitiationResponse;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.UUID;

import static com.ing.developer.app.common.GenericAdapterErrorHandler.throwInternalizedException;

@Component
public class PaymentInitiationAdapter {

    private final UUID clientId;
    private final SinglePaymentInitiationApi paymentInitiationServicePisApi;

    public PaymentInitiationAdapter(Environment env, SinglePaymentInitiationApi paymentInitiationServicePisApi) {
        this.clientId = env.getRequiredProperty("psd2.oauth.client-id", UUID.class);
        this.paymentInitiationServicePisApi = paymentInitiationServicePisApi;
    }

    public PaymentInitiationResponse initiatePayment(PaymentInitiation initiation) {
        URI tppRedirectURI = URI.create("http://localhost:8080");
        try {
            return paymentInitiationServicePisApi.v1PaymentsPain001PaymentProductPost("sepa-credit-transfers", clientId, tppRedirectURI, "127.0.0.1", initiation.externalize(), null);
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), initiation.getClass().getSimpleName(), e);
        }
    }

}
