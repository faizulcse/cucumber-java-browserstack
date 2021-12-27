package com.bykea.stepdefs;

import com.bykea.utils.BaseSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.ResourceBundle;

public class Hooks {
    BaseSetup setup = new BaseSetup();
    static boolean bsLocal = Boolean.parseBoolean(System.getenv("BROWSERSTACK_LOCAL") == null ? ResourceBundle.getBundle("config").getString("browserstackLocal") : System.getenv("BROWSERSTACK_LOCAL"));

    static {
        if (bsLocal) {
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