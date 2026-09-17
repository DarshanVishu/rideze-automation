package com.rideze.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class BrowserFactory {

    private BrowserFactory() {}

    public static WebDriver createDriver(String browser) {
        WebDriver driver;
        String targetBrowser = (browser == null) ? "chrome" : browser.toLowerCase().trim();

        switch (targetBrowser) {
            case "firefox":
                driver = new FirefoxDriver(DiverOptions.getFirefoxOptions());
                break;
            case "edge":
                driver = new EdgeDriver(DiverOptions.getEdgeOptions());
                break;
            case "chrome":
            default:
                driver = new ChromeDriver(DiverOptions.getChromeOptions());
                break;
        }

        return driver;
    }
}