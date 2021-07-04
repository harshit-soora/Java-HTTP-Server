package com.demo.httpServer.core;

import com.demo.httpServer.httpParser.HttpRequest;

import java.net.Socket;

/*
 * This file will set the corresponding views for each HTTP Request
 * We will define different functions based upon all the possible request target
 * These method will in return call the HttpConnectionWorkerThread to show the html file

 * We will pass the HttpRequest to this class
 */
public class HttpRequestHandler {
    private HttpRequest httpRequest;
    private Socket socket;

    public HttpRequestHandler(HttpRequest req, Socket soc){
        this.socket = soc;
        this.httpRequest = req;
    }

    // TODO
}
