package com.bykea;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    @Before()
    public void setUp() {
        BaseSetup.startLocalTest();
        BaseSetup.startDriver();
    }

    @After()
    public void tearDown() {
        BaseSetup.stopDriver();
        BaseSetup.stopLocalTest();
    }
}
