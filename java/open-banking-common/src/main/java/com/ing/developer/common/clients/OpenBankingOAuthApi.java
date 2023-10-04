package com.ing.developer.common.clients;

import com.ing.developer.common.OBSigner;
import org.tomitribe.auth.signatures.Signer;
import com.ing.developer.common.Signing;
import com.ing.developer.common.Utils;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.AuthorizationURLResponse;
import org.openapitools.client.model.TokenResponse;

import javax.ws.rs.client.Client;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ing.developer.common.Utils.getTimeStamp;

public class OpenBankingOAuthApi {

    private final DefaultApi client;
    private final String tpPSignatureCertificate;
    private final PrivateKey privateKey;
    private final OBSigner signer;

    private OBSigner featSigner;
    private Utils.Pair<TokenResponse, Long> tokenTimeStampPair;

    public OpenBankingOAuthApi(String clientId, Utils.Pair<Certificate, PrivateKey> trustMaterial, Client client) {
        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(client);
        this.client = new DefaultApi(apiClient);
        this.tpPSignatureCertificate = Signing.base64encode(trustMaterial.getFirst());
        this.privateKey = trustMaterial.getSecond();
        this.signer = Signing.getNewSigner(clientId, privateKey);
    }

    public String getConsentUri(String redirectUri, String scope, String countryCode) {
        String digest = Signing.digest("");
        String date = Signing.getDate();

        String path = "/oauth2/authorization-server-url?redirect_uri=" + encode(redirectUri) + "&scope=" + encode(scope) + "&country_code=" + countryCode;

        TokenResponse token = getToken();

        String clientId = token.getClientId();
        String signature = Signing.sign(getFeatSigner(clientId), "get", path, getMandatoryHeaders(digest, date)).toString().substring("Signature ".length());
        try {
            AuthorizationURLResponse response = client.authorizationServerUrlUsingGET("Bearer " + token.getAccessToken(), signature, date, digest, scope, redirectUri, countryCode);
            return response.getLocation() + "?client_id=" + clientId + "&scope=" + scope.replace(" ", "%20") + "&redirect_uri=" + redirectUri + "&state=" + UUID.randomUUID();
        } catch (ApiException e) {
            return Utils.throwHttpExceptionBasedOnStatusCode(e.getCode(), "", e.getMessage());
        }
    }

    public TokenResponse getCustomerToken(String authorizationCode) {
        String grantType = "authorization_code";
        String digest = Signing.digest("code=" + authorizationCode + "&grant_type=" + grantType);
        String date = Signing.getDate();

        TokenResponse token = getToken();

        String signature = Signing.sign(getFeatSigner(token.getClientId()), "post", "/oauth2/token", getMandatoryHeaders(digest, date)).toString();
        String authorization = "Bearer " + token.getAccessToken();

        try {
            return client.oauth2TokenPost(authorization, date, digest, grantType, signature.substring("Signature ".length()), tpPSignatureCertificate, null, authorizationCode, null, null);
        } catch (ApiException e) {
            return Utils.throwHttpExceptionBasedOnStatusCode(e.getCode(), "", e.getMessage());
        }
    }

    public TokenResponse getToken() {
        if (!isTokenExpired(tokenTimeStampPair)) {
            return tokenTimeStampPair.getFirst();
        }

        String grantType = "client_credentials";
        String digest = Signing.digest("grant_type=" + grantType);
        String date = Signing.getDate();

        String authorization = Signing.sign(signer, "post", "/oauth2/token", getMandatoryHeaders(digest, date)).toString();

        try {
            TokenResponse tokenResponse = client.oauth2TokenPost(authorization, date, digest, grantType, null, tpPSignatureCertificate, null, null, null, null);
            tokenTimeStampPair = new Utils.Pair<>(tokenResponse, getTimeStamp());
            return tokenTimeStampPair.getFirst();
        } catch (ApiException e) {
            return Utils.throwHttpExceptionBasedOnStatusCode(e.getCode(), "", e.getMessage());
        }
    }

    private Map<String, String> getMandatoryHeaders(String digest, String date) {
        return Collections.unmodifiableMap(new HashMap<String, String>() {{
            put("Accept", "application/json");
            put("User-Agent", "openbanking-sdk/0.0.3-SNAPSHOT java");
            put("Digest", digest);
            put("Date", date);
        }});
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private OBSigner getFeatSigner(String clientId) {
        if (featSigner == null) {
            featSigner = Signing.getNewSigner(clientId, privateKey);
        }
        return featSigner;
    }

    private boolean isTokenExpired(Utils.Pair<TokenResponse, Long> tokenPair) {
        if (tokenPair == null) return true;
        TokenResponse token = tokenPair.getFirst();
        Long timeStamp = tokenPair.getSecond();
        if (token == null || timeStamp == null || token.getExpiresIn() == null) return true;
        return (timeStamp + token.getExpiresIn()) - getTimeStamp() < 60; // consider the token also expired if it expires within 60 seconds.
    }

}
