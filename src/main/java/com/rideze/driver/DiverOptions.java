package com.rideze.driver;

import com.rideze.config.ConfigFactory;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DiverOptions {
    private DiverOptions() {}

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        if (ConfigFactory.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    public static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        if (ConfigFactory.isHeadless()) {
            options.addArguments("-headless");
        }
        return options;
    }

    public static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        if (ConfigFactory.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }
}