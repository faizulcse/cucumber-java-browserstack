package com.bykea.stepdefs;

import com.bykea.utils.BaseSetup;
import io.cucumber.java.*;

public class Hooks extends BaseSetup {
    @Before
    public void setUp(Scenario scenario) {
        if (RUN_BS)
            enableLocalTesting();
        startDriver(scenario);
    }

    @After
    public void tearDown() {
        stopDriver();
        if (RUN_BS)
            disableLocalTesting();
    }
}
