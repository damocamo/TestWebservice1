@UpdateOrder
Feature: This test places an oder in the web Application

  Scenario Outline: Loginto the applicaiton and place an order
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
    And I select order with details
      | Name       | <name>       |
      | Street     | <street>     |
      | City       | <city>       |
      | State      | <state>      |
      | Zip        | <zip>        |
      | Card       | <card>       |
      | CardNumber | <cardNumber> |
      | Exp        | <exp>        |

    And I update product <productUpdate> and quantity <quantityUpdate>
    And I click on Update
    Then Validate Update
      | Name           | <name>           |
      | ProductUpdate  | <productUpdate>  |
      | QuantityUpdate | <quantityUpdate> |
    And I click on LogoutButton

    Examples:
      | Tags       | testName            | username | password | product       | quantity | productUpdate | quantityUpdate | name   | street | city      | state | zip  | card | cardNumber | exp   |
      | "Version1" | "Updating an Order" | "Tester" | "test"   | "ScreenSaver" | "2"      | "FamilyAlbum" | "4"            | Damien | pablo  | Melbourne | Vic   | 3000 | Visa | 123456789  | 20/20 |

  Scenario: Show Report
    Given Complete and display report no.
