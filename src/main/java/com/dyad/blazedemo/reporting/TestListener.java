package com.dyad.blazedemo.reporting;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.dyad.blazedemo.driver.DriverFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());
        ExtentTestManager.setTest(test);
        test.info("Test execution started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.pass("Test passed");
        }
        ExtentTestManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.fail(result.getThrowable());
            try {
                String screenshotPath = captureScreenshot(result.getMethod().getMethodName());
                test.addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                LOGGER.error("Unable to capture failure screenshot", e);
            }
        }
        ExtentTestManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
    }

    private String captureScreenshot(String testName) throws IOException {
        WebDriver driver = DriverFactory.getDriver();
        Path directory = Path.of("reports", "screenshots");
        Files.createDirectories(directory);
        Path screenshot = directory.resolve(testName + "-failure.png");
        Files.write(screenshot, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
        return screenshot.toString();
    }
}
