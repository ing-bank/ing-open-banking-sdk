package com.ing.developer.app.apis.payment.initiation.model;

import static com.ing.developer.common.Utils.withCentsDelimiter;

public class InstructedAmount {
    public Long amountInCents;
    public Currency currency;

    public String externalize() {
        return toString();
    }

    @Override
    public String toString() {
        return "{" +
                "amountInCents:" + withCentsDelimiter(amountInCents) +
                "currency" + currency.name() +
                "}";
    }
}
