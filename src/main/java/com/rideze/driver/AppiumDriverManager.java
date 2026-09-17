package com.rideze.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class AppiumDriverManager {

    private static final ThreadLocal<AndroidDriver> driver = new ThreadLocal<>();

    public static AndroidDriver getDriver() {
        return driver.get();
    }

    public static void initDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setAutomationName("UiAutomator2");
        options.setDeviceName("emulator-5554");
        
        // Point to the built-in Android Settings app for now
        options.setAppPackage("com.android.settings");
        options.setAppActivity("com.android.settings.Settings");
        
        options.setAutoGrantPermissions(true);
        options.setNoReset(true);

        try {
            URL appiumServerUrl = URI.create("http://127.0.0.1:4723/").toURL();
            AndroidDriver androidDriver = new AndroidDriver(appiumServerUrl, options);
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.set(androidDriver);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium Server URL: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}