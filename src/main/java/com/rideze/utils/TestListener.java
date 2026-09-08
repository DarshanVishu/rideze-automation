package com.rideze.utils;

import com.rideze.driver.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        System.err.println("[FAIL] " + result.getMethod().getMethodName());
        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String destPath = System.getProperty("user.dir") + "/reports/screenshots/" 
                    + result.getMethod().getMethodName() + "_" + timestamp + ".png";

            try {
                FileUtils.copyFile(srcFile, new File(destPath));
                System.err.println("Failure screenshot saved at: " + destPath);
            } catch (IOException e) {
                System.err.println("Could not capture failure screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIPPED] " + result.getMethod().getMethodName());
    }
}