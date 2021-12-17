package com.bykea;

import com.browserstack.local.Local;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseSetup {
    private static Local localTest;
    private static WebDriver driver;
    public static String AUTOMATE_USERNAME = System.getenv("BS_USERNAME");
    public static String AUTOMATE_ACCESS_KEY = System.getenv("BS_ACCESS_KEY");
    public static String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static void startDriver() {
        try {
            Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
            driver = new RemoteWebDriver(new URL(URL), getCap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopDriver() {
        if (driver != null)
            driver.quit();
    }

    public WebDriver getDriver() {
        if (driver != null)
            return driver;
        else {
            System.out.println("Driver not initialize");
            return null;
        }
    }

    public static DesiredCapabilities getCap() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browserstack.local", isLocalConnected() ? "true" : "false");
        cap.setCapability("platformName", "Android");
        cap.setCapability("device", "Samsung Galaxy S9");
        cap.setCapability("platformVersion", "8.0");
        cap.setCapability("app_url", "bs://9916a937854351eeae596872b07965f496bb4946");
        cap.setCapability("automationName", "Appium");
        return cap;
    }

    public static Local getLocalTest() {
        return localTest;
    }

    public static void startLocalTest() {
        try {
            localTest = new Local();
            HashMap<String, String> bsLocalArgs = new HashMap<String, String>();
            bsLocalArgs.put("key", AUTOMATE_ACCESS_KEY);
            bsLocalArgs.put("forcelocal", "true");
            localTest.start(bsLocalArgs);
        } catch (Exception ignore) {

        }
    }

    public static void stopLocalTest() {
        try {
            if (getLocalTest().isRunning())
                getLocalTest().stop();
        } catch (Exception ignore) {

        }
    }

    public static boolean isLocalConnected() {
        return localTest != null;
    }
}
