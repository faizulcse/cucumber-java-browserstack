package com.bykea.utils;

import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseSetup {
    static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    static final String BROWSERSTACK_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
    static final String ROOT_DIR = System.getProperty("user.dir");
    static final boolean BS_RUN = Boolean.parseBoolean(System.getProperty("browserstack"));
    Scenario scenario;

    public void startDriver(Scenario name) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        try {
            scenario = name;
            AppiumDriver<?> driver;
            if (!BS_RUN)
                driver = new AppiumDriver<>(new URL(APPIUM_URL), getLocalCapabilities());
            else {
                enableLocalTesting();
                driver = new AppiumDriver<>(new URL(BROWSERSTACK_URL), getBrowserStackCapabilities());
                BrowserStack.printResultLink(driver.getSessionId().toString());
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            DriverManager.setWebDriver(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSessionId() {
        return DriverManager.getDriver().getSessionId().toString();
    }

    public void enableLocalTesting() {
        try {
            Local local = new Local();
            HashMap<String, String> localArgs = new HashMap<>();
            localArgs.put("key", AUTOMATE_ACCESS_KEY);
            localArgs.put("forcelocal", "true");
            local.start(localArgs);
            DriverManager.setLocalTesting(local);
        } catch (Exception ignore) {
        }
    }

    public void disableLocalTesting() throws Exception {
        if (DriverManager.getLocalTesting() != null)
            DriverManager.getLocalTesting().stop();
    }

    public void stopDriver() {
        if (BS_RUN)
            BrowserStack.setTestStatus(getSessionId(), scenario);
        try {
            if (DriverManager.getDriver() != null)
                DriverManager.getDriver().quit();
            if (BS_RUN)
                disableLocalTesting();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DesiredCapabilities getLocalCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0");
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Samsung S8");
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "12000");
        cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
        cap.setCapability(MobileCapabilityType.APP, ROOT_DIR + "/APK/app.apk");
        cap.setCapability(MobileCapabilityType.FULL_RESET, "true");
        cap.setCapability(MobileCapabilityType.NO_RESET, "false");
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
}
