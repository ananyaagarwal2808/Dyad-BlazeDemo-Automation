package com.dyad.parabank.utils;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class Waits {
    private Waits() {}
    public static WebDriverWait wait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(Config.waitSeconds()));
    }
    public static WebElement visible(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    public static WebElement clickable(WebDriver driver, By locator) {
        return wait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }
    public static void urlContains(WebDriver driver, String value) {
        wait(driver).until(ExpectedConditions.urlContains(value));
    }
}
