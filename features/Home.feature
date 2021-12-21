@login
Feature: Bykea home page feature

  @test
  Scenario: User should be able to login bykea app successfully_3
    Given User open bykea app
    And   User click on the login button
    And   User enter username on the login page
    And   User enter password on the login page
    And   User click on the login submit button
    Then  User should be able to see that user can login successfully