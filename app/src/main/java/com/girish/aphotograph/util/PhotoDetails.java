package com.girish.aphotograph.util;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Girish on 11-Dec-17.
 */

@Parcel
public class PhotoDetails {

    @SerializedName("id")
    private
    long id;

    @SerializedName("name")
    private
    String name;

    @SerializedName("description")
    private
    String description;

    @SerializedName("image_url")
    private
    String url;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
