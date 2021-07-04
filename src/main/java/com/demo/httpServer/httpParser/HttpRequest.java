package com.demo.httpServer.httpParser;

/*
 * Called from Request Parser to extract the specific field  of the request

 * All the fields for HTTP Request will be set inside this Java Class
 * We will return this class to the HTTP Server to handle the request afterwards
 */

/*
 * HEAD is used to get status and headers (obtains metadata information)
 * It is used to ask whether some file exist or not
 * Also used in caching to check its consistency (match md5 checksum hash)
 * Check if a document is changed after last accessed
 * Check Links validity and accessibility

 * GET is used to get status, headers and content.
 * It is used to get complete page
 */

import java.util.HashMap;

public class HttpRequest{
    private HttpMethod method;
    private String methodTarget;
    private String httpVersion;
    private String contentBody;
    private HashMap<String, String> headerList;

    /*
     * Let us keep this class to package encapsulation level
     * As this will be called through RequestParser
     * All SETTERS will be of package privileged

     * First we set the headerList file to check if some malicious header
     * is not passed into the request
     */
    HttpRequest(){
        HttpHeader.getInstance().loadHeaderList("src/main/resources/headerList.txt");
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

    void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getMethodTarget() {
        return methodTarget;
    }

    void setMethodTarget(String methodTarget) {
        this.methodTarget = methodTarget;
    }


    /*
     * Header Field getters and setters
     */
    public HashMap<String, String> getHeaderList() {
        return headerList;
    }

    public boolean getHeaderExistInfo(String key){
        if(this.headerList == null)
            return false;

        if(this.headerList.containsKey(key)){
            return true;
        }
        return false;
    }

    public String getHeaderValue(String key) throws RuntimeException {
        if(getHeaderExistInfo(key)){
            return this.headerList.get(key);
        }

        throw new RuntimeException("No such header field set inside the request");
    }

    // Here we need to extract the key and values from raw string
    void setHeaderRaw(String rawHeaderData) throws ParsingException, RuntimeException {
        String key=null;
        String value=null;

        final int SP = 0x20; // 32 Space
        int i=0;
        int start=-1;
        boolean keySet = false;
        boolean keySetHard = false;
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

    void setHeader(String key, String value) throws ParsingException{
        if(this.headerList == null) {
            this.headerList = new HashMap<String, String>();
        }
        if(key==null){
            throw new RuntimeException("Error extracting the header key");
        }
        if(value==null){
            throw new ParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

        boolean validHeader = HttpHeader.getInstance().validateHeader(key);
        if(!validHeader) { // No such header exist
            throw new ParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

        if(!this.headerList.containsKey(key)){
            this.headerList.put(key, value);
        }
        else { // Two headers of similar key are passed inside the request
            throw new ParsingException(
                HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST
            );
        }
    }


    /*
     * Content Field getters and setters
     */
    public String getContentBody() {
        return contentBody;
    }

    void setContentBody(String contentData) {
        this.contentBody = contentData;
    }
}
