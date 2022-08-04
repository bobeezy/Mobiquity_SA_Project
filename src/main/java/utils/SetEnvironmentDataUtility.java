package main.java.utils;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import main.java.engine.DriverFactory;
import main.java.config.GlobalEnums;

public class SetEnvironmentDataUtility extends DriverFactory {

    //region <setTestEnvironment>
    public void setTestEnvironment(String environmentName) {

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
    //endregion
}
