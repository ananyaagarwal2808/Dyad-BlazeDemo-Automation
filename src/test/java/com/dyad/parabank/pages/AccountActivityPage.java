package com.dyad.parabank.pages;

import com.dyad.parabank.utils.Waits;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountActivityPage {
    private final WebDriver driver;
    private final By activityTable = By.id("transactionTable");
    private final By transactionRows = By.cssSelector("#transactionTable tbody tr");

    public AccountActivityPage(WebDriver driver) { this.driver = driver; }

    public boolean hasTransferTransaction(BigDecimal amount) {
        Waits.visible(driver, activityTable);
        List<WebElement> rows = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    List<WebElement> found = d.findElements(transactionRows);
                    return found.isEmpty() ? null : found;
                });
        String expected = amount.setScale(2).toPlainString();
        return rows.stream().anyMatch(row -> {
            String text = row.getText().replace("$", "").replace(",", "");
            return text.contains(expected) && text.toLowerCase().contains("transfer");
        });
    }
}
