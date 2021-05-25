package com.ing.developer.app.apis.account.information;

import com.ing.developer.account.information.client.model.Account;
import com.ing.developer.app.apis.oauth.OAuthService;
import com.ing.developer.app.apis.oauth.model.Token;
import com.ing.developer.app.databases.token.CustomerTokenService;
import com.ing.developer.common.exceptions.http.OpenBankingHttpForbiddenException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountInformationService {

    private final OAuthService oAuthService;
    private final CustomerTokenService customerTokenService;
    private final AccountInformationAdapter adapter;

    public AccountInformationService(OAuthService oAuthService, CustomerTokenService customerTokenService, AccountInformationAdapter adapter) {
        this.oAuthService = oAuthService;
        this.customerTokenService = customerTokenService;
        this.adapter = adapter;
    }

    public String getConsentUri() {
        String redirectUri = "http://localhost:8080/account/authorize/token";
        String scope = "payment-accounts:balances:view payment-accounts:transactions:view";
        String countryCode = "NL";
        return oAuthService.getConsentUri(redirectUri, scope, countryCode);
    }

    public String getCustomerAccessToken(UUID userId, String authorizationCode) {
        Token knownToken = customerTokenService.getCustomerAccessToken(userId);
        if (knownToken == null) {
            Token newToken = oAuthService.getCustomerToken(authorizationCode);
            return customerTokenService.saveCustomerAccessToken(userId, newToken).getAccessToken();
        } else {
            return knownToken.getAccessToken();
        }
    }

    public List<Account> getAccounts(UUID uuid) {
        Token customerAccessToken = customerTokenService.getCustomerAccessToken(uuid);
        if (customerAccessToken == null)
            throw new OpenBankingHttpForbiddenException(Account.class.getSimpleName() + "<br><br><a href=\"/account/authorize\">Authorize</a>");
        return adapter.getAccounts(customerAccessToken.getAccessToken());
    }

}
