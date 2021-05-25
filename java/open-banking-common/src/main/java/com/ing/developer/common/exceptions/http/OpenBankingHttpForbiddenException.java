package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpForbiddenException extends OpenBankingHttpException {

    public <T> OpenBankingHttpForbiddenException(String typeName) {
        super("Forbidden for resource: " + typeName);
    }

}
