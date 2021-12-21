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
    private Scenario scenario;
    private static AppiumDriver<?> driver;
    public static String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static String BROWSERSTACK_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
    private static final String APK_FILE = System.getProperty("user.dir") + "/APK/app.apk";
    public static final boolean IS_BROWSERSTACK = Boolean.parseBoolean(System.getProperty("browserstack"));

    public void startDriver(Scenario name) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        scenario = name;
        try {
            if (IS_BROWSERSTACK) {
                driver = new AppiumDriver<>(new URL(BROWSERSTACK_URL), getBrowserStackCapabilities());
            } else
                driver = new AppiumDriver<>(new URL(APPIUM_URL), getLocalCapabilities());
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopDriver() {
        if (driver != null)
            driver.quit();
    }

    public AppiumDriver<?> getDriver() {
        return driver;
    }

    public String getSessionId() {
        return driver.getSessionId().toString();
    }

    public DesiredCapabilities getLocalCapabilities() {
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

    public DesiredCapabilities getBrowserStackCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browserstack.local", "true");
        cap.setCapability("name", scenario.getName());
        cap.setCapability("browserstack.gpsLocation", "24.8666842,67.0809064");
        cap.setCapability("platformName", "Android");
        cap.setCapability("device", "Samsung Galaxy S21");
        cap.setCapability("platformVersion", "11.0");
        cap.setCapability("autoGrantPermissions", "true");
        cap.setCapability("app_url", "bs://9916a937854351eeae596872b07965f496bb4946");
        return cap;
    }

    public boolean isBsLocalRun() {
        return Boolean.parseBoolean(getBrowserStackCapabilities().getCapability("browserstack.local").toString());
    }
}
