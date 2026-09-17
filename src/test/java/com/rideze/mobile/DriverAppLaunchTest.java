package com.rideze.mobile;

import com.rideze.driver.AppiumDriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DriverAppLaunchTest {

    @BeforeMethod
    public void setUp() {
        AppiumDriverManager.initDriver();
    }

    @Test
    public void testDriverAppLaunch() {
        Assert.assertNotNull(AppiumDriverManager.getDriver(), "Driver session must be active");
        System.out.println("Driver App launched successfully!");
        System.out.println("Current Package: " + AppiumDriverManager.getDriver().getCurrentPackage());
    }

    @AfterMethod
    public void tearDown() {
        AppiumDriverManager.quitDriver();
    }
}