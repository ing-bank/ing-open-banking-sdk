package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpBadRequestException extends OpenBankingHttpException {

    public <T> OpenBankingHttpBadRequestException(String typeName) {
        super("Bad request for resource: " + typeName);
    }

}
