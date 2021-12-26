package com.bykea.stepdefs;

import com.bykea.utils.BaseSetup;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {
    BaseSetup setup = new BaseSetup();

    static {
        if (BaseSetup.BS) {
            BaseSetup.enableLocalTesting();
            Runtime.getRuntime().addShutdownHook(new Thread(BaseSetup::disableLocalTesting));
        }
    }

    @Before
    public void setUp(Scenario name) {
        BaseSetup.scenario = name;
        setup.startDriver();
    }

    @After
    public void tearDown() {
        setup.stopDriver();
    }
}