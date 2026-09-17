package com.rideze.driver;

import com.rideze.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static void initDriver() {
        initDriver(null);
    }

    public static void initDriver(String platformOrBrowser) {
        // Clean up any existing active session before switching platforms
        if (DriverManager.getDriver() != null) {
            quitDriver();
        }

        String browser = (platformOrBrowser != null) ? platformOrBrowser : ConfigReader.get("browser");

        try {
            if ("android".equalsIgnoreCase(browser) || "mobile".equalsIgnoreCase(browser)) {
                // 1. Mobile Appium Driver Configuration
                UiAutomator2Options options = new UiAutomator2Options()
                        .setDeviceName(ConfigReader.get("mobileDeviceName"))
                        .setPlatformName(ConfigReader.get("mobilePlatformName"))
                        .setAutomationName(ConfigReader.get("mobileAutomationName"))
                        .setAppPackage(ConfigReader.get("mobileAppPackage"))
                        .setAppActivity(ConfigReader.get("mobileAppActivity"))
                        .setNoReset(Boolean.parseBoolean(ConfigReader.get("mobileNoReset")))
                        .setAutoGrantPermissions(true);

                AndroidDriver androidDriver = new AndroidDriver(
                        new URL(ConfigReader.get("appiumServerUrl")), 
                        options
                );

                // Handle Expo Go Launcher & automatically tap into RideZe Driver
                try {
                    String pkg = ConfigReader.get("mobileAppPackage");
                    androidDriver.activateApp(pkg);

                    WebDriverWait expoWait = new WebDriverWait(androidDriver, Duration.ofSeconds(6));
                    try {
                        WebElement projectCard = expoWait.until(
                                ExpectedConditions.elementToBeClickable(
                                        By.xpath("//*[@text='RideZe Driver' or contains(@text, 'RideZe')]")
                                )
                        );
                        projectCard.click();
                        
                        // Wait for the Metro bundler to mount the UI components
                        Thread.sleep(4000);
                    } catch (Exception notOnHomeScreen) {
                        // Project is already rendered and in foreground
                    }
                } catch (Exception e) {
                    try {
                        androidDriver.activateApp("host.exp.exponent");
                    } catch (Exception ignored) {}
                }

                DriverManager.setDriver(androidDriver);

            } else {
                // 2. Desktop Web Driver Configuration (Chrome / Firefox)
                WebDriver webDriver;
                if ("firefox".equalsIgnoreCase(browser)) {
                    webDriver = new FirefoxDriver();
                } else {
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.addArguments("--remote-allow-origins=*");
                    if (Boolean.parseBoolean(ConfigReader.get("headless"))) {
                        options.addArguments("--headless=new");
                    }
                    webDriver = new ChromeDriver(options);
                }

                webDriver.manage().timeouts().implicitlyWait(
                        Duration.ofSeconds(Long.parseLong(ConfigReader.get("implicitWait")))
                );
                webDriver.manage().timeouts().pageLoadTimeout(
                        Duration.ofSeconds(Long.parseLong(ConfigReader.get("pageLoadTimeout")))
                );

                webDriver.get(ConfigReader.get("baseUrl"));
                DriverManager.setDriver(webDriver);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (DriverManager.getDriver() != null) {
            try {
                DriverManager.getDriver().quit();
            } catch (Exception ignored) {}
            DriverManager.unload();
        }
    }
}