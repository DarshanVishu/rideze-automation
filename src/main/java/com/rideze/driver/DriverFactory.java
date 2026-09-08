package com.rideze.driver;

import com.rideze.config.ConfigFactory;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public final class DriverFactory {
    private DriverFactory() {}

    public static void initDriver() {
        if (DriverManager.getDriver() == null) {
            // Check CLI/System property first, fallback to config
            String browser = System.getProperty("browser", ConfigFactory.getBrowser());
            
            WebDriver driver = BrowserFactory.createDriver(browser);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigFactory.getImplicitWait()));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            // Opens base application URL
            driver.get(ConfigFactory.getBaseUrl());
            DriverManager.setDriver(driver);
        }
    }

    public static void quitDriver() {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.err.println("Failed to cleanly quit WebDriver: " + e.getMessage());
            } finally {
                DriverManager.unload();
            }
        }
    }
}