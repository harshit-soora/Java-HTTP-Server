package com.demo.httpServer.httpParser;

/*
 * This file have the list of all Error code that a server can return back
 * to client in case of any issue in the request
 */

public enum HttpStatusCode {
    /* -------- Client Errors ---------*/
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method not allowed : Unauthorised"),
    CLIENT_ERROR_414_LONG_REQUEST(414, "URI Too Long"),
    // Request should be within 8000 octet

    /* -------- Server Errors ---------*/
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Method yet not implemented");

    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatusCode(int status_code, String message) {
        this.STATUS_CODE = status_code;
        this.MESSAGE = message;
    }
}
