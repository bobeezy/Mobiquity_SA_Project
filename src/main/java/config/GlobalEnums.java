package main.java.config;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import main.java.engine.DriverFactory;

public class GlobalEnums extends DriverFactory {

    public static String httpsString = "https://";
    public static String mobiquityBaseUrl = httpsString + property.returnPropVal_api(API_FILE_NAME, "mobiquityBaseUrlPath");
    public static String commentPath = property.returnPropVal_api(API_FILE_NAME, "commentUrlPath");
    public static String postPath = property.returnPropVal_api(API_FILE_NAME, "postUrlPath");
    public static String userPath = property.returnPropVal_api(API_FILE_NAME, "userUrlPath");

    //region <API>
    public enum Environment {

        COMMENT(mobiquityBaseUrl, commentPath, "comment"),
        POST(mobiquityBaseUrl, postPath, "post"),
        USER(mobiquityBaseUrl, userPath, "user");

        public final String baseUrl;
        public final String path;
        public final String environmentName;

        //Setters
        Environment(String baseUrl, String path, String environmentName) {

            this.baseUrl = baseUrl;
            this.path = path;
            this.environmentName = environmentName;
        }
    }
    //endregion
}
