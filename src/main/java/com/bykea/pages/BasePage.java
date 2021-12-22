package com.bykea.pages;

import com.bykea.utils.BaseSetup;
import com.bykea.utils.DriverManager;
import io.appium.java_client.AppiumDriver;

public class BasePage extends BaseSetup {
    AppiumDriver<?> driver;

    public BasePage() {
        driver = DriverManager.getDriver();
    }
}
