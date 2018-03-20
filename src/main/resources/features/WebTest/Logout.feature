@Logout
Feature: This is a simple test to logout of the application

  Scenario Outline: Loginto the website and then Logging Out
    Given I initialise a new report
    And I record the <testName> and <Tags>
    When I navigate to the Test Website
    And I enter <username> and <password>
    Then Validate im on WebOrders Page
    And I click on LogoutButton
    Then Validate I have logged out of the system

    Examples:
      | Tags       | testName                     | username | password |
      | "Version1" | "Log out of the Applicaiton" | "Tester" | "test"   |

  Scenario: Show Report
    Given Complete and display report no.
