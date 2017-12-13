package com.girish.aphotograph.util;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Girish on 13-Dec-17.
 */

public class HighResImageDetail {
    @SerializedName("images")
    public List<ImageDetail> details;
}
