package com.dyad.parabank.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private DriverFactory() {}

    public static void start() {
        String browser = Config.browser().toLowerCase();
        WebDriver driver;
        if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (Config.headless()) options.addArguments("-headless");
            driver = new FirefoxDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            if (Config.headless()) options.addArguments("--headless=new");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
        }
        DRIVER.set(driver);
        driver.manage().window().maximize();
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) throw new IllegalStateException("WebDriver has not been started.");
        return driver;
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
