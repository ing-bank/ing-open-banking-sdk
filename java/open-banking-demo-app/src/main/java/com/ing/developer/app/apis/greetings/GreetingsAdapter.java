package com.ing.developer.app.apis.greetings;

import com.ing.developer.showcase.client.ApiException;
import com.ing.developer.showcase.client.api.GreetingsApi;
import org.springframework.stereotype.Component;

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

}
