package main.java.utils;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import main.java.config.GlobalEnums;
import main.java.engine.DriverFactory;
import main.java.exceptions.TestEnvSetupException;

public class SetEnvironmentDataUtility extends DriverFactory {

    //region <setTestEnvironment>
    public void setTestEnvironment(String environmentName) {

        try {
            switch(environmentName) {
                case "comment":
                    env = GlobalEnums.Environment.COMMENT;
                    setUpBaseUrl();
                    break;
                case "post":
                    env = GlobalEnums.Environment.POST;
                    setUpBaseUrl();
                    break;
                case "user":
                    env = GlobalEnums.Environment.USER;
                    setUpBaseUrl();
                    break;
            }

            log("Set Environment Name: '" + environmentName + "' successfully", "INFO", "text");
        }
        catch (Exception e) {
            throw new TestEnvSetupException("[ERROR] Something went wrong while setting up the test environment --- " + e.getMessage());
        }
    }
    //endregion
}
