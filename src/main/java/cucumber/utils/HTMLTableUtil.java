package cucumber.utils;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.stepDefinitions.AbstractStepDefinition;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import static com.wagnerandade.coollection.Coollection.*;

import java.util.*;


public class HTMLTableUtil {
    protected static String colName;

    public static List<TableDataCollection> _tableDataCollection = new ArrayList<TableDataCollection>();

    public static List<String> controlsWitinTableCell = new ArrayList<String>();

    public static List<TableDataCollection> ReadTable(WebElement table) {
        List<WebElement> cellElements;
        //Get all the table columns
        List<WebElement> columns = table.findElements(By.tagName("th"));
        List<String> controlsWitinTableCell = HTMLTableUtil.controlsWitinTableCell;
        //Get all the table rows
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        //create the row index
        int rowIndex = 0;

        if (columns.size() <= 0)
            rowIndex = 1;

        for (WebElement rowElement : rows) {

            int colIndex = 0;
            List<WebElement> colData = rowElement.findElements(By.tagName("td"));
            for (WebElement colValue : colData) {
                TableDataCollection collection = new TableDataCollection();

                //record the row number
                collection.setRowNumber(rowIndex);

                //record column index
                collection.setColumnNumber(colIndex + 1);

                //record the table cell td object
                collection.setTableCell(colValue);

                //record the table column name is there is a table header
                if (columns.size() > 0) {
                    if (new String(columns.get(colIndex).getText().toString().trim()).equals(""))
                        colName = "Column" + colIndex;
                    else
                        colName = columns.get(colIndex).getText();
                    collection.setColName(colName);
                } else {
                    colName = "Column" + colIndex;
                }

                //record the table column value
                collection.setColValue(colValue.getText().toString());

                //capture special cell values
                if (controlsWitinTableCell.size() > 0) {
                    //check to see if the column is a special column with buttons,checkbox etc in it and you want to capture it
                    if (new String(colValue.getText().toString().trim()).equals("")) {
                        //iterate through the controlsWitinTableCell list and capture all required elements with in a table cell
                        for (String controlType : controlsWitinTableCell) {
                            cellElements = colValue.findElements(By.tagName(controlType)); //assumption there is only one allowed child element with in the column
                            if (cellElements.size() > 0) {
                                if (new String(cellElements.get(0).getTagName().trim().toLowerCase().toString()).equals(controlType.trim().toLowerCase())) //match found so capture the element
                                {
                                    collection.setSpecialColumnValues(cellElements);
                                }
                            }
                        }

                    }
                    /*else
                        collection.setSpecialColumnValues(null);*/
                }
                _tableDataCollection.add(collection);
                //Move to next column
                colIndex++;
            }
            //Move to next row
            rowIndex++;
        }
        return _tableDataCollection;
    }

    //Read the cell value for a particular column in  a specific row number of a html table
    public static String ReadCell(String columnName, int rowNumber) {
        Object cellData = from(_tableDataCollection).where("getColName", eq(columnName)).and("getRowNumber", eq(rowNumber)).all().get(0).colValue;
        return cellData.toString();
    }

    /*returns the WebElement cell reference with in a row of a  HTML Table */
    public static WebElement TableCell(String columnName, int rowNumber) {
        Object cellObject = from(_tableDataCollection).where("getColName", eq(columnName)).and("getRowNumber", eq(rowNumber)).all().get(0).tableCell;
        return (WebElement) cellObject;
    }

    public static WebElement TableCell(int columnNumber, int rowNumber) {
        Object cellObject = from(_tableDataCollection).where("getColumnNumber", eq(columnNumber)).and("getRowNumber", eq(rowNumber)).all().get(0).tableCell;
        return (WebElement) cellObject;
    }

    //gets the column count
    public static int GetTableColumnCount() {
        //not yet implimented
        int colCount = -1;
        Object cellObject = from(_tableDataCollection).all().get(0).columnNumber;
        return colCount;
    }

    /*
        Get the row index in a HTML table row <TR> based on the Key Column Name and Key Column Value/
     */
    public static int TableRowIndex(String keyColumnName, String keyColumnValue) {
        int rowIndex = -1;
        try {
            Object data = from(HTMLTableUtil._tableDataCollection).where("getColName", eq(keyColumnName)).and("getColValue", eq(keyColumnValue)).all().get(0).rowNumber;
            rowIndex = (int) data;
            return rowIndex;
        } catch (Exception e) {

        }
        return -1;
    }

    /*
    Get the row index in a HTML table column <TD> based on the Key Column Name /
 */
    public static int TableColumnIndex(String keyColumnName) {
        int rowIndex = -1;
        try {
            Object data = from(HTMLTableUtil._tableDataCollection).where("getColName", eq(keyColumnName)).all().get(0).columnNumber;
            rowIndex = (int) data;
            return rowIndex;
        } catch (Exception e) {

        }
        return -1;
    }

    /*Gets the table row index based on the key column value on the table.If there is more than
     * one ket column it gets the first match,so its recommended to use this method only if the data in the
     * HTML table is unique to get accurate results*/
    public static int TableRowIndex(String keyColumnValue) {
        int rowIndex = -1;
        try {
            Object data = from(HTMLTableUtil._tableDataCollection).where("getColValue", eq(keyColumnValue)).all().get(0).rowNumber;
            rowIndex = (int) data;
            return rowIndex;
        } catch (Exception e) {

        }
        return -1;
    }

    public static boolean IsElementMatched(WebElement element, Map<String, String> domCharactersticsToMatch, WebDriver driver) {
        boolean flag = true;
        String jsCommand = "";
        for (String jsCmdProperty : domCharactersticsToMatch.keySet()) {
            Object elementProperty = JavaScriptUtils.ExecuteJavaScript(jsCmdProperty, element, driver);
            String domValueToMatch = domCharactersticsToMatch.get(jsCmdProperty);
            //check to see if elementProperty and domValueToMatch are equeal
            if (!new String(elementProperty.toString().toLowerCase().trim()).equals(domValueToMatch.toString().toLowerCase().trim())) {
                flag = false;
            }
        }
        return flag;
    }

    public static boolean IsElementMatchedImage(WebElement element, Map<String, String> domCharactersticsToMatch, WebDriver driver) {
        boolean flag = false;
        String jsCommand = "";
        //make sure that this is not contain text
        if (element.getText().contentEquals(""))
            return element.findElement(By.tagName("input")).getAttribute("type").contains(domCharactersticsToMatch.get("textContent"));

        return flag;
    }

    public static boolean IsElementMatched(WebElement element, String domCharactersticsToMatch) {
        boolean flag = false;
        if (domCharactersticsToMatch.toLowerCase().contentEquals(element.getText().toLowerCase())) {
            flag = true;
        }
        return flag;
    }

    /*
        Get the control reference on which the click action has to be performed inside a HTML table cell.
        controlToClickCharacteristic is contains filter where TagName is one key on position 0 and the other key is in position 1

        For Example

        Map<String,String> controlToClickCharacteristic =  new HashMap<>();
        controlToClickCharacteristic.put("TagName","A");
        controlToClickCharacteristic.put("className","btn btn-default btn-xcrud btn btn-warning");

     */
    public static WebElement ChildObjectWithinTableCell(WebElement tbl, int rowIndex, Map<String, String> controlToClickCharacteristic, WebDriver driver) {
        try {
            WebElement tBody = null;
            WebElement tr = null;

            //Check to See if the Table has Thead and TBody tags
            try {
                tBody = tbl.findElement(By.tagName("tbody"));
            } catch (NoSuchElementException e) {

            } catch (StaleElementReferenceException e) {

            }

            String tblXpath = JavaScriptUtils.getAbsoluteXPath(tbl, driver);

            if (tBody == null) {
                tr = tbl.findElement(By.xpath(tblXpath + "/tr[" + rowIndex + "]"));
            } else {
                tr = tbl.findElement(By.xpath(tblXpath + "/tbody[1]/tr[" + ++rowIndex + "]"));
            }

            List<WebElement> columns = tr.findElements(By.tagName("td"));

            for (WebElement column : columns) {

                //Iterate inside the child elements of the column and find the match
                String childElementTagName = controlToClickCharacteristic.get("tagName").toString();

                //get the list of all children which suit the filter criteria by tag name
                List<WebElement> children = column.findElements(By.tagName(childElementTagName));

                if (children.size() <= 0) //we are looking for TD not the children inside it
                {
                    if (controlToClickCharacteristic.get("textContent").toString().contains("image")|| controlToClickCharacteristic.get("textContent").toString().contains("checkbox")) {
                        if (IsElementMatchedImage(column, controlToClickCharacteristic, driver))
                            return column;
                    } else if (IsElementMatched(column, controlToClickCharacteristic, driver)) {
                        return column;
                    }
                } else //there are children inside the TD which needs to match the filter criteria
                {
                    //get he value for tagName key from the controlToClickCharacteristic collection
                    for (WebElement child : children) {
                        //execute Javascript based on the key in controlToClickCharacteristic
                        if (IsElementMatched(child, controlToClickCharacteristic, driver)) {
                            return child;
                        }
                    }
                }

            }
        } catch (Exception e) {

        }
        return null;
    }



    public static void scrollVertically(WebElement element, WebDriver driver) throws InterruptedException {
        JavascriptExecutor js;
        js = (JavascriptExecutor) driver;
        int offset_top = element.getLocation().getY();
        js.executeScript("scrollTo(0, arguments[0]); return [];", offset_top);
        Thread.sleep(2000);
    }

    //Click on specific cell in the table
    public static boolean ClickCell(WebElement tbl, String keyColumnValue, String rowLineMatch, Map<String, String> controlToClickCharacteristics, boolean IsLogError, WebDriver driver) {
        try {
            //Read the table into a collection
            HTMLTableUtil.ReadTable(tbl);

            //get the row index based on the keyColumnName and keyColumnValue
            //Check if we want to click on the image of a cell)
            int rowIndex;
            if (keyColumnValue.contentEquals("image")||keyColumnValue.contentEquals("checkbox")) {
                rowIndex = HTMLTableUtil.TableRowIndex(rowLineMatch);
            } else
                rowIndex = HTMLTableUtil.TableRowIndex(keyColumnValue);
            if (rowIndex == -1) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the table row index for  Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return false;
                //Log the Error
            }

            //get the cell reference on which the click action has to be performed
            WebElement clickControl = HTMLTableUtil.ChildObjectWithinTableCell(tbl, rowIndex, controlToClickCharacteristics, driver);
            if (clickControl == null) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the child element to click in the table for Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return false;
                //Log Error
            }

            scrollVertically(clickControl, driver);

            //click on the control
            clickControl.click();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }





    /**
     * The returns true of the ColumnTitleValueToFind is in the Row of the key ColumnName/Value - This is used for the refresh page where an item changes with time
     *
     * @param tbl
     * @param keyColumnName                 - Unique key
     * @param keyColumnValue                - Unique value
     * @param ColumnTitleValueToFind        - This is the actual value in the row we want check is equal to (note this value changes)
     * @param controlToClickCharacteristics
     * @param IsLogError
     * @return
     */
    public static boolean checkValueFromTableExsists(WebElement tbl, String keyColumnName, String keyColumnValue, String ColumnTitleValueToFind, Map<String, String> controlToClickCharacteristics, boolean IsLogError, WebDriver driver) {
        try {
            boolean flag = true;
            HTMLTableUtil._tableDataCollection = new ArrayList<TableDataCollection>();
            //Read the table into a collection
            HTMLTableUtil.ReadTable(tbl);

            //get the row index based on the keyColumnName and keyColumnValue
            int rowIndex = HTMLTableUtil.TableRowIndex(keyColumnName, keyColumnValue);
            if (rowIndex == -1) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the table row index for Ky Column name  : " + keyColumnName + " and Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return false;
                //Log the Error
            }

            //get the cell reference on which the we want to get the value of the Columvalue
            WebElement clickControl = HTMLTableUtil.ChildObjectWithinTableCell(tbl, rowIndex, controlToClickCharacteristics, ColumnTitleValueToFind, driver);
            if (clickControl == null) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the child element to click in the table for Ky Column name  : " + keyColumnName + " and Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return false;
                //If this goes to Telstra
            } else {
                return true;
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * This method will retrun the value of the child element in the row with Column otherValueIntable
     *
     * @param tbl
     * @param rowIndex
     * @param controlToClickCharacteristic
     * @param otherValueIntable
     * @return
     */
    public static WebElement ChildObjectWithinTableCell(WebElement tbl, int rowIndex, Map<String, String> controlToClickCharacteristic, String otherValueIntable, WebDriver driver) {
        try {
            WebElement tBody = null;
            WebElement tr = null;

            //Check to See if the Table has Thead and TBody tags
            try {
                tBody = tbl.findElement(By.tagName("tbody"));
            } catch (NoSuchElementException e) {

            } catch (StaleElementReferenceException e) {

            }

            String tblXpath = JavaScriptUtils.getAbsoluteXPath(tbl, driver);

            if (tBody == null) {
                tr = tbl.findElement(By.xpath(tblXpath + "/tr[" + rowIndex + "]"));
            } else {
                tr = tbl.findElement(By.xpath(tblXpath + "/tbody[1]/tr[ " + ++rowIndex + "]"));
            }

            List<WebElement> columns = tr.findElements(By.tagName("td"));

            for (WebElement column : columns) {

                //Iterate inside the child elements of the column and find the match
                String childElementTagName = controlToClickCharacteristic.get("tagName").toString();

                //get the list of all children which suit the filter criteria by tag name
                List<WebElement> children = column.findElements(By.tagName(childElementTagName));

                if (children.size() <= 0) //we are looking for TD not the children inside it
                {
                    if (IsElementMatched(column, otherValueIntable)) {
                        return column;
                    }
                } else //there are children inside the TD which needs to match the filter criteria
                {
                    //get he value for tagName key from the controlToClickCharacteristic collection
                    for (WebElement child : children) {
                        //execute Javascript based on the key in controlToClickCharacteristic
                        if (IsElementMatched(child, otherValueIntable)) {
                            return child;
                        }
                    }
                }

            }
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * The returns the data of the Element inside a row, you must give keycolumn name and vlaue to get specific row
     *
     * @param tbl
     * @param keyColumnName                 - Unique key
     * @param keyColumnValue                - Unique value
     * @param ColumnNumberInTable           - This is the actual column number in the TD we want to ge the value form
     * @param controlToClickCharacteristics
     * @param IsLogError
     * @return
     */
    public static WebElement getValueFromTable(WebElement tbl, String keyColumnName, String keyColumnValue, int ColumnNumberInTable, Map<String, String> controlToClickCharacteristics, boolean IsLogError, WebDriver driver) {
        boolean flag = true;
        WebElement clickControl = null;
        HTMLTableUtil._tableDataCollection = new ArrayList<TableDataCollection>();

        try {

            //Read the table into a collection
            HTMLTableUtil.ReadTable(tbl);
            //get the row index based on the keyColumnName and keyColumnValue
            int rowIndex = HTMLTableUtil.TableRowIndex(keyColumnName, keyColumnValue);
            if (rowIndex == -1) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the table row index for Ky Column name  : " + keyColumnName + " and Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return null;
                //Log the Error
            }

            //get the cell reference on which the we want to get the value of the Columvalue
            clickControl = HTMLTableUtil.ChildObjectWithinTableCell(tbl, rowIndex, controlToClickCharacteristics, ColumnNumberInTable, driver);
            if (clickControl == null) {
                if (IsLogError)
                    AbstractStepDefinition.logsysout("Failed to find the child element to click in the table for Ky Column name  : " + keyColumnName + " and Ky Column Value : " + keyColumnValue, LogStatus.FATAL, true);

                return null;
                //If this goes to Telstra
            } else if (clickControl.getText().contains("Telstra")) {
                return null;
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return clickControl;
    }


    /**
     * This method will retrun the value of the child element in the row, we use columntoCehck as an into for the column in the row to return
     *
     * @param tbl
     * @param rowIndex
     * @param controlToClickCharacteristic
     * @param columntoCehck
     * @return
     */
    public static WebElement ChildObjectWithinTableCell(WebElement tbl, int rowIndex, Map<String, String> controlToClickCharacteristic, int columntoCehck, WebDriver driver) {
        try {
            WebElement tBody = null;
            WebElement tr = null;

            //Check to See if the Table has Thead and TBody tags
            try {
                tBody = tbl.findElement(By.tagName("tbody"));
            } catch (NoSuchElementException e) {

            } catch (StaleElementReferenceException e) {

            }

            String tblXpath = JavaScriptUtils.getAbsoluteXPath(tbl, driver);

            if (tBody == null) {
                tr = tbl.findElement(By.xpath(tblXpath + "/tr[" + rowIndex + "]"));
            } else {
                tr = tbl.findElement(By.xpath(tblXpath + "/tbody[1]/tr[" + rowIndex + "]"));
            }

            List<WebElement> columns = tr.findElements(By.tagName("td"));

            int i = 0;
            for (WebElement column : columns) {

                if (i == columntoCehck) {
                    //Iterate inside the child elements of the column and find the match
                    String childElementTagName = controlToClickCharacteristic.get("tagName").toString();

                    //get the list of all children which suit the filter criteria by tag name
                    List<WebElement> children = column.findElements(By.tagName(childElementTagName));

                    if (children.size() <= 0) //we are looking for TD not the children inside it
                    {
                        return column;
                    }
                }
                i++;
            }
        } catch (Exception e) {

        }
        return null;
    }


    public static class TableDataCollection {
        public int rowNumber;

        public int getRowNumber() {
            return rowNumber;
        }

        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }

        public int columnNumber;

        public int getColumnNumber() {
            return columnNumber;
        }

        public void setColumnNumber(int colNumber) {
            this.columnNumber = colNumber;
        }

        public int rowCount;

        public int getRowCount() {
            return rowCount;
        }

        public void setRowCount(int rowCount) {
            this.rowCount = rowCount;
        }

        public String colName;

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            this.colName = colName;
        }

        public String colValue;

        public String getColValue() {
            return colValue;
        }

        public void setColValue(String colValue) {
            this.colValue = colValue;
        }

        public List<WebElement> specialColumnValues;

        public List<WebElement> getSpecialColumnValues() {
            return specialColumnValues;
        }

        public void setSpecialColumnValues(List<WebElement> spclColumnValues) {
            specialColumnValues = spclColumnValues;
        }

        public WebElement tableCell;

        public WebElement getTableCell() {
            return tableCell;
        }

        public void setTableCell(WebElement tableCell) {
            this.tableCell = tableCell;
        }
    }


}
