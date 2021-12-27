package com.bykea.utils;

import com.browserstack.local.Local;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseSetup {
    Scenario scenario;
    private static Local local;
    public static final ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final boolean BS = Boolean.parseBoolean(System.getenv("BROWSERSTACK") == null ? bundle.getString("browserstack") : System.getenv("BROWSERSTACK"));
    public static final boolean BS_LOCAL = Boolean.parseBoolean(System.getenv("BROWSERSTACK_LOCAL") == null ? bundle.getString("browserstackLocal") : System.getenv("BROWSERSTACK_LOCAL"));
    protected static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME") == null ? System.getProperty("browserstack_username") : System.getenv("BROWSERSTACK_USERNAME");
    protected static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY") == null ? System.getProperty("browserstack_access_key") : System.getenv("BROWSERSTACK_ACCESS_KEY");
    protected static final String APPIUM_URL = bundle.getString("appiumUrl");
    protected static final String BROWSERSTACK_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    protected static final String API_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@api-cloud.browserstack.com";
    protected static final String ROOT_DIR = System.getProperty("user.dir");
    protected static final String SERVER_URL = BS ? BROWSERSTACK_URL : APPIUM_URL;

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public void startDriver() {
        System.out.println(System.getenv("BROWSERSTACK"));
        System.out.println(System.getenv("BROWSERSTACK_USERNAME"));
        System.out.println(System.getenv("BROWSERSTACK_ACCESS_KEY"));
        System.out.println(System.getenv("BS_CREDENTIALS"));
        try {
            Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
            AppiumDriver<?> driver;
            driver = new AppiumDriver<>(new URL(SERVER_URL), getDesiredCaps());
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            DriverManager.setWebDriver(driver);
            if (BS) printResultLink(driver.getSessionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SessionId getSessionId() {
        return DriverManager.getDriver() != null ? DriverManager.getDriver().getSessionId() : null;
    }

    public String getEndpoint(SessionId id) {
        return API_URL + "/app-automate/sessions/" + id + ".json";
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
        try {
            if (getSessionId() != null) {
                if (BS)
                    updateTestStatus(getSessionId());
                DriverManager.getDriver().quit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DesiredCapabilities getDesiredCaps() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("device", System.getenv("DEVICE_PROFILE") == null ? bundle.getString("deviceName") : System.getenv("DEVICE_PROFILE").split("@")[0]);
        caps.setCapability("platformVersion", System.getenv("DEVICE_PROFILE") == null ? bundle.getString("platformVersion") : System.getenv("DEVICE_PROFILE").split("@")[1]);
        caps.setCapability("platformName", System.getenv("PLATFORM_NAME") == null ? bundle.getString("platformName") : System.getenv("PLATFORM_NAME"));
        caps.setCapability("browserstack.gpsLocation", System.getenv("GPS_LOCATION") == null ? bundle.getString("gpsLocation") : System.getenv("GPS_LOCATION"));
        caps.setCapability("app", System.getProperty("APP_NAME") == null ? bundle.getString("app") : System.getenv("APP_NAME"));
        caps.setCapability("autoGrantPermissions", "true");
        caps.setCapability("fullReset", "true");
        caps.setCapability("noReset", "false");
        if (BS) {
            caps.setCapability("browserstack.local", BS_LOCAL);
            caps.setCapability("project", "BYKEA AUTOMATION PROJECT");
            caps.setCapability("build", "Bykea Automation Build: " + System.getenv("BUILD_NUMBER"));
            caps.setCapability("name", getScenario());
        } else {
            caps.setCapability("appPackage", bundle.getString("appPackage"));
            caps.setCapability("appActivity", bundle.getString("appActivity"));
        }
        return caps;
    }

    public static void enableLocalTesting() {
        try {
            local = new Local();
            HashMap<String, String> localArgs = new HashMap<>();
            localArgs.put("key", AUTOMATE_ACCESS_KEY);
            local.start(localArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disableLocalTesting() {
        try {
            local.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}