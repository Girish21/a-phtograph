package com.girish.aphotograph.adapter;

import android.view.View;

/**
 * Created by Girish on 11-Dec-17.
 */

public interface TouchListener {
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}
