package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.pageObject.LoginPage;
import cucumber.pageObject.OrdersPage;
import cucumber.pageObject.WebOrdersPage;
import org.junit.Assert;

public class WebOrdersStepDefinition extends AbstractStepDefinition {
    WebOrdersPage webOrders = getWebOrdersPage();


    /**
     * This step simply validates that we are on the WebOrders page by checking page title
     *
     */
    @Then("^Validate im on WebOrders Page")
    public void validateWebOrders() {
        logger.log(LogStatus.INFO, "STEP: Validate im on WebOrders Page");
        Assert.assertEquals(webOrders.getPageTitle(), Ordertitle);
        AfterStepDefinitions.takeScreenshot("On WebOrders Page.", false, null);
        AbstractStepDefinition.logsysout("On WebOrders Page.", LogStatus.PASS, true);

    }

    /**
     * This step navigates to Order Screen within the application and checks page title
     *
     */
    @Then("^I navigate to Order Screen$")
    public void navigateToOrderScreen() throws Throwable {
        logger.log(LogStatus.INFO, "STEP: I navigate to Order Screen");
        webOrders.navigateToOrders();
        //Wait for text to be present
        getCustomWaitsPage().waitForTextPresent(Ordertitle, driver, pmanger.getTime());
        //Make sure we are on the Orders Page
        Assert.assertEquals(webOrders.getPageTitle(), Ordertitle);
        AfterStepDefinitions.takeScreenshot("On Orders Page.", false, null);
        AbstractStepDefinition.logsysout("On Order Page.", LogStatus.PASS, true);

    }

    /**
     * This step logs out of the application and then checks we are on the Login Page
     *
     */
    @Then("^I click on LogoutButton")
    public void clickLogout() {
        logger.log(LogStatus.INFO, "STEP: I click on LogoutButton");
        webOrders.logOut();
        //Wait for login Page title to be present
        getCustomWaitsPage().waitForTextPresent(LoginTitle, driver, pmanger.getTime());
        AbstractStepDefinition.logsysout("Logged out of the System", LogStatus.PASS, true);
    }


}
