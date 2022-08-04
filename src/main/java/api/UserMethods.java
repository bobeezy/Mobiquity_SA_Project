package main.java.api;

/**
 * @author lionel.mangoua
 * date: 03/08/22
 */

import io.restassured.response.ValidatableResponse;
import main.java.engine.DriverFactory;
import org.hamcrest.Matchers;
import org.testng.Assert;

import static main.java.api.CustomHeaders.buildCustomHeaders;
import static main.java.api.CustomHeaders.customHeadersMap;

public class UserMethods extends DriverFactory {

    APICommonMethods api = new APICommonMethods();

    public ValidatableResponse get_searchUserByUsername(String usrName) throws Exception {

        log("========================== Find User By Username ============================", "INFO", "text");

        //Build Headers
        buildCustomHeaders("Content-Type", contentTypeJson);

        String uriString = property.returnPropVal_api(API_FILE_NAME, "searchUserByUsername_uri");
        uriString = uriString.replace("user_name", usrName);

        response = api.getMethod(uriString, customHeadersMap);
        return response;
    }

    /**
     * validations
     */
    //region <validateSearchUserByUsername>
    public void validateSearchUserByUsername(ValidatableResponse response, int status, String usrName, String scenarioType) {

        response.assertThat().statusCode(status);
        log("ASSERT: StatusCode \nEXPECTED: 200 \nACTUAL: " + status, "INFO", "text");

        if (scenarioType.equalsIgnoreCase("Negative")) {
            response.body("isEmpty()", Matchers.is(true));

            log("ASSERT: We are not getting any value    \nEXPECTED: Empty list []  \nACTUAL: []", "INFO", "text");
        }
        else {
            String usrnm;
            usrnm = APICommonMethods.getValueFromJsonResp(response, "username");
            userId = Integer.parseInt(APICommonMethods.getValueFromJsonResp(response, "id"));

            Assert.assertEquals(usrnm, usrName);

            log("ASSERT: Username    \nEXPECTED: " + usrName + "  \nACTUAL: " + usrnm, "INFO", "text");
            log("EXTRACTED USER ID: " + userId, "INFO", "text");
        }
    }
    //endregion
}