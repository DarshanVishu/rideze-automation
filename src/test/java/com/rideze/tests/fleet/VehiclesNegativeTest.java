package com.rideze.tests.fleet;

import com.rideze.config.ConfigReader;
import com.rideze.pages.LoginPage;
import com.rideze.pages.booking.DashboardPage;
import com.rideze.pages.fleet.VehiclesPage;
import com.rideze.tests.BaseTest;
import com.rideze.utils.DataUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class VehiclesNegativeTest extends BaseTest {

    private VehiclesPage vehiclesPage;

    @BeforeMethod
    public void loginAndNavigateToFleet() {
        String base = ConfigReader.get("baseUrl", "http://localhost:3001");
        base = base.replaceAll("/+$", ""); // clean trailing slash

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

    @AfterMethod(alwaysRun = true)
    public void dismissModal() {
        if (vehiclesPage != null) {
            vehiclesPage.closeActiveModal();
        }
    }

    @Test(description = "Verify validation error or submission block when submitting empty vehicle form")
    public void testAddVehicleWithEmptyFields() {
        vehiclesPage.clickAddVehicle();

        WebElement regInput = getDriver().findElement(
                By.xpath("//div[contains(@role,'dialog') or contains(@class,'modal') or contains(.,'Add Vehicle')][last()]//*[text()='Registration Number']/following::input[1]")
        );

        Boolean isInputValid = (Boolean) ((JavascriptExecutor) getDriver()).executeScript(
                "return arguments[0].checkValidity();", regInput
        );

        vehiclesPage.fillVehicleDetails("", "", "", "")
                    .attemptSubmitExpectingFailure();

        boolean blocked = (isInputValid != null && !isInputValid) || vehiclesPage.isModalOpen() || vehiclesPage.isValidationErrorDisplayed();

        Assert.assertTrue(blocked, "Form submission was not blocked when required fields were empty.");
    }

    @Test(description = "Verify form handles or rejects invalid registration characters")
    public void testAddVehicleWithInvalidRegistrationFormat() {
        String dynamicInvalidPlate = DataUtils.generateUniqueInvalidRegistrationNumber();

        vehiclesPage.clickAddVehicle()
                    .fillVehicleDetails(dynamicInvalidPlate, "Tata", "Tigor EV", "4")
                    .attemptSubmitExpectingFailure();

        Assert.assertTrue(
                vehiclesPage.isValidationErrorDisplayed() || vehiclesPage.isModalOpen(),
                "Form unexpectedly submitted with an invalid registration format [" + dynamicInvalidPlate + "]."
        );
    }
}