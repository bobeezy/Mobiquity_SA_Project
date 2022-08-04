package main.java.api;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import io.restassured.response.ValidatableResponse;
import main.java.engine.DriverFactory;
import org.hamcrest.Matchers;
import org.testng.Assert;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static main.java.api.CustomHeaders.buildCustomHeaders;
import static main.java.api.CustomHeaders.customHeadersMap;

public class CommentMethods extends DriverFactory {

    APICommonMethods api = new APICommonMethods();

    public ValidatableResponse get_searchUserCommentsByPostId(String postId) throws Exception {

        log("========================== Find User Comments By Post Id ============================", "INFO", "text");

        //Build Headers
        buildCustomHeaders("Content-Type", contentTypeJson);

        String uriString = property.returnPropVal_api(API_FILE_NAME, "searchUserCommentsByPostId_uri");
        uriString = uriString.replace("post_id", postId);

        response = api.getMethod(uriString, customHeadersMap);
        return response;
    }

    /**
     * validations
     */
    //region <validateSearchUserCommentsByPostId>
    public void validateSearchUserCommentsByPostId(ValidatableResponse response, int status, String post_Id, String scenarioType) {

        response.assertThat().statusCode(status);
        log("ASSERT: StatusCode \nEXPECTED: 200 \nACTUAL: " + status, "INFO", "text");

        if(scenarioType.equalsIgnoreCase("Negative")) {
            response.body("isEmpty()", Matchers.is(true));

            log("ASSERT: We are not getting any value    \nEXPECTED: Empty list []  \nACTUAL: []", "INFO", "text");
        }
        else {
            //validate id
            validateCommentId(response, post_Id);

            //validate email
            validateCommentEmail(response);
        }
    }
    //endregion

    //region <validateCommentId>
    public void validateCommentId(ValidatableResponse response, String post_Id) {

        int commentId;
        commentId = Integer.parseInt(APICommonMethods.getValueFromJsonResp(response, "id"));

        Assert.assertEquals(commentId + "", post_Id);

        log(new StringBuilder().append("ASSERT: Id \nEXPECTED: ").append(post_Id).append(" \nACTUAL: ").append(commentId).toString(), "INFO", "text");
    }
    //endregion

    //region <validateCommentEmail>
    public void validateCommentEmail(ValidatableResponse response) {

        String extractedEmail;
        extractedEmail = APICommonMethods.getValueFromJsonResp(response, "email");

        //validate email format using the official java email package
        isValidEmailAddress(extractedEmail);

        boolean isValidEmail = isValidEmailAddress(extractedEmail);

        if(isValidEmail == true) {
            log("EXTRACTED VALID EMAIL: " + extractedEmail, "INFO", "text");
            Assert.assertTrue(true, "Extracted Email '" + extractedEmail + "' is Valid");
        }
        else {
            log("EXTRACTED INVALID EMAIL: " + extractedEmail, "ERROR", "text");
            Assert.fail("\n[ERROR] Extracted Email '" + extractedEmail + "' is Invalid");
        }
    }
    //endregion

    //region <isValidEmailAddress>
    public static boolean isValidEmailAddress(String email) {

        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        }
        catch(AddressException ex) {
            result = false;
        }
        return result;
    }
    //endregion
}