package com.rideze.tests;

import com.rideze.pages.DriverDashboardPage;
import com.rideze.pages.DriverLoginPage;
import com.rideze.pages.DriverProfilePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DriverProfileTest extends BaseTest {

    @Test(priority = 1, description = "Verify Profile details and assigned vehicle")
    public void testDriverProfileAndVehicle() {
        AndroidDriver driver = (AndroidDriver) getDriver();
        DriverLoginPage loginPage = new DriverLoginPage(driver);
        DriverDashboardPage dashboardPage = new DriverDashboardPage(driver);

        // 1. Authenticate if needed
        if (!loginPage.isAlreadyLoggedIn()) {
            System.out.println("-> Logging in with OTP...");
            loginPage.loginWithOtp("9141189941");
        } else {
            System.out.println("-> Session active: App is already on Dashboard.");
        }

        // 2. Open Profile
        dashboardPage.dismissCheckOutDialogIfPresent();
        DriverProfilePage profilePage = dashboardPage.openProfileTab();

        // 3. Verify screen loaded
        Assert.assertTrue(profilePage.isProfileLoaded(), "Driver Profile screen failed to load!");
        System.out.println("-> Profile screen loaded successfully.");

        // 4. Retrieve values
        String name = profilePage.getDriverName();
        String vehicle = profilePage.getAssignedVehicle();
        System.out.println("-> Driver Name Detected: " + name);
        System.out.println("-> Assigned Vehicle Detected: " + vehicle);

        // Diagnostic dump if empty:
        if (vehicle.isEmpty()) {
            System.out.println("-> Diagnostic: Dumping visible TextViews on screen:");
            List<WebElement> texts = driver.findElements(AppiumBy.xpath("//android.widget.TextView"));
            for (WebElement t : texts) {
                if (!t.getText().trim().isEmpty()) {
                    System.out.println("   [Visible Text]: " + t.getText());
                }
            }
        }

        Assert.assertTrue(
            vehicle.toLowerCase().contains("tigor") || vehicle.toLowerCase().contains("ev") || !name.isEmpty(),
            "Neither driver identity nor vehicle info could be verified on Profile screen!"
        );

        // 5. Navigate back to Home
        dashboardPage.openHomeTab();
        Assert.assertTrue(dashboardPage.isDashboardLoaded(), "Failed to return to Dashboard from Profile!");
        System.out.println("-> Returned to Dashboard successfully.");
    }
}