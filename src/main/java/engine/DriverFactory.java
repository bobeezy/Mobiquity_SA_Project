package main.java.engine;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import main.java.config.GlobalEnums;
import main.java.exceptions.BaseUrlSetupException;
import main.java.exceptions.CheckTestArrayException;
import main.java.exceptions.ExtractedTestDataException;
import main.java.exceptions.SheetNameException;
import main.java.utils.ExcelUtility;
import main.java.utils.PropertyFileReader;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;

public class DriverFactory {

    public static PropertyFileReader property = new PropertyFileReader();
    public static String API_FILE_NAME = "api";
    public static String testName;
    public static int userId;
    public static String[][] excelData = null;
    public static String[][] commentData = null;
    public static String[][] postData = null;
    public static String[][] userData = null;
    public static Map<String, String> currentTestData = null; //to get test data from excel sheet
    public static String USER_DATA_XLSX_FILE_PATH = property.returnPropVal_api(API_FILE_NAME, "userDataSheetPath");
    public static GlobalEnums.Environment env; //setup Environment
    public static final Logger LOGGER = LoggerFactory.getLogger(DriverFactory.class);
    public static List<Integer> postsIdList = new ArrayList<>();
    public static List<Integer> postsUserIdList = new ArrayList<>();
    //Headers
    public static ValidatableResponse response = null;
    public static RequestSpecBuilder builder;
    public static RequestSpecification requestSpec; //used with ValidatableResponse
    public static ResponseSpecification responseSpec; //used with ValidatableResponse
    public static ResponseSpecBuilder respec;
    public static String contentTypeJson = property.returnPropVal_api(API_FILE_NAME, "content_type_json");
    public static String cacheControl = property.returnPropVal_api(API_FILE_NAME, "cache_control");

    @BeforeClass
    //region <startEngine>
    public void startEngine() {

        //Read excel sheets
        setUpExcelDataSheets();

        //region <To remove the warning: "log4j:WARN No appenders could be found for logger">
        Properties prop = new Properties();
        prop.setProperty("log4j.rootLogger", "WARN");
        PropertyConfigurator.configure(prop);
        //endregion

        //Set Up default environment for API tests
        env = GlobalEnums.Environment.USER;

        //Set Up Base Url
        setUpBaseUrl();
    }
    //endregion

    //API
    //region <Set Up Base Url>
    public static void setUpBaseUrl() {

        try {
            RestAssured.baseURI = env.baseUrl;
            RestAssured.basePath = env.path;

            builder = new RequestSpecBuilder();
            respec = new ResponseSpecBuilder();

            builder.addFilter(new AllureRestAssured());//To setup Filter that gonna attach Request/Response logs to report
            respec.expectResponseTime(lessThan(10L), TimeUnit.SECONDS); //verify response time lesser than 10 milliseconds

            requestSpec = builder.build();
            responseSpec = respec.build();

            log("Setup Base URL successfully \n", "INFO", "text");
        }
        catch(Exception e) {
            throw new BaseUrlSetupException("[ERROR] Something went wrong setting up the base URL --- " + e.getMessage());
        }


    }
    //endregion

    //Test Data Setup
    //region <xlsxFilePath>
    /**
     * Set up file path for "UserData" excel sheet
     */
    public static String xlsxFilePath() {

        String path;
        path = USER_DATA_XLSX_FILE_PATH;

        log("Setup .xlsx File Path: '" + path + "' successfully \n", "INFO", "text");
        return path;
    }
    //endregion

    //region <setUpExcelDataSheets>
    /**
     * This method is used to read the excel file and store
     * all data from each sheet into a set up variable.
     * Every time we create a new excel sheet, we need to set it
     * up here.
     */
    public static void setUpExcelDataSheets() {

        try {
            //Read excel sheets based on the Sheet Name
            commentData = ExcelUtility.readExcelFile("Comment"); //Comment = sheet name
            log(" --- Read the excel sheet 'Comment' successfully --- ", "INFO", "text");
            if(commentData == null || commentData.equals("")) {
                LOGGER.info("[ERROR] commentData value is: " + commentData);
            }

            postData = ExcelUtility.readExcelFile("Post"); //Post = sheet name
            log(" --- Read the excel sheet 'Post' successfully --- ", "INFO", "text");
            if(postData == null || postData.equals("")) {
                LOGGER.info("[ERROR] postData value is: " + postData);
            }

            userData = ExcelUtility.readExcelFile("User"); //User = sheet name
            log(" --- Read the excel sheet 'User' successfully --- ", "INFO", "text");
            if(userData == null || userData.equals("")) {
                LOGGER.info("[ERROR] userData value is: " + userData);
            }
        }
        catch(Exception e) {
            log("Something went wrong reading the excel sheet --- " + e, "INFO", "text");
        }
    }
    //endregion

    //region <setTestDataForTest>
    /**
     * This method is used to set a test method
     * so that its data can be used within a different test method.
     * It stores every data based on the excel sheet name into currentTestData..
     * Every time we create a new Sheet in the .xlsx file, this function should be updated
     */
    public static void setTestDataForTest(String testCaseName) {

        try {
            testName = testCaseName;

            if(checkTestOnArray(testName, commentData)) {
                currentTestData = getSpecificTestData(testName, commentData);
            } //Pet sheet in 'UserData.xlsx' file

            if(checkTestOnArray(testName, postData)) {
                currentTestData = getSpecificTestData(testName, postData);
            } //Store sheet in 'UserData.xlsx' file

            if(checkTestOnArray(testName, userData)) {
                currentTestData = getSpecificTestData(testName, userData);
            } //User sheet in 'UserData.xlsx' file

            log("Setup test data for test '" + testName + "' successfully \n", "INFO", "text");
        }
        catch(Exception e) {
            throw new SheetNameException("[ERROR] Something went wrong reading the sheet Name --- " + e.getMessage());
        }
    }
    //endregion

    //region <getSpecific TestData>
    /**
     * This method gets a specific test case data from the excelData 2D array
     */
    public static Map<String, String> getSpecificTestData(String testName, String[][] excelData) {

        try {
            currentTestData = new LinkedHashMap<>();

            int numRows = excelData.length; //Get number of rows
            int numCols = excelData[0].length; //Get number of columns

            for(int i = 0; i < numRows; i++) {
                if(excelData[i][0].equalsIgnoreCase(testName)) {
                    for(int j = 0; j < numCols; j++) {
                        currentTestData.put(excelData[0][j], excelData[i][j]);
                    }
                }
            }

            log("Get specific data for test case: '" + testName + "' successfully \n", "INFO", "text");
            return currentTestData;
        }
        catch (Exception e) {
            throw new ExtractedTestDataException("[ERROR] Something went wrong while getting specific data for test case " + testName + " --- " + e.getMessage());
        }
    }
    //endregion

    //region <checkTestOnArray>
    /**
     * This method checks that the testName extracted from the excel sheet
     * is stored in the excelData array
     */
    public static boolean checkTestOnArray(String testName, String[][] excelData) {

        try {
            int numRows = excelData.length;

            for(int i = 0; i < numRows; i++) {
                if(excelData[i][0].equalsIgnoreCase(testName)) {
                    return true;
                }
            }

            return false;
        }
        catch (Exception e){
            throw new CheckTestArrayException("[ERROR] Something went wrong while checking that the testName extracted from the excel sheet is stored in Array --- " + e.getMessage());
        }
    }
    //endregion

    //region <logger>
    public static void logger(final String message, final String level, String format) {

        if(format.equalsIgnoreCase(("json"))) {
            String json = (new JSONObject(message)).toString(4); //To convert into pretty Json format
            LOGGER.info("\n" + json); //To print on the console
        }
        else {
            LOGGER.info(message); //To print on the console
        }
    }

    public static void log(final String message, final String level, String format) {

        try {
            logger(message, level, format);
        }
        catch (JSONException err) {
            logger(message, level, "text");
        }
    }
    //endregion
}