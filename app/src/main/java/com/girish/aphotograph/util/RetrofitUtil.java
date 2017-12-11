package com.girish.aphotograph.util;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Girish on 11-Dec-17.
 */

public interface RetrofitUtil {

    @GET("photos")
    Call<ParseDataModel> listGetData(@QueryMap Map<String, String> options);

}
