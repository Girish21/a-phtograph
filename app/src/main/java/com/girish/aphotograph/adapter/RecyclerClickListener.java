package com.girish.aphotograph.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Girish on 11-Dec-17.
 */

public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {

    private Context context;
    private RecyclerView recyclerView;
    private TouchListener listener;
    private GestureDetector detector;


    public RecyclerClickListener(Context context, RecyclerView recyclerView, TouchListener listener) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.listener = listener;

        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (listener != null && view != null && detector.onTouchEvent(e))
            listener.onClick(view, rv.getChildLayoutPosition(view));
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
