package com.dyad.parabank.pages;

import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;

public class AccountsOverviewPage {
    private final WebDriver driver;
    private final By accountsOverview = By.linkText("Accounts Overview");
    private final By openNewAccount = By.linkText("Open New Account");
    private final By transferFunds = By.linkText("Transfer Funds");
    private final By accountRows = By.cssSelector("table#accountTable tbody tr");

    public AccountsOverviewPage(WebDriver driver) { this.driver = driver; }

    public BigDecimal getBalance(String accountId) {
        WebElement row = Waits.visible(driver, By.xpath("//table[@id='accountTable']//a[normalize-space()='" + accountId + "']/ancestor::tr"));
        String balance = row.findElement(By.xpath("./td[2]")).getText().replace("$", "").replace(",", "").trim();
        return new BigDecimal(balance);
    }

    public String getFirstAccountId() {
        WebElement row = Waits.visible(driver, accountRows);
        return row.findElement(By.cssSelector("td:first-child a")).getText().trim();
    }

    public OpenAccountPage openNewAccount() {
        Waits.clickable(driver, openNewAccount).click();
        return new OpenAccountPage(driver);
    }

    public TransferFundsPage openTransferFunds() {
        Waits.clickable(driver, transferFunds).click();
        return new TransferFundsPage(driver);
    }

    public AccountActivityPage openAccountActivity(String accountId) {
        Waits.clickable(driver, By.xpath("//table[@id='accountTable']//a[normalize-space()='" + accountId + "']")).click();
        return new AccountActivityPage(driver);
    }
}
