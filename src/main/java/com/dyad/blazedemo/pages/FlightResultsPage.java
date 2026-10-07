package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.base.BasePage;
import com.dyad.blazedemo.models.Flight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FlightResultsPage extends BasePage {
    private static final Logger LOGGER = LogManager.getLogger(FlightResultsPage.class);

    private final By resultsTable = By.cssSelector("table.table");
    private final By flightRows = By.cssSelector("table.table tbody tr");
    private final By pageHeading = By.cssSelector("h3");

    public FlightResultsPage(WebDriver driver) {
        super(driver);
        waitForVisibility(resultsTable);
    }

    public boolean areFlightsDisplayed() {
        return !driver.findElements(flightRows).isEmpty();
    }

    public List<Flight> getAllFlights() {
        List<WebElement> rows = driver.findElements(flightRows);
        List<Flight> flights = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            WebElement row = rows.get(i);
            List<WebElement> cells = row.findElements(By.tagName("td"));

            if (cells.size() < 6) {
                LOGGER.warn("Skipping unexpected flight row {} with {} cells", i + 1, cells.size());
                continue;
            }

            String flightNumber = cells.get(1).getText().trim();
            String airline = cells.get(2).getText().trim();
            String departureTime = cells.get(3).getText().trim();
            String arrivalTime = cells.get(4).getText().trim();
            BigDecimal price = parsePrice(cells.get(5).getText());

            flights.add(new Flight(
                    airline,
                    flightNumber,
                    departureTime,
                    arrivalTime,
                    price,
                    i));
        }

        return flights;
    }

    public Flight getCheapestFlight() {
        return getAllFlights().stream()
                .min(Comparator.comparing(Flight::price))
                .orElseThrow(() -> new AssertionError("No valid flights were available"));
    }

    public void logFlightDetails(List<Flight> flights) {
        LOGGER.info("Available flights: {}", flights.size());
        for (Flight flight : flights) {
            LOGGER.info("Flight | Airline: {} | Flight Number: {} | Departure: {} | Arrival: {} | Price: ${}",
                    flight.airline(), flight.flightNumber(), flight.departureTime(),
                    flight.arrivalTime(), flight.price());
        }
    }

    public PurchasePage selectFlight(Flight flight) {
        List<WebElement> rows = driver.findElements(flightRows);
        if (flight.rowIndex() >= rows.size()) {
            throw new IllegalStateException("Flight row is no longer available: " + flight.rowIndex());
        }

        WebElement row = rows.get(flight.rowIndex());
        WebElement chooseButton = row.findElement(
                By.cssSelector("input[type='submit'][value='Choose This Flight']"));
        wait.until(driver -> chooseButton.isDisplayed() && chooseButton.isEnabled());
        chooseButton.click();
        return new PurchasePage(driver);
    }

    public String getPageHeading() {
        return waitForVisibility(pageHeading).getText().trim();
    }

    private BigDecimal parsePrice(String rawPrice) {
        String normalized = rawPrice.replaceAll("[^0-9.]", "");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Unable to parse flight price: " + rawPrice);
        }
        return new BigDecimal(normalized);
    }
}
