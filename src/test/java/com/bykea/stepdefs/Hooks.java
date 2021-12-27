package com.bykea.stepdefs;

import com.bykea.utils.BaseSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.ResourceBundle;

public class Hooks {
    BaseSetup setup = new BaseSetup();
    static String bsLocal = ResourceBundle.getBundle("config").getString("browserstackLocal");

    static {
        if (Boolean.parseBoolean(bsLocal)) {
            BaseSetup.enableLocalTesting();
            Runtime.getRuntime().addShutdownHook(new Thread(BaseSetup::disableLocalTesting));
        }
    }

    @Before
    public void setUp(Scenario name) {
        setup.setScenario(name);
        setup.startDriver();
    }

    @After
    public void tearDown() {
        setup.stopDriver();
    }
}