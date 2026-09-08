package com.rideze.pages.booking;

import com.rideze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CustomerDashboardPage extends BasePage {

    // --- Hub Navigation ---
    private final By heroHeading = By.xpath("//*[contains(text(),'Where are you') or contains(text(),'headed')]");
    private final By airportCard = By.xpath("//*[contains(text(),'Airport Transfer')]");
    private final By outstationCard = By.xpath("//*[contains(text(),'Outstation Travel')]");
    private final By sightseeingCard = By.xpath("//*[contains(text(),'City Sightseeing')]");

    // --- Common Wizard Controls ---
    private final By locationStepHeading = By.xpath("//*[contains(text(),'Where to?')]");
    private final By continueBtn = By.xpath("//button[contains(.,'Continue')]");
    private final By googlePredictionItem = By.xpath("//div[contains(@class,'pac-item') or contains(.,'Mysuru') or contains(@class,'suggestion') or contains(.,'Karnataka')] | //*[contains(text(),'Chamundi') or contains(text(),'Mysuru')]");

    // --- 1. Airport Locators ---
    private final By airportPickupInput = By.xpath("//input[contains(@placeholder,'address or hotel') or contains(@placeholder,'Pickup')]");
    private final By terminalDropdown = By.xpath("//*[contains(text(),'Select terminal') or contains(@class,'select-trigger')]");
    private final By terminalOption = By.xpath("//*[contains(text(),'Terminal 1') or contains(text(),'T1') or contains(@role,'option')]");
    private final By flightInput = By.xpath("//input[contains(@placeholder,'6E 2341') or contains(@placeholder,'Flight')]");

    // --- 2. Outstation Locators ---
    private final By outstationOriginInput = By.xpath("//input[contains(@placeholder,'Origin city') or contains(@placeholder,'Origin')]");
    private final By outstationDestinationInput = By.xpath("//input[contains(@placeholder,'Destination city') or contains(@placeholder,'Destination')]");
    private final By roundTripToggle = By.xpath("//button[contains(.,'Round-trip')]");

    // --- 3. City Sightseeing Locators ---
    private final By packageCard = By.xpath("//*[contains(text(),'Mysuru Heritage') or contains(text(),'Half Day') or contains(@class,'package')]");
    private final By sightseeingPickupInput = By.xpath("//input[contains(@placeholder,'Pickup within Mysuru') or contains(@placeholder,'within Mysuru')]");
    private final By sightseeingDropoffInput = By.xpath("//input[contains(@placeholder,'Drop-off within Mysuru') or contains(@placeholder,'Drop-off')]");

    public boolean isDashboardLoaded() {
        try {
            return isDisplayed(heroHeading) || isDisplayed(airportCard);
        } catch (Exception e) {
            return false;
        }
    }

 // Fills input, triggers Google Places dropdown, and selects the first match
    private void fillGoogleLocation(By locator, String query) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                WebElement input = waitForVisibility(locator);
                js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", input);
                input.click();
                input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                input.sendKeys(query);

                // Short wait for the Google Places menu to render
                Thread.sleep(800);

                List<WebElement> predictions = getDriver().findElements(googlePredictionItem);
                if (!predictions.isEmpty()) {
                    js.executeScript("arguments[0].click();", predictions.get(0));
                } else {
                    input.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                }

                js.executeScript("arguments[0].dispatchEvent(new Event('input', { bubbles: true }));", input);
                js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", input);
                break;
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            } catch (Exception ignored) {
                break;
            }
        }
    }

    // --- Airport Transfer ---
    public CustomerDashboardPage openAirportBooking() {
        WebElement card = waitForClickable(airportCard);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", card);
        waitForVisibility(locationStepHeading);
        return this;
    }

    public CustomerDashboardPage fillAirportTransferDetails(String pickupLocation, String flightNumber) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        fillGoogleLocation(airportPickupInput, pickupLocation);

        try {
            List<WebElement> dropdowns = getDriver().findElements(terminalDropdown);
            if (!dropdowns.isEmpty()) {
                js.executeScript("arguments[0].click();", dropdowns.get(0));
                Thread.sleep(300);
                List<WebElement> options = getDriver().findElements(terminalOption);
                if (!options.isEmpty()) {
                    js.executeScript("arguments[0].click();", options.get(0));
                }
            }
        } catch (Exception ignored) {}

        try {
            List<WebElement> flights = getDriver().findElements(flightInput);
            if (!flights.isEmpty()) {
                flights.get(0).sendKeys(flightNumber);
            }
        } catch (Exception ignored) {}

        return this;
    }

    // --- Outstation Travel ---
    public CustomerDashboardPage openOutstationBooking() {
        WebElement card = waitForClickable(outstationCard);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", card);
        waitForVisibility(locationStepHeading);
        return this;
    }

    public CustomerDashboardPage fillOutstationRoute(String origin, String destination, boolean roundTrip) {
        if (roundTrip) {
            try {
                WebElement toggle = waitForClickable(roundTripToggle);
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", toggle);
            } catch (Exception ignored) {}
        }

        fillGoogleLocation(outstationOriginInput, origin);
        fillGoogleLocation(outstationDestinationInput, destination);

        return this;
    }

    // --- City Sightseeing ---
    public CustomerDashboardPage openCitySightseeing() {
        WebElement card = waitForClickable(sightseeingCard);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", card);
        waitForVisibility(locationStepHeading);
        return this;
    }

    public CustomerDashboardPage fillSightseeingRoute(String pickup, String dropoff) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        try {
            List<WebElement> packages = getDriver().findElements(packageCard);
            if (!packages.isEmpty()) {
                js.executeScript("arguments[0].click();", packages.get(0));
            }
        } catch (Exception ignored) {}

        fillGoogleLocation(sightseeingPickupInput, pickup);
        fillGoogleLocation(sightseeingDropoffInput, dropoff);

        return this;
    }

    // --- Stepper Navigation ---
    public CustomerDashboardPage clickContinue() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        try {
            WebElement btn = waitForVisibility(continueBtn);
            js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", btn);
            js.executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.err.println("Failed to click continue: " + e.getMessage());
        }
        return this;
    }

    public boolean isStepAdvanced() {
        try {
            return getDriver().getPageSource().contains("Schedule")
                || getDriver().getPageSource().contains("Vehicle")
                || !isDisplayed(locationStepHeading);
        } catch (Exception e) {
            return false;
        }
    }
}