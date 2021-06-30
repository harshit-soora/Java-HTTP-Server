package com.demo.httpServer.httpParser;

/*
* HTTP request method are listed here
 */
public enum HttpMethod{
    GET, HEAD, POST;
    public static final int MAX_LENGTH;

    static {
        int temporaryLen = -1;
        for(HttpMethod method : values()){
            if(temporaryLen < method.name().length())
                temporaryLen = method.name().length();
        }
        MAX_LENGTH = temporaryLen;
    }
}
