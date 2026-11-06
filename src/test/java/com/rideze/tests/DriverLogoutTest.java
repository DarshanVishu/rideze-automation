package com.rideze.tests;

import com.rideze.pages.DriverDashboardPage;
import com.rideze.pages.DriverProfilePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class DriverLogoutTest extends BaseTest {

    private final By loginScreenMarker = AppiumBy.xpath("//*[contains(@text, 'Sign in') or contains(@text, 'Get OTP') or contains(@text, 'Enter mobile number') or contains(@text, 'Phone')]");

    @Test(description = "Verify Driver can navigate to profile and log out cleanly")
    public void testDriverLogout() {
        AndroidDriver driver = (AndroidDriver) getDriver();
        WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(25));
        DriverDashboardPage dashboardPage = new DriverDashboardPage(driver);

        // 1. Ensure Dashboard is loaded
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Dashboard is not visible before initiating logout!");

        // 2. Open Profile page
        System.out.println("-> Navigating to Profile tab...");
        DriverProfilePage profilePage = dashboardPage.openProfileTab();

        // 3. Perform logout
        profilePage.performLogout();

        // 4. Verify redirected to Phone Number Login screen
        boolean returnedToLogin = extendedWait.until(ExpectedConditions.visibilityOfElementLocated(loginScreenMarker)).isDisplayed();

        Assert.assertTrue(returnedToLogin, "Driver was not returned to Login screen after logout!");
        System.out.println("-> PASSED: Driver successfully logged out and returned to Login screen!");
    }
}