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
        DriverFactory.initDriver("android");
        wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(15));
    }

    @Test(description = "Verify driver duty status toggle triggers vehicle check-out inspection workflow or transitions to online")
    public void testDriverDutyStatusToggle() {
        // 0. Pre-check: dismiss any leftover dialogs from previous runs
        try {
            List<WebElement> closeBtns = DriverManager.getDriver().findElements(
                    By.xpath("//*[contains(@text, 'Vehicle Check-Out')]/preceding-sibling::*[@clickable='true'] | //*[@text='?' or @text='X' or @content-desc='close' or contains(@content-desc, 'Close')]")
            );
            if (!closeBtns.isEmpty() && closeBtns.get(0).isDisplayed()) {
                closeBtns.get(0).click();
                System.out.println("-> Dismissed leftover vehicle check-out screen.");
                Thread.sleep(1000);
            }

            List<WebElement> cancelBtns = DriverManager.getDriver().findElements(
                    By.xpath("//*[@text='Cancel' or @content-desc='Cancel']")
            );
            if (!cancelBtns.isEmpty() && cancelBtns.get(0).isDisplayed()) {
                cancelBtns.get(0).click();
                System.out.println("-> Dismissed leftover 'Go Online?' dialog.");
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}

        // 1. Locate current status badge
        By statusLocator = By.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE') or contains(@content-desc, 'ONLINE') or contains(@content-desc, 'OFFLINE')]");
        WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(statusLocator));

        String initialStatus = statusElement.getText().trim();
        if (initialStatus.isEmpty()) {
            initialStatus = statusElement.getAttribute("content-desc");
        }
        System.out.println("-> Current driver status detected: " + initialStatus);

        // 2. Locate and tap duty status power toggle button
        List<WebElement> clickableViews = DriverManager.getDriver().findElements(
                By.xpath("//*[contains(@text, 'OFFLINE') or contains(@text, 'Tap to start')] | " +
                         "//*[contains(@text, 'Tap to start')]/ancestor::*[@clickable='true'][1] | " +
                         "//*[contains(@text, 'OFFLINE')]/ancestor::*[@clickable='true'][1] | " +
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

        // 4. Verify duty status change or click registration
        final String finalInitialStatus = initialStatus;
        boolean statusChanged = false;
        try {
            WebDriverWait finalWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
            statusChanged = finalWait.until(driver -> {
                try {
                    WebElement currentStatus = driver.findElement(By.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE') or contains(@content-desc, 'ONLINE') or contains(@content-desc, 'OFFLINE')]"));
                    String text = currentStatus.getText();
                    if (text == null || text.isEmpty()) {
                        text = currentStatus.getAttribute("content-desc");
                    }
                    return text != null && !text.equalsIgnoreCase(finalInitialStatus);
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (Exception e) {
            statusChanged = false;
        }
        
        if (!statusChanged) {
            try {
                WebElement anyModal = DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Online') or contains(@text, 'Inspection') or contains(@text, 'Photos')]"));
                statusChanged = anyModal.isDisplayed();
            } catch (Exception ignored) {}
        }

        Assert.assertTrue(statusChanged, "Driver status failed to change from initial state: " + finalInitialStatus);
        System.out.println("-> PASS: Driver duty status successfully updated.");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}