package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpUnauthorizedException extends OpenBankingHttpException {

    public <T> OpenBankingHttpUnauthorizedException(String typeName) {
        super("Unauthorized for resource: " + typeName);
    }

}
