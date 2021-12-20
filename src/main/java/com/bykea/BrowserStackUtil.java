package com.bykea;

import com.browserstack.local.Local;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;

public class BrowserStackUtil {
    private static Local local;
    public static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static final String SERVER_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    public static final String API_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@api-cloud.browserstack.com";

    public static void startLocalTest() {
        try {
            if (!isLocalConnected()) {
                local = new Local();
                HashMap<String, String> localArgs = new HashMap<>();
                localArgs.put("key", System.getenv("BROWSERSTACK_ACCESS_KEY"));
                localArgs.put("forcelocal", "true");
                local.start(localArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopLocalTest() {
        try {
            if (isLocalConnected())
                local.stop();
        } catch (Exception ignore) {

        }
    }

    private static boolean isLocalConnected() {
        try {
            return local.isRunning();
        } catch (Exception ignore) {
            return false;
        }
    }

    public static void setTestStatus(String session, Scenario scenario) {
        String status = scenario.isFailed() ?
                "{\n    \"status\":\"Failed\",\n    \"reason\":\"Error in execute steps.\"\n}" :
                "{\n   \"status\":\"Passed\",\n     \"reason\":\"All steps completed.\"\n}";
        RestAssured
                .given()
                .contentType("application/json")
                .body(status)
                .put(API_URL + "/app-automate/sessions/" + session + ".json")
                .then()
                .assertThat()
                .statusCode(200);
    }

    public static void printResultLink(String session) {
        Response res = RestAssured.given().when().get(API_URL + "/app-automate/sessions/" + session + ".json");
        JsonObject automationSession = new Gson().fromJson(res.asString(), JsonObject.class).getAsJsonObject("automation_session");
        System.out.println(automationSession.get("browser_url").getAsString());
    }

    public static String getRecentAppList() {
        Response res = RestAssured.given().when().get(API_URL + "/app-automate/recent_apps/");
        res.then().assertThat().statusCode(200);
        return res.asString();
    }

    public static String getRecentApp(String appList, String customId) {
        String recentApp = null;
        JsonArray appArray = new Gson().fromJson(appList, JsonArray.class).getAsJsonArray();
        for (int i = 0; i < appArray.size(); i++) {
            if (appArray.get(i).getAsJsonObject().get("app_name").getAsString().equals(customId)) {
                recentApp = appArray.get(i).getAsJsonObject().get("app_url").getAsString();
                break;
            }
        }
        if (recentApp == null)
            System.out.println("No recent app found: " + customId);
        return recentApp;
    }
}
