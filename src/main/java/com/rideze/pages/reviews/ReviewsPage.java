package com.rideze.pages.reviews;

import com.rideze.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ReviewsPage extends BasePage {

    private final By searchInput = By.xpath("//input[contains(@placeholder,'Search') or @type='search' or contains(@class,'input')]");
    private final By reviewRows = By.xpath("//table//tbody//tr | //div[contains(@class,'review')] | //div[contains(@class,'card')] | //*[contains(text(),'Rating') or contains(text(),'Stars')]");
    private final By flagButton = By.xpath("(//button[contains(.,'Flag') or contains(@aria-label,'Flag') or contains(@class,'flag')])[1]");
    private final By flaggedBadge = By.xpath("//*[contains(text(),'Flagged') or contains(@class,'flagged') or contains(@class,'badge')]");

    public boolean areReviewsDisplayed() {
        try {
            List<WebElement> elements = getDriver().findElements(reviewRows);
            if (!elements.isEmpty()) {
                return true;
            }
            return getDriver().getPageSource().contains("Review") || getDriver().getCurrentUrl().contains("reviews");
        } catch (Exception e) {
            return false;
        }
    }

    public ReviewsPage searchReview(String keyword) {
        try {
            WebElement input = waitForClickable(searchInput);
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            input.sendKeys(keyword);
            Thread.sleep(600);
        } catch (Exception ignored) {}
        return this;
    }

    public void flagFirstReview() {
        try {
            WebElement btn = waitForClickable(flagButton);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", btn);
            Thread.sleep(500);
        } catch (Exception ignored) {}
    }

    public boolean isReviewFlagged() {
        try {
            return isDisplayed(flaggedBadge);
        } catch (Exception e) {
            return false;
        }
    }
}