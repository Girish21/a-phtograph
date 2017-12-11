package com.girish.aphotograph;

import android.app.Application;
import android.content.Context;

import com.girish.aphotograph.extra.EndPoints;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Girish on 11-Dec-17.
 */

public class MyApplication extends Application {
    private static MyApplication application = null;
    private static Retrofit retrofit = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (application == null)
            application = this;
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(EndPoints.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
    }

    public static Retrofit getRetrofitApiClient() {
        return retrofit;
    }

    public static Context getContext() {
        return application.getApplicationContext();
    }
}
