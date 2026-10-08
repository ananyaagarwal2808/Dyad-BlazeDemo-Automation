package com.dyad.parabank.pages;

import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class OpenAccountPage {
    private final WebDriver driver;
    private final By accountType = By.id("type");
    private final By fromAccount = By.id("fromAccountId");
    private final By openAccountButton = By.cssSelector("input[value='Open New Account']");
    private final By newAccountId = By.id("newAccountId");

    public OpenAccountPage(WebDriver driver) { this.driver = driver; }

    public String openSavingsAccount(String sourceAccountId) {
        new Select(Waits.visible(driver, accountType)).selectByVisibleText("SAVINGS");
        new Select(Waits.visible(driver, fromAccount)).selectByVisibleText(sourceAccountId);
        Waits.clickable(driver, openAccountButton).click();
        return Waits.visible(driver, newAccountId).getText().trim();
    }
}
