package com.demo.httpServer.core;

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
            outputStream = socket.getOutputStream();

            HtmlFileLoader loader = new HtmlFileLoader("src/main/resources/templates/index.html");
            String html = loader.getHtmlData();

            final String CRLF = "\r\n"; // 13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + // Status Line  :   HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF + // HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());

            LOGGER.info(" * Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
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
