package com.steve.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.steve.util.Json;


import java.io.FileNotFoundException;
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
    public void loadConfigurationFile(String filePath)  {
        FileReader fileReader= null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer buffer = new StringBuffer();
        int i;
        while(true){
            try {
                if (!((i= fileReader.read() ) !=-1)) break;
            } catch (IOException e) {
                throw new HttpConfigurationException(e);
            }
            buffer.append((char)i);
        }
        JsonNode contents = null;
        try {
            contents = Json.parse(buffer.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File contents",e);
        }
        try {
            myCurrentConfiguration= Json.fromJson(contents, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error parsing the Configuration File internally",e);
        }

    }
    /**
     * Returns the current loaded configuration
     *
     *
     */

    public Configuration getCurrentConfiguration(){
if(myCurrentConfiguration==null){
    throw new HttpConfigurationException("No Current configuration present");
}
return myCurrentConfiguration;
    }

}

