package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.LogStatus;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.pageObject.AbstractPage;
import cucumber.pageObject.CommonLogoutPage;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AfterStepDefinitions extends AbstractStepDefinition {

    public static String MegaFeedName = "_MEGAFEED\\";
    static Scenario beforeScenario;
    static String tempFeedName;
    CommonLogoutPage commonLogoutPage = getCommonLogoutPage();

    String CSVFileHeader = "Workstream,Feed,Test,Result,Tags,Comments"; // These are the heading values for the CSV file.
    static ArrayList<ArrayList<String>> CSVRecords = new ArrayList<ArrayList<String>>(); // This is used to store each test and its details

    /**
     * Actually take the screenshot (if authorised, based on isError property is
     * enabled)
     * <p>
     * Also: if megafeed is enabled, use that log file and name for screenshots
     *
     * @param isError
     * @return
     */
    public static void takeScreenshot(String customText, Boolean isError, WebElement targetElement) {
        // Authorization check
        Boolean authorisedScreenshot = false;
        if (ErrorScreenshotOnly) {
            // Screenshots are only enabled if they are errors
            if (isError) {
                // Screenshot is for error, accept
                authorisedScreenshot = true;
            }
        } else {
            // Screenshots for everything is enabled, accept
            authorisedScreenshot = true;
        }

        // Only take screenshot if meets above criteria
        if (authorisedScreenshot) {
            logsysout("Screenshot is authorised", LogStatus.PASS, false);
            try {

                // Set location of screenshots folder // Example: CHAMELEON/cha---one-number-service
                if (runningTestsByWorkstream)
                    tempFeedName = AbstractPage.getWorkstream() + "\\" + AbstractStepDefinition.getDirectoryFeedName();
                else
                    tempFeedName = AbstractStepDefinition.getDirectoryFeedName();

                // Set relative path of screenshots inside folder above // Example: Screenshots/U4-TC014_TC015/Submission of Request UI Check1(2018-01-29 10:42:55).png
                String timeStampNow = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(Calendar.getInstance().getTime());
                logsysout("Setting screenshot timestamp of: " + timeStampNow, LogStatus.INFO, false);

                // Example: ./Screenshots/U4-TC014_TC015/SubmissionofRequestUICheck1[2018-01-29 10:42:55].png
                String relativeImagePath = "\\Screenshots\\" + getDirectoryTestName() + "\\"
                        + getDirectoryString(beforeScenario.getName(), false) + screenshotIndex + "[" + timeStampNow + "].png";
                String absoluteImagePath = "";

                // Take screenshot, and write to temp file
                int tries = 0;
                File outputfile = new File("./screen.png"); // temporary first location of screenshot
                while (tries < 1) {
                    try {
                        tries++; // start attempt

                        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        //The below method will save the screen shot in d drive with name "screenshot.png"
                        FileUtils.copyFile(scrFile, outputfile);


                    } catch (Exception e) {
                        if (e.toString().contains("UnhandledAlertException")) {
                            Alert alert = driver.switchTo().alert();
                            logsysout("An alert was blocking the screenshot: '" + alert.getText() + "'.", LogStatus.WARNING, true);
                            alert.accept();
                            tries--; // take the screenshot again
                        } else {
                            logsysout("Could not take the screenshot, with timestamp of: " + timeStampNow, LogStatus.ERROR, false);
                            AbstractStepDefinition.printStackTrace(e);
                        }
                    }
                }

                // Configure absolutepath to copy file and log
                if (AbstractStepDefinition.MegaFeedEnabled) {
                    // Example: D://Users/matthew.sumner/projects/_logs/_MEGAFEED/CHAMELEON/cha---one-number-service/Screenshots/U4-TC014_TC015/SubmissionofRequestUICheck1[2018-01-29 10:42:55].png
                    absoluteImagePath = getLogDirectory() + MegaFeedName + tempFeedName + relativeImagePath;

                    // Example: ./CHAMELEON/cha---one-number-service/Screenshots/U4-TC014_TC015/SubmissionofRequestUICheck1[2018-01-29 10:42:55].png
                    relativeImagePath = "\\" + tempFeedName + relativeImagePath; // Re-configure path for megafeed

                } else
                    // Example: D://Users/matthew.sumner/projects/_logs/CHAMELEON/cha---one-number-service/Screenshots/U4-TC014_TC015/SubmissionofRequestUICheck1[2018-01-29 10:42:55].png
                    absoluteImagePath = getLogDirectory() + tempFeedName + relativeImagePath;

                // Delete old screenshots (Only if not keeping historical record)
                if (!keepPreviousRunReports)
                    deleteOldScreenshots(absoluteImagePath);

                // Copy new screenshot
                logsysout("Copying screenshot to: " + absoluteImagePath);
                transferFile(outputfile, new File(absoluteImagePath));

                // Log new screenshot
                relativeImagePath = logger.addScreenCapture("." + relativeImagePath);

                // Crop file to element if necessary TODO
                if (targetElement != null) {

                }

                // Add to index, just incase this test needs multiple
                // screenshots
                screenshotIndex++;

                if (isError) { // Log as error
                    if (childTestEnabled) // Log error in child test
                        childtest.log(LogStatus.ERROR, customText + relativeImagePath);
                    else // Log error in normal test
                        logger.log(LogStatus.ERROR, customText + relativeImagePath);

                } else { // Log as info
                    if (childTestEnabled) // log info in child test
                        childtest.log(LogStatus.INFO, customText + relativeImagePath);
                    else // Log info in normal test
                        logger.log(LogStatus.INFO, customText + relativeImagePath);
                }

                // return image;

            } catch (Exception e) {
                // AbstractStepDefinition.printStackTrace(e);
                logsysout("An error occurred when taking a screenshot: " + e, LogStatus.ERROR, false);
                logsysout("Screenshot was likely unsuccessful. Refer to consoleoutput.txt", LogStatus.INFO, true);
                // return "INFO";
            }
        } else {
            logsysout("Screenshots are currently only enabled for errors");
            // return "ERROR";
        }
    }

    /**
     * This deletes screenshots from a previous date. Using the absolute path of a screenshot, we can get the parent folder -
     * - then cycle through each file.
     *
     * @param absoluteImagePath
     */
    private static void deleteOldScreenshots(String absoluteImagePath) {
        try {
            logsysout("Deleting old screenshots... (Keeping previous reports is disabled)", LogStatus.INFO, false);

            // Get today's date
            String timeStampNow = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

            // Get screenshot parent directory
            File screenshotParentDirectory = new File(absoluteImagePath).getParentFile();

            // Get list of files in screenshot (parent) directory
            File[] directoryListing = screenshotParentDirectory.listFiles();

            // Iterate through each file
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    // if filename doesn't contain today's timestamp, it is old
                    if (!child.getName().contains(timeStampNow)) {
                        logsysout("Deleting old screenshot: " + child.getName());
                        child.delete(); // Delete
                    }
                }
                logsysout("Complete.", LogStatus.INFO, false);

            } else {
                logsysout("Could not properly identify directory contents to delete old screenshots.");
            }

        } catch (Exception e) {
            logsysout("An error occurred when deleting old screenshots: " + e, LogStatus.ERROR, true);
            printStackTrace(e);
        }
    }


    @After()
    public void flushReport(Scenario scenario) {
        if (scenario.isFailed()) {
            AbstractStepDefinition.logsysout("An error occurred during the run of this step.", LogStatus.ERROR, true);
        }
        closeLog(false);
    }

    @Given("^Complete and display report no.")
    public void displayReport() throws Throwable {
        logsysout("Test(s) Finished, launching report...");
        closeLog(true);
    }

    @Given("^Logout from the system")
    public void logout() {
        commonLogoutPage.logOut();
    }
}