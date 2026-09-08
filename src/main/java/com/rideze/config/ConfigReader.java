package com.rideze.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    private ConfigReader() {}

    static {
        // 1. Determine target environment (defaults to 'qa' if -Denv is omitted)
        String activeEnv = System.getProperty("env", "qa").trim().toLowerCase();
        String configFile = "config/" + activeEnv + ".properties";

        // 2. Load environment-specific properties file from classpath
        InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(configFile);
        if (is == null) {
            is = ConfigReader.class.getClassLoader().getResourceAsStream(activeEnv + ".properties");
        }
        if (is == null) {
            // Fallback to generic config.properties if env-specific file is missing
            is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
        }

        if (is == null) {
            throw new RuntimeException("Could not find configuration file for environment: " + activeEnv);
        }

        try {
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read properties file for environment: " + activeEnv, e);
        }
    }

    public static String get(String key) {
        // Priority 1: Command line / System property (e.g., -DbaseUrl=http://dev.rideze.com)
        String sysVal = System.getProperty(key);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }

        // Priority 2: Properties file value
        String val = properties.getProperty(key);
        if (val == null) {
            throw new RuntimeException("Property key '" + key + "' was not found in configuration.");
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