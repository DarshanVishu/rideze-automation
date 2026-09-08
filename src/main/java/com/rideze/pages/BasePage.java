package com.rideze.pages;

import com.rideze.config.ConfigFactory;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(ConfigFactory.getExplicitWait()));
    }

    protected WebElement waitForVisibility(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        waitForClickable(locator).click();
    }

    public void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    // Alias to resolve enterText calls in VehiclesPage, ReviewsPage, etc.
    public void enterText(By locator, String text) {
        type(locator, text);
    }

    public boolean isDisplayed(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    public void clear(By locator) {
        waitForVisibility(locator).clear();
    }
}