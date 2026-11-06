package com.rideze.tests.booking;

import com.rideze.config.ConfigReader;
import com.rideze.pages.booking.CustomerDashboardPage;
import com.rideze.pages.booking.CustomerLoginPage;
import com.rideze.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CustomerBookingTest extends BaseTest {

    private CustomerDashboardPage customerPage;

    @BeforeMethod
    public void setupCustomerSession() {
        // 1. Ensure maximum viewport to keep all booking cards visible
        getDriver().manage().window().maximize();

        // 2. Navigate directly to customer base URL to reset any multi-step wizard state
        String customerUrl = ConfigReader.get("customerBaseUrl");
        getDriver().get(customerUrl);

        // 3. Handle OTP login if redirected or logged out
        CustomerLoginPage customerLoginPage = new CustomerLoginPage();
        if (customerLoginPage.isCustomerLoginPageDisplayed()) {
            customerLoginPage.loginWithMobileAndOtp(
                    ConfigReader.get("customerPhone"),
                    ConfigReader.get("customerOtp")
            );
        }

        customerPage = new CustomerDashboardPage();
    }

    @Test(priority = 1, description = "Verify customer can view booking dashboard after OTP login")
    public void testCustomerDashboardRender() {
        Assert.assertTrue(customerPage.isDashboardLoaded(), 
                "Customer booking dashboard failed to render after OTP login.");
    }

    @Test(priority = 2, description = "Verify customer can configure Airport Transfer route and advance wizard")
    public void testAirportTransferRouteEntry() {
        customerPage.openAirportBooking()
                    .fillAirportTransferDetails("Indiranagar 100ft Road", "6E 2145")
                    .clickContinue();

        Assert.assertTrue(customerPage.isStepAdvanced(), 
                "Failed to advance past location step in Airport Transfer wizard.");
    }

    @Test(priority = 3, description = "Verify customer can configure Outstation Travel route and advance wizard")
    public void testOutstationRouteEntry() {
        customerPage.openOutstationBooking()
                    .fillOutstationRoute("Bengaluru", "Mysuru", false)
                    .clickContinue();

        Assert.assertTrue(customerPage.isStepAdvanced(), 
                "Failed to advance past location step in Outstation wizard.");
    }

    @Test(priority = 4, description = "Verify customer can select City Sightseeing package and advance wizard")
    public void testCitySightseeingRouteEntry() {
        customerPage.openCitySightseeing()
                    .fillSightseeingRoute("Mysore Palace North Gate", "Chamundi Hills Viewpoint")
                    .clickContinue();

        Assert.assertTrue(customerPage.isStepAdvanced(), 
                "Failed to advance past location step in City Sightseeing wizard.");
    }
}