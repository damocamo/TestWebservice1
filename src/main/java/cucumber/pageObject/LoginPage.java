package cucumber.pageObject;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.stepDefinitions.AbstractStepDefinition;
import cucumber.utils.PropertyManager;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class LoginPage extends AbstractPage {

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    PropertyManager pmanger = getPmanger();
    //Finding by ID fastest
    @FindBy(id = "ctl00_MainContent_username")
    private WebElement userName;

    @FindBy(id = "ctl00_MainContent_password")
    private WebElement password;

    @FindBy(id = "ctl00_MainContent_login_button")
    private WebElement loginButton;


    /**
     * Sends the user name and password to the page and returns the next page
     *
     * @param userNameText
     * @param passwordText
     * @return WebOrders
     * @throws InterruptedException
     */
    public WebOrdersPage logintoApplication(String userNameText, String passwordText) throws InterruptedException {
        try {
            AbstractStepDefinition.logsysout("Entering username and password...", LogStatus.INFO, false);
            getCustomWaitsPage().waitForElementVisible(loginButton, driver, pmanger.getTime());
            userName.sendKeys(userNameText);
            password.clear();
            password.sendKeys(passwordText);
            AbstractStepDefinition.logsysout("Clicking login...", LogStatus.INFO, false);
            loginButton.click();
       } catch (Exception e) {
            AbstractStepDefinition.logsysout("Unable to log into the page.", LogStatus.FATAL, true);
            AbstractStepDefinition.printStackTrace(e);
            Assert.assertTrue(false);
        }
        return PageFactory.initElements(driver, WebOrdersPage.class);
    }


}
