package com.ing.developer.app.apis.oauth;

import com.ing.developer.app.apis.oauth.model.Token;
import com.ing.developer.common.clients.OpenBankingOAuthApi;
import org.springframework.stereotype.Component;

import static com.ing.developer.common.Utils.getTimeStamp;

@Component
public class OAuthAdapter {

    private final OpenBankingOAuthApi openBankingOAuthApi;

    public OAuthAdapter(OpenBankingOAuthApi openBankingOAuthApi) {
        this.openBankingOAuthApi = openBankingOAuthApi;
    }

    public String getConsentUri(String redirectUri, String scope, String countryCode) {
        return openBankingOAuthApi.getConsentUri(redirectUri, scope, countryCode);
    }

    public Token getCustomerToken(String authorizationCode) {
        return new Token(openBankingOAuthApi.getCustomerToken(authorizationCode), getTimeStamp());
    }

}
