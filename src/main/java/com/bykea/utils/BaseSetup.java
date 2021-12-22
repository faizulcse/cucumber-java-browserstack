package com.bykea.utils;

import com.browserstack.local.Local;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
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
    static final String DEVICE_PROFILE = System.getProperty("profile") == null ? "s21" : System.getProperty("profile");
    Scenario scenario;

    public void startDriver(Scenario name) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        try {
            scenario = name;
            AppiumDriver<?> driver;
            if (!BS_RUN)
                driver = new AppiumDriver<>(new URL(APPIUM_URL), getLocalCapabilities(DEVICE_PROFILE));
            else {
                enableLocalTesting();
                driver = new AppiumDriver<>(new URL(BROWSERSTACK_URL), getBrowserStackCapabilities(DEVICE_PROFILE));
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

    public DesiredCapabilities getLocalCapabilities(String deviceProfile) {
        DesiredCapabilities cap = new DesiredCapabilities();
        Properties props = getDeviceProps(deviceProfile);
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, props.getProperty("platformName"));
        cap.setCapability(MobileCapabilityType.PLATFORM_VERSION, props.getProperty("platformVersion"));
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, props.getProperty("deviceName"));
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "12000");
        cap.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, "true");
        cap.setCapability(MobileCapabilityType.APP, "C:/Users/BS705/Downloads/" + props.getProperty("app"));
        cap.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, props.getProperty("appPackage"));
        cap.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, props.getProperty("appActivity"));
        cap.setCapability(MobileCapabilityType.FULL_RESET, props.getProperty("fullReset"));
        cap.setCapability(MobileCapabilityType.NO_RESET, props.getProperty("noReset"));
        return cap;
    }

    public DesiredCapabilities getBrowserStackCapabilities(String deviceProfile) {
        Properties props = getDeviceProps(deviceProfile);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browserstack.local", props.getProperty("local"));
        cap.setCapability("name", scenario.getName());
        cap.setCapability("browserstack.gpsLocation", props.getProperty("gpsLocation"));
        cap.setCapability("platformName", props.getProperty("platformName"));
        cap.setCapability("device", props.getProperty("deviceName"));
        cap.setCapability("platformVersion", props.getProperty("platformVersion"));
        cap.setCapability("autoGrantPermissions", props.getProperty("autoGrantPermissions"));
        cap.setCapability("app_url", props.getProperty("app"));
        return cap;
    }

    public static Properties getDeviceProps(String name) {
        String profile = ROOT_DIR + "/devices/" + name + ".properties";
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(profile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
