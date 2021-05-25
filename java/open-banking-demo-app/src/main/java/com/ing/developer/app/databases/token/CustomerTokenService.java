package com.ing.developer.app.databases.token;

import com.ing.developer.app.apis.oauth.model.Token;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ing.developer.common.Utils.getTimeStamp;

@Service
public class CustomerTokenService {

    private final CustomerTokenRepository repository;

    public CustomerTokenService(CustomerTokenRepository repository) {
        this.repository = repository;
    }

    public Token getCustomerAccessToken(UUID uuid) {
        Token customerAccessToken = repository.getCustomerAccessToken(uuid);
        if (isTokenExpired(customerAccessToken)) {
            repository.saveCustomerAccessToken(uuid, null);
            return null;
        } else {
            return customerAccessToken;
        }
    }

    public Token saveCustomerAccessToken(UUID uuid, Token customerAccessToken) {
        return repository.saveCustomerAccessToken(uuid, customerAccessToken);
    }

    private boolean isTokenExpired(Token token) {
        if (token == null) return true;
        return (token.getTimeStamp() + token.getExpiresIn()) - getTimeStamp() < 60; // consider the token also expired if it expires within 60 seconds.
    }

}
