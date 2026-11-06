package com.rideze.tests.fleet;

import com.rideze.config.ConfigReader;
import com.rideze.pages.LoginPage;
import com.rideze.pages.booking.DashboardPage;
import com.rideze.pages.fleet.VehiclesPage;
import com.rideze.tests.BaseTest;
import com.rideze.utils.DataUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VehiclesTest extends BaseTest {

    private VehiclesPage vehiclesPage;

    @BeforeMethod
    public void loginAndNavigateToFleet() {
        String base = ConfigReader.get("baseUrl", "http://localhost:3001");
        base = base.replaceAll("/+$", ""); // strip trailing slashes

        getDriver().get(base + "/login");

        LoginPage loginPage = new LoginPage();
        if (loginPage.isLoginPageDisplayed()) {
            DashboardPage dashboardPage = loginPage.loginAsAdmin(
                    ConfigReader.get("adminEmail"),
                    ConfigReader.get("adminPassword")
            );
            vehiclesPage = dashboardPage.navigateToFleet();
        } else {
            getDriver().get(base + "/fleet");
            vehiclesPage = new VehiclesPage();
        }
    }

    @Test(description = "Verify admin can register a new vehicle into the fleet with Prime category, capacity 4, and vehicle image")
    public void testAddNewVehicleSuccessfully() {
        String uniqueReg = DataUtils.generateUniqueRegistrationNumber();
        String imagePath = System.getProperty("user.dir") + "/src/test/resources/test_car.png";

        vehiclesPage.clickAddVehicle()
                    .fillVehicleDetails(uniqueReg, "Hyundai", "Verna", "Prime", "4")
                    .uploadVehicleImage(imagePath)
                    .submitVehicleForm();

        vehiclesPage.searchVehicle(uniqueReg);
        Assert.assertTrue(
                vehiclesPage.isVehicleInList(uniqueReg),
                "Newly registered vehicle [" + uniqueReg + "] was not found in the fleet list."
        );
    }

    @Test(description = "Verify Ford vehicle number KA01AB0707 can be found using the search bar")
    public void testSearchFordVehicle() {
        String existingPlate = "KA01AB0707";
        vehiclesPage.searchVehicle(existingPlate);
        Assert.assertTrue(
                vehiclesPage.isVehicleInList(existingPlate),
                "Existing vehicle [" + existingPlate + "] was not returned in search results."
        );
    }
}