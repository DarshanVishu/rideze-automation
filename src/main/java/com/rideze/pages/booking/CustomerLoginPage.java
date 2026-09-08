package com.rideze.pages.booking;

import com.rideze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CustomerLoginPage extends BasePage {

    // Locators targeting the phone input container
    private final By phoneInput = By.xpath("//div[contains(@class,'Mobile') or contains(@class,'phone') or contains(.,'+91')]//input | //input[@type='tel' or @type='number' or contains(@placeholder,'9876543210')]");
    private final By getOtpBtn = By.xpath("//button[contains(.,'Get OTP')]");
    private final By otpContainer = By.xpath("//*[contains(text(),'Enter OTP')]");
    private final By otpInputs = By.xpath("//input[contains(@class,'otp') or @type='text' or @type='tel' or @maxlength='1']");
    private final By verifyBtn = By.xpath("//button[contains(.,'Verify & Continue') or contains(.,'Verify')]");

    public boolean isCustomerLoginPageDisplayed() {
        return isDisplayed(phoneInput) || isDisplayed(getOtpBtn) || getDriver().getCurrentUrl().contains("/auth/login");
    }

    public void loginWithMobileAndOtp(String phone, String otp) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        // 1. Locate and fill phone input
        WebElement phoneField = waitForVisibility(phoneInput);
        phoneField.click();
        phoneField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        phoneField.sendKeys(phone);

        // 2. Click 'Get OTP'
        WebElement getOtpElement = waitForClickable(getOtpBtn);
        js.executeScript("arguments[0].click();", getOtpElement);

        // 3. Wait for OTP view
        waitForVisibility(otpContainer);

        // 4. Fill OTP digits
        List<WebElement> boxes = getDriver().findElements(otpInputs);
        if (boxes.size() >= 6) {
            for (int i = 0; i < 6 && i < otp.length(); i++) {
                WebElement box = boxes.get(i);
                box.click();
                box.sendKeys(String.valueOf(otp.charAt(i)));
                js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", box);
            }
        } else if (!boxes.isEmpty()) {
            boxes.get(0).sendKeys(otp);
        }

        // 5. Submit verification
        WebElement verifyElement = waitForClickable(verifyBtn);
        js.executeScript("arguments[0].click();", verifyElement);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}
    }
}