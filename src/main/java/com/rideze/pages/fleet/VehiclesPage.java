package com.rideze.pages.fleet;

import com.rideze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class VehiclesPage extends BasePage {

    // Main Page Locators
    private final By fleetHeader = By.xpath("//*[contains(text(),'Fleet Management') or contains(text(),'Fleet')]");
    private final By addVehicleBtn = By.xpath("//button[contains(.,'Add Vehicle')]");
    private final By searchInput = By.xpath("//input[contains(@placeholder,'Search') or @type='search' or contains(@class,'input')]");

    // Modal Scoped Locators
    private final String modalScope = "//div[contains(.,'Add Vehicle') and (contains(@class,'modal') or @role='dialog' or contains(@class,'rounded'))][last()]";
    private final By modalHeader = By.xpath(modalScope + "//*[contains(text(),'Add Vehicle')]");
    private final By registrationNumberInput = By.xpath(modalScope + "//*[text()='Registration Number']/following::input[1]");
    private final By manufacturerInput = By.xpath(modalScope + "//*[text()='Manufacturer']/following::input[1]");
    private final By modelInput = By.xpath(modalScope + "//*[text()='Model']/following::input[1]");
    
    // Category Trigger inside Modal
    private final By categoryTrigger = By.xpath(modalScope + "//*[text()='Category']/following::*[contains(@class,'cursor-pointer') or contains(@class,'border') or self::button][1]");
    private final By capacityInput = By.xpath(modalScope + "//*[contains(text(),'Capacity')]/following::input[1]");
    private final By fileInput = By.xpath(modalScope + "//input[@type='file']");

    // Modal Action Buttons
    private final By modalSubmitBtn = By.xpath(modalScope + "//button[normalize-space()='Add Vehicle']");
    private final By cancelBtn = By.xpath(modalScope + "//button[contains(text(),'Cancel')]");
    private final By modalOverlay = By.xpath("//div[@role='dialog'] | //div[contains(@class,'fixed inset-0')]");
    private final By validationError = By.xpath("//*[contains(@class,'text-red') or contains(@class,'error') or contains(@class,'alert') or contains(text(),'required') or contains(text(),'Invalid')]");

    public boolean isPageLoaded() {
        try {
            return getWait().until(driver -> 
                driver.getCurrentUrl().contains("fleet") || 
                isDisplayed(fleetHeader) || 
                isDisplayed(addVehicleBtn)
            );
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isModalOpen() {
        return isDisplayed(modalHeader);
    }

    public VehiclesPage clickAddVehicle() {
        click(addVehicleBtn);
        waitForVisibility(registrationNumberInput);
        return this;
    }

    public VehiclesPage selectCategory(String categoryName) {
        try {
            WebElement dropdown = waitForVisibility(categoryTrigger);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", dropdown);

            Thread.sleep(300);

            By option = By.xpath("//*[contains(text(),'" + categoryName + "') and not(self::option)]");
            WebElement optElem = waitForClickable(option);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", optElem);

            Thread.sleep(300);
        } catch (Exception ignored) {}
        return this;
    }

    public VehiclesPage uploadVehicleImage(String absoluteFilePath) {
        try {
            ((JavascriptExecutor) getDriver()).executeScript(
                "var el = document.querySelector('input[type=\"file\"]'); if(el) { el.style.display = 'block'; el.style.opacity = '1'; }"
            );
            WebElement upload = getDriver().findElement(fileInput);
            upload.sendKeys(absoluteFilePath);
        } catch (Exception ignored) {}
        return this;
    }

    public VehiclesPage fillVehicleDetails(String regNumber, String manufacturer, String model, String category, String capacity) {
        WebElement reg = waitForVisibility(registrationNumberInput);
        reg.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        reg.sendKeys(regNumber);

        WebElement make = waitForVisibility(manufacturerInput);
        make.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        make.sendKeys(manufacturer);

        WebElement mdl = waitForVisibility(modelInput);
        mdl.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        mdl.sendKeys(model);

        if (capacity != null && !capacity.isEmpty()) {
            try {
                WebElement cap = waitForVisibility(capacityInput);
                cap.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                cap.sendKeys(capacity);
            } catch (Exception e) {
                WebElement cap = getDriver().findElement(capacityInput);
                ((JavascriptExecutor) getDriver()).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    cap, capacity
                );
            }
        }

        if (category != null && !category.isEmpty()) {
            selectCategory(category);
        }

        return this;
    }

    public VehiclesPage fillVehicleDetails(String regNumber, String manufacturer, String model, String capacity) {
        return fillVehicleDetails(regNumber, manufacturer, model, null, capacity);
    }

    public VehiclesPage submitVehicleForm() {
        WebElement submitElement = waitForClickable(modalSubmitBtn);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", submitElement);

        try {
            getWait().until(ExpectedConditions.invisibilityOfElementLocated(modalOverlay));
        } catch (Exception ignored) {}

        try {
            Thread.sleep(1200);
        } catch (InterruptedException ignored) {}

        return this;
    }

    public VehiclesPage attemptSubmitExpectingFailure() {
        try {
            WebElement submitElement = waitForClickable(modalSubmitBtn);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", submitElement);
            Thread.sleep(600);
        } catch (Exception ignored) {}
        return this;
    }

    public void closeActiveModal() {
        try {
            if (isDisplayed(cancelBtn)) {
                click(cancelBtn);
            }
        } catch (Exception ignored) {}
    }

    public void searchVehicle(String query) {
        try {
            getWait().until(ExpectedConditions.invisibilityOfElementLocated(modalHeader));
        } catch (Exception ignored) {}

        WebElement search = waitForClickable(searchInput);
        search.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        search.sendKeys(query);

        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {}
    }

    public boolean isVehicleInList(String vehicleInfo) {
        try {
            By locator = By.xpath("//*[contains(text(),'" + vehicleInfo + "')]");
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidationErrorDisplayed() {
        return isDisplayed(validationError);
    }
}