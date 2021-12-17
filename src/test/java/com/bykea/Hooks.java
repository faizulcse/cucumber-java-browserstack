package com.bykea;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    private final BaseSetup baseSetup = new BaseSetup();

    @Before()
    public void setUp() {
        baseSetup.startLocalTest();
        baseSetup.startDriver();
    }

    @After()
    public void tearDown() {
        baseSetup.stopDriver();
        baseSetup.stopLocalTest();
    }
}
