package com.dyad.blazedemo.pages;

import com.dyad.blazedemo.base.BasePage;
import com.dyad.blazedemo.utils.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class PurchasePage extends BasePage {
    private final By name = By.id("inputName");
    private final By address = By.id("address");
    private final By city = By.id("city");
    private final By state = By.id("state");
    private final By zipCode = By.id("zipCode");
    private final By cardType = By.id("cardType");
    private final By cardNumber = By.id("creditCardNumber");
    private final By cardMonth = By.id("creditCardMonth");
    private final By cardYear = By.id("creditCardYear");
    private final By nameOnCard = By.id("nameOnCard");
    private final By rememberMe = By.id("rememberMe");
    private final By purchaseButton = By.cssSelector("input[type='submit'][value='Purchase Flight']");

    public PurchasePage(WebDriver driver) {
        super(driver);
        waitForVisibility(name);
    }

    public PurchasePage fillPassengerDetails() {
        enterText(name, TestData.PASSENGER_NAME);
        enterText(address, TestData.ADDRESS);
        enterText(city, TestData.CITY);
        enterText(state, TestData.STATE);
        enterText(zipCode, TestData.ZIP_CODE);

        Select cardTypeSelect = new Select(waitForVisibility(cardType));
        cardTypeSelect.selectByValue(TestData.CARD_TYPE);

        enterText(cardNumber, TestData.CREDIT_CARD_NUMBER);
        enterText(cardMonth, TestData.CREDIT_CARD_MONTH);
        enterText(cardYear, TestData.CREDIT_CARD_YEAR);
        enterText(nameOnCard, TestData.NAME_ON_CARD);

        if (!driver.findElement(rememberMe).isSelected()) {
            click(rememberMe);
        }
        return this;
    }

    public ConfirmationPage clickPurchaseFlight() {
        click(purchaseButton);
        return new ConfirmationPage(driver);
    }
}
