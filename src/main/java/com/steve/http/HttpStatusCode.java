package com.steve.http;

public enum HttpStatusCode {
    /* CLIENT ERROR CODES */

    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not allowed"),
    CLIENT_ERROR_414_NOT_FOUND(414, "URI too long"),

    //    SERVER ERRORS
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented");
    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int STATUS_CODE, String MESSAGE) {
        this.STATUS_CODE = STATUS_CODE;
        this.MESSAGE = MESSAGE;
    }
}
