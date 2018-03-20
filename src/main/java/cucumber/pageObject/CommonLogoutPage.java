package cucumber.pageObject;

import cucumber.utils.CustomWaits;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CommonLogoutPage extends AbstractPage {

    @FindBy(id = "ctl00_logout")
    private WebElement logoutButton;

    public CommonLogoutPage(WebDriver driver) {
        super(driver);
    }

    public void logOut() {
        getCustomWaitsPage().scrollToTop(logoutButton);
        logoutButton.click();
    }
}

