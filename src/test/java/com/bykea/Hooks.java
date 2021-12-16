package com.bykea;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before()
    public void setUp() {
        new BaseSetup().startDriver();
    }

    @After()
    public void tearDown() {
        new BaseSetup().stopDriver();
    }
}
