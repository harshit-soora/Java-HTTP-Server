package com.demo.httpServer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread{
    /*
    * This will will listen to new connections
    * Thanks to this class we can take in simultaneous connections at a time
    * This will we done with socket programming
    */

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        // This will be needed when we required to connect to this address
        // But here we are listening thus we are open at a port and external connects to it.
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try {
            while ( serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();

                LOGGER.info(" * Connection Processing Finished.");
            }
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            if (serverSocket!=null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
