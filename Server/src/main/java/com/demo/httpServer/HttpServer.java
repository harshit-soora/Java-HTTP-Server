package com.demo.httpServer;

import com.demo.httpServer.config.Configuration;
import com.demo.httpServer.config.ConfigurationManager;
import com.demo.httpServer.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpServer {
    /*
    * This will be our Driver Class
    */
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Starting the server ...");

        ConfigurationManager.getInstance().loadConfFile("src/main/resources/config.json");
        Configuration conf = ConfigurationManager.getInstance().getConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle later.
        }
    }
}
