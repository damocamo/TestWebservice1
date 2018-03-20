package cucumber.stepDefinitions;


import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.java.en.And;
import cucumber.pageObject.LoginPage;

public class LoginPageStepDefinition extends AbstractStepDefinition {
    LoginPage loginPage = getLoginPage();

    /**
     * This method simple enters the username and password in the application
     *
     * @throws InterruptedException
     */
    @And("^I enter \"([^\"]*)\" and \"([^\"]*)\"$")
    public void enterUserNamePassword(String userNameText, String passwordText) throws InterruptedException {
        //I enter <userName> and <password>
        logger.log(LogStatus.INFO, "STEP: I enter <userName> and <password>");
        loginPage.logintoApplication(userNameText,passwordText);
        AbstractStepDefinition.logsysout("Entered user name and password", LogStatus.PASS, true);
    }
}