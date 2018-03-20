package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.pageObject.OrdersPage;
import org.junit.Assert;

import java.util.Map;

public class OrdersStepDefinition extends AbstractStepDefinition {
    OrdersPage ordersPage = getOrdersPage();


    /**
     * This method we enter the product we want through the feature file
     * @param product
     * @param quantity
     * @throws Throwable
     */
    @And("^I enter product \"([^\"]*)\" and quantity \"([^\"]*)\"$")
    public void enterProductAndQuantity(String product, String quantity) {
        logger.log(LogStatus.INFO, "STEP: I enter product <product> and quantity <quantity>");

        ordersPage.enterPrdoctQuantity(product, quantity);
        AfterStepDefinitions.takeScreenshot("Orders Page details entered", false, null);
        AbstractStepDefinition.logsysout("Entered product and quantity", LogStatus.PASS, true);
    }

    /**
     * This method we enter the customer details we sent through the feature file
     *
     * @param dataTbl
     */
    @Then("^I enter$")
    public void iEnter(Map<String, String> dataTbl) {
        logger.log(LogStatus.INFO, "STEP: I enter <Details>");
        ordersPage.iEnter(dataTbl);
        AbstractStepDefinition.logsysout("Entered detailed information", LogStatus.PASS, true);
    }
    /**
     * This method we enter the payment information using the datatabl from feature file
     *
     * @param dataTbl
     */
    @Then("^Payment Information$")
    public void paymentInformation(Map<String, String> dataTbl) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Payment Information");
        ordersPage.paymentInformation(dataTbl);
        AfterStepDefinitions.takeScreenshot("Payment entered", false, null);
        AbstractStepDefinition.logsysout("Entered payment information", LogStatus.PASS, true);
    }
    /**
     * This method we simply click process button
     */
    @And("^I click on Process")
    public void clickProcess() {
        logger.log(LogStatus.INFO, "STEP: Process Order");
        ordersPage.clickProcess();
        AbstractStepDefinition.logsysout("Processed Order", LogStatus.PASS, true);
    }

    @And("^I click on Update")
    public void clickUpdate() {
        logger.log(LogStatus.INFO, "STEP: Update Order");
        ordersPage.clickUpdate();
        AbstractStepDefinition.logsysout("Updated Order Checked", LogStatus.PASS, true);
    }

    /**
     * This method we checks we have made the order by the text on the screen
     *
     */
    @Then("^Validate order")
    public void validateOrder() {
        logger.log(LogStatus.INFO, "STEP: Validate Order");
        Assert.assertTrue(ordersPage.validateOrder());
        AfterStepDefinitions.takeScreenshot("Order Created", false, null);
    }
    /**
     * This method we enter the update details we sent through the feature file
     *
     * @param product
     * @param quantity
     */
    @And("^I update product \"([^\"]*)\" and quantity \"([^\"]*)\"$")
    public void updateProductAndQuantity(String product, String quantity) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: I enter product <product> and quantity <quantity>");
        ordersPage.enterPrdoctQuantity(product, quantity);
        AfterStepDefinitions.takeScreenshot("Orders Page details entered", false, null);
        AbstractStepDefinition.logsysout("Update product and quantity", LogStatus.PASS, true);
    }

}
