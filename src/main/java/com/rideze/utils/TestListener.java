package com.rideze.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.rideze.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("====== STARTING TEST SUITE: " + context.getName() + " ======");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("====== FINISHED TEST SUITE: " + context.getName() + " ======");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("--> Running Test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[PASS] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[FAIL] " + result.getMethod().getMethodName());
        
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String base64Screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                
                // If using ExtentReports instance tied to the thread/listener:
                if (ExtentReportListener.getTest() != null) {
                    ExtentReportListener.getTest().log(
                        Status.FAIL, 
                        "Test Failed: " + result.getThrowable().getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIP] " + result.getMethod().getMethodName());
    }
}