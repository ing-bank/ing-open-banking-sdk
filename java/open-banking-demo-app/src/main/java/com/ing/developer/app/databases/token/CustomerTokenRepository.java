package com.ing.developer.app.databases.token;

import com.ing.developer.app.apis.oauth.model.Token;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class CustomerTokenRepository {

    private final Map<UUID, Token> userTokens = new HashMap<>(); // For Demo Purposes only!

    public Token getCustomerAccessToken(UUID uuid) {
        return userTokens.get(uuid);
    }

    public Token saveCustomerAccessToken(UUID uuid, Token customerAccessToken) {
        userTokens.put(uuid, customerAccessToken);
        return customerAccessToken;
    }

}
