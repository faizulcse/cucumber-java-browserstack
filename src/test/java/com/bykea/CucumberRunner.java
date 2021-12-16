package com.bykea;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@CucumberOptions(
        features = {"features"},
        glue = {},
        tags = "@test",
        plugin = {"pretty", "html:target/cucumber-html", "json:target/cucumber.json"}
)

@RunWith(Cucumber.class)
public class CucumberRunner {

}