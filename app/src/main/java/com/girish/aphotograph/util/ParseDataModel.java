package com.girish.aphotograph.util;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.ParcelPropertyConverter;

import java.util.List;

/**
 * Created by Girish on 11-Dec-17.
 */

@Parcel
public class ParseDataModel {

    @SerializedName("photos")
    @ParcelPropertyConverter(DataModelParcelConverter.class)
    public List<PhotoDetails> details;

}
