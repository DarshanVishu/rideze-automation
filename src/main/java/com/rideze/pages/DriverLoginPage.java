package com.rideze.pages;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverLoginPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    // Locators
    private final By phoneInputField = AppiumBy.xpath("//android.widget.EditText");
    private final By getOtpButton = AppiumBy.xpath("//*[contains(@text, 'Get OTP') or contains(@text, 'GET OTP') or contains(@text, 'Continue') or contains(@text, 'Send OTP')]");
    private final By verifyOtpButton = AppiumBy.xpath("//*[contains(@text, 'Verify & Continue') or contains(@text, 'Verify') or contains(@text, 'VERIFY')]");
    private final By dashboardMarker = AppiumBy.xpath("//*[contains(@text, 'Hello') or contains(@text, 'OFFLINE') or contains(@text, 'ONLINE')]");
    private final By otpScreenMarker = AppiumBy.xpath("//*[contains(@text, 'Enter OTP')]");

    public DriverLoginPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    public boolean isAlreadyLoggedIn() {
        try {
            By dashboardMarker = AppiumBy.xpath(
                "//*[contains(@text, 'Hello')] | " +
                "//*[contains(@text, 'ONLINE')] | " +
                "//*[contains(@text, 'OFFLINE')] | " +
                "//*[contains(@text, 'Home')] | " +
                "//android.widget.FrameLayout[contains(@content-desc, 'Profile')]"
            );
            return shortWait.until(ExpectedConditions.presenceOfElementLocated(dashboardMarker)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void enterMobileNumber(String phoneNumber) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(phoneInputField));
        input.click();
        input.clear();
        input.sendKeys(phoneNumber);
        System.out.println("-> Entered phone number: " + phoneNumber);
    }

    public void tapGetOtpButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(getOtpButton));
            button.click();
            System.out.println("-> Tapped 'Get OTP' button via locator.");
        } catch (Exception e) {
            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 540, "y", 1200));
            System.out.println("-> Tapped 'Get OTP' via coordinate fallback.");
        }
    }

    /**
     * Focuses the first PIN box using coordinates verified from Appium Inspector: (191, 1277)
     */
    public void focusFirstPinBox() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(otpScreenMarker));
        driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 191, "y", 1277));
        System.out.println("-> Focused on first PIN box at (191, 1277).");
    }

    /**
     * Injects digits via native Appium KeyEvents (bypasses Windows PATH adb requirement)
     */
    public void injectOtpViaAdb(String otp) {
        for (char ch : otp.toCharArray()) {
            AndroidKey key = switch (ch) {
                case '0' -> AndroidKey.DIGIT_0;
                case '1' -> AndroidKey.DIGIT_1;
                case '2' -> AndroidKey.DIGIT_2;
                case '3' -> AndroidKey.DIGIT_3;
                case '4' -> AndroidKey.DIGIT_4;
                case '5' -> AndroidKey.DIGIT_5;
                case '6' -> AndroidKey.DIGIT_6;
                case '7' -> AndroidKey.DIGIT_7;
                case '8' -> AndroidKey.DIGIT_8;
                case '9' -> AndroidKey.DIGIT_9;
                default -> null;
            };

            if (key != null) {
                driver.pressKey(new KeyEvent(key));
                try {
                    Thread.sleep(150);
                } catch (InterruptedException ignored) {}
            }
        }
        System.out.println("-> Injected OTP digits via native driver KeyEvents: " + otp);
    }

    public void tapVerifyOtpButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(verifyOtpButton));
            button.click();
            System.out.println("-> Tapped 'Verify & Continue' button via locator.");
        } catch (Exception e) {
            // Coordinate fallback for Verify & Continue button
            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 540, "y", 1570));
            System.out.println("-> Tapped 'Verify & Continue' via coordinate fallback.");
        }
    }

    public void enterPhoneNumber(String phoneNumber) {
        enterMobileNumber(phoneNumber);
    }

    public void clickGetOtp() {
        tapGetOtpButton();
    }

    public void clickVerifyOtp() {
        tapVerifyOtpButton();
    }

    public void loginWithOtp(String phoneNumber) {
        if (isAlreadyLoggedIn()) {
            System.out.println("-> Driver session already active on Dashboard. Bypassing login.");
            return;
        }

        enterMobileNumber(phoneNumber);
        tapGetOtpButton();

        focusFirstPinBox();
        injectOtpViaAdb("123456");
        tapVerifyOtpButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardMarker));
        System.out.println("-> Successfully authenticated and navigated to Dashboard.");
    }
}