package com.ing.developer.common.exceptions;

public class OpenBankingException extends RuntimeException {

    public OpenBankingException(String message) {
        super(message);
    }

    public OpenBankingException(Throwable cause) {
        super(cause);
    }

    public OpenBankingException(String message, Throwable cause) {
        super(message, cause);
    }

}
