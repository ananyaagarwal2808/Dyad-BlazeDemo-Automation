package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class HomePage extends BasePage {
    private final By departureCity = By.id("fromPort");
    private final By destinationCity = By.id("toPort");
    private final By findFlightsButton = By.cssSelector("input[type='submit']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open(String url) {
        driver.get(url);
        waitForVisibility(departureCity);
        return this;
    }

    public HomePage selectDepartureCity(String city) {
        new Select(waitForVisibility(departureCity)).selectByVisibleText(city);
        return this;
    }

    public HomePage selectDestinationCity(String city) {
        new Select(waitForVisibility(destinationCity)).selectByVisibleText(city);
        return this;
    }

    public FlightResultsPage clickFindFlights() {
        click(findFlightsButton);
        return new FlightResultsPage(driver);
    }
}
