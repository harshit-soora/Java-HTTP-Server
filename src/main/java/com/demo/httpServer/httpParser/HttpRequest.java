package com.demo.httpServer.httpParser;

/*
 * Called from Request Parser to extract the specific field  of the request
 */

/*
 * All the fields for HTTP Request will be set inside this Java Class
 * We will return this class to the HTTP Server to handle the request afterwards
 */


import java.util.HashMap;

public class HttpRequest{
    private HttpMethod method;
    private String methodTarget;
    private String httpVersion;
    private HashMap<String, String> headerList;

    /*
    * Let us keep this class to package encapsulation level
    * As this will be called through RequestParser
     */
    HttpRequest(){

    }

    /*
     * Request Line getters and setters
     */
    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String methodName) throws ParsingException {
        for(HttpMethod method : HttpMethod.values()){
            if(methodName.equals(method.name())){
                this.method = HttpMethod.valueOf(methodName);
                return;
            }
        }

        throw new ParsingException(
            HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getMethodTarget() {
        return methodTarget;
    }

    public void setMethodTarget(String methodTarget) {
        this.methodTarget = methodTarget;
    }

    /*
     * Header Field getters and setters
     */
    public HashMap<String, String> getHeaderList() {
        return headerList;
    }

    public String getHeaderValue(String key) throws RuntimeException {
        if(this.headerList.containsKey(key)){
            return this.headerList.get(key);
        }

        throw new RuntimeException("No such header field set inside the request");
    }

    // Here we need to extract the key and values from raw string
    public void setHeaderRaw(String rawHeaderData) throws ParsingException, RuntimeException {
        String key=null;
        String value=null;

        final int SP = 0x20; // 32 Space
        int i=0;
        int start=-1;
        boolean keySet = false;
        boolean keySetHard = false;
        String buff;
        while(i < rawHeaderData.length()){
            if(rawHeaderData.charAt(i)==':' && !keySet) {
                keySet = true;
            }
            else if(rawHeaderData.charAt(i)==SP && keySet){
                if(!keySetHard) {
                    key = rawHeaderData.substring(0, i - 1); // begin and end index
                    start = i + 1;
                }
                keySetHard = true;
            }

            i++;
        }

        if(keySet && start != -1){
            value = rawHeaderData.substring(start, i);
        }

        setHeader(key, value);
    }

    public void setHeader(String key, String value) throws ParsingException{
        if(this.headerList == null)
            this.headerList = new HashMap<String, String>();

        if(key==null){
            throw new RuntimeException("Error extracting the header key");
        }

        HttpHeader.getInstance().loadHeaderList("src/main/resources/headerList.txt");
        boolean validHeader = HttpHeader.getInstance().validateHeader(key);

        if(!validHeader) {
            System.out.println(key);
            System.out.println(value);
            // No such header exist
            throw new ParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

        if(!this.headerList.containsKey(key)){
            if(value != null)
                this.headerList.put(key, value);
            else
                throw new ParsingException(
                    HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST
                );
        }

        else {
            // Two headers of similar key are passed inside the request
            throw new ParsingException(
                HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST
            );
        }
    }
}
