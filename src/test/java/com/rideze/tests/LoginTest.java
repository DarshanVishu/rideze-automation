package com.rideze.tests;

import com.rideze.config.ConfigReader;
import com.rideze.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTest extends BaseTest {

    @Test(description = "Verify admin login page renders branding and elements")
    public void testLoginPageRender() {
        LoginPage loginPage = new LoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Sign-in button is not displayed.");
        Assert.assertTrue(loginPage.isBrandingHeaderDisplayed(), "Sign-in header/branding text is missing.");
    }

    @Test(description = "Verify successful admin login redirects to the dispatch board")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage();

        String email = ConfigReader.get("adminEmail");
        String password = ConfigReader.get("adminPassword");

        loginPage.login(email, password);

        // Explicit wait to verify URL transition to /dispatch
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        boolean urlChanged = wait.until(ExpectedConditions.urlContains("/dispatch"));
        Assert.assertTrue(urlChanged, "URL did not transition to /dispatch after sign-in.");

        // Assert presence of the Dispatch Board header
        By dispatchHeader = By.xpath("//h1[contains(text(),'Dispatch Board')]");
        Assert.assertTrue(getDriver().findElement(dispatchHeader).isDisplayed(), 
                "Dispatch Board header was not visible on the dashboard.");
    }

    @Test(description = "Verify invalid credentials do not grant access to dispatch")
    public void testInvalidLogin() {
        LoginPage loginPage = new LoginPage();

        loginPage.login("invalid_user@rideze.in", "WrongPassword123!");

        // Assert user is kept on the login page
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                "Application navigated away from the login page despite invalid credentials.");
        Assert.assertFalse(getDriver().getCurrentUrl().contains("/dispatch"), 
                "User unexpectedly reached /dispatch with invalid credentials.");
    }
}