package com.ing.developer.app.apis.payment.initiation.model;

public class CreditorAccount {
    public String iban;

    public String externalize() {
        return toString();
    }

    @Override
    public String toString() {
        return "{iban:" + iban + "}";
    }
}
