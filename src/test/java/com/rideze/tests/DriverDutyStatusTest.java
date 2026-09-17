package com.rideze.tests;

import com.rideze.pages.DriverDashboardPage;
import com.rideze.pages.DriverLoginPage;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverDutyStatusTest extends BaseTest {

    @Test(description = "Verify Driver is active and in ONLINE state on Dashboard")
    public void testToggleDriverOnline() {
        AndroidDriver driver = (AndroidDriver) getDriver();
        DriverLoginPage loginPage = new DriverLoginPage(driver);
        DriverDashboardPage dashboardPage = new DriverDashboardPage(driver);

        // 1. Authenticate if not already on Dashboard
        if (!loginPage.isAlreadyLoggedIn()) {
            System.out.println("-> App is not on Dashboard. Performing OTP login first...");
            loginPage.loginWithOtp("9141189941");
        }

        // 2. Dismiss any stale modal before checking state
        dashboardPage.dismissCheckOutDialogIfPresent();

        // 3. If ALREADY online, stay online! Do NOT tap power button
        if (dashboardPage.isOnline()) {
            System.out.println("-> Driver is ALREADY in ONLINE state!");
        } else {
            System.out.println("-> Driver is OFFLINE. Toggling to ONLINE...");
            dashboardPage.tapPowerButton();
            dashboardPage.handleGoOnlineDialog();
            dashboardPage.dismissCheckOutDialogIfPresent();
            dashboardPage.handleRuntimePermissions();
        }

        // 4. Assert online state
        Assert.assertTrue(dashboardPage.isOnline(), "Driver failed to verify ONLINE / Active status!");
        System.out.println("-> PASSED: Driver verified ONLINE and ready for trips!");
    }
}