@RemoveOrder
Feature: This test places an oder in the web Application

  Scenario Outline: Loginto the applicaiton place and order and delete the order
    Given I initialise a new report
    And I record the <testName> and <Tags>
    When I navigate to the Test Website
    And I enter <username> and <password>
    Then Validate im on WebOrders Page
    And I navigate to Order Screen
    And I enter product <product> and quantity <quantity>
    And  I enter
      | Name   | <name>   |
      | Street | <street> |
      | City   | <city>   |
      | State  | <state>  |
      | Zip    | <zip>    |
    And Payment Information
      | Card       | <card>       |
      | CardNumber | <cardNumber> |
      | Exp        | <exp>        |
    And I click on Process
    Then Validate order
    And I navigate to View All Orders
    And I delete order with details
      | Name | <name> |
    And Click delete
    Then I check order is removed
      | Name | <name> |
    And I click on LogoutButton

    Examples:
      | Tags       | testName            | username | password | product       | quantity | name   | street | city      | state | zip  | card | cardNumber | exp   |
      | "Version1" | "Removing an Order" | "Tester" | "test"   | "ScreenSaver" | "2"      | Damien | pablo  | Melbourne | Vic   | 3000 | Visa | 123456789  | 20/20 |

  Scenario: Show Report
    Given Complete and display report no.
