package com.demo.httpServer.core;

import com.demo.httpServer.httpParser.HttpRequest;

/*
 * This file will set the corresponding views for each HTTP Request
 * We will define different functions based upon all the possible request target
 * These method will in return call the HttpConnectionWorkerThread to show the html file

 * We will pass the HttpRequest to this class
 */
public class HttpRequestHandler {
    private HttpRequest httpRequest;

    public HttpRequestHandler(HttpRequest req){
        this.httpRequest = req;
    }

    // TODO
}
