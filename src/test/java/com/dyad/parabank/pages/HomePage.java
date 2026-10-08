package com.dyad.parabank.pages;

import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;
    private final By registerLink = By.linkText("Register");
    private final By loginButton = By.cssSelector("input[value='Log In']");
    private final By username = By.name("username");
    private final By password = By.name("password");

    public HomePage(WebDriver driver) { this.driver = driver; }
    public RegistrationPage clickRegister() { Waits.clickable(driver, registerLink).click(); return new RegistrationPage(driver); }
    public AccountsOverviewPage login(String user, String pass) {
        Waits.visible(driver, username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        Waits.clickable(driver, loginButton).click();
        return new AccountsOverviewPage(driver);
    }
}
