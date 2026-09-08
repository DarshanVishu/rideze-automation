package com.rideze.config;

public final class ConfigFactory {

    private ConfigFactory() {}

    public static String getBaseUrl() {
        return ConfigReader.get("baseUrl");
    }

    public static String getAdminUrl() {
        return ConfigReader.get("adminUrl");
    }

    public static String getApiBaseUrl() {
        return ConfigReader.get("apiBaseUrl");
    }

    public static String getBrowser() {
        return ConfigReader.get("browser");
    }

    public static boolean isHeadless() {
        return ConfigReader.getBoolean("headless");
    }

    public static int getImplicitWait() {
        return ConfigReader.getInt("implicitWait");
    }

    public static int getExplicitWait() {
        return ConfigReader.getInt("explicitWait");
    }

    public static int getPageLoadTimeout() {
        return ConfigReader.getInt("pageLoadTimeout");
    }
}