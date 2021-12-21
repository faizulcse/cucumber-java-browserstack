package com.bykea;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks extends BaseSetup {
    @Before
    public void setUp(Scenario scenario) {
        startDriver(scenario);
    }

    @After
    public void tearDown(Scenario scenario) {
        stopDriver();
    }
}
