package com.rideze.tests;

import com.rideze.pages.DriverDashboardPage;
import com.rideze.pages.DriverLoginPage;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverAppLoginTest extends BaseTest {

    @Test(description = "Verify Driver is active and in ONLINE state on Dashboard")
    public void testToggleDriverOnline() {
        // Retrieve the driver instance from BaseTest
        AndroidDriver driver = (AndroidDriver) getDriver();

        DriverLoginPage loginPage = new DriverLoginPage(driver);
        DriverDashboardPage dashboardPage = new DriverDashboardPage(driver);

        // 1. If not logged in, execute login first
        if (!loginPage.isAlreadyLoggedIn()) {
            System.out.println("-> App is not on Dashboard. Performing OTP login first...");
            loginPage.loginWithOtp("9141189941");
        }

        // 2. Dismiss vehicle check-out dialog if left open from a prior run
        dashboardPage.dismissCheckOutDialogIfPresent();

        // 3. If currently OFFLINE, toggle to ONLINE
        if (dashboardPage.isOffline()) {
            System.out.println("-> Driver is currently OFFLINE. Toggling to ONLINE...");
            dashboardPage.tapPowerButton();
            dashboardPage.handleGoOnlineDialog();
            dashboardPage.handleRuntimePermissions();
        } else {
            System.out.println("-> Driver is already in ONLINE state.");
        }

        // 4. Verify driver is in ONLINE state
        Assert.assertTrue(dashboardPage.isOnline(), "Driver failed to verify ONLINE / Active status!");
        System.out.println("-> PASSED: Driver verified ONLINE and ready for trips!");
    }
}