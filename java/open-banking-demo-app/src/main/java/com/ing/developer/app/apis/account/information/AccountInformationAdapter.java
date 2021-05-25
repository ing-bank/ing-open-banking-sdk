package com.ing.developer.app.apis.account.information;

import com.ing.developer.account.information.client.ApiException;
import com.ing.developer.account.information.client.api.AccountDetailsApi;
import com.ing.developer.account.information.client.model.Account;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.ing.developer.app.common.GenericAdapterErrorHandler.throwInternalizedException;

@Component
public class AccountInformationAdapter {

    private final UUID clientId;
    private final AccountDetailsApi accountDetailsApi;

    public AccountInformationAdapter(Environment env, AccountDetailsApi accountDetailsApi) {
        this.clientId = env.getRequiredProperty("psd2.oauth.client-id", UUID.class);
        this.accountDetailsApi = accountDetailsApi;
    }

    public List<Account> getAccounts(String customerAccessToken) {
        try {
            return accountDetailsApi.v3AccountsGet(clientId, "Bearer " + customerAccessToken).getAccounts();
        } catch (ApiException e) {
            return throwInternalizedException(e.getCode(), "accounts", e);
        }
    }

}
