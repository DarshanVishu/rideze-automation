package com.rideze.pages.booking;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.rideze.pages.BasePage;

public class CustomerDashboardPage extends BasePage {

    // --- Hub Navigation (matching across nested text nodes) ---
    private final By heroHeading = By.xpath("//*[contains(.,'Where are you') or contains(.,'headed')]");
    private final By airportCard = By.xpath("//*[contains(.,'Airport Transfer') or contains(.,'Airport')][not(self::script)][not(self::style)]");
    private final By outstationCard = By.xpath("//*[contains(.,'Outstation')][not(self::script)][not(self::style)]");
    private final By sightseeingCard = By.xpath("//*[contains(.,'Sightseeing')][not(self::script)][not(self::style)]");

    // --- Common Wizard Controls ---
    private final By locationStepHeading = By.xpath("//*[contains(.,'Where to?') or contains(.,'Pickup') or contains(.,'Select Route') or contains(.,'Package')]");
    private final By continueBtn = By.xpath("//button[contains(.,'Continue') or contains(.,'Next')]");
    private final By googlePredictionItem = By.xpath("//div[contains(@class,'pac-item') or contains(.,'Mysuru') or contains(@class,'suggestion') or contains(.,'Karnataka')] | //*[contains(text(),'Chamundi') or contains(text(),'Mysuru')]");

    // --- 1. Airport Locators ---
    private final By airportPickupInput = By.xpath("//input[contains(@placeholder,'address') or contains(@placeholder,'hotel') or contains(@placeholder,'Pickup') or @type='text']");
    private final By terminalDropdown = By.xpath("//*[contains(text(),'Select terminal') or contains(@class,'select-trigger')]");
    private final By terminalOption = By.xpath("//*[contains(text(),'Terminal 1') or contains(text(),'T1') or contains(@role,'option')]");
    private final By flightInput = By.xpath("//input[contains(@placeholder,'6E') or contains(@placeholder,'Flight')]");

    // --- 2. Outstation Locators ---
    private final By outstationOriginInput = By.xpath("//input[contains(@placeholder,'Origin') or contains(@name,'origin')]");
    private final By outstationDestinationInput = By.xpath("//input[contains(@placeholder,'Destination') or contains(@name,'destination')]");
    private final By roundTripToggle = By.xpath("//button[contains(.,'Round-trip')]");

    // --- 3. City Sightseeing Locators ---
    private final By packageCard = By.xpath("//*[contains(.,'Mysuru Heritage') or contains(.,'Half Day') or contains(@class,'package')]");
    private final By sightseeingPickupInput = By.xpath("//input[contains(@placeholder,'Pickup') or contains(@placeholder,'within Mysuru')]");
    private final By sightseeingDropoffInput = By.xpath("//input[contains(@placeholder,'Drop-off') or contains(@placeholder,'within Mysuru')]");

    public boolean isDashboardLoaded() {
        try {
            return isDisplayed(heroHeading) || isDisplayed(airportCard);
        } catch (Exception e) {
            return false;
        }
    }

    private void clickCardResiliently(By cardLocator) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        try {
            WebElement card = waitForClickable(cardLocator);
            js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", card);
            js.executeScript("arguments[0].click();", card);
        } catch (Exception e) {
            List<WebElement> elements = getDriver().findElements(cardLocator);
            if (!elements.isEmpty()) {
                js.executeScript("arguments[0].scrollIntoView({behavior: 'instant', block: 'center'});", elements.get(0));
                js.executeScript("arguments[0].click();", elements.get(0));
            }
        }
        
        // Soft wait for the input or heading to render rather than hard timing out
        try {
            waitForVisibility(locationStepHeading);
        } catch (Exception ignored) {}
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
        clickCardResiliently(airportCard);
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
        clickCardResiliently(outstationCard);
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
        clickCardResiliently(sightseeingCard);
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
            Thread.sleep(800);
        } catch (Exception e) {
            System.err.println("Failed to click continue: " + e.getMessage());
        }
        return this;
    }

    public boolean isStepAdvanced() {
        try {
            String source = getDriver().getPageSource();
            return source.contains("Schedule")
                || source.contains("Vehicle")
                || source.contains("Fare")
                || source.contains("Review")
                || !isDisplayed(locationStepHeading);
        } catch (Exception e) {
            return false;
        }
    }
}