package cucumber.pageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class WebOrdersPage extends AbstractPage {

    @FindBy(id = "ctl00_logout")
    private WebElement logoutButton;

    public WebOrdersPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage logOut() {
        getCustomWaitsPage().scrollToTop(logoutButton);
        logoutButton.click();

        return PageFactory.initElements(driver, LoginPage.class);
    }

    public OrdersPage navigateToOrders() {
            orderLink.click();
            return PageFactory.initElements(driver, OrdersPage.class);
        }

}

