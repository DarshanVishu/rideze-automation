package com.rideze.tests;

import com.rideze.driver.DriverFactory;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("") String suiteBrowser) {
        String testClassName = this.getClass().getSimpleName();

        // 1. Determine target platform: Mobile overrides everything if class matches mobile pattern
        String targetPlatform;
        if (testClassName.contains("Driver") || testClassName.contains("Shift") || testClassName.contains("Mobile")) {
            targetPlatform = "android";
        } else if (suiteBrowser != null && !suiteBrowser.trim().isEmpty()) {
            // Picked up from testng XML <parameter name="browser" value="..."/>
            targetPlatform = suiteBrowser.trim();
        } else {
            // Picked up from command-line -Dbrowser=... or defaults to chrome
            targetPlatform = System.getProperty("browser", "chrome");
        }

        // Always tear down any leftover browser or app session from prior suites
        DriverFactory.quitDriver();

        try {
            DriverFactory.initDriver(targetPlatform);
        } catch (Exception e) {
            System.err.println("Driver initialization failed for " + targetPlatform + ", forcing clean retry: " + e.getMessage());
            DriverFactory.quitDriver();
            DriverFactory.initDriver(targetPlatform);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Guarantee clean session boundaries between tests
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}