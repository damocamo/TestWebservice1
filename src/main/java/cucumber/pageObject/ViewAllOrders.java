package cucumber.pageObject;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.stepDefinitions.AbstractStepDefinition;
import cucumber.utils.HTMLTableUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.HashMap;
import java.util.Map;

public class ViewAllOrders extends AbstractPage {

    //Table
    @FindBy(id = "ctl00_MainContent_orderGrid")
    private WebElement tableElement;

    //Table
    @FindBy(id = "ctl00_MainContent_btnDelete")
    private WebElement deleteButton;


    public ViewAllOrders(WebDriver driver) {
        super(driver);
    }


    public boolean selectOrder(Map<String, String> map) {
        Map<String, String> filters = new HashMap<>();
        filters.put("tagName", "TD");
        //Checks that the value of the Name has been entered on the page (we will keep this simple and check name
        if (HTMLTableUtil.checkValueFromTableExsists(tableElement, "Name", map.get("Name"), map.get("Name"), filters, false, driver)) {
            //Now we can confrim we can add tag to check to search impage in table
            filters.put("textContent", "image");
            //click on the order image for row that is found for Name
            return HTMLTableUtil.ClickCell(tableElement, "image", map.get("Name"), filters, false, driver);
        }
        return false;
    }

    public boolean validateUpdate(Map<String, String> map) {
        Map<String, String> filters = new HashMap<>();
        filters.put("tagName", "TD");
        boolean flag = false;
        //Checks that the value of the Name has been entered on the page (we will keep this simple and check name
        if (HTMLTableUtil.checkValueFromTableExsists(tableElement, "Name", map.get("Name"), map.get("Name"), filters, false, driver)) {     //Checks that the update Values are present
            flag = HTMLTableUtil.checkValueFromTableExsists(tableElement, "Product", map.get("ProductUpdate").replace("\"", ""), map.get("ProductUpdate").replace("\"", ""), filters, false, driver);
            if (flag)
                flag = HTMLTableUtil.checkValueFromTableExsists(tableElement, "#", map.get("QuantityUpdate").replace("\"", ""), map.get("QuantityUpdate").replace("\"", ""), filters, false, driver);
        }
        return flag;
    }

    public boolean delete(Map<String, String> map) {
        Map<String, String> filters = new HashMap<>();
        filters.put("tagName", "TD");
        //Checks that the value of the Name has been entered on the page (we will keep this simple and check name
        if (HTMLTableUtil.checkValueFromTableExsists(tableElement, "Name", map.get("Name"), map.get("Name"), filters, false, driver)) {            //Now we can confrim we can add tag to check to search impage in table
            filters.put("textContent", "checkbox");
            //click on the order image for row that is found for Name
            return HTMLTableUtil.ClickCell(tableElement, "checkbox", map.get("Name"), filters, false, driver);
        }
        return false;
    }

    public void clickdelete() {
        AbstractStepDefinition.logsysout("Clicking Delete...", LogStatus.INFO, false);
        deleteButton.click();
    }

    public boolean hasBeenRemoved(Map<String, String> map) {
        Map<String, String> filters = new HashMap<>();
        filters.put("tagName", "TD");
        //If element has been removed we return false (with a not to correct the assert in the test)
        return !HTMLTableUtil.checkValueFromTableExsists(tableElement, "Name", map.get("Name"), map.get("Name"), filters, false, driver);

    }
}
