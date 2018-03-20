@Login
Feature: This is a simple test to log into the application

  Scenario Outline: Loginto the website
    Given I initialise a new report
    And I record the <testName> and <Tags>
    When I navigate to the Test Website
    And I enter <username> and <password>
    Then Validate im on WebOrders Page
    And I click on LogoutButton

    Examples:
      | Tags       | testName               | username | password |
      | "Version1" | "Log into Applicaiton" | "Tester" | "test"   |

  Scenario: Show Report
    Given Complete and display report no.
