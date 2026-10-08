package com.dyad.parabank.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public final class ReportManager {
    private static final ExtentReports REPORT = create();
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();
    private ReportManager() {}
    private static ExtentReports create() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/reports/ParaBank-Test-Report.html");
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Framework", "Selenium + TestNG + POM");
        extent.setSystemInfo("Application", "ParaBank");
        return extent;
    }
    public static void startTest(String name) { TEST.set(REPORT.createTest(name)); }
    public static ExtentTest test() { return TEST.get(); }
    public static void log(String message) { if (TEST.get() != null) TEST.get().info(message); }
    public static void flush() { REPORT.flush(); }
}
