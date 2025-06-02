package com.steve;

import com.steve.config.Configuration;
import com.steve.config.ConfigurationManager;

// Driver  class
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello from HTTP server!");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
                Configuration configuration= ConfigurationManager.getInstance().getCurrentConfiguration();

                System.out.println("Using Port: "+ configuration.getPort());
        System.out.println("Using webroot: "+ configuration.getWebroot());
    }

}