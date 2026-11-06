package com.rideze.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    private ConfigReader() {}

    static {
        String activeEnv = System.getProperty("env", "qa").trim().toLowerCase();
        String configFile = "config/" + activeEnv + ".properties";

        InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(configFile);
        if (is == null) {
            is = ConfigReader.class.getClassLoader().getResourceAsStream(activeEnv + ".properties");
        }
        if (is == null) {
            is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
        }

        if (is != null) {
            try {
                properties.load(is);
            } catch (Exception e) {
                System.err.println("Failed to read properties file for environment: " + activeEnv + " - " + e.getMessage());
            }
        } else {
            System.err.println("Warning: Configuration file not found on classpath for environment: " + activeEnv);
        }
    }

    public static String get(String key) {
        // Priority 1: Command line / JVM System property (e.g., -DbaseUrl=...)
        String sysVal = System.getProperty(key);
        if (sysVal != null && !sysVal.trim().isEmpty()) {
            return sysVal.trim();
        }

        // Priority 2: OS Environment Variable (exact key or UPPERCASE)
        String envVal = System.getenv(key);
        if (envVal == null || envVal.trim().isEmpty()) {
            envVal = System.getenv(key.toUpperCase().replace(".", "_"));
        }
        if (envVal != null && !envVal.trim().isEmpty()) {
            return envVal.trim();
        }

        // Priority 3: Properties file value
        String val = properties.getProperty(key);
        if (val != null && !val.trim().isEmpty()) {
            return val.trim();
        }

        return null;
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value != null && !value.trim().isEmpty()) ? value : defaultValue;
    }

    public static int getInt(String key) {
        String val = get(key);
        if (val == null) {
            throw new RuntimeException("Property key '" + key + "' was not found in configuration.");
        }
        return Integer.parseInt(val);
    }

    public static boolean getBoolean(String key) {
        String val = get(key);
        return Boolean.parseBoolean(val);
    }
}