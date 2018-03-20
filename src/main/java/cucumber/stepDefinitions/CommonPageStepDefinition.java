package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.pageObject.LoginPage;
import cucumber.pageObject.OrdersPage;
import cucumber.pageObject.WebOrdersPage;
import cucumber.pageObject.WebOrdersPage;
import cucumber.utils.PropertyManager;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class CommonPageStepDefinition extends AbstractStepDefinition {

    // Static user auth information
    public static String currentUser = null;
    public static String currentPassword = null;
    // Setting up values
    static PropertyManager pmanger = getPmanger();
    // Title get driver and pages,
    WebDriver driver = getDriver();
    LoginPage loginPage = getLoginPage();
    OrdersPage ordersPage = getOrdersPage();
    WebOrdersPage webOrdersPage = getWebOrdersPage();


    /**
     * /**
     * This method simple creates a new page object loads the driver and then
     * navigates to login, returns the main Web Orders object.
     *
     */
    @And("^I navigate to the Test Website$")
    public void iNagivateToTestWebsite() {
        // Log
        logger.log(LogStatus.INFO, "STEP: Log into TestWebsite");

        try {
            logsysout("Navigating to: [" + pmanger.getEnvironmentIP() + "]");
            // Create login page object and navigate to LoginPage
            loginPage.navigateToWebApp();
            //Checking we are actually on the website
            getCustomWaitsPage().waitForTextPresent(LoginTitle, driver, pmanger.getTime());

            Assert.assertEquals(loginPage.getPageTitle(),LoginTitle);
            // Log updates after valid
            logsysout("Step Passed", LogStatus.INFO, true);

        } catch (Exception e) {
            AbstractStepDefinition.logsysout("An error occurred navigating", LogStatus.FATAL, true);
            AbstractStepDefinition.printStackTrace(e);
            Assert.assertTrue(false);
        }
        AbstractStepDefinition.logsysout("Navigated to Web", LogStatus.PASS, true);

    }


    @And("^Validate I have logged out of the system$")
    public void validateLoggedOut() throws Throwable {
        logger.log(LogStatus.INFO, "STEP: Logout of system");
        Assert.assertEquals(loginPage.getPageTitle(),LoginTitle);
        AfterStepDefinitions.takeScreenshot("On Login Page.", false, null);
        AbstractStepDefinition.logsysout("Logged out of the system", LogStatus.PASS, true);
    }



}
