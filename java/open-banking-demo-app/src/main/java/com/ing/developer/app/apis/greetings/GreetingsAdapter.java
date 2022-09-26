package com.ing.developer.app.apis.greetings;

import com.ing.developer.common.Utils;
import com.ing.developer.common.clients.Companion;
import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.cert.Certificate;

import static com.ing.developer.app.common.GenericAdapterErrorHandler.throwInternalizedException;

@Component
public class GreetingsAdapter {

    private final GreetingsApi greetingsApi;

    public GreetingsAdapter(GreetingsApi greetingsApi) {
        this.greetingsApi = greetingsApi;
    }

    public String getGreeting() {
        try {
            return greetingsApi.greetingsSingleGet(null).getMessage();
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), "greetings", e);
        }
    }

    public String getGreetingMtlsPinning() {
        try {
            greetingsApi.getApiClient().setMTLSPinning(true);
            return greetingsApi.mtlsOnlyGreetingsGet(null).getMessage();
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), "greetings mtls", e);
        }
    }

    public String getGreetingJWS() {
        try {
            greetingsApi.getApiClient().setMTLSPinning(true).setJwsSigning(true);
            return greetingsApi.signedGreetingsGet(null, null).getMessage();
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), "greetings", e);
        }
    }

}
