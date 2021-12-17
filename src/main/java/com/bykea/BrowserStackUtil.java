package com.bykea;

import com.browserstack.local.Local;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserStackUtil {
    private static Local localTest;
    private static WebDriver driver;
    public static String AUTOMATE_USERNAME = System.getenv("BS_USERNAME");
    public static String AUTOMATE_ACCESS_KEY = System.getenv("BS_ACCESS_KEY");
    public static String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @Before
    public void setUp() {
        startLocalTest();
        startDriver();
    }

    @After
    public void tearDown() {
        stopDriver();
        stopLocalTest();
    }

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

    public static DesiredCapabilities getCap() {
//         Use Java Client v6.0.0 or above
//         HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
//         browserstackOptions.put("local", isLocalConnected() ? "true" : "false");
//         browserstackOptions.put("browserstack.idleTimeout", "60");
//         browserstackOptions.put("gpsLocation", "24.8666842,670809064");
//         browserstackOptions.put("geoLocation", "PK");
        
//         DesiredCapabilities capabilities = new DesiredCapabilities();
//         capabilities.setCapability("bstack:options", browserstackOptions);
//         capabilities.setCapability("platformName", "android");
//         capabilities.setCapability("platformVersion", "11.0");
//         capabilities.setCapability("deviceName", "Samsung Galaxy S21");
//         capabilities.setCapability("app", "bs://9916a937854351eeae596872b07965f496bb4946");
//         capabilities.setCapability("otherApps", new String[]{"bs://f83ece3fbf27f772bb1f6275ca1bbd1822fda733"});

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browserstack.local", isLocalConnected() ? "true" : "false");
        cap.setCapability("browserstack.gpsLocation", "24.8666842,67.0809064");
        cap.setCapability("platformName", "Android");
        cap.setCapability("device", "Samsung Galaxy S21");
        cap.setCapability("os_version", "11.0");
        cap.setCapability("autoGrantPermissions", "true");
        cap.setCapability("app_url", " bs://9916a937854351eeae596872b07965f496bb4946");
        return cap;
    }

    public static void startLocalTest() {
        try {
            localTest = new Local();
            HashMap<String, String> bsLocalArgs = new HashMap<>();
            bsLocalArgs.put("key", AUTOMATE_ACCESS_KEY);
            bsLocalArgs.put("forcelocal", "true");
            localTest.start(bsLocalArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopLocalTest() {
        try {
            if (isLocalConnected())
                localTest.stop();
        } catch (Exception ignore) {
        }
    }

    private static boolean isLocalConnected() {
        try {
            return localTest.isRunning();
        } catch (Exception ignore) {
            return false;
        }
    }
}
