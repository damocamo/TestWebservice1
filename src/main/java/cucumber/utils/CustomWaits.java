package cucumber.utils;

import com.google.common.base.Predicate;
import cucumber.pageObject.AbstractPage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CustomWaits extends AbstractPage {

    public CustomWaits(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits for the specified text to be present on the page.
     *
     * @param text             - the text to wait for
     * @param driver           - the active WebDriver session
     * @param timeoutInSeconds - the maximum time to wait before throwing an exception
     */
    public static void waitForTextPresent(final String text, final WebDriver driver, final int timeoutInSeconds) {
        new WebDriverWait(driver, timeoutInSeconds).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.getPageSource().contains(text);
            }
        });
    }
    public static boolean isClickable(WebElement webe,WebDriver driver,final int timeoutInSecond) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeoutInSecond);
            wait.until(ExpectedConditions.elementToBeClickable(webe));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for the specified element to be visible on the page.
     *
     * @param element          - the element to wait for
     * @param driver           - the active WebDriver session
     * @param timeoutInSeconds - the maximum time to wait before throwing an exception
     */
    public static void waitForElementVisible(final WebElement element, final WebDriver driver,
                                             final int timeoutInSeconds) {

        try {
            Thread.sleep(500);
            new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void waitForTextInElementToBeVisible(WebDriver driver, WebElement element, String attributeValue, String textValue) {
        new WebDriverWait(driver, 5).until(ExpectedConditions.attributeContains(element, attributeValue, textValue));
    }

    // /TO FIX if need be
    public static void waitForElementVisibleINT(final WebElement element, final WebDriver driver,
                                                final int timeoutInSeconds) {
        new WebDriverWait(driver, timeoutInSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    public static void scrollToBottom(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void scrollToTop(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public static void scrollVertically(WebDriver driver, int offsetTop) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("scrollTo(0, arguments[0]); return [];", offsetTop);
        Thread.sleep(2000);
    }
}
