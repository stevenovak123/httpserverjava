package com.steve;

import com.steve.config.Configuration;
import com.steve.config.ConfigurationManager;
import com.steve.core.ServerListenerThread;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


// Driver  class
public class Main {
    private final static Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        logger.info("Server Starting");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();
        logger.info("Using Port: " + configuration.getPort());
        logger.info("Using webroot: " + configuration.getWebroot());

/**
 * Server Socket to create tcp connections
 */
        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(configuration.getPort(), configuration.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}