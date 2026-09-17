package com.rideze.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IncomingRideModal {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait longWait;

    // Locators
    private final By acceptButton = AppiumBy.xpath(
        "//*[contains(@text, 'ACCEPT') or contains(@text, 'Accept') or contains(@text, 'Accept Ride')]"
    );
    private final By rejectButton = AppiumBy.xpath(
        "//*[contains(@text, 'REJECT') or contains(@text, 'Decline') or contains(@text, 'Reject')]"
    );
    private final By fareAmount = AppiumBy.xpath(
        "//*[contains(@text, '₹') or contains(@text, 'INR')]"
    );
    private final By pickupAddress = AppiumBy.xpath(
        "//*[contains(@content-desc, 'Pickup') or contains(@text, 'Pickup')]/following-sibling::android.widget.TextView"
    );

    public IncomingRideModal(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    /**
     * Waits up to 30 seconds for an incoming trip offer to display on screen
     */
    public boolean isRideOfferVisible() {
        try {
            return longWait.until(ExpectedConditions.visibilityOfElementLocated(acceptButton)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the fare presented on the offer sheet
     */
    public String getEstimatedFare() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(fareAmount)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Retrieves the pickup address text displayed on the incoming ride offer modal
     */
    public String getPickupAddress() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pickupAddress)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Taps the Accept button to confirm the trip assignment
     */
    public ActiveTripPage acceptRide() {
        WebElement accept = wait.until(ExpectedConditions.elementToBeClickable(acceptButton));
        accept.click();
        System.out.println("-> Tapped ACCEPT on incoming ride modal.");
        return new ActiveTripPage(driver);
    }

    /**
     * Rejects or ignores the incoming booking
     */
    public void rejectRide() {
        try {
            WebElement reject = wait.until(ExpectedConditions.elementToBeClickable(rejectButton));
            reject.click();
            System.out.println("-> Tapped REJECT on incoming ride modal.");
        } catch (Exception ignored) {}
    }
}