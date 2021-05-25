package com.ing.developer.common.exceptions.http;

import com.ing.developer.common.exceptions.OpenBankingException;

public class OpenBankingHttpException extends OpenBankingException {

    public OpenBankingHttpException(String message) {
        super(message);
    }

    public OpenBankingHttpException(String message, Throwable t) {
        super(message, t);
    }

}
