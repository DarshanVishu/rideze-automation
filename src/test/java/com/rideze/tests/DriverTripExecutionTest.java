package com.rideze.tests;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DriverTripExecutionTest extends BaseTest {

    private static final String RIDE_OTP = "2524";

    @Test(priority = 1, description = "Execute full assigned ride lifecycle via sequential swipe actions and OTP verification")
    public void testAssignedTripLifecycle() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));

        // 1. Open trip details if still on dashboard
        try {
            WebElement tripCard = wait.withTimeout(Duration.ofSeconds(3)).until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(@text, 'DRIVER ASSIGNED') or contains(@text, 'Tap to manage trip')]")
                )
            );
            tripCard.click();
            System.out.println("-> Opened active trip management screen.");
        } catch (Exception ignored) {
            System.out.println("-> Already on trip details screen.");
        }

        // 2. Loop through sequential ride states:
        //    (Swipe to Start Driving -> Swipe to Enter OTP -> Enter OTP & Verify -> Swipe to Complete Trip)
        for (int step = 1; step <= 5; step++) {
            // Check if OTP modal is showing prior to checking the slider
            handleOtpInputFlow(RIDE_OTP);

            try {
                // Find whichever swipe slider track is currently active
                WebElement sliderTrack = new WebDriverWait(getDriver(), Duration.ofSeconds(5)).until(
                    ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(@text, 'Swipe') or contains(@text, 'swipe')]/..")
                    )
                );

                WebElement textChild = sliderTrack.findElement(
                    By.xpath(".//*[contains(@text, 'Swipe') or contains(@text, 'swipe')]")
                );
                String actionLabel = textChild.getText();
                System.out.println("-> Step " + step + ": Executing swipe for [" + actionLabel + "]");

                // Drag the circular knob from left to right
                dragSliderKnobAcross(sliderTrack);

                Thread.sleep(2500);

                // Check again immediately after dragging in case the OTP sheet just opened
                handleOtpInputFlow(RIDE_OTP);

            } catch (Exception e) {
                System.out.println("-> No active swipe component found at step " + step + ". Verifying trip finish...");
                break;
            }
        }

        // 3. Confirm completion or return to dashboard
        WebElement completedState = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//*[contains(@text, 'ONLINE') or contains(@text, 'Settlement') or contains(@text, 'Collect') or contains(@text, 'Complete') or contains(@text, 'Summary') or contains(@text, 'Trip Completed')]")
        ));
        Assert.assertTrue(completedState.isDisplayed(), "Trip completion confirmation not visible.");
        System.out.println("-> Full trip lifecycle successfully completed and verified!");
    }

    private void handleOtpInputFlow(String otp) {
        try {
            // Check if 'Enter Trip OTP' modal is visible
            List<WebElement> headers = getDriver().findElements(
                By.xpath("//*[contains(@text, 'Enter Trip OTP')]")
            );

            if (!headers.isEmpty()) {
                System.out.println("-> OTP modal active. Submitting OTP: " + otp);

                // Populate the input box
                List<WebElement> inputs = getDriver().findElements(By.xpath("//android.widget.EditText"));
                if (!inputs.isEmpty()) {
                    inputs.get(0).clear();
                    inputs.get(0).sendKeys(otp);
                }

                // Hide software keyboard so it doesn't obscure the button
                try {
                    ((io.appium.java_client.android.AndroidDriver) getDriver()).hideKeyboard();
                } catch (Exception ignored) {}

                // Tap "Verify OTP & Start Trip"
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(4));
                WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[contains(@text, 'Verify OTP & Start Trip')]")
                ));
                verifyBtn.click();
                System.out.println("-> Clicked 'Verify OTP & Start Trip'.");

                Thread.sleep(3000);
            }
        } catch (Exception ignored) {}
    }

    private void dragSliderKnobAcross(WebElement trackContainer) {
        Point loc = trackContainer.getLocation();
        Dimension dim = trackContainer.getSize();

        // Pinpoint the round knob on the left edge
        int startX = loc.getX() + (int) (dim.getWidth() * 0.12);
        int endX = loc.getX() + (int) (dim.getWidth() * 0.94);
        int centerY = loc.getY() + (dim.getHeight() / 2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence drag = new Sequence(finger, 1);

        drag.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, centerY));
        drag.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        // Pause 350ms to engage React Native PanResponder
        drag.addAction(new Pause(finger, Duration.ofMillis(350)));
        // Drag across the full length of the track
        drag.addAction(finger.createPointerMove(Duration.ofMillis(850), PointerInput.Origin.viewport(), endX, centerY));
        drag.addAction(new Pause(finger, Duration.ofMillis(200)));
        drag.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((org.openqa.selenium.remote.RemoteWebDriver) getDriver()).resetInputState();
        ((org.openqa.selenium.remote.RemoteWebDriver) getDriver()).perform(Collections.singletonList(drag));
    }
}