package main.java.api;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import io.restassured.response.ValidatableResponse;
import main.java.engine.DriverFactory;
import org.hamcrest.Matchers;
import org.testng.Assert;

import static main.java.api.CustomHeaders.buildCustomHeaders;
import static main.java.api.CustomHeaders.customHeadersMap;

public class PostMethods extends DriverFactory {

    APICommonMethods api = new APICommonMethods();

    public ValidatableResponse get_searchUserPostsByUserId(String usrId) throws Exception {

        log("========================== Find User Posts By User Id ============================", "INFO", "text");

        //Build Headers
        buildCustomHeaders("Content-Type", contentTypeJson);

        String uri_string = property.returnPropVal_api(api_fileName, "searchUserPostsByUserId_uri");
        uri_string = uri_string.replace("user_id", usrId);

        response = api.getMethod(uri_string, customHeadersMap);
        return response;
    }

    /**
     * validations
     */
    //region <validateSearchUserPostsByUserId>
    public void validateSearchUserPostsByUserId(ValidatableResponse response, int status, String usrId, String scenarioType) {

        response.assertThat().statusCode(status);
        log("ASSERT: StatusCode \nEXPECTED: 200 \nACTUAL: " + status, "INFO", "text");

        if(scenarioType.equalsIgnoreCase("Negative")) {
            response.body("isEmpty()", Matchers.is(true));

            log("ASSERT: We are not getting any value    \nEXPECTED: Empty list []  \nACTUAL: []", "INFO", "text");
        }
        else {
            getAllPostsIdData(response); //extract all Posts IDs

            getAllPostsUserIdData(response, usrId); //extract all Posts UserIDs

            log("EXTRACTED POST IDs List: " + postsIdList, "INFO", "text");
            log("EXTRACTED POST UserIDs List: " + postsUserIdList, "INFO", "text");
        }
    }
    //endregion

    //region <getAllPostsIdData>
    public void getAllPostsIdData(ValidatableResponse response) {

        postsIdList = response.extract().jsonPath().getList("id");
    }
    //endregion

    //region <getAllPostsUserIdData>
    public void getAllPostsUserIdData(ValidatableResponse response, String userId) {

        postsUserIdList = response.extract().jsonPath().getList("userId");

        int extractedUserId = 0;

        //validate that only the expected userIds data are returned
        for(int i = 0; i < postsUserIdList.size(); i++) {
            extractedUserId = postsUserIdList.get(i);

            Assert.assertEquals(extractedUserId + "", userId);
        }

        log(new StringBuilder().append("ASSERT: UserId \nEXPECTED: ").append(userId).append(" \nACTUAL: ").append(extractedUserId).toString(), "INFO", "text");
    }
    //endregion
}