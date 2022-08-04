package main.java.api;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import java.util.HashMap;
import java.util.Map;

import static main.java.engine.DriverFactory.cacheControl;
import static main.java.engine.DriverFactory.contentTypeJson;

public class CustomHeaders {

    //Creating a HashMap
    public static Map<String, String> customHeadersMap = new HashMap<>();

    //region <This method allows you to add header to headersMaps>
    public static void buildCustomHeaders(String headerKey, String headerValue) {

        //Adding key-value pairs to a HashMap
        customHeadersMap.put(headerKey, headerValue);
    }
    //endregion

    //region <Hard code header could be done in this method using Map>
    public static void buildCustomHeaders() {

        //Adding key-value pairs to a HashMap
        customHeadersMap.put("content-type", contentTypeJson);
        customHeadersMap.put("cache-control", cacheControl);
    }
    //endregion
}
