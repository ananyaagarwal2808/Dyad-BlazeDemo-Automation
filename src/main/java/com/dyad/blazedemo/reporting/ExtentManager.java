package com.dyad.blazedemo.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public final class ExtentManager {
    private static ExtentReports extentReports;

    private ExtentManager() { }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            new File("reports").mkdirs();
            ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ExtentReport.html");
            reporter.config().setDocumentTitle("Dyad BlazeDemo Automation Report");
            reporter.config().setReportName("BlazeDemo Flight Booking E2E");

            extentReports = new ExtentReports();
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("Application", "BlazeDemo");
            extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM");
            extentReports.setSystemInfo("Java", System.getProperty("java.version"));
            extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extentReports;
    }
}
