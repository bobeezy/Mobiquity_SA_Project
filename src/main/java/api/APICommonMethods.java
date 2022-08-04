package main.java.api;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import main.java.engine.DriverFactory;

import java.util.Map;

import static io.restassured.RestAssured.given;

@SuppressWarnings("unchecked")
public class APICommonMethods extends DriverFactory {

    /**
     * @description This method is used to extract value from Json response
     * @param response
     * @param keyString
     * @return
     */
    public static String getValueFromJsonResp(ValidatableResponse response, String keyString) {

        String value = null;
        try {
            value = response.extract().path(keyString).toString();
            value = value.replaceAll("\\[", "").replaceAll("\\]", "");
        }
        catch (Exception e) {
            log("Failed to get value from JSON Response: " + e, "ERROR", "text");
        }

        return value;
    }

    //region <GET method>
    public ValidatableResponse getMethod(String endpoint, Map header) throws Exception {

        log("Method: GET\n---------------- URL ------------------\n"
                + RestAssured.baseURI + "" + RestAssured.basePath + endpoint, "INFO", "text");

        log("--------------- HEADERS ---------------\n" + header, "INFO", "text");

        response =
                given().
                        spec(requestSpec).
                        headers(header).
                when().
                        get(endpoint).
                then();

        log("--------------- RESPONSE ---------------\n", "INFO", "text");
        log(response.extract().body().asString(), "INFO", "json");

        return response;
    }
    //endregion
}
