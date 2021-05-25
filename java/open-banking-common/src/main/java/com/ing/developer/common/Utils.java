package com.ing.developer.common;

import com.ing.developer.common.exceptions.http.*;

public class Utils {
    public static String withCentsDelimiter(Long amountInCents) {
        String amount = amountInCents.toString();
        return amount.substring(0, amount.length() - 2) + "." + amount.substring(amount.length() - 2);
    }

    public static class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return this.first;
        }

        public S getSecond() {
            return this.second;
        }

    }

    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static <T> T throwHttpExceptionBasedOnStatusCode(int statusCode, String resourceSimpleName, String message) {
        if (statusCode == 400) throw new OpenBankingHttpBadRequestException(resourceSimpleName);
        if (statusCode == 401) throw new OpenBankingHttpUnauthorizedException(resourceSimpleName);
        if (statusCode == 403) throw new OpenBankingHttpForbiddenException(resourceSimpleName);
        if (statusCode == 404) throw new OpenBankingHttpNotFoundException(resourceSimpleName);
        throw new OpenBankingHttpException(message);
    }

}
