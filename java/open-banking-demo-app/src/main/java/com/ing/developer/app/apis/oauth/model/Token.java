package com.ing.developer.app.apis.oauth.model;

import org.openapitools.client.model.TokenResponse;

import java.util.Objects;

public class Token {

    private final String accessToken;
    private final int expiresIn;
    private final long timeStamp;

    public Token(TokenResponse tokenResponse, long timeStamp) {
        this.accessToken = Objects.requireNonNull(tokenResponse.getAccessToken());
        this.expiresIn = Objects.requireNonNull(tokenResponse.getExpiresIn());
        this.timeStamp = timeStamp;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
