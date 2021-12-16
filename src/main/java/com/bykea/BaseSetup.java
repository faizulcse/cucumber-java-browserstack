package com.bykea;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseSetup {
    private AndroidDriver<?> driver;
    private DesiredCapabilities cap;
    private String BS_SERVER_URL = "";

    public void startDriver() {
        System.out.println("before");
        try {
            driver = new AndroidDriver<AndroidElement>(new URL(BS_SERVER_URL), getCap());
            setDriver(driver);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void stopDriver() {
        System.out.println("after");
        if (getDriver() != null)
            driver.quit();
    }

    public AndroidDriver<?> getDriver() {
        return driver;
    }

    public void setDriver(AndroidDriver<?> driver) {
        this.driver = driver;
    }

    public DesiredCapabilities getCap() {
        cap = new DesiredCapabilities();
        cap.setCapability("", "");
        return cap;
    }

    public void setCap(DesiredCapabilities cap) {
        this.cap = cap;
    }
}
