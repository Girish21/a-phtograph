package com.girish.aphotograph.extra;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Girish on 11-Dec-17.
 */

public class QueryParamsUtil {
    public static Map<String, String> getGeneralQuery(String type, String sortType) {
        Map<String, String> query = new HashMap<>();

        if (type.contains(EndPoints.EDITORS))
            query.put(EndPoints.FEATURE_KEY, EndPoints.EDITORS);
        else
            query.put(EndPoints.FEATURE_KEY, EndPoints.POPULAR);

        query.put(EndPoints.SORT_KEY, sortType);
        query.put(EndPoints.IMAGE_SIZE_KEY, EndPoints.DEFAULT_IMAGE_SIZE);
        query.put(EndPoints.STORE_KEY, EndPoints.DEFAULT_STORE);
        query.put(EndPoints.CONSUMER_KEY_KEY, EndPoints.CONSUMER_KEY);

        return query;
    }

    public static Map<String, String> getPhotoQuery() {
        Map<String, String> query = new HashMap<>();

        query.put(EndPoints.IMAGE_SIZE_KEY, "2048");
        query.put(EndPoints.CONSUMER_KEY_KEY, EndPoints.CONSUMER_KEY);

        return query;
    }
}
