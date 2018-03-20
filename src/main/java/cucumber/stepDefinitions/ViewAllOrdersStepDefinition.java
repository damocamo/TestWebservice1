package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.pageObject.ViewAllOrders;
import cucumber.pageObject.WebOrdersPage;
import org.junit.Assert;

import java.util.Map;

public class ViewAllOrdersStepDefinition extends AbstractStepDefinition {
    WebOrdersPage webOrders = getWebOrdersPage();
    ViewAllOrders viewAllOrders = getViewAllOrders();
    String title = "Web Orders";

    /**
     * This method we simply navigate to View All Orders page
     */
    @And("^I navigate to View All Orders$")
    public void iNavigateToViewAllOrders() throws Throwable {
        logger.log(LogStatus.INFO, "STEP: I navigate to View All Orders");
        ordersPage.navigateToAllOrders();
        AbstractStepDefinition.logsysout("Navigated to View All Orders", LogStatus.PASS, true);
    }
    /**
     * This method we select order with the details provided (we are using the Name as the KEY)
     */
    @And("^I select order with details$")
    public void iSelectOrderWithDetails(Map<String, String> dataTbl) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Select Order Details");
        Assert.assertTrue(viewAllOrders.selectOrder(dataTbl));
        AbstractStepDefinition.logsysout("Selected Order Details", LogStatus.PASS, true);
    }
    /**
     * This method we validate the order update (we are using the Name as the KEY, and check other update details)
     */
    @Then("^Validate Update$")
    public void validateUpdate(Map<String, String> dataTbl) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Validate Update");
        Assert.assertTrue(viewAllOrders.validateUpdate(dataTbl));
        AfterStepDefinitions.takeScreenshot("Orders Page details entered", false, null);
        AbstractStepDefinition.logsysout("Update Validated", LogStatus.PASS, true);
    }

    @And("^I delete order with details$")
    public void iDeleteOrderWithDetails(Map<String, String> map) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Delete Selected Order");
        Assert.assertTrue(viewAllOrders.delete(map));
        AbstractStepDefinition.logsysout("Remove Order Item Selected", LogStatus.PASS, true);
    }
    /**
     * This method we click the delete button (previous step selected the row we want to delete)
     */
    @And("^Click delete$")
    public void clickDelete() throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Click Delete");
        viewAllOrders.clickdelete();
        AfterStepDefinitions.takeScreenshot("Deleted", false, null);
        AbstractStepDefinition.logsysout("Removed Order", LogStatus.PASS, true);
    }

    /**
     * This method we validates we have removed the slected item using the key name
     */
    @Then("^Validate order has been removed$")
    public void validateOrderHasBeenRemoved(Map<String, String> map) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Delete Selected Order");
        Assert.assertTrue(viewAllOrders.hasBeenRemoved(map));
        AbstractStepDefinition.logsysout("Remove Order Item Selected", LogStatus.PASS, true);
    }

    @Then("^I check order is removed$")
    public void iCheckOrderIsRemoved(Map<String, String> map) throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Delete Selected Order");
        Assert.assertTrue(viewAllOrders.hasBeenRemoved(map));
        AbstractStepDefinition.logsysout("Remove Order Item Selected", LogStatus.PASS, true);
    }
}