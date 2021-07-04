package com.demo.httpServer.core;

import com.demo.httpServer.httpParser.HttpRequest;
import com.demo.httpServer.httpParser.ParsingException;
import com.demo.httpServer.httpParser.RequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{
    /*
    * Since the .accept() call is blocking
    * All the process after a connection establishment should be
    * done in a new thread (Worker Thread)
    */

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    final String CRLF = "\r\n"; // 13, 10 //package encapsulation

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Denotes the input coming from the Browser / Webpage
        // Due to client activity or request forums
        InputStream inputStream = null;
        // {method SP request-target SP HTTP-VERSION CRLF} US-ASCII

        // Denotes the webpage data that we return to Client in response
        // or these may be initial webpage's HTML data
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            RequestParser reqParser = new RequestParser();
            String htmlData = null;
            try {
                HttpRequest request = reqParser.parseHttpRequest(inputStream);
                HttpRequestHandler reqHandler = new HttpRequestHandler(request, socket);
                htmlData = reqHandler.setView();
            } catch (ParsingException e) {
                e.printStackTrace();
            }

            outputStream = socket.getOutputStream();
            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line  :   HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                    "Content-Length: " + htmlData.getBytes().length + CRLF + // HEADER
                    CRLF +
                    htmlData +  // CONTENT
                    CRLF + CRLF;

            outputStream.write(response.getBytes());
        }
        catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        }
        finally {
            if (inputStream!= null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket!= null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
