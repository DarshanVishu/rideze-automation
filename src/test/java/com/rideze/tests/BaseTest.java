package com.rideze.tests;

import com.rideze.config.ConfigFactory;
import com.rideze.driver.DriverFactory;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        try {
            DriverFactory.initDriver();

            String baseUrl = System.getProperty("baseUrl");
            if (baseUrl != null && !baseUrl.isBlank()) {
                getDriver().get(baseUrl);
            } else {
                getDriver().get(ConfigFactory.getBaseUrl());
            }
        } catch (Exception e) {
            System.err.println("CRITICAL FAILURE INITIALIZING DRIVER IN SETUP:");
            e.printStackTrace();
            DriverFactory.quitDriver();
            throw e;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            // TestNG runs listener onTestFailure before @AfterMethod.
            // Keep driver open until this block finishes so failure captures complete cleanly.
        } finally {
            DriverFactory.quitDriver();
        }
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
