package testCases;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import main.java.api.CommentMethods;
import main.java.api.PostMethods;
import main.java.api.UserMethods;
import main.java.engine.DriverFactory;
import main.java.utils.SetEnvironmentDataUtility;
import org.testng.ITestResult;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;

public class MobiquityTest extends DriverFactory {

    SetEnvironmentDataUtility setEnv = new SetEnvironmentDataUtility();
    CommentMethods commentMeth = new CommentMethods();
    PostMethods postMeth = new PostMethods();
    UserMethods userMeth = new UserMethods();

    @Parameters({"testCase", "environmentName"})
    @BeforeMethod
    public void init(String testCase, String environmentName){
        //initialize
        setTestDataForTest(testCase); //set Data to extract from sheet
        setEnv.setTestEnvironment(environmentName); //set values
    }

    @Features("Mobiquity Test")
    @Description("Perform an API request to search user by username")
    @Test
    public void searchUserByUsername() throws Exception {

        response = userMeth.get_searchUserByUsername(currentTestData.get("Username"));
        userMeth.validateSearchUserByUsername(response, 200, currentTestData.get("Username"), currentTestData.get("ScenarioType"));
    }

    @Features("Mobiquity Test")
    @Description("Perform an API request to search user posts by userId")
    @Test
    public void searchUserPostsByUserId() throws Exception {

        if(currentTestData.get("ScenarioType").equalsIgnoreCase("Negative")) {
            response = postMeth.get_searchUserPostsByUserId(currentTestData.get("Id")); //use id value from excel sheet
        }
        else {
            response = postMeth.get_searchUserPostsByUserId(String.valueOf(userId)); //use id value extracted from previous request
        }

        postMeth.validateSearchUserPostsByUserId(response, 200, String.valueOf(userId), currentTestData.get("ScenarioType"));
    }

    @Features("Mobiquity Test")
    @Description("Perform an API request to search user comments by id")
    @Test
    public void searchUserCommentsByPostId() throws Exception {

        int extractedUserId;

        if(currentTestData.get("ScenarioType").equalsIgnoreCase("Negative")) {
            response = commentMeth.get_searchUserCommentsByPostId(currentTestData.get("PostId"));
            commentMeth.validateSearchUserCommentsByPostId(response, 200, currentTestData.get("PostId"), currentTestData.get("ScenarioType"));
        }
        else {
            for(int i = 0; i < postsIdList.size(); i++) {
                int iteration = i + 1;
                log("\n**** Iteration " + iteration + " ****\n", "INFO", "text");
                extractedUserId = postsIdList.get(i);

                response = commentMeth.get_searchUserCommentsByPostId(String.valueOf(extractedUserId));
                commentMeth.validateSearchUserCommentsByPostId(response, 200, String.valueOf(extractedUserId), currentTestData.get("ScenarioType"));
            }

            //clear lists
            postsIdList.removeAll(postsIdList);
            postsUserIdList.removeAll(postsUserIdList);
        }
    }

    @AfterMethod
    //region <afterTestScreenshots>
    public void afterTestScreenshots(ITestResult result) {

        //Print Log with testName and take screenshot after each @Test method
        log("\n=============== " + result.getName() + " test executed successfully ===============\n","INFO",  "text");
    }
    //endregion
}
