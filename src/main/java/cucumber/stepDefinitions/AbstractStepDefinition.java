package cucumber.stepDefinitions;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import cucumber.pageObject.*;
import cucumber.utils.CustomWaits;
import cucumber.utils.PropertyManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class AbstractStepDefinition {

    // These can go in proerties file however ill leave them here for the moment
    public static CommonLogoutPage commonLogoutPage;
    public static ExtentTest logger; // this is what we use to add logs
    public static ExtentTest childtest; // used for logging to a sub-test (e.g. click here for list of ingest files)
    public static Integer screenshotIndex = 1; // used to give unique names to screenshots
    public static Boolean MegaFeedEnabled = true; // all workstream test results in one report
    public static Boolean ErrorScreenshotOnly = false; // screenshots only occur for errors
    public static Boolean HeadlessModeEnabled = false; // non-visible chromedriver
    public static Boolean keepPreviousRunReports = false; // this determines if we timestamp and number the master log dir.
    public static Boolean runningTestsByWorkstream = false; // when enabled, this determines the logic on report directory, appending etc.
    public static Boolean DebugMode = false; // when enabled, most logsysouts will be written to report
    public static Boolean childTestEnabled = false; // used to determine if we log to childtest ExtentTest, or normal logger.
    public static Boolean childTestUsed = false;
    public static String sysoutprefix = "[SYS] "; // standard to make system.out cleaner
    public static Boolean startNewLog = true; // used to determine if log needs to be initialised.
    //// Property
    protected static PropertyManager pmanger = getPmanger();

    //// Setup pages locally
    protected static WebDriver driver; // this is what we set to chromedriver
    protected static LoginPage loginPage;
    protected static WebOrdersPage webOrders;
    protected static OrdersPage ordersPage;
    protected static ViewAllOrders viewAllOrders;

    protected static CustomWaits customWaits; // this is our waits class. E.g. getCustomWaitsPage().waitForPageLoad


    static Boolean startNewMegaFeed = true; // used to determine if need to replace existing megafeed.
    static Integer testErrorCount = 0; // counts all LogStatus.ERROR, .FATAL, and .FAIL's
    // static Boolean firstRun = true;
    //// logger variables
    static ExtentReports report;
    static NetworkMode reportAccessType = NetworkMode.ONLINE; // This determines if generated report can use external CSS (won't work in model / without internet)
    static String currentLog = ""; // This is an absolute link to the report currently being written to
    static String previousLogFeed = "";
    static String previousWorkstream = "";
    //// System output file
    static String SysOutFilename = "Console Output.txt"; // the name of the full system output file
    static String currentSysOut = ""; // This is an absolute link to the consoleoutput.txt currently being written to
    static String currentSysOutLink = "<br><sub><i>" + // Relative link to currentSysOut currently being written to
            "<a href=\"./" + AbstractStepDefinition.SysOutFilename + "\">View Full Output</a>" + "</i></sub>";
    static FileOutputStream sysoutputFile = null; // 1. the stream to print to CurrentSysOut
    static TeeOutputStream sysoutputSplitter = null; // 2. the stream that splits to sysoutputFile and System.out
    static PrintStream sysoutputStream = null; // 3. the stream that goes to splitter (and what we set System.setOut)
    private static String keepPreviousRunReports_InitialisedDirectory = null; // this records the directory for the run
    //// Browser Names
    private static String InternetExplorer = "I.E"; // Name of option in properties.parameters
    private static String IEServer = "IEDriverServer.exe"; // Name of file
    private static String Firefox = "FireFox"; // Name of option in properties.parameters
    private static String FirefoxServer = "geckodriver.exe"; // Name of file
    private static String Chrome = "Chrome"; // Name of option in properties.parameters
    private static String ChromeServer = "chromedriver.exe"; // Name of file
    //// this is used as a global string to ensure we now which page we are on
    // private static String whichFeed = "NOTHING";
    private static String testName = ""; // Example: Name of test
    private static String feedName = ""; // Example: Name of tests specfic
    private static String testTags = ""; // Example: Could be anything
    private static String testIngestFiles = null; //
    private static String featureName; //

    //Page Titles;
    public String Ordertitle = "Web Orders";
    public String LoginTitle = "Web Orders Login";
    String reportConfig = "reportconfig.xml"; // the name of the file that customises the generated report

    //// Generate Property
    public static PropertyManager getPmanger() {
        return AbstractPage.getPmanger();
    }

    /**
     * This simply gets the current timestamp and adds it in brackets
     *
     * @return String
     */
    public static String getcurrentTime() {
        String timeStampNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return "[" + timeStampNow + "] - ";
    }

    /**
     * This calls the method in LaunchTests.java to output the stack trace to System.out.
     * This means, it will also be sent to the systemoutput file.
     *
     * @param e Exception
     */
    public static void printStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        System.out.println("[EXCEPTION]: " + sw.toString());
    }

    /**
     * This is called with system output, to both make a log within the report and
     * add prefix (e.g.'[SYS]') beforehand
     *
     * @param sysout The text to print out
     */
    public static void logsysout(String sysout) {

        // Output to consoleoutput FIRST.
        System.out.println(sysoutprefix + getcurrentTime() + sysout);

        if (DebugMode) {
            if (childTestEnabled)
                childtest.log(LogStatus.INFO, sysoutprefix + getcurrentTime() + sysout);
            else
                logger.log(LogStatus.INFO, sysoutprefix + getcurrentTime() + sysout);
        }
    }

    /**
     * This is called with system output, to both make a log within the report and
     * add prefix (e.g.'[SYS]') beforehand, with extra options:
     * <p>
     * [1. Custom LogStatus] [2. forceLogEntry if debug mode is off]
     *
     * @param sysout        The text to print out
     * @param status        The tag (INFO, ERROR etc.)
     * @param forceLogEntry (false = only display in report in Debug Mode)
     */
    public static void logsysout(String sysout, LogStatus status, Boolean forceLogEntry) {
        // Print caller class if error
        if (status == LogStatus.ERROR || status == LogStatus.FATAL)
            sysout = sysout + System.lineSeparator() + "<br><sub>*" + getCallerClassName() + "*</sub> " + currentSysOutLink;

        if (DebugMode) { // Log everything
            if (childTestEnabled)
                childtest.log(status, sysoutprefix + sysout);
            else
                logger.log(status, sysoutprefix + sysout);

        } else if (forceLogEntry) { // log selected w/o prefix
            if (childTestEnabled)
                childtest.log(status, sysout);
            else
                logger.log(status, sysout);
        }
        System.out.println(sysoutprefix + getcurrentTime() + sysout);

        // update no of errors
        if (status == LogStatus.ERROR || status == LogStatus.FATAL || status == LogStatus.FAIL) {
            // if (status == LogStatus.FATAL)
            if (forceLogEntry)
                AfterStepDefinitions.takeScreenshot("Reason for Screenshot: '" + status.name().toLowerCase() + "'.", true, null);
            testErrorCount++;
        }

    }


    /**
     * This just records the caller.
     *
     * @return String
     */
    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        return stElements[3].toString();// Arrays.toString(stElements);
    }

    /**
     * Uses the browser and location of the driver to create a new driver or return
     * the driver
     *
     * @param Browser  The browser option selected
     * @param Location The location of the webdriver
     * @return WebDriver
     */
    protected static WebDriver setDriver(String Browser, String Location) {

        if (driver == null) {
            logsysout("Loading WebDriver: " + Browser + " (" + Location + ")");

            // DesiredCapabilities capabilities = null;

            if (Browser.contains(Firefox)) {
                File file = new File(Location + FirefoxServer);
                System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
                // capabilities = DesiredCapabilities.firefox();
                driver = new FirefoxDriver();

            } else if (Browser.contains(InternetExplorer)) {
                File file = new File(Location + IEServer);
                System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
                // capabilities = DesiredCapabilities.internetExplorer();
                driver = new InternetExplorerDriver();

            } else if (Browser.contains(Chrome)) {
                File file = new File(Location + ChromeServer);
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

                ChromeOptions options = new ChromeOptions();

                // Headless Mode (if enabled via UI -> Add options to Google Chrome)
                if (HeadlessModeEnabled) {
                    options.addArguments("headless");
                    options.addArguments("window-size=1600x900");

                    // Another attempt to download via Headless
                    String defaultDownloadDir = System.getProperty("user.home") + "/Downloads/";
                    HashMap<String, Object> ChromePrefs = new HashMap<>();
                    ChromePrefs.put("profile.default_content_settings.popups", 0);
                    ChromePrefs.put("download.default_directory", defaultDownloadDir);
                    ChromePrefs.put("browser.set_download_behavior", "{ behavior: 'allow' , downloadPath: '" + defaultDownloadDir + "'}");
                    //options.addArguments("disable-gpu");
                    options.setExperimentalOption("prefs", ChromePrefs);
                }
                driver = new ChromeDriver(options);

            } else {
                logsysout("Browser not chosen. Fix properties file.", LogStatus.FATAL, false);
                System.exit(0);
            }

            driver.manage().window().maximize();

            // Set implicit page load timeout... only required once for duration
            // of driver
            driver.manage().timeouts().pageLoadTimeout(pmanger.getTime(), TimeUnit.SECONDS);

        }
        return driver;
    }

    /**
     * Uses the browser and location of the driver to create a new driver or return
     * the driver
     *
     * @return WebDriver
     */
    public static WebDriver getDriver() {
        // If exists, return, otherwise cant
        if (driver != null)
            return driver;
        else {
            return setDriver(pmanger.getBrowser(), pmanger.getLocationDriver());
        }
    }

    /**
     * This returns the feed name set at beginning of log file.
     *
     * @return Feed Name
     */
    public static String getFeedName() {
        // Example: Basic Subscriber - Account Number
        return feedName;
    }

    /**
     * This returns the test tags set at beginning of test
     *
     * @return Test Tags
     **/
    public static String[] getTestTags() {
        return testTags.split(",");
    }

    /**
     * Sets the test tags e.g. CR114,N18.1
     *
     * @param tags comma-delimited string
     */
    protected void setTestTags(String tags) {
        testTags = tags;
    }

    /**
     * This returns a directory-safe version of the feedname.
     *
     * @return Feed Name for Directory
     */
    public static String getDirectoryFeedName() {
        return getDirectoryString(getFeedName(), true);
    }

    /**
     * This returns the test name set at beginning of test
     *
     * @return Test Name
     */
    public static String getTestName() {
        return testName;
    }

    /**
     * Sets the test name e.g. TC20 - Submission of Request blah
     *
     * @param name
     */
    protected void setTestName(String name) {
        testName = name;
    }

    /**
     * This returns a directory-safe version of the testname, and only includes TC
     * ID. (not full test name)
     *
     * @return Test Name for Directory
     */
    public static String getDirectoryTestName() {

        String legalName = getDirectoryString(getTestName(), false);

        // Snip the name, split by dashes
        String partslegalName[] = legalName.split("-");

        // Check each part
        if (partslegalName.length > 1)
            for (int i = 0; i < partslegalName.length; i++) {
                // if part has TC or FEQ, let's use it
                if ((partslegalName[i].toLowerCase().contains("tc")) || (partslegalName[i].toLowerCase().contains("feq"))) {

                    // If TCid part is the 2nd part (i.e. U1 - TCXX - nameOfTest)
                    if (partslegalName[i] == partslegalName[1])
                        // We return both 1st & 2nd parts
                        return partslegalName[0] + partslegalName[1];

                    else
                        // We just return the found section
                        return partslegalName[i];
                }
            }

        // If there is no "TC" id part (OR there is only 1 part), we just return a directory-safe value
        // (only one entire part was created)
        return partslegalName[0];
    }

    /**
     * This turns a string into a directory-safe name.
     *
     * @param string
     * @return directory version of string
     */
    protected static String getDirectoryString(String string, Boolean allowWhitespace) {
        // Get name that is valid for Windows.
        String legalName = string.replaceAll("[\\\\/:*?\"<>|]", "_");

        if (allowWhitespace) // whitespace is allowed
            return legalName;

        else // remove whitespaces
            return legalName.replaceAll("\\s+", "");
    }

    /**
     * This just checks the properties file, sets the network mode of the report log
     */
    private static void setLogNetworkMode() {
        switch (pmanger.getLogNetworkMode()) {
            case "Offline":
                reportAccessType = NetworkMode.OFFLINE;
                break;
            case "Online":
                reportAccessType = NetworkMode.ONLINE;
                break;

            default:
                System.out.println(
                        "ERROR: Could not set network mode of log. Please ensure correct value in properties file");
        }
    }

    /**
     * This decides how to create the report (if it needs appending or not).
     *
     * @return if log will be appended to, or not
     */
    private static Boolean appendLog() {

        // startNewLog is true at startup (for individual features)
        if (startNewLog) {

            if (MegaFeedEnabled) {// Megafeed is enabled, therefore different rules apply
                if (startNewMegaFeed) {
                    startNewMegaFeed = false; // only replaces once per megafeed (per run instance)
                    startNewLog = true; // start new megafeed
                } else {
                    startNewLog = false;// Append current megafeed
                }
            }
        }

        return startNewLog; // Note, we set this to false after initialising the report.
    }

    /**
     * Finishes and flushes the current test to the file. If action paramter =
     * "show" then the driver navigates to and displays the report
     *
     * @param showReport "show" to open report
     */
    public static void closeLog(Boolean showReport) {

        // Finish log
        if (childTestEnabled)
            endChildTest(); // Close child if it was open (shouldn't be)
        if (childTestUsed) { // If a child was used during the test, add a friendly msg
            logsysout("Test has completed.<br><i>Other Information</i><br>", LogStatus.INFO, true);
            childTestUsed = false; // reset
        }

        report.endTest(logger); // End the test (cannot logsysout after this)
        report.flush(); // Flush to file

        if (showReport) {
            // Show report
            driver.manage().window().maximize();
            driver.get(currentLog);

            // Show dashboard
            getCustomWaitsPage().waitForTextPresent("Report", driver, pmanger.getTime());
        }
        // Print out no of errors
        System.out.println("[ERR] Runtime errors (accumulated): [" + testErrorCount.toString() + "]");
    }

    //
    public static String getLogDirectory() {
        // Read contents of Log location
        // Example: D:\\Users\\matthew.sumner\\projects\\_logs\\
        String logDirectory = pmanger.getLog();
        String logDirectoryArchive = "Archive\\";
        String logDirectoryDebug = "Debug\\";

        // Check if we need to keep existing logs
        if (keepPreviousRunReports) {
            // If log dir has not yet been initialised for this run, create a new folder.
            if (keepPreviousRunReports_InitialisedDirectory == null) {
                // Get list of files in log directory
                File[] directoryListing = new File(logDirectory).listFiles();
                // Count # of existing folders named "run"
                int previousRuns = 0;
                Boolean archiveRuns = false;

                // If there are are > 10 runs, we add the existing ones to the archive
                if (directoryListing.length >= 12) { // 10 runs + Debug and Archive folder
                    AbstractStepDefinition.logsysout("Number of existing run folders have reached 10. Archiving...");
                    archiveRuns = true;
                }

                // Iterate through each file
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        // If contains a directory with 'Run'
                        if (child.getName().toLowerCase().contains("run")) {
                            if (archiveRuns) // we must archive run folders
                                transferFile(child, new File(logDirectory + logDirectoryArchive + child.getName()));
                            else
                                previousRuns++; // We must iterate the number
                        }
                    }
                }

                // Get today's date
                String timeStampNow = new SimpleDateFormat("[yyyy-MM-dd]_[h.mmaa]").format(Calendar.getInstance().getTime());
                // Create new Log Directory
                // Example: D:\\Users\matthew.sumner\projects\_logs\Run-2_[2018-02-13]_[12.33pm]\
                keepPreviousRunReports_InitialisedDirectory = logDirectory + "Run-" + (previousRuns + 1) + "_" + timeStampNow.toLowerCase() + "\\";
            }
            return keepPreviousRunReports_InitialisedDirectory;

        } else // otherwise, return the plain log directory (no timestamp or Run #) (UPDATE: added 'debug' dir)
            return logDirectory + logDirectoryDebug;
    }

    public static CustomWaits getCustomWaitsPage() {
        if (customWaits == null)
            customWaits = new CustomWaits(driver);
        return PageFactory.initElements(driver, CustomWaits.class);
    }

    /**
     * This copies a file to another directory, then deletes the original source file.
     *
     * @param srcFile  File to copy
     * @param destFile Destination incl. Filename
     */
    public static void transferFile(File srcFile, File destFile) {
        try {
            AbstractStepDefinition.logsysout("Transferring File: [" + srcFile.getName() + "] to: '" + destFile.getAbsolutePath() + "'");
            // Copy the file to destination
            if (srcFile.isDirectory())
                FileUtils.copyDirectory(srcFile, destFile); // copy dir
            else
                FileUtils.copyFile(srcFile, destFile); // copy file
            // Delete the original (source) file
            FileUtils.deleteQuietly(srcFile);
            AbstractStepDefinition.logsysout("Transferred.");

        } catch (Exception e) {
            logsysout("An error occurred while transferring: " + srcFile.getName() + "");
            printStackTrace(e);
        }
    }

    /**
     * This appends (and closes) the child test, and tells logsysout to use parent test when logging.
     */
    public static void endChildTest() {
        if (childTestEnabled) {
            logger.appendChild(childtest);
            childTestEnabled = false;
        }
    }

    /**
     * This creates a child test, and tells logsysout that the steps need to be recorded under this child test.
     */
    public static void enableChildTest(String childName) {
        childtest = report.startTest(childName);
        childTestEnabled = true; // This tells logsysout to log to the child
        childTestUsed = true; // This is used to say a friendly message after the test is complete
    }

    protected void setTestIngestFiles(String ingestfiles) {
        testIngestFiles = ingestfiles;
    }

    public void setfeature(String name) {
        // Example: internet-ip-logs // Example: multiple---requests
        featureName = name;
    }


    protected LoginPage getLoginPage() {
        if (loginPage == null)
            loginPage = new LoginPage(driver);
        return PageFactory.initElements(driver, LoginPage.class);
    }

    protected WebOrdersPage getWebOrdersPage() {
        if (webOrders == null)
            webOrders = new WebOrdersPage(driver);
        return PageFactory.initElements(driver, WebOrdersPage.class);
    }

    protected OrdersPage getOrdersPage() {
        if (ordersPage == null)
            ordersPage = new OrdersPage(driver);
        return PageFactory.initElements(driver, OrdersPage.class);
    }

    protected ViewAllOrders getViewAllOrders() {
        if (viewAllOrders == null)
            viewAllOrders = new ViewAllOrders(driver);
        return PageFactory.initElements(driver, ViewAllOrders.class);
    }


    // Logout
    protected CommonLogoutPage getCommonLogoutPage() {
        if (commonLogoutPage == null)
            commonLogoutPage = new CommonLogoutPage(driver);
        return PageFactory.initElements(driver, CommonLogoutPage.class);
    }


    /**
     * Sets the location/directory, name, calls inject config, and decides whether
     * to append or replace the log report file.
     */
    public void setupLog() {

        // Set log file location
        String logdir = getLogDirectory();
        if (MegaFeedEnabled) {
            // Set report target
            currentLog = logdir + AfterStepDefinitions.MegaFeedName + "\\" + "MegaFeed.html";
            // Set sysout target
            currentSysOut = logdir + AfterStepDefinitions.MegaFeedName + "\\" + SysOutFilename;
        } else { // Not using MegaFeed
            // If using workstream, we also add a folder for that.
            if (runningTestsByWorkstream)
                logdir = logdir + AbstractPage.getWorkstream();

            // Set report target
            currentLog = logdir + "\\" + getDirectoryFeedName() + "\\"
                    + getDirectoryFeedName() + ".html";
            // Set sysout target
            currentSysOut = logdir + "\\" + getDirectoryFeedName() + "\\"
                    + SysOutFilename;
        }

        // Initialize log
        setLogNetworkMode();
        report = new ExtentReports(currentLog, appendLog(), reportAccessType);

        // Set previousLogFeed to current, so next run we can determine what do
        previousLogFeed = feedName;
        previousWorkstream = AbstractPage.getWorkstream();

        // Create, and check if config file exists
        File srcConfigFile = new File(this.getClass().getClassLoader().getResource(reportConfig).getPath());
        report.loadConfig(srcConfigFile);
//        String js = "$(document).ready(function() {var dates=[];$('.test-started-time, .test-ended-time').each(function() { dates.push(new Date($(this).text().replace('-', '/').replace('-', '/'))); });var maxDate=new Date(Math.max.apply(null,dates));var minDate=new Date(Math.min.apply(null,dates));$('.suite-started-time').text(minDate.toLocaleFormat('%Y-%m-%d %H:%M:%S'));$('.suite-ended-time').text(maxDate.toLocaleFormat('%Y-%m-%d %H:%M:%S'));function newFunc() {var ended = $('.suite-ended-time').text().replace('-', '/').replace('-', '/');var started = $('.suite-started-time').text().replace('-', '/').replace('-', '/');var diff = new Date(new Date(ended) - new Date(started));var hours = Math.floor(diff / 36e5),minutes = Math.floor(diff % 36e5 / 60000),seconds = Math.floor(diff % 60000 / 1000);$('.suite-total-time-taken').text(hours + 'h ' + minutes + 'm ' + seconds + 's');} newFunc();})";
//        extent.config().insertJs(js);

        // Start header log with feedName
        if (startNewLog) {
            startLog();
            startNewLog = false; // Then, always reset to false (Won't startNewLog until new launch, or next class.java call
        }

        // Start actual test
        logger = report.startTest(getTestName());
        //logger.getTest().setStartedTime(Calendar.getInstance().getTime());
        // Add tags - only unique
        if (!AbstractPage.getWorkstream().toUpperCase().contains(getFeedName().toUpperCase())) {
            logger.assignCategory(AbstractPage.getWorkstream(), getFeedName());
        } else {
            logger.assignCategory(AbstractPage.getWorkstream());
        }
        // Add any custom tags from report
        if (getTestTags().length > 0) {
            addCustomTags();
        }

        logsysout("Starting test for: [" + AbstractPage.getWorkstream() + "] " + getFeedName(), LogStatus.INFO, false);
        logsysout("Test <i>'" + getTestName() + "'</i> Started! " + currentSysOutLink, LogStatus.INFO, true);
        // Append a list of ingest files for the test.
        if (getTestName().toLowerCase().contains("tc"))
            recordIngestFiles(); // look for ingest files (previously stored if applicable)
        else setTestIngestFiles(null); // make sure to not use (if existing)
    }

    /**
     * Create log with basic information, before running tests and appending to it
     * <p>
     * Also: Outputs if megafeed is enabled
     */
    private void startLog() {
        // Create log file with sample test (to easily identify the feed)
        logger = report.startTest(AbstractPage.getWorkstream() + " (" + getFeedName() + ") Test Log");

        // Set consoleoutput file for each feed.
        // Run each time unless in MegaFeed mode. // If in MegaFeed, only run if it hasn't already been initiated.
        if (!MegaFeedEnabled || sysoutputStream == null)
            initializeSysOutput();

        // Both log and system output files are initialized
        logsysout("Log successfully initialized " + currentSysOutLink, LogStatus.INFO, true);

        // check DebugMode
        String debugMessage = null;
        if (DebugMode)
            debugMessage = "Debug mode is enabled. For all runtime details click on 'View Full Output'.";
        else
            debugMessage = "Debug mode is disabled. For more runtime details enable it from the launcher, or click on 'View Full Output'.";
        logsysout(debugMessage, LogStatus.INFO, true);

        // take note if is megafeed
        if (MegaFeedEnabled) {
            logsysout("MegaFeed is enabled, all feed types will be logged in the MegaFeed report log");
        }
        closeLog(false);
    }

    /**
     * This outputs system out to a file as well as console window.
     */
    private void initializeSysOutput() {
        try {
            System.out.println(sysoutprefix + "Streaming system out to new file: " + currentSysOut);

            // if system output is already initialized
            if (sysoutputStream != null) {
                // Close previous streams
                // We DON'T close the streams, as this will kill System.out
                sysoutputStream = null;
                sysoutputSplitter = null;
                sysoutputFile = null;

                // Reset system.out
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            }

            // We set the file
            sysoutputFile = new FileOutputStream(currentSysOut);
            // Use teeoutputstream to output to normal console AND output file.
            sysoutputSplitter = new TeeOutputStream(System.out, sysoutputFile);
            sysoutputStream = new PrintStream(sysoutputSplitter, true); //true - auto-flush after println
            System.setOut(sysoutputStream);

            System.out.println(sysoutprefix + "System Output Initialized.");

        } catch (Exception e) {
            System.out.println(sysoutprefix + "FATAL: Could not set system output file.");
            printStackTrace(e);
        }
    }

    /**
     * This adds the tags to the test in report.
     * Rules:
     * "old" / "outdated" = mark as warning
     * "skip" / "ignore" = skip test
     * *comment* = output to log
     */
    private void addCustomTags() {
        Boolean skipTest = false; // used to determine if one of the tags instructed a skip
        logsysout("Adding Custom Tags: " + Arrays.toString(getTestTags()));

        // For each tag, check if old/outdated, or skip/ignore to action.
        for (String tag : getTestTags()) {
            // For comments, add to log rather than tag.
            if (tag.toLowerCase().contains("comment")) {
                logsysout("Test comments <i>(from .feature)</i>:" + System.lineSeparator() + "[" + tag + "]", LogStatus.INFO, true);
                break; // Don't go to switch for comments, or assign the tag
            }
            logger.assignCategory(tag); // Add tag

            switch (tag.toLowerCase()) { // For non-comments:
                // mark as warning
                case "old":
                case "outdated":
                    logsysout("This test is marked as '" + tag + "'.", LogStatus.WARNING, true);
                    break;

                // skip test
                case "ignore":
                case "skip":
                case "req_fail":
                    logsysout("Skipping test... <i>(As instructed by .feature file tag: '" + tag + "')</i>", LogStatus.SKIP, true);
                    skipTest = true; // We will skip the test via cucumber exception after iterating through all tags
                    break;

                default:
                    // n/a
            }
        }

        // If one of the tags instructed skip:
        if (skipTest)
            throw new cucumber.api.PendingException(); // This will tell cucumber to skip the current test, and continue to the next
    }

    /**
     * This records the list of ingest files associated with each test (listed in .feature file)
     */
    private void recordIngestFiles() {

        // If the test contains ingest files, lets list them
        if (testIngestFiles != null)
            if (testIngestFiles.length() > 0) {
                System.out.println("Recording ingest files associated with test...");
                enableChildTest("Click to view the ingest files associated with this test"); // enable this to log the actual test steps in another child node

                // Check if the string contains multiple ingest files
                if (testIngestFiles.contains(",")) {
                    String ingestfiles[] = testIngestFiles.split(","); // Split ingest files
                    for (String ingestfile : ingestfiles) {
                        // List each ingest files
                        logsysout(ingestfile, LogStatus.UNKNOWN, true);
                    }

                } else { // There is only one ingest file; output single line
                    logsysout(testIngestFiles, LogStatus.UNKNOWN, true);
                }
                endChildTest(); // Append the child report (above ingest file list) to the current test log
                testIngestFiles = null; // Reset string for next test
            }
    }

}