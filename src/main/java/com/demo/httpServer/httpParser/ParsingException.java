package com.demo.httpServer.httpParser;

/*
* All exceptions are handled through this class
* This class in return will return the appropriate Error message code
 */

public class ParsingException extends Exception{

    private HttpStatusCode errorCode;

    public ParsingException(HttpStatusCode errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCode getErrorCode() {
        return errorCode;
    }
}
