package com.dyad.parabank.pages;

import com.dyad.parabank.utils.TestData.RegistrationData;
import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {
    private final WebDriver driver;
    private final By firstName = By.id("customer.firstName");
    private final By lastName = By.id("customer.lastName");
    private final By address = By.id("customer.address.street");
    private final By city = By.id("customer.address.city");
    private final By state = By.id("customer.address.state");
    private final By zipCode = By.id("customer.address.zipCode");
    private final By phone = By.id("customer.phoneNumber");
    private final By ssn = By.id("customer.ssn");
    private final By username = By.id("customer.username");
    private final By password = By.id("customer.password");
    private final By repeatedPassword = By.id("repeatedPassword");
    private final By register = By.cssSelector("input[value='Register']");

    public RegistrationPage(WebDriver driver) { this.driver = driver; }

    public AccountsOverviewPage register(RegistrationData data) {
        Waits.visible(driver, firstName).sendKeys(data.firstName());
        driver.findElement(lastName).sendKeys(data.lastName());
        driver.findElement(address).sendKeys(data.address());
        driver.findElement(city).sendKeys(data.city());
        driver.findElement(state).sendKeys(data.state());
        driver.findElement(zipCode).sendKeys(data.zipCode());
        driver.findElement(phone).sendKeys(data.phone());
        driver.findElement(ssn).sendKeys(data.ssn());
        driver.findElement(username).sendKeys(data.username());
        driver.findElement(password).sendKeys(data.password());
        driver.findElement(repeatedPassword).sendKeys(data.password());
        Waits.clickable(driver, register).click();
        Waits.visible(driver, By.linkText("Accounts Overview"));
        return new AccountsOverviewPage(driver);
    }
}
