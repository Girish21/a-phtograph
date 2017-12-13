package com.girish.aphotograph.util;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Girish on 13-Dec-17.
 */

public class ImageDetail {

    @SerializedName("https_url")
    public String url;

    @SerializedName("format")
    public String format;

    public String getUrl() {
        return url;
    }

    public String getFormat() {
        return format;
    }
}
