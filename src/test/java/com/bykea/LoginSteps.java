package com.bykea;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class LoginSteps {
    @Given("^User open bykea app$")
    public void userOpenBykeaApp() {
        System.out.println("Open");
    }

    @And("User click on the login button")
    public void userClickOnTheLoginButton() {
    }

    @And("User enter username on the login page")
    public void userEnterUsernameOnTheLoginPage() {
    }

    @And("User enter password on the login page")
    public void userEnterPasswordOnTheLoginPage() {
    }

    @And("User click on the login submit button")
    public void userClickOnTheLoginSubmitButton() {
    }

    @Then("User should be able to see that user can login successfully")
    public void userShouldBeAbleToSeeThatUserCanLoginSuccessfully() {
    }
}
