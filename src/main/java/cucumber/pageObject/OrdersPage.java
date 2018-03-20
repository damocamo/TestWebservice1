package cucumber.pageObject;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.stepDefinitions.AbstractStepDefinition;
import cucumber.utils.HTMLSelectionUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class OrdersPage extends AbstractPage {

    @FindBy(id = "ctl00_MainContent_fmwOrder_ddlProduct")
    private WebElement dropdownElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_txtQuantity")
    private WebElement quantityElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_txtName")
    private WebElement nameElement;
    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox2")
    private WebElement streetElement;
    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox3")
    private WebElement cityElement;
    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox4")
    private WebElement stateElement;
    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox5")
    private WebElement zipElement;

    @FindBy(how = How.CLASS_NAME, using = "RadioList")
    private WebElement cardElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_cardList_0")
    private WebElement visaElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox6")
    private WebElement cardNumberElement;
    @FindBy(id = "ctl00_MainContent_fmwOrder_TextBox1")
    private WebElement expDateElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_InsertButton")
    private WebElement processElement;

    @FindBy(id = "ctl00_MainContent_fmwOrder_UpdateButton")
    private WebElement updateElement;

    public OrdersPage(WebDriver driver) {
        super(driver);
    }

    public void enterPrdoctQuantity(String product, String quantity) {
        AbstractStepDefinition.logsysout("Entering Product...", LogStatus.INFO, false);
        //Calls utli that does the selection
        HTMLSelectionUtil.SelectItemFromDropDown(dropdownElement,product);
        quantityElement.clear();
        quantityElement.sendKeys(quantity);
    }

    public void iEnter(Map<String, String> map) {

        AbstractStepDefinition.logsysout("Entering user details...", LogStatus.INFO, false);

        //Just putting in some minor checks that the feature files has been entered in correctly
        //Then clear and send keys to each element
        //Name
        if ((map.containsKey("Name")) && (!new String(map.get("Name").trim()).equals(""))) {
            nameElement.clear();
            nameElement.sendKeys(map.get("Name"));
        }
        //Street
        if ((map.containsKey("Street")) && (!new String(map.get("Street").trim()).equals(""))) {
            streetElement.clear();
            streetElement.sendKeys(map.get("Street"));

        }
        //City
        if ((map.containsKey("City")) && (!new String(map.get("City").trim()).equals(""))) {
            cityElement.clear();
            cityElement.sendKeys(map.get("City"));
        }
        //State
        if ((map.containsKey("State")) && (!new String(map.get("State").trim()).equals(""))) {
            stateElement.clear();
            stateElement.sendKeys(map.get("State"));
        }
        //Zip
        if ((map.containsKey("Zip")) && (!new String(map.get("Zip").trim()).equals(""))) {
            zipElement.clear();
            zipElement.sendKeys(map.get("Zip"));
        }
    }

      public void paymentInformation(Map<String, String> map) {
        AbstractStepDefinition.logsysout("Entering Payment...", LogStatus.INFO, false);


        if ((map.containsKey("Card")) && (!new String(map.get("Card").trim()).equals(""))) {
            if (map.get("Card").contains("Visa")) {
                visaElement.click();
            }
        }

        //State
        if ((map.containsKey("CardNumber")) && (!new String(map.get("CardNumber").trim()).equals(""))) {
            cardNumberElement.clear();
            cardNumberElement.sendKeys(map.get("CardNumber"));
        }
        //Zip
        if ((map.containsKey("Exp")) && (!new String(map.get("Exp").trim()).equals(""))) {
            expDateElement.clear();
            expDateElement.sendKeys(map.get("Exp"));
        }
    }

    public void clickProcess() {
        AbstractStepDefinition.logsysout("Clicking Process...", LogStatus.INFO, false);
        processElement.click();
    }

    public boolean validateOrder() {
        return driver.getPageSource().contains("New order has been successfully added.");
    }

    public ViewAllOrders navigateToAllOrders() {
       // AbstractPage.getAllOrdersLink().click();
       viewAllOrdersLink.click();

        return PageFactory.initElements(driver, cucumber.pageObject.ViewAllOrders.class);
    }

    public ViewAllOrders clickUpdate() {
        AbstractStepDefinition.logsysout("Clicking Update...", LogStatus.INFO, false);
        updateElement.click();
        //This takes us back to ViewAllOrdersPage
        return PageFactory.initElements(driver, cucumber.pageObject.ViewAllOrders.class);
    }
}
