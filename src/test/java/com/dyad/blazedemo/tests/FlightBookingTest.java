package com.dyad.blazedemo.tests;

import com.aventstack.extentreports.ExtentTest;
import com.dyad.blazedemo.driver.DriverFactory;
import com.dyad.blazedemo.models.Flight;
import com.dyad.blazedemo.pages.ConfirmationPage;
import com.dyad.blazedemo.pages.FlightResultsPage;
import com.dyad.blazedemo.pages.HomePage;
import com.dyad.blazedemo.pages.PurchasePage;
import com.dyad.blazedemo.reporting.ExtentTestManager;
import com.dyad.blazedemo.utils.ConfigReader;
import com.dyad.blazedemo.utils.TestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

@Listeners(com.dyad.blazedemo.reporting.TestListener.class)
public class FlightBookingTest {
    private static final Logger LOGGER = LogManager.getLogger(FlightBookingTest.class);
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initializeDriver();
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test(description = "Search Boston to London, select the cheapest flight and validate booking confirmation")
    public void verifyCheapestFlightBooking() {
        ExtentTest report = ExtentTestManager.getTest();

        HomePage homePage = new HomePage(driver)
                .open(ConfigReader.get("baseUrl"));
        report.info("Opened BlazeDemo: " + ConfigReader.get("baseUrl"));

        FlightResultsPage resultsPage = homePage
                .selectDepartureCity(TestData.DEPARTURE_CITY)
                .selectDestinationCity(TestData.DESTINATION_CITY)
                .clickFindFlights();
        report.info("Searched flights from " + TestData.DEPARTURE_CITY + " to " + TestData.DESTINATION_CITY);

        List<Flight> flights = resultsPage.getAllFlights();
        Assert.assertFalse(flights.isEmpty(), "Validation 1 failed: minimum one flight must be available");
        report.pass("Validation 1: " + flights.size() + " flight(s) are available");

        resultsPage.logFlightDetails(flights);
        for (Flight flight : flights) {
            report.info(String.format("Airline: %s | Flight: %s | Departure: %s | Arrival: %s | Price: $%s",
                    flight.airline(), flight.flightNumber(), flight.departureTime(), flight.arrivalTime(), flight.price()));
        }

        Flight cheapestFlight = resultsPage.getCheapestFlight();
        BigDecimal expectedMinimumPrice = flights.stream()
                .map(Flight::price)
                .min(BigDecimal::compareTo)
                .orElseThrow(() -> new AssertionError("No flight price was available"));

        Assert.assertEquals(cheapestFlight.price(), expectedMinimumPrice,
                "Validation 2 failed: cheapest flight was not identified correctly");
        report.pass(String.format("Validation 2: cheapest flight is %s (%s) at $%s",
                cheapestFlight.airline(), cheapestFlight.flightNumber(), cheapestFlight.price()));

        BigDecimal selectedFlightPrice = cheapestFlight.price();
        PurchasePage purchasePage = resultsPage.selectFlight(cheapestFlight);
        report.info("Selected cheapest flight at $" + selectedFlightPrice);

        purchasePage.fillPassengerDetails();
        report.info("Passenger and dummy payment details entered");

        ConfirmationPage confirmationPage = purchasePage.clickPurchaseFlight();
        Assert.assertTrue(confirmationPage.isConfirmationDisplayed(),
                "Confirmation page was not displayed after purchase");

        String bookingId = confirmationPage.getBookingId();
        BigDecimal bookingAmount = confirmationPage.getBookingAmount();
        String maskedCardNumber = confirmationPage.getMaskedCardNumber();

        report.info("Booking ID: " + bookingId);
        report.info("Booking Amount: $" + bookingAmount);
        report.info("Card Number: " + maskCardNumber(maskedCardNumber));

        Assert.assertFalse(bookingId.isBlank(),
                "Validation 3 failed: Booking ID must be generated");
        report.pass("Validation 3: Booking ID generated successfully");

        Assert.assertEquals(bookingAmount, selectedFlightPrice,
                "Validation 4 failed: booking amount must match selected flight price");
        report.pass("Validation 4: booking amount matches selected flight price: $" + selectedFlightPrice);

        LOGGER.info("Booking completed successfully. Booking ID={}, Amount=${}, Card={}",
                bookingId, bookingAmount, maskCardNumber(maskedCardNumber));
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            return "N/A";
        }
        String digits = cardNumber.replaceAll("[^0-9]", "");
        if (digits.length() <= 4) {
            return "****" + digits;
        }
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }
}
