package com.rideze.tests;

import com.rideze.driver.DriverFactory;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class DriverShiftStatusTest {

    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        DriverFactory.quitDriver();
        DriverFactory.initDriver("android");
        wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15));
    }

    @Test(description = "Verify driver duty status toggle triggers vehicle check-out inspection workflow or transitions to online")
    public void testDriverDutyStatusToggle() {
        // 0. Pre-check: dismiss any leftover dialogs from previous runs
        try {
            // Check for stale vehicle check-out close button
            List<WebElement> closeBtns = DriverManager.getDriver().findElements(
                    By.xpath("//*[contains(@text, 'Vehicle Check-Out')]/preceding-sibling::*[@clickable='true'] | //*[@text='✕' or @text='X' or @content-desc='close' or contains(@content-desc, 'Close')]")
            );
            if (!closeBtns.isEmpty() && closeBtns.get(0).isDisplayed()) {
                closeBtns.get(0).click();
                System.out.println("-> Dismissed leftover vehicle check-out screen.");
                Thread.sleep(1000);
            }

            // Check for stale "Cancel" or "Go Online?" modal
            List<WebElement> cancelBtns = DriverManager.getDriver().findElements(
                    By.xpath("//*[@text='Cancel' or @content-desc='Cancel']")
            );
            if (!cancelBtns.isEmpty() && cancelBtns.get(0).isDisplayed()) {
                cancelBtns.get(0).click();
                System.out.println("-> Dismissed leftover 'Go Online?' dialog.");
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}

        // 1. Locate current status badge (checking both text and content-desc)
        By statusLocator = By.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE') or contains(@content-desc, 'ONLINE') or contains(@content-desc, 'OFFLINE')]");
        WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(statusLocator));

        String initialStatus = statusElement.getText().trim();
        if (initialStatus.isEmpty()) {
            initialStatus = statusElement.getAttribute("content-desc");
        }
        System.out.println("-> Current driver status detected: " + initialStatus);

        // 2. Locate and tap duty status power toggle button
        List<WebElement> clickableViews = DriverManager.getDriver().findElements(
                By.xpath("//*[contains(@text, 'Tap to start') or contains(@text, 'Tap to go')]/preceding-sibling::*[@clickable='true'] | " +
                         "//*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE')]/..//*[@clickable='true'] | " +
                         "//*[contains(@text, 'Tap to start') or contains(@text, 'Tap to go')] | " +
                         "//*[@className='android.widget.ImageView' and @clickable='true']")
        );

        boolean clicked = false;
        for (WebElement btn : clickableViews) {
            try {
                btn.click();
                clicked = true;
                System.out.println("-> Tapped duty toggle button.");
                break;
            } catch (Exception ignored) {}
        }
        Assert.assertTrue(clicked, "Failed to tap duty status toggle button.");

        // 3. Handle "Go Online?" confirmation dialog if triggered
        try {
            WebDriverWait dialogWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(4));
            WebElement goOnlineConfirmBtn = dialogWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@text='Go Online' and not(contains(@text, '?'))] | //*[@content-desc='Go Online']")
            ));
            goOnlineConfirmBtn.click();
            System.out.println("-> Tapped 'Go Online' on confirmation modal.");
        } catch (Exception ignored) {
            System.out.println("-> No 'Go Online?' modal appeared, proceeding.");
        }

        // 4. Handle Vehicle Inspection check-out prompt or direct ONLINE transition
        try {
            WebDriverWait modalWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(5));
            WebElement takePhotosBtn = modalWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@text='Take Photos' or contains(@text, 'Take Photo')]")
            ));
            takePhotosBtn.click();
            System.out.println("-> Tapped 'Take Photos' on vehicle check-out prompt.");

            // Verify Vehicle Check-Out Screen
            WebElement checkoutTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@text, 'Vehicle Check-Out')]")
            ));
            Assert.assertTrue(checkoutTitle.isDisplayed(), "Vehicle Check-Out screen title not displayed.");
            System.out.println("-> PASS: 'Vehicle Check-Out' screen loaded successfully.");

            // Verify Inspection Photo Cards
            Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Front View')]")).isDisplayed(), "Front View card missing.");
            Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Rear View')]")).isDisplayed(), "Rear View card missing.");
            Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Left Side')]")).isDisplayed(), "Left Side card missing.");
            Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Right Side')]")).isDisplayed(), "Right Side card missing.");
            System.out.println("-> PASS: All 4 photo inspection categories verified (Front, Rear, Left, Right).");

            // Enter Odometer KM
            WebElement odoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@text='e.g. 45,210' or contains(@text, '45,210') or @className='android.widget.EditText']")
            ));
            odoInput.click();
            odoInput.clear();
            odoInput.sendKeys("45250");
            System.out.println("-> Entered odometer reading: 45250 KM.");

            // Select Fuel Level
            WebElement fuelOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@text='80%' or @text='100%']")
            ));
            fuelOption.click();
            System.out.println("-> Selected fuel level percentage.");

            // Verify Submit Button
            WebElement submitBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@text, 'Submit Vehicle Inspection')]")
            ));
            Assert.assertTrue(submitBtn.isDisplayed(), "Submit Vehicle Inspection button is not displayed.");
            System.out.println("-> PASS: 'Submit Vehicle Inspection' action verified.");

            // Cleanly close inspection screen
            try {
                WebElement closeBtn = DriverManager.getDriver().findElement(
                        By.xpath("//*[contains(@text, 'Vehicle Check-Out')]/preceding-sibling::*[@clickable='true'] | //*[@text='✕' or @text='X' or @content-desc='close' or contains(@content-desc, 'Close')]")
                );
                closeBtn.click();
                System.out.println("-> Closed vehicle check-out form cleanly.");
            } catch (Exception ignored) {}

        } catch (Exception e) {
            // If inspection flow was not required, verify ONLINE transition directly
            System.out.println("-> Direct transition without inspection modal. Checking ONLINE status...");
            WebElement onlineBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@text, 'ONLINE') or contains(@content-desc, 'ONLINE')]")
            ));
            Assert.assertTrue(onlineBadge.isDisplayed(), "Driver status did not transition to ONLINE.");
            System.out.println("-> PASS: Driver status verified as ONLINE.");
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}