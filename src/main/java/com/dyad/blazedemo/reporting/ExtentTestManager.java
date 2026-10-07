package com.dyad.blazedemo.reporting;

import com.aventstack.extentreports.ExtentTest;

public final class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> TEST = new ThreadLocal<>();

    private ExtentTestManager() { }

    public static void setTest(ExtentTest test) {
        TEST.set(test);
    }

    public static ExtentTest getTest() {
        return TEST.get();
    }

    public static void removeTest() {
        TEST.remove();
    }
}
