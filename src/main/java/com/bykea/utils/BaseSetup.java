package com.bykea.utils;

import com.browserstack.local.Local;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseSetup {
    Scenario scenario;
    protected boolean localConnected;
    protected static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    protected static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    protected static final String BROWSERSTACK_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    protected static final String API_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@api-cloud.browserstack.com";
    protected static final String APPIUM_URL = "http://127.0.0.1:4723/wd/hub";
    protected static final String ROOT_DIR = System.getProperty("user.dir");
    protected static final boolean RUN_BS = Boolean.parseBoolean(System.getProperty("browserstack"));
    protected static final String DEVICE_PROFILE = System.getProperty("profile") == null ? "s21" : System.getProperty("profile");

    public void startDriver(Scenario name) {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        System.out.println("Selected device: " + getDeviceProps(DEVICE_PROFILE).getProperty("deviceName") + ", BrowserStack: " + RUN_BS + ", BS Local: " + localConnected);
        try {
            scenario = name;
            AppiumDriver<?> driver;
            if (RUN_BS) {
                driver = new AppiumDriver<>(new URL(BROWSERSTACK_URL), getBrowserStackCapabilities(DEVICE_PROFILE));
                printResultLink(driver.getSessionId());
            } else {
                driver = new AppiumDriver<>(new URL(APPIUM_URL), getLocalCapabilities(DEVICE_PROFILE));
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            DriverManager.setWebDriver(driver);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public SessionId getSessionId() {
        return DriverManager.getDriver() != null ? DriverManager.getDriver().getSessionId() : null;
    }

    public String getEndpoint(SessionId id) {
        return API_URL + "/app-automate/sessions/" + id + ".json";
    }

    public void enableLocalTesting() {
        Local local = new Local();
        HashMap<String, String> localArgs = new HashMap<>();
        localArgs.put("key", AUTOMATE_ACCESS_KEY);
        localArgs.put("forcelocal", "true");
        try {
            local.start(localArgs);
            localConnected = true;
            DriverManager.setLocalTesting(local);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void disableLocalTesting() {
        try {
            DriverManager.getLocalTesting().stop();
        } catch (Exception ignored) {
        }
    }

    public void updateTestStatus(SessionId id) {
        JsonObject result = new JsonObject();
        result.addProperty("status", scenario.getStatus().toString());
        result.addProperty("reason", scenario.isFailed() ? "Found some errors in executing the test!" : "All steps are successfully completed!");
        RestAssured.given().contentType("application/json").body(result).put(getEndpoint(id)).then().assertThat().statusCode(200);
    }

    public void printResultLink(SessionId id) {
        Response response = RestAssured.given().when().get(getEndpoint(id));
        response.then().assertThat().statusCode(200);
        System.out.println("Report log: " + new Gson().fromJson(response.asString(), JsonObject.class).getAsJsonObject("automation_session").get("browser_url").getAsString());
    }

    public void stopDriver() {
        if (RUN_BS && getSessionId() != null)
            updateTestStatus(getSessionId());
        try {
            DriverManager.getDriver().quit();
        } catch (Exception ignored) {
        }
    }

    public DesiredCapabilities getLocalCapabilities(String profile) {
        DesiredCapabilities cap = new DesiredCapabilities();
        Properties props = getDeviceProps(profile);
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

    public DesiredCapabilities getBrowserStackCapabilities(String profile) {
        Properties props = getDeviceProps(profile);
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("project", System.getenv("PROJECT_NAME"));
        cap.setCapability("build", System.getenv("BUILD_NUMBER"));
        cap.setCapability("name", scenario.getName());
        cap.setCapability("browserstack.local", props.getProperty("local"));
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