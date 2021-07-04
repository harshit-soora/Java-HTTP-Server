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

    /*
     * This code will set the view based upon the request from the server
     * This may in return call some private functions based upon the request target
     */
    public String setView(){
        String reqTarget = httpRequest.getMethodTarget();
        String html;
        HtmlFileLoader loader;

        if(reqTarget == null){
            loader = new HtmlFileLoader("src/main/resources/templates/index.html");
            html = loader.getHtmlData();
        }
        else if(reqTarget.equals("/request")){
            loader = new HtmlFileLoader("src/main/resources/templates" + reqTarget + ".html");
            html = loader.getHtmlData();
        }
        else{// reqTarget.equals("/") // As we are only requested for the home page
            loader = new HtmlFileLoader("src/main/resources/templates/index.html");
            html = loader.getHtmlData();
        }
        return html;
    }
}
