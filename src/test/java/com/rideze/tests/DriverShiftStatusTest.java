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
        wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(12));
    }

    @Test(description = "Verify driver duty status toggle triggers vehicle check-out inspection workflow")
    public void testDriverDutyStatusToggle() {
        // Self-healing: If already stranded on Vehicle Check-Out screen, dismiss it first
        try {
            WebDriverWait quickWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(2));
            WebElement staleCloseBtn = quickWait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(@text, 'Vehicle Check-Out')]/preceding-sibling::*[@clickable='true'] | //*[@text='✕' or @text='X' or @content-desc='close' or contains(@content-desc, 'Close')]")
            ));
            staleCloseBtn.click();
            System.out.println("-> Dismissed leftover vehicle check-out screen to return to home.");
            Thread.sleep(1500);
        } catch (Exception ignored) {}

        // 1. Locate current status badge
        WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE')]")
        ));

        String initialStatus = statusElement.getText().trim();
        System.out.println("-> Current driver status detected: " + initialStatus);

        // 2. Locate and tap duty status toggle button
        List<WebElement> clickableViews = DriverManager.getDriver().findElements(
                By.xpath("//*[contains(@text, 'Tap to go')]/preceding-sibling::*[@clickable='true'] | //*[contains(@text, 'ONLINE') or contains(@text, 'OFFLINE')]/..//*[@clickable='true'] | //*[contains(@text, 'Tap to go')]")
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

        // 3. Handle 'Check-Out Required' prompt and tap 'Take Photos'
        WebDriverWait modalWait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(8));
        WebElement takePhotosBtn = modalWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@text='Take Photos' or contains(@text, 'Take Photo')]")
        ));
        takePhotosBtn.click();
        System.out.println("-> Tapped 'Take Photos' on vehicle check-out prompt.");

        // 4. Verify Vehicle Check-Out Screen Header
        WebElement checkoutTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(@text, 'Vehicle Check-Out')]")
        ));
        Assert.assertTrue(checkoutTitle.isDisplayed(), "Vehicle Check-Out screen title not displayed.");
        System.out.println("-> PASS: 'Vehicle Check-Out' screen loaded successfully.");

        // 5. Verify the 4-Angle Inspection Photo Cards
        Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Front View')]")).isDisplayed(), "Front View card missing.");
        Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Rear View')]")).isDisplayed(), "Rear View card missing.");
        Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Left Side')]")).isDisplayed(), "Left Side card missing.");
        Assert.assertTrue(DriverManager.getDriver().findElement(By.xpath("//*[contains(@text, 'Right Side')]")).isDisplayed(), "Right Side card missing.");
        System.out.println("-> PASS: All 4 photo inspection categories verified (Front, Rear, Left, Right).");

        // 6. Enter Odometer Reading (KM)
        WebElement odoInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@text='e.g. 45,210' or contains(@text, '45,210') or @className='android.widget.EditText']")
        ));
        odoInput.click();
        odoInput.clear();
        odoInput.sendKeys("45250");
        System.out.println("-> Entered odometer reading: 45250 KM.");

        // 7. Select Fuel Level
        WebElement fuelOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@text='80%' or @text='100%']")
        ));
        fuelOption.click();
        System.out.println("-> Selected fuel level percentage.");

        // 8. Verify Submit Vehicle Inspection Button is present
        WebElement submitBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(@text, 'Submit Vehicle Inspection')]")
        ));
        Assert.assertTrue(submitBtn.isDisplayed(), "Submit Vehicle Inspection button is not displayed.");
        System.out.println("-> PASS: 'Submit Vehicle Inspection' action verified.");

        // 9. Cleanly close check-out screen to leave emulator ready for subsequent runs
        try {
            WebElement closeBtn = DriverManager.getDriver().findElement(
                    By.xpath("//*[contains(@text, 'Vehicle Check-Out')]/preceding-sibling::*[@clickable='true'] | //*[@text='✕' or @text='X' or @content-desc='close' or contains(@content-desc, 'Close')]")
            );
            closeBtn.click();
            System.out.println("-> Closed vehicle check-out form cleanly.");
        } catch (Exception ignored) {}
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}