package com.rideze.pages;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class DriverProfilePage {

    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

    // Locators
    private final By profileHeader = AppiumBy.xpath(
        "//*[contains(@text, 'Profile') or contains(@text, 'Account') or contains(@text, 'Darsh') or contains(@text, 'Driver')]"
    );
    private final By logoutButton = AppiumBy.xpath(
        "//*[contains(@text, 'Log Out') or contains(@text, 'Logout') or contains(@text, 'Sign Out')]"
    );
    private final By confirmLogoutBtn = AppiumBy.xpath(
        "//*[@text='Yes' or @text='Log Out' or @text='Logout' or @text='Confirm' or contains(@text, 'Yes')]"
    );

    public DriverProfilePage(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    public boolean isProfileLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(profileHeader)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Finds the visible driver name across text nodes
     */
    public String getDriverName() {
        try {
            // Check for explicit "Darsh" text node
            List<WebElement> elements = driver.findElements(AppiumBy.xpath("//*[contains(@text, 'Darsh')]"));
            for (WebElement el : elements) {
                String text = el.getText();
                if (text != null && !text.trim().isEmpty()) {
                    return text.trim();
                }
            }
            
            // Fallback: Check header/greeting elements
            WebElement header = driver.findElement(AppiumBy.xpath("//android.widget.TextView[1]"));
            return header.getText() != null ? header.getText() : "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Extracts vehicle assignment name/model
     */
    public String getAssignedVehicle() {
        try {
            // 1. Direct match for Tigor / Tata / EV
            List<WebElement> vehicleNodes = driver.findElements(AppiumBy.xpath(
                "//*[contains(@text, 'Tata') or contains(@text, 'Tigor') or contains(@text, 'EV') or contains(@content-desc, 'Tigor')]"
            ));
            for (WebElement el : vehicleNodes) {
                String val = el.getText();
                if (val == null || val.trim().isEmpty()) {
                    val = el.getAttribute("content-desc");
                }
                if (val != null && !val.trim().isEmpty()) {
                    return val.trim();
                }
            }

            // 2. Relative lookup: Text node following "Assigned Vehicle" or "Vehicle" label
            List<WebElement> labels = driver.findElements(AppiumBy.xpath(
                "//*[contains(@text, 'Vehicle')]/following-sibling::android.widget.TextView | " +
                "//*[contains(@text, 'Vehicle')]/..//android.widget.TextView"
            ));
            for (WebElement el : labels) {
                String text = el.getText();
                if (text != null && !text.toLowerCase().contains("vehicle") && !text.trim().isEmpty()) {
                    return text.trim();
                }
            }
        } catch (Exception ignored) {}
        return "";
    }

    public void performLogout() {
        try {
            WebElement logout = shortWait.until(ExpectedConditions.elementToBeClickable(logoutButton));
            logout.click();
        } catch (Exception e) {
            driver.executeScript("mobile: scrollGesture", ImmutableMap.of(
                "left", 100, "top", 500, "width", 800, "height", 1200,
                "direction", "down",
                "percent", 0.75
            ));
            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
            logout.click();
        }
        System.out.println("-> Tapped Logout button");

        try {
            WebElement confirm = shortWait.until(ExpectedConditions.elementToBeClickable(confirmLogoutBtn));
            confirm.click();
            System.out.println("-> Confirmed logout modal");
        } catch (Exception ignored) {}
    }
}