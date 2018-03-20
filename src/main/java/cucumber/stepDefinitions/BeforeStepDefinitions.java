package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.pageObject.AbstractPage;

public class BeforeStepDefinitions extends AbstractStepDefinition {

    // public static Integer logIndex = 0;

    /**
     * Before each test is run, get the scenario name (for screenshots)
     *
     * @param scenario
     */
    @Before
    public void before(Scenario scenario) {
        // Declare scenario name to get name for report directory
        AfterStepDefinitions.beforeScenario = scenario;
        setfeature(scenario.getId().split(";")[0]);

    }

    /**
     * Sets Up the reports and starts a new log
     */
    @Given("^I initialise a new report$")
    public void iInitialiseANewReport() {
        startNewLog = true;
    }

    //WebTest
    /**
     * This is used to input the data to set up the logS
     *
     * @param testName
     * @param tags
         */
    @And("^I record the \"([^\"]*)\" and \"([^\"]*)\"$")
    //  And I record the <testName> and <Tags>
    public void setTestName(String testName, String tags) {
        setTestName(testName); // Tc01 - Export request result
        setTestTags(tags); // Release Version
        setupLog(); // Finally, setup the log
        screenshotIndex = 1;
    }


}