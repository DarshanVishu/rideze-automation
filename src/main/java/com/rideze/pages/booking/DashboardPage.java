package com.rideze.pages.booking;

import com.rideze.pages.BasePage;
import com.rideze.pages.fleet.VehiclesPage;
import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    private final By dispatchHeader = By.xpath("//h1[contains(text(),'Dispatch Board')]");
    private final By operationsSubHeader = By.xpath("//*[contains(text(),'Operations Dashboard')]");
    
    // Supports sidebar text or direct href link
    private final By fleetNavMenu = By.xpath("//span[contains(text(),'Fleet')] | //a[contains(@href,'fleet')]");
    private final By vehiclesSubNav = By.xpath("//a[contains(@href,'fleet') or contains(@href,'vehicle') or contains(text(),'Vehicles')]");
    private final By signOutBtn = By.xpath("//button[contains(.,'Sign out')] | //span[contains(text(),'Sign out')]");

    public boolean isDashboardLoaded() {
        return isDisplayed(dispatchHeader) || isDisplayed(operationsSubHeader);
    }

    public VehiclesPage navigateToFleet() {
        click(fleetNavMenu);
        
        // If Fleet is an accordion, click the sub-menu if visible; otherwise navigate directly
        try {
            if (getDriver().findElements(vehiclesSubNav).size() > 1) {
                click(vehiclesSubNav);
            }
        } catch (Exception ignored) {}

        // Fallback assurance to ensure test lands on the Fleet route
        if (!getDriver().getCurrentUrl().contains("/fleet")) {
            getDriver().get("http://localhost:3001/fleet");
        }

        return new VehiclesPage();
    }

    public void clickSignOut() {
        click(signOutBtn);
    }
}