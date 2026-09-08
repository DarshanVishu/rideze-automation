package com.rideze.pages;

import com.rideze.pages.booking.DashboardPage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private final By emailInput = By.xpath("//input[@type='email' or contains(@placeholder, 'rideze.in') or @id='email' or @name='email']");
    private final By passwordInput = By.xpath("//input[@type='password' or @id='password' or @name='password']");
    private final By signInButton = By.xpath("//button[contains(.,'Sign in')]");
    private final By brandingHeader = By.xpath("//h2[contains(text(),'Sign in')] | //*[contains(text(),'Manage your fleet')]");
    private final By errorMessage = By.xpath("//*[contains(@class, 'error') or contains(@class, 'alert') or contains(text(), 'Invalid')]");

    public LoginPage enterEmail(String email) {
        enterText(emailInput, email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        enterText(passwordInput, password);
        return this;
    }

    public void clickSignIn() {
        click(signInButton);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
    }

    // Returns DashboardPage to allow method chaining in test classes
    public DashboardPage loginAsAdmin(String email, String password) {
        login(email, password);
        return new DashboardPage();
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(signInButton);
    }

    public boolean isBrandingHeaderDisplayed() {
        return isDisplayed(brandingHeader);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
}