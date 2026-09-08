package com.rideze.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final String PROPERTY_FILE_PATH = "src/test/resources/qa.properties";

    static {
        try {
            FileInputStream fis = new FileInputStream(PROPERTY_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not find or load qa.properties file at: " + PROPERTY_FILE_PATH);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property key '" + key + "' not found in qa.properties.");
        }
        return value.trim();
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}