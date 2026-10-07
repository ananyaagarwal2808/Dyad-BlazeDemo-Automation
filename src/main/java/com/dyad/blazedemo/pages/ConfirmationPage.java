package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

public class ConfirmationPage extends BasePage {
    private final By confirmationHeading = By.cssSelector("h1");
    private final By confirmationTable = By.cssSelector("table.table");

    public ConfirmationPage(WebDriver driver) {
        super(driver);
        waitForVisibility(confirmationHeading);
    }

    public boolean isConfirmationDisplayed() {
        return isDisplayed(confirmationHeading)
                && waitForVisibility(confirmationHeading).getText().toLowerCase().contains("thank you");
    }

    public String getBookingId() {
        return getValueFromConfirmationTable("Id");
    }

    public BigDecimal getBookingAmount() {
        String rawAmount = getValueFromConfirmationTable("Amount");
        String normalized = rawAmount.replaceAll("[^0-9.]", "");
        if (normalized.isBlank()) {
            throw new IllegalStateException("Unable to parse booking amount: " + rawAmount);
        }
        return new BigDecimal(normalized);
    }

    public String getMaskedCardNumber() {
        return getValueFromConfirmationTable("Card Number");
    }

    public String getConfirmationMessage() {
        return waitForVisibility(confirmationHeading).getText().trim();
    }

    private String getValueFromConfirmationTable(String label) {
        waitForVisibility(confirmationTable);
        By valueLocator = By.xpath(
                "//table[contains(@class,'table')]//tr[td[1][normalize-space()="
                        + quoteForXPath(label) + "]]//td[2]");
        return waitForVisibility(valueLocator).getText().trim();
    }

    private String quoteForXPath(String value) {
        return "'" + value.replace("'", "&apos;") + "'";
    }
}
