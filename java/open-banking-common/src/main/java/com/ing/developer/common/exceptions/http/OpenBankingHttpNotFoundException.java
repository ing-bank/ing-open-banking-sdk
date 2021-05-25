package com.ing.developer.common.exceptions.http;

public class OpenBankingHttpNotFoundException extends OpenBankingHttpException {

    public <T> OpenBankingHttpNotFoundException(String typeName) {
        super("Resource: " + typeName + " not found");
    }

}
