package com.rideze.driver;

import com.rideze.config.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    public static void initDriver() {
        initDriver(null);
    }

    public static void initDriver(String platformOrBrowser) {
        if (DriverManager.getDriver() != null) {
            quitDriver();
        }

        String browserConfig = ConfigReader.get("browser");
        String browser = (platformOrBrowser != null) 
                ? platformOrBrowser 
                : System.getProperty("browser", browserConfig != null ? browserConfig : "chrome");

        String headlessConfig = ConfigReader.get("headless");
        boolean isHeadless = Boolean.parseBoolean(
                System.getProperty("headless", headlessConfig != null ? headlessConfig : "false")
        );

        try {
            if ("android".equalsIgnoreCase(browser) || "mobile".equalsIgnoreCase(browser)) {
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
                        Thread.sleep(4000);
                    } catch (Exception notOnHomeScreen) {}
                } catch (Exception e) {
                    try {
                        androidDriver.activateApp("com.ridezedriver");
                    } catch (Exception ignored) {}
                }

                DriverManager.setDriver(androidDriver);

            } else {
                WebDriver webDriver;
                String normalizedBrowser = browser.toLowerCase().trim();

                switch (normalizedBrowser) {
                    case "firefox":
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        if (isHeadless) {
                            firefoxOptions.addArguments("-headless");
                        }
                        webDriver = new FirefoxDriver(firefoxOptions);
                        break;

                    case "edge":
                        EdgeOptions edgeOptions = new EdgeOptions();
                        edgeOptions.addArguments("--remote-allow-origins=*");
                        if (isHeadless) {
                            edgeOptions.addArguments("--headless=new");
                            edgeOptions.addArguments("--no-sandbox");
                            edgeOptions.addArguments("--disable-dev-shm-usage");
                        } else {
                            edgeOptions.addArguments("--start-maximized");
                        }
                        webDriver = new EdgeDriver(edgeOptions);
                        break;

                    case "safari":
                        webDriver = new SafariDriver();
                        break;

                    case "chrome":
                    default:
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--remote-allow-origins=*");
                        System.setProperty("webdriver.chrome.silentOutput", "true");
                        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);

                        if (isHeadless) {
                            chromeOptions.addArguments("--headless=new");
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            chromeOptions.addArguments("--disable-gpu");
                            chromeOptions.addArguments("--window-size=1920,1080");
                        } else {
                            chromeOptions.addArguments("--start-maximized");
                        }
                        webDriver = new ChromeDriver(chromeOptions);
                        break;
                }

                // Resilient timeout fallbacks
                long implicitWait = 10;
                try {
                    String waitProp = ConfigReader.get("implicitWait");
                    if (waitProp != null && !waitProp.trim().isEmpty()) {
                        implicitWait = Long.parseLong(waitProp.trim());
                    }
                } catch (Exception ignored) {}

                long pageLoadTimeout = 30;
                try {
                    String timeoutProp = ConfigReader.get("pageLoadTimeout");
                    if (timeoutProp != null && !timeoutProp.trim().isEmpty()) {
                        pageLoadTimeout = Long.parseLong(timeoutProp.trim());
                    }
                } catch (Exception ignored) {}

                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
                webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

                if (!isHeadless && !"safari".equalsIgnoreCase(normalizedBrowser)) {
                    webDriver.manage().window().maximize();
                }

                // Resilient URL navigation
                String targetUrl = System.getProperty("baseUrl", ConfigReader.get("baseUrl"));
                if (targetUrl != null && !targetUrl.trim().isEmpty()) {
                    webDriver.get(targetUrl.trim());
                }

                DriverManager.setDriver(webDriver);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver for " + browser + ": " + e.getMessage(), e);
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
