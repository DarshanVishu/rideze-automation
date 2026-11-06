package com.rideze.pages;

import com.rideze.driver.DriverManager;
import com.rideze.pages.booking.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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

    public DashboardPage loginAsAdmin(String email, String password) {
        login(email, password);
        return new DashboardPage();
    }

    public boolean isLoginPageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(signInButton));
            return btn != null && btn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBrandingHeaderDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(brandingHeader));
            return header != null && header.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            WebElement err = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return err != null && err.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}