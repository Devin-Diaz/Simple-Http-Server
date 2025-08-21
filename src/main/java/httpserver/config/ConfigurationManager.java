package httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {}

    public static ConfigurationManager getInstance() {
        if(myConfigurationManager == null) {
            myConfigurationManager =  new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    public void loadConfigurationFile(String filePath)  {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }

        StringBuffer sb = new StringBuffer();
        int i;

        try {
            while((i = fileReader.read()) != - 1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            throw new HttpConfigurationException(e);
        }

        JsonNode config = null;
        try {
            config = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Error Parsing Configuration File");
        }

        try {
            myCurrentConfiguration = Json.fromJson(config, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Error Parsing Configuration File. Internal");
        }
    }

    public Configuration getCurrentConfiguration() {
        if(myCurrentConfiguration == null) {
            throw new HttpConfigurationException("No Current Configuration Set");
        }
        return myCurrentConfiguration;
    }

}
