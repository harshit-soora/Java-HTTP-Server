package com.demo.httpServer.config;

public class HttpConfigurationException extends RuntimeException{
    public HttpConfigurationException() {
    }

    public HttpConfigurationException(String s) {
        super(s);
    }

    public HttpConfigurationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HttpConfigurationException(Throwable throwable) {
        super(throwable);
    }

}
