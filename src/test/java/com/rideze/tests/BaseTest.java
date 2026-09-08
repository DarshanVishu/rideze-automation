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
        try {
            DriverFactory.initDriver();
        } catch (Exception e) {
            System.err.println("CRITICAL FAILURE INITIALIZING DRIVER IN SETUP:");
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Screenshots are handled automatically by TestListener on failure
        //DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}