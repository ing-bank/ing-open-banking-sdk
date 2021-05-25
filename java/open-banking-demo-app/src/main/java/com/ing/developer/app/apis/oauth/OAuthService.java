package com.ing.developer.app.apis.oauth;

import com.ing.developer.app.apis.oauth.model.Token;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final OAuthAdapter adapter;

    public OAuthService(OAuthAdapter adapter) {
        this.adapter = adapter;
    }

    public String getConsentUri(String redirectUri, String scope, String countryCode) {
        return adapter.getConsentUri(redirectUri, scope, countryCode);
    }

    public Token getCustomerToken(String authorizationCode) {
        return adapter.getCustomerToken(authorizationCode);
    }

}
