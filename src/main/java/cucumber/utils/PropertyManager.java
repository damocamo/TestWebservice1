package cucumber.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertyManager {

    public static String programDataDir = "automation_data/";
    public static String propertiesFile = programDataDir + "parameters.properties";

    private String templateFileName = "parameters.template";
    // List of what to find and set
    private List<String> propertyList = new ArrayList<String>();
    // List of set properties
    private List<String[]> propertyCollection = new ArrayList<String[]>();

    /**
     * This reads the contents of the template, writes to external file
     *
     * @throws IOException
     */
    public static void writeResourceToFile(String src, File target) {
        try {
            ClassLoader classloader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream isSrc = classloader.getResourceAsStream(src);

            FileUtils.copyInputStreamToFile(isSrc, target);
        } catch (Exception e) {
            System.out
                    .println("An Error occurred while trying to create application files.");
            e.printStackTrace();
        }
    }

    /**
     * This compares the synced template and the local parameters file for
     * changes. If there are differences, a message will be displayed, and the
     * program shall quit.
     * <p>
     * If there is no properties file, then it will create one using the
     * template.
     */
    private void findChanges() {
        try {
            File properties = new File(propertiesFile);

            // Check if file exists
            if (!properties.exists() || properties.isDirectory()) {

                // Create new properties from template
                System.out.println("Creating new properties file...");
                writeResourceToFile(templateFileName, properties);
                System.out
                        .println("Properties file created. Please adjust, and restart the application.");
                // Stop runtime, so the updated properties file can be used
                System.exit(0);

            } else {
                // Check for any changes
                Integer sizeOfProperties = Files.readAllLines(
                        Paths.get(properties.getPath()),
                        Charset.defaultCharset()).size();

                // Template
                File tempProperties = new File(properties + ".temp");
                writeResourceToFile(templateFileName, tempProperties);
                Integer sizeOfTemplate = Files.readAllLines(
                        Paths.get(tempProperties.getPath()),
                        Charset.defaultCharset()).size();
                FileUtils.deleteQuietly(tempProperties);
                if (sizeOfProperties != sizeOfTemplate) {
                    System.out
                            .println("ERROR: Your properties file is outdated, ensure it matches the latest version, or delete the file. (WARNING: delete will reset all parameters)");
                    System.exit(0);
                }
            }

        } catch (Exception e) {
            System.out
                    .println("FATAL: An error occurred when validating the properties file."
                            + e);
            e.printStackTrace();
            System.exit(0);
        }
    }

    // ============================================================================================================
    // SET VALUES
    // --------

    /**
     * Simply loads in the property file and then sets the parameters
     */
    public void generateProperty() {

        // Declare properties instance
        Properties prop = new Properties();
        InputStream input = null;

        try {
            // Compare template and selected properties, display if different
            // versions (sizes)
            findChanges();

            // Find these properties


            //------------------------------------------
            propertyList.add("BrowserDriver");
            propertyList.add("LocationOfDriver");
            propertyList.add("EnvironmentIP");
            propertyList.add("TimeOut");
            propertyList.add("LoggFile");
            propertyList.add("LogNetworkMode");
            //------------------------------------------

            // Load the selected option
            input = new FileInputStream(propertiesFile);
            prop.load(input);

            // For all properties listed above, get and set their values in a ArrayList.
            // (Save label + value array in list)
            for (String property : propertyList) {
                propertyCollection.add(new String[]{(property), (prop.get(property).toString())});
            }

            System.out.println("Properties loaded.");

        } catch (Exception e) {
            System.out.println("FATAL: Something is wrong with the parameters file. " + e);
            e.printStackTrace();
        }
    }

    // ============================================================================================================
    // RETRIEVE VALUES
    // --------

    /**
     * This cycles through each set property in propertyCollection, and returns the
     * value for the given label
     *
     * @param propertyLabel
     * @return property
     */
    private String retrieveValue(String propertyLabel) {
        for (String[] propertyCollectionItem : propertyCollection) { // Look through all saved labels & values
            if (propertyCollectionItem[0].toString() == propertyLabel) { // When finding the label that matches
                return propertyCollectionItem[1]; // Return its associated value
            }
        }
        return null;
    }

    // ========
    // PROPERTIES
    // --------

    /**
     * This is the option for which webdriver to use ([Chrome], [I.E], or [Firefox])
     */
    public String getBrowser() {
        return retrieveValue("BrowserDriver");
    }

    /**
     * This is the directory of selenium webdrivers (not browser-specific)
     */
    public String getLocationDriver() {
        return retrieveValue("LocationOfDriver");
    }

    /**
     * This is the RMS IP address
     */
    public String getEnvironmentIP() {
        return retrieveValue("EnvironmentIP");
    }

    /**
     * This returns the timeout value (usually 300 seconds)
     */
    public int getTime() {
        return Integer.parseInt(retrieveValue("TimeOut"));
    }

    /**
     * This is the directory used to generate logs
     */
    public String getLog() {
        return retrieveValue("LoggFile");
    }

    /**
     * This will determine the report.html network mode. Offline must be used for Model environment (and others that do not have internet access)s
     */
    public String getLogNetworkMode() {
        return retrieveValue("LogNetworkMode");
    }

}
