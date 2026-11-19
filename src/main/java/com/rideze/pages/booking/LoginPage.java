package com.rideze.pages.booking;

import com.rideze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private final By emailOrPhoneInput = By.xpath(
            "//input[@type='email' or @type='tel' or contains(@placeholder,'email') or contains(@placeholder,'Email') or contains(@placeholder,'9876543210')]"
    );
    private final By passwordInput = By.xpath("//input[@type='password']");
    private final By submitButton = By.xpath("//button[@type='submit' or contains(.,'Login') or contains(.,'Sign In')]");

    public boolean isLoginPageDisplayed() {
        return isDisplayed(submitButton) || getDriver().getCurrentUrl().contains("/login");
    }

    public void loginWithCredentials(String identifier, String password) {
        WebElement idField = waitForVisibility(emailOrPhoneInput);
        idField.clear();
        idField.sendKeys(identifier);

        WebElement passField = waitForVisibility(passwordInput);
        passField.clear();
        passField.sendKeys(password);

        waitForClickable(submitButton).click();
    }

    public void loginWithOtp(String phone, String otp) {
        new CustomerLoginPage().loginWithMobileAndOtp(phone, otp);
    }
}
