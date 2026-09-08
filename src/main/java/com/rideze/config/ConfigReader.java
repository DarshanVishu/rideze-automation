package com.rideze.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    private ConfigReader() {}

    static {
        InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config/qa.properties");
        if (is == null) {
            is = ConfigReader.class.getClassLoader().getResourceAsStream("qa.properties");
        }

        if (is == null) {
            throw new RuntimeException("Could not find qa.properties in src/main/resources or src/main/resources/config!");
        }

        try {
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read qa.properties", e);
        }
    }

    public static String get(String key) {
        String val = properties.getProperty(key);
        if (val == null) {
            throw new RuntimeException("Property key '" + key + "' was not found in qa.properties.");
        }
        return val.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}