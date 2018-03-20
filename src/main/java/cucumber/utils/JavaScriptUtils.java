package cucumber.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtils {
    protected static JavascriptExecutor executeJavaScript;

    public static Object ExecuteJavaScript(String javaScriptCommand, WebElement webElement, WebDriver driver) {
        try {
            executeJavaScript = (JavascriptExecutor) driver;
            Object value = executeJavaScript.executeScript("return arguments[0]." + javaScriptCommand, webElement);
            return value;
        } catch (Exception e) {

        }
        return null;
    }

    public static String getAbsoluteXPath(WebElement element, WebDriver driver) {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "function absoluteXPath(element) {" +
                        "var comp, comps = [];" +
                        "var parent = null;" +
                        "var xpath = '';" +
                        "var getPos = function(element) {" +
                        "var position = 1, curNode;" +
                        "if (element.nodeType == Node.ATTRIBUTE_NODE) {" +
                        "return null;" +
                        "}" +
                        "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling)" +
                        "{" +
                        "if (curNode.nodeName == element.nodeName) {" +
                        "++position;" +
                        "}" +
                        "}" +
                        "return position;" +
                        "};" +

                        "if (element instanceof Document) {" +
                        "return '/';" +
                        "}" +
                        "for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) " +
                        "{" +
                        "comp = comps[comps.length] = {};" +
                        "switch (element.nodeType) {" +
                        "case Node.TEXT_NODE:" +
                        "comp.name = 'text()';" +
                        "break;" +
                        "case Node.ATTRIBUTE_NODE:" +
                        "comp.name = '@' + element.nodeName;" +
                        "break;" +
                        "case Node.PROCESSING_INSTRUCTION_NODE:" +
                        "comp.name = 'processing-instruction()';" +
                        "break;" +
                        "case Node.COMMENT_NODE:" +
                        "comp.name = 'comment()';" +
                        "break;" +
                        "case Node.ELEMENT_NODE:" +
                        "comp.name = element.nodeName;" +
                        "break;" +
                        "}" +
                        "comp.position = getPos(element);" +
                        "}" +

                        "for (var i = comps.length - 1; i >= 0; i--) " +
                        "{" +
                        "comp = comps[i];" +
                        "xpath += '/' + comp.name.toLowerCase();" +
                        "if (comp.position !== null) {" +
                        "xpath += '[' + comp.position + ']';" +
                        "}" +
                        "}" +

                        "return xpath;" +

                        "} return absoluteXPath(arguments[0]);", element);
    }

}
