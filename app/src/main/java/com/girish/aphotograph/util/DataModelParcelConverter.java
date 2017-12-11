package com.girish.aphotograph.util;

import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;
import org.parceler.TypeRangeParcelConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girish on 11-Dec-17.
 */

class DataModelParcelConverter implements ParcelConverter<List<PhotoDetails>> {
    @Override
    public void toParcel(List<PhotoDetails> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (PhotoDetails item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public List<PhotoDetails> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<PhotoDetails> items = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            items.add((PhotoDetails) Parcels.unwrap(parcel.readParcelable(PhotoDetails.class.getClassLoader())));
        }
        return items;
    }
}
