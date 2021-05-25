package com.ing.developer.app.common;

import static com.ing.developer.common.Utils.throwHttpExceptionBasedOnStatusCode;

public class GenericAdapterErrorHandler {

    public static <T> T throwInternalizedException(int statusCode, String resourceSimpleName, com.ing.developer.account.information.client.ApiException e) {
        return throwHttpExceptionBasedOnStatusCode(statusCode, resourceSimpleName, e.getMessage());
    }

    public static <T> T throwInternalizedException(int statusCode, String resourceSimpleName, com.ing.developer.payment.initiation.client.ApiException e) {
        return throwHttpExceptionBasedOnStatusCode(statusCode, resourceSimpleName, e.getMessage());
    }

    public static <T> T throwInternalizedException(int statusCode, String resourceSimpleName, com.ing.developer.payment.request.client.ApiException e) {
        return throwHttpExceptionBasedOnStatusCode(statusCode, resourceSimpleName, e.getMessage());
    }

    public static <T> T throwInternalizedException(int statusCode, String resourceSimpleName, com.ing.developer.showcase.client.ApiException e) {
        return throwHttpExceptionBasedOnStatusCode(statusCode, resourceSimpleName, e.getMessage());
    }

}
