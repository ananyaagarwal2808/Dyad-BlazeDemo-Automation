package com.dyad.parabank.pages;

import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class TransferFundsPage {
    private final WebDriver driver;
    private final By amount = By.id("amount");
    private final By fromAccount = By.id("fromAccountId");
    private final By toAccount = By.id("toAccountId");
    private final By transferButton = By.cssSelector("input[value='Transfer']");
    private final By resultTitle = By.xpath("//*[contains(normalize-space(), 'Transfer Complete!')]");

    public TransferFundsPage(WebDriver driver) { this.driver = driver; }

    public void transfer(String amountValue, String sourceAccount, String destinationAccount) {
        Waits.visible(driver, amount).clear();
        driver.findElement(amount).sendKeys(amountValue);
        new Select(driver.findElement(fromAccount)).selectByVisibleText(sourceAccount);
        new Select(driver.findElement(toAccount)).selectByVisibleText(destinationAccount);
        Waits.clickable(driver, transferButton).click();
        Waits.visible(driver, resultTitle);
    }

    public boolean isTransferSuccessful() {
        return driver.findElement(resultTitle).getText().toLowerCase().contains("transfer complete");
    }
}
