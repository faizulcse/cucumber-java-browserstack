import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Main {
    public static final String AUTOMATE_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    public static final String AUTOMATE_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    public static final String API_URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY + "@api-cloud.browserstack.com";

    public static void main(String[] args) {
        System.out.println(getRecentApp("app.apk"));
    }

    public static String getRecentApp(String appName) {
        String recentApp = null;
       RestAssured.given().when().get(API_URL + "/app-automate/recent_apps/").then().log().all();
//        res.prettyPrint();
//        JsonArray appArray = new Gson().fromJson(res.asString(), JsonArray.class).getAsJsonArray();
//        for (int i = 0; i < appArray.size(); i++) {
//            if (appArray.get(i).getAsJsonObject().get("app_name").getAsString().equals(appName)) {
//                recentApp = appArray.get(i).getAsJsonObject().get("app_url").getAsString();
//                break;
//            }
//        }
        return recentApp;
    }
}