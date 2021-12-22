package com.bykea.utils;

import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;

public class DriverManager {
    private static ThreadLocal<AppiumDriver<?>> driverThread = new ThreadLocal<>();
    private static ThreadLocal<Local> localTestingThread = new ThreadLocal<>();

    public static AppiumDriver<?> getDriver() {
        return driverThread.get();
    }

    public static void setWebDriver(AppiumDriver<?> driver) {
        driverThread.set(driver);
    }

    public static Local getLocalTesting() {
        return localTestingThread.get();
    }

    public static void setLocalTesting(Local local) {
        localTestingThread.set(local);
    }
}
