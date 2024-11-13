package com.ing.developer.app.apis.payment.request;

import com.ing.developer.payment.request.client.ApiException;
import com.ing.developer.payment.request.client.api.RegistrationApi;
import com.ing.developer.payment.request.client.model.CertificateResponse;
import com.ing.developer.payment.request.client.model.RegistrationRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static com.ing.developer.app.common.GenericAdapterErrorHandler.throwInternalizedException;

@Component
public class PaymentRequestAdapter {

    private final String clientId;
    private final RegistrationApi registrationApi;

    public PaymentRequestAdapter(Environment env, RegistrationApi registrationApi) {
        this.clientId = env.getRequiredProperty("premium.oauth.client-id");
        this.registrationApi = registrationApi;
    }

    public CertificateResponse registerMerchant(RegistrationRequest registrationRequest) {
        try {
            return registrationApi.paymentRequestsRegistrationsPost(clientId, null, registrationRequest);
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), registrationRequest.getClass().getSimpleName(), e);
        }
    }

}
