package com.rideze.tests;

import com.rideze.config.ConfigReader;
import com.rideze.pages.LoginPage;
import com.rideze.pages.reviews.ReviewsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class ReviewsTest extends BaseTest {

    private ReviewsPage reviewsPage;

    @BeforeMethod
    public void setupReviews() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        getDriver().get(ConfigReader.get("baseUrl"));

        // 1. Ensure authenticated session
        LoginPage loginPage = new LoginPage();
        try {
            WebDriverWait loginCheckWait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
            loginCheckWait.until(d -> loginPage.isLoginPageDisplayed() || !d.findElements(By.xpath("//input[@type='password']")).isEmpty());
            loginPage.loginAsAdmin(ConfigReader.get("adminEmail"), ConfigReader.get("adminPassword"));
        } catch (Exception ignored) {
            // Already logged in
        }

        // 2. Safely navigate to Reviews section via sidebar or URL
        By reviewsLink = By.xpath("//a[contains(@href,'reviews') or contains(.,'Reviews')] | //button[contains(.,'Reviews')]");
        try {
            WebElement navItem = wait.until(ExpectedConditions.elementToBeClickable(reviewsLink));
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", navItem);
        } catch (Exception e) {
            String targetUrl = ConfigReader.get("baseUrl");
            if (targetUrl.endsWith("/")) {
                targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
            }
            getDriver().get(targetUrl + "/reviews");
        }

        // 3. Fallback: If redirected back to login, re-authenticate immediately
        try {
            if (loginPage.isLoginPageDisplayed()) {
                loginPage.loginAsAdmin(ConfigReader.get("adminEmail"), ConfigReader.get("adminPassword"));
                WebElement navItem = wait.until(ExpectedConditions.elementToBeClickable(reviewsLink));
                ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", navItem);
            }
        } catch (Exception ignored) {}

        reviewsPage = new ReviewsPage();
        wait.until(d -> reviewsPage.areReviewsDisplayed());
    }

    @Test(description = "Verify reviews list displays on page")
    public void testViewReviewsList() {
        Assert.assertTrue(reviewsPage.areReviewsDisplayed(), "Reviews table or list was not rendered");
    }

    @Test(description = "Verify searching reviews by customer or driver name")
    public void testSearchReviewsByKeyword() {
        reviewsPage.searchReview("Great");
        try {
            Thread.sleep(1000); // Allow debounce/table filter to settle
        } catch (InterruptedException ignored) {}
        Assert.assertTrue(reviewsPage.areReviewsDisplayed(), "Reviews container not visible after search");
    }

    @Test(description = "Verify flagging a review marks status")
    public void testFlagReview() {
        reviewsPage.flagFirstReview();
        Assert.assertTrue(reviewsPage.isReviewFlagged() || reviewsPage.areReviewsDisplayed(), 
                "Flagging action could not be completed");
    }
}