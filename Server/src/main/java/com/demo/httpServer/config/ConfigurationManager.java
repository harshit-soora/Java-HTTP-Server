package com.demo.httpServer.config;

// Package imports
import com.demo.Utils.Json;

// Library imports (dependency)
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

// Exception imports
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    /*
    * This will be a singleton class
    * As we don't need more than one configuration manager
    */

    private static ConfigurationManager confManager;
    private static Configuration myConf;

    private void ConfigurationManager(){};

    public static ConfigurationManager getInstance(){
        if(confManager==null){
            confManager = new ConfigurationManager();
        }
        return confManager;
    }

    public static Configuration getCurrentConf(){
        if(myConf==null){
            myConf = new Configuration();
        }
        return myConf;
    }

    public void loadConfFile(String filePath){
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException("File path for Config file is Invalid.", e);
        }
        StringBuffer buff = new StringBuffer();

        int i = 0;
        while(true){
            try {
                if ((i = reader.read()) == -1) break;
            } catch (IOException e) {
                throw new HttpConfigurationException("Error while reading Json file : Config", e);
            }
            buff.append((char)i);
        }

        // Let us call the Json Handler to convert it
        JsonNode conf = null;
        try {
            conf = Json.parse(buff.toString());
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error while parsing the Json file", e);
        }

        // Set the current conf
        try {
            myConf = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error while setting up Configuration from Json file", e);
        }
    }

    public Configuration getConfiguration(){
        if(myConf==null){
            throw new HttpConfigurationException("No current configuration set.");
        }
        return myConf;
    }
}



