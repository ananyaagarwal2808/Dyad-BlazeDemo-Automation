package com.dyad.parabank.utils;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public abstract class BaseTest {
    protected final Logger log = LogManager.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() { new File("target/reports").mkdirs(); new File("target/screenshots").mkdirs(); }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.start();
        ReportManager.startTest(getClass().getSimpleName());
        DriverFactory.get().get(Config.baseUrl());
        log.info("Opened ParaBank: {}", Config.baseUrl());
        ReportManager.test().log(Status.INFO, "Opened ParaBank");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            String screenshot = captureScreenshot(result.getName());
            ReportManager.test().fail(result.getThrowable());
            if (screenshot != null) ReportManager.test().addScreenCaptureFromPath(screenshot);
        } else {
            ReportManager.test().pass("Test completed successfully");
        }
        DriverFactory.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() { ReportManager.flush(); }

    private String captureScreenshot(String name) {
        try {
            Path destination = Path.of("target/screenshots", name + "-failure.png");
            Files.copy(((TakesScreenshot) DriverFactory.get()).getScreenshotAs(OutputType.FILE).toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (Exception e) {
            log.warn("Could not capture screenshot", e);
            return null;
        }
    }
}
