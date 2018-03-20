package cucumber.pageObject;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.stepDefinitions.AbstractStepDefinition;
import cucumber.stepDefinitions.AfterStepDefinitions;
import cucumber.utils.CustomWaits;
import cucumber.utils.PropertyManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;

public class AbstractPage {

    // I find the elements by ID, link text as its faster. Xpath should be last choice...
    @FindBy(linkText = "View all orders")
    public static WebElement viewAllOrdersLink;

    @FindBy(linkText = "View all products")
    public static WebElement viewAllProductsLink;

    @FindBy(linkText = "Order")
    public static WebElement orderLink;

    // Strings for environment
    static String envWorkstream = "";

    // Web driver
    protected static WebDriver driver;

    // Property
    public static PropertyManager pmanger = getPmanger(); // Note: this will get a different instance of Pmanger to that initialised in UI.java

    // Generate Property make sure its setup
    public static PropertyManager getPmanger() {
        if (pmanger == null) {
            pmanger = new PropertyManager();
            pmanger.generateProperty();
        }
        return pmanger;
    }

    // CustomWaits instance
    protected static CustomWaits getCustomWaitsPage() {
        return AbstractStepDefinition.getCustomWaitsPage();
    }

    // Login page
    private String LoginPage = "";

    // Constructor
    public AbstractPage(WebDriver driver) {
        AbstractPage.driver = driver;
    }

    public static void setWorkstream(String workstream) {
        envWorkstream = workstream;
    }

    public static String getWorkstream() {
        return envWorkstream;
    }


    /**
     * Methods simply goes to the login page (this use the parameter file where to
     * navigate
     *
     * @return
     */
    public cucumber.pageObject.LoginPage navigateToWebApp() {
        LoginPage = pmanger.getEnvironmentIP();
        driver.navigate().to(LoginPage);
        return PageFactory.initElements(driver, cucumber.pageObject.LoginPage.class);
    }

    // gets the page title
    public String getPageTitle() {
        return driver.getTitle();
    }

}
