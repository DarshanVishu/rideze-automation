package com.rideze.tests;

import com.rideze.driver.DriverFactory;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setUp() {
        String testClassName = this.getClass().getSimpleName();

        // If the test class is for the Driver app, force Android/Appium
        String targetPlatform = (testClassName.contains("Driver") || testClassName.contains("Shift") || testClassName.contains("Mobile"))
                ? "android"
                : "chrome";

        // Always tear down any leftover browser or app session from prior suites
        DriverFactory.quitDriver();

        try {
            DriverFactory.initDriver(targetPlatform);
        } catch (Exception e) {
            System.err.println("Driver initialization failed, forcing clean retry: " + e.getMessage());
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