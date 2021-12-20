package com.bykea;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseSetup {
    public static AppiumDriver<?> driver;
    private static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
    private static final String APK_FILE = System.getProperty("user.dir") + "/APK/app.apk";
    public static final boolean IS_BROWSERSTACK = Boolean.parseBoolean(System.getProperty("browserstack"));

    public static void startDriver() {
        if (IS_BROWSERSTACK && isBsLocalRun())
            BrowserStackUtil.startLocalTest();
        try {
            Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
            driver = IS_BROWSERSTACK ? new AppiumDriver<>(new URL(BrowserStackUtil.SERVER_URL), getBrowserStackCapabilities()) : new AppiumDriver<>(new URL(APPIUM_URL), getLocalCapabilities());
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (IS_BROWSERSTACK)
            BrowserStackUtil.printResultLink(getSessionId());
    }

    public static String getSessionId() {
        return driver.getSessionId().toString();
    }

    public static void stopDriver(Scenario scenario) {
        try {
            if (IS_BROWSERSTACK)
                BrowserStackUtil.setTestStatus(getSessionId(), scenario);
            if (IS_BROWSERSTACK && isBsLocalRun())
                BrowserStackUtil.stopLocalTest();
            if (driver != null)
                driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isBsLocalRun() {
        return Boolean.parseBoolean(getBrowserStackCapabilities().getCapability("browserstack.local").toString());
    }

    public static DesiredCapabilities getLocalCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0");
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Samsung S8");
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "12000");
        cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
        cap.setCapability(MobileCapabilityType.APP, APK_FILE);
        return cap;
    }

    public static DesiredCapabilities getBrowserStackCapabilities() {
        String appList = BrowserStackUtil.getRecentAppList();
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browserstack.local", "true");
        cap.setCapability("browserstack.gpsLocation", "24.8666842,67.0809064");
        cap.setCapability("platformName", "Android");
        cap.setCapability("device", "Samsung Galaxy S21");
        cap.setCapability("platformVersion", "11.0");
        cap.setCapability("autoGrantPermissions", "true");
        cap.setCapability("app_url", BrowserStackUtil.getRecentApp(appList, "app.apk"));
        cap.setCapability("otherApps", new String[]{BrowserStackUtil.getRecentApp(appList, "partnerapk.apk")});
        return cap;
    }
}
