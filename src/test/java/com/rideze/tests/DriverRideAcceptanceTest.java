package com.rideze.tests;

import com.rideze.pages.ActiveTripPage;
import com.rideze.pages.DriverDashboardPage;
import com.rideze.pages.DriverLoginPage;
import com.rideze.pages.IncomingRideModal;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverRideAcceptanceTest extends BaseTest {

    @Test(priority = 1, description = "Verify incoming ride offer notification and acceptance flow")
    public void testIncomingRideAcceptance() {
        AndroidDriver driver = (AndroidDriver) getDriver();
        DriverLoginPage loginPage = new DriverLoginPage(driver);
        DriverDashboardPage dashboardPage = new DriverDashboardPage(driver);
        IncomingRideModal rideModal = new IncomingRideModal(driver);

        // 1. Ensure driver session is active
        if (!loginPage.isAlreadyLoggedIn()) {
            System.out.println("-> Session inactive. Logging in with OTP...");
            loginPage.loginWithOtp("9141189941");
        } else {
            System.out.println("-> Driver session active on Dashboard.");
        }

        // 2. Clear lingering popups and verify driver is ONLINE
        dashboardPage.dismissCheckOutDialogIfPresent();

        if (!dashboardPage.isDriverOnline()) {
            System.out.println("-> Driver is OFFLINE. Toggling to ONLINE...");
            dashboardPage.toggleDutyStatus();
            Assert.assertTrue(dashboardPage.isDriverOnline(), "Failed to put driver into ONLINE state!");
        }
        System.out.println("-> Driver is confirmed ONLINE and ready for trip dispatch.");

        // 3. Await incoming ride offer popup (waits up to 30s)
        System.out.println("-> Waiting for incoming ride offer modal...");
        Assert.assertTrue(
            rideModal.isRideOfferVisible(),
            "Incoming ride modal did not appear within timeout. Ensure trip was dispatched!"
        );

        // 4. Capture offer details
        String fare = rideModal.getEstimatedFare();
        String pickup = rideModal.getPickupAddress();
        System.out.println("-> Incoming Trip Offer Detected!");
        System.out.println("   [Fare]: " + fare);
        System.out.println("   [Pickup]: " + pickup);

        // 5. Accept trip offer
        ActiveTripPage activeTripPage = rideModal.acceptRide();

        // 6. Verify transition to active trip screen
        Assert.assertTrue(
            activeTripPage.isActiveTripScreenLoaded(),
            "App failed to transition to Active Trip / Pickup Navigation screen!"
        );
        System.out.println("-> PASSED: Ride accepted and transitioned to active trip state successfully.");
    }
}