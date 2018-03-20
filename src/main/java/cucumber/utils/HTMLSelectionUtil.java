package cucumber.utils;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class HTMLSelectionUtil {
    public static boolean SelectItemFromDropDown(WebElement elementDropDown, String item) {
        try {
            boolean flag = false;
            Select dropDown = new Select(elementDropDown);
            dropDown.selectByVisibleText(item);

            //TODO:Check to see if the selection has been performed
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

}
