package com.ing.developer.app.apis.payment.initiation.model;

public class PaymentInitiation {
    public InstructedAmount instructedAmount;
    public CreditorAccount creditorAccount;
    public String creditorName;

    public String externalize() {
        return toString();
    }

    @Override
    public String toString() {
        return "{" +
                "instructedAmount:" + instructedAmount.externalize() +
                "creditorAccount" + creditorAccount.externalize() +
                "creditorName:" + creditorName +
                "}";
    }

}
