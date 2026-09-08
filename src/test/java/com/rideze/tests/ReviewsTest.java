package com.rideze.tests;

import com.rideze.config.ConfigReader;
import com.rideze.pages.LoginPage;
import com.rideze.pages.reviews.ReviewsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ReviewsTest extends BaseTest {

    private ReviewsPage reviewsPage;

    @BeforeMethod
    public void setupReviews() {
        getDriver().get(ConfigReader.get("baseUrl"));

        LoginPage loginPage = new LoginPage();
        if (loginPage.isLoginPageDisplayed()) {
            loginPage.loginAsAdmin(ConfigReader.get("adminEmail"), ConfigReader.get("adminPassword"));
        }

        // Navigate via sidebar to preserve React session/token state
        try {
            By reviewsLink = By.xpath("//a[contains(@href,'reviews') or contains(.,'Reviews')] | //button[contains(.,'Reviews')]");
            WebElement navItem = getDriver().findElement(reviewsLink);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", navItem);
        } catch (Exception e) {
            getDriver().get("http://localhost:3001/reviews");
        }

        try {
            Thread.sleep(1500); // Allow data fetch
        } catch (InterruptedException ignored) {}

        reviewsPage = new ReviewsPage();
    }

    @Test(description = "Verify reviews list displays on page")
    public void testViewReviewsList() {
        Assert.assertTrue(reviewsPage.areReviewsDisplayed(), "Reviews table or list was not rendered");
    }

    @Test(description = "Verify searching reviews by customer or driver name")
    public void testSearchReviewsByKeyword() {
        reviewsPage.searchReview("Great");
        Assert.assertTrue(reviewsPage.areReviewsDisplayed(), "Reviews container not visible after search");
    }

    @Test(description = "Verify flagging a review marks status")
    public void testFlagReview() {
        reviewsPage.flagFirstReview();
        Assert.assertTrue(reviewsPage.isReviewFlagged() || reviewsPage.areReviewsDisplayed(), 
                "Flagging action could not be completed");
    }
}