package com.main.auto.service;

import java.util.ResourceBundle;

public class ConfigurationManager {

    public static final ConfigurationManager instance = new ConfigurationManager();
    private static ResourceBundle resourceBundle;

    private ConfigurationManager() {

    }


    public static ConfigurationManager getInstance() {
        return instance;
    }

    public static String getProperty(String propertyFile, String key){
        resourceBundle = ResourceBundle.getBundle(String.format("com.resources.%s", propertyFile));
        String value = resourceBundle.getString(key);
        return value;
    }
}
