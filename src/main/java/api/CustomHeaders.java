package main.java.api;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import java.util.HashMap;
import java.util.Map;

public class CustomHeaders {

    //Creating a HashMap
    public static Map<String, String> customHeadersMap = new HashMap<>();

    //region <This method allows you to add header to headersMaps>
    public static void buildCustomHeaders(String headerKey, String headerValue) {

        //Adding key-value pairs to a HashMap
        customHeadersMap.put(headerKey, headerValue);
    }
    //endregion
}
