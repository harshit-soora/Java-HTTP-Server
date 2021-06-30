package com.demo.httpServer.httpParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.demo.httpServer.httpParser.HttpStatusCode.*;

/*
 * This is head file of this package
 * We will be implementing continuous stream buffer to catch the error earlier and stop the parsing
 */

/*
* We will implement GET and HEAD first (Must according to RFC-7231)
* Next we will extend this with POST
 */

/*
* GENERAL GUIDELINES
* -Request methods are case sensitive
* -It supports only US-ASCII codes
* -Method is not recognised - 501 error
* -Method is recognised but can't be executed - 405 error
 */

public class RequestParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestParser.class);

    private static final int SP = 0x20; // 32 Space
    private static final int CR = 0x0D; // 13 Carriage Return
    private static final int LF = 0x0A; // 10 Line Feed


    public HttpRequest parseHttpRequest(InputStream inputStream) throws ParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parseHeaders(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseBody(reader, request);

        return request;
    }

    /*
     * Start-Line : Method + SP + Req Target + SP + Http Version + CRLF : request case
     * or
     * Status Line : response case)
     * (Header-Field + CRLF) + CRLF
     * (Message Body)
     * ()-->denotes the field may be absent ; CRLF is nothing but CR + LF
     */

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, ParsingException {
        int _byte;
        boolean methodSet = false;
        boolean reqTargetSet = false;
        StringBuilder processingDataBuff = new StringBuilder();

        while((_byte = reader.read()) >= 0){
            if(_byte == CR){
                _byte = reader.read();
                if(_byte == LF){
                    if(!methodSet || !reqTargetSet)
                        throw new ParsingException(CLIENT_ERROR_400_BAD_REQUEST);

                    // set Version
                    request.setHttpVersion(processingDataBuff.toString());
                    LOGGER.debug("Version of HTTP : {}" , processingDataBuff.toString());
                    return;
                }
                else{
                    throw new ParsingException(CLIENT_ERROR_400_BAD_REQUEST);
                }
            }

            else if(_byte == SP){
                if(!methodSet){
                    // set method
                    LOGGER.debug("Method of Request : {}" , processingDataBuff.toString());
                    request.setMethod(processingDataBuff.toString());
                    methodSet = true;
                }else if(!reqTargetSet){
                    // set target
                    LOGGER.debug("Target of Request : {}" , processingDataBuff.toString());
                    request.setMethodTarget(processingDataBuff.toString());
                    reqTargetSet = true;
                }
                else{
                    throw new ParsingException(CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuff.delete(0, processingDataBuff.length());
            }
            else{
                if(!methodSet){
                    if(processingDataBuff.length() > HttpMethod.MAX_LENGTH){
                        throw new ParsingException(SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
                processingDataBuff.append((char) _byte);
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws ParsingException, IOException {
        /*
         * If empty line ends with a CRLF means header list is ended
         * Example:
         * header1 + CRLF + CRLF
         * header1 + CRLF + header2 + CRLF + CRLF
         */

        int _byte;
        boolean headerCRLFSet = false;
        StringBuilder processingDataBuff = new StringBuilder();

        while((_byte = reader.read()) >= 0){
            if(_byte == CR){
                _byte = reader.read();
                if(_byte == LF){
                    if(headerCRLFSet)
                        return;

                    // set Header if any
                    String currProcessingDataString = processingDataBuff.toString();
                    if(currProcessingDataString != null){
                        LOGGER.debug("Processing Header : {}" , currProcessingDataString);
                        request.setHeaderRaw(currProcessingDataString);
                    }
                    processingDataBuff.delete(0, processingDataBuff.length());

                    headerCRLFSet = true;
                }
                else{
                    throw new ParsingException(CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            else{
                headerCRLFSet = false;
                processingDataBuff.append((char) _byte);
            }
        }
    }

    /*
     * This method will in return call the appropriate Method Handler (GET, HEAD, POST)
     * HEAD is already satisfied as it doesn't have the content / body
     */
    private void parseBody(InputStreamReader reader, HttpRequest request) {
        // TODO
    }
}
