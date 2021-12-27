package com.bykea.stepdefs;

import com.bykea.utils.BaseSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.util.ResourceBundle;

public class Hooks {
    BaseSetup setup = new BaseSetup();
    static String localConfig = ResourceBundle.getBundle("config").getString("browserstackLocal");
    static String bsLocal = System.getenv("BROWSERSTACK_LOCAL");
    static boolean isLocal = Boolean.parseBoolean(bsLocal != null ? bsLocal : localConfig);

    static {
        if (isLocal) {
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