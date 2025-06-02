package com.steve.config;
/**
 * We are using a singleton pattern
 * Single manager shared through out the project
 *
 */

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;
    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;

    }
/**
* Used to load a configuration File by the path provided
 */
    public void loadConfigurationFile(String filePath){

    }
    /**
     *     Returns the current loaded configuration
     * */

    public void getCurrentConfiguration(){

    }
}

