package com.rideze.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ActiveTripPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By tripStatusIndicator = AppiumBy.xpath(
        "//*[contains(@text, 'Heading to Pickup') or contains(@text, 'Arrived') or contains(@text, 'Start Trip') or contains(@text, 'Navigate')]"
    );
    private final By arrivedAtPickupBtn = AppiumBy.xpath(
        "//*[contains(@text, 'Arrived') or contains(@text, 'ARRIVED')]"
    );
    private final By startTripBtn = AppiumBy.xpath(
        "//*[contains(@text, 'Start Trip') or contains(@text, 'START TRIP')]"
    );
    private final By cancelTripBtn = AppiumBy.xpath(
        "//*[contains(@text, 'Cancel Trip') or contains(@text, 'Cancel')]"
    );

    public ActiveTripPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Verifies that the screen transitioned into an ongoing trip
     */
    public boolean isActiveTripScreenLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(tripStatusIndicator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Confirms arrival at the rider's pickup location
     */
    public void confirmArrivedAtPickup() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(arrivedAtPickupBtn));
        btn.click();
        System.out.println("-> Tapped Arrived at Pickup.");
    }

    /**
     * Taps the Start Trip button
     */
    public void startTrip() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(startTripBtn));
        btn.click();
        System.out.println("-> Tapped Start Trip.");
    }

    /**
     * Cancels the active trip
     */
    public void cancelTrip() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(cancelTripBtn));
        btn.click();
        System.out.println("-> Tapped Cancel Trip.");
    }

    /**
     * Returns underlying AndroidDriver instance
     */
    public AndroidDriver getDriver() {
        return this.driver;
    }
}