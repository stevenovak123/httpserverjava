package com.steve.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.steve.util.Json;


import java.io.FileReader;
import java.io.IOException;

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
    public void loadConfigurationFile(String filePath) throws IOException {
        FileReader fileReader= new FileReader(filePath);
        StringBuffer buffer = new StringBuffer();
        int i;
        while( (i= fileReader.read() ) !=-1){
            buffer.append((char)i);
        }
        JsonNode contents = Json.parse(buffer.toString());
        myCurrentConfiguration= Json.fromJson(contents, Configuration.class);

    }
    /**
     * Returns the current loaded configuration
     *
     *
     */

    public void getCurrentConfiguration(){

    }
}

