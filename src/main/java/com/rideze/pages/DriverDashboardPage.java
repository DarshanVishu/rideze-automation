package com.rideze.pages;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverDashboardPage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    // Header & Greeting
    private final By greetingHeader = AppiumBy.xpath("//*[contains(@text, 'Hello')]");

    // Status Indicators - STRICT match against visible text
    private final By onlineBadge = AppiumBy.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'GPS ACTIVE')]");
    private final By offlineBadge = AppiumBy.xpath("//*[contains(@text, 'OFFLINE')]");

    // Power / Duty Action text locator
    private final By dutyActionText = AppiumBy.xpath("//*[contains(@text, 'Tap to go')]");

    // Dialog Buttons
    private final By confirmGoOnlineBtn = AppiumBy.xpath("//*[@text='Go Online'] | //*[@content-desc='Go Online']");
    private final By cancelCheckOutBtn = AppiumBy.xpath(
        "//*[@content-desc='Cancel'] | //*[@text='Cancel'] | //android.widget.TextView[@text='Cancel']"
    );

    // Bottom Navigation Bar
    private final By profileNavIcon = AppiumBy.xpath("//android.widget.FrameLayout[contains(@content-desc, 'Profile')] | //android.view.ViewGroup[contains(@content-desc, 'Profile')] | //*[contains(@text, 'Profile')]");
    private final By homeNavIcon = AppiumBy.xpath("//*[contains(@text, 'Home')]");

    public DriverDashboardPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    public boolean isDashboardLoaded() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(greetingHeader)).isDisplayed();
    }

    public boolean isOnline() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(onlineBadge)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDriverOnline() {
        return isOnline();
    }

    public boolean isOffline() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(offlineBadge)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void tapPowerButton() {
        try {
            WebElement actionLabel = wait.until(ExpectedConditions.presenceOfElementLocated(dutyActionText));
            Rectangle rect = actionLabel.getRect();
            int tapX = rect.x + (rect.width / 2);
            int tapY = rect.y - 120;

            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", tapX, "y", tapY));
            System.out.println("-> Dynamically tapped Power Button relative to label at (" + tapX + ", " + tapY + ")");
        } catch (Exception e) {
            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 540, "y", 800));
            System.out.println("-> Tapped Power Button at center coordinate fallback (540, 800)");
        }
    }

    public void handleGoOnlineDialog() {
        try {
            WebElement btn = shortWait.until(ExpectedConditions.elementToBeClickable(confirmGoOnlineBtn));
            btn.click();
            System.out.println("-> Confirmed 'Go Online' dialog");
        } catch (Exception ignored) {}
    }

    public void toggleDutyStatus() {
        tapPowerButton();
        handleGoOnlineDialog();
    }

    public void dismissCheckOutDialogIfPresent() {
        try {
            WebElement cancel = shortWait.until(ExpectedConditions.elementToBeClickable(cancelCheckOutBtn));
            cancel.click();
            System.out.println("-> Dismissed vehicle inspection dialog via locator");
        } catch (Exception e) {
            try {
                WebElement modalHeader = shortWait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.xpath("//*[contains(@text, 'Check-Out Required')]")
                ));
                if (modalHeader.isDisplayed()) {
                    driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 540, "y", 1420));
                    System.out.println("-> Dismissed vehicle inspection dialog via tap (540, 1420)");
                }
            } catch (Exception ignored) {}
        }
    }

    public void handleRuntimePermissions() {
        try {
            By allowBtn = AppiumBy.xpath("//*[contains(@text, 'While using the app') or contains(@text, 'Allow')]");
            shortWait.until(ExpectedConditions.elementToBeClickable(allowBtn)).click();
            System.out.println("-> Granted Location Permission");
        } catch (Exception ignored) {}
    }

    public void openHomeTab() {
        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(homeNavIcon)).click();
        } catch (Exception e) {
            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 135, "y", 2260));
        }
    }

    public DriverProfilePage openProfileTab() {
        try {
            shortWait.until(ExpectedConditions.elementToBeClickable(profileNavIcon)).click();
        } catch (Exception e) {
            driver.executeScript("mobile: clickGesture", ImmutableMap.of("x", 950, "y", 2260));
        }
        return new DriverProfilePage(driver);
    }
}