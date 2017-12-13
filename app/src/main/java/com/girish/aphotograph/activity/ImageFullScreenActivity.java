package com.girish.aphotograph.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.girish.aphotograph.R;
import com.girish.aphotograph.extra.TouchImageView;

public class ImageFullScreenActivity extends AppCompatActivity {

    Toolbar toolbar;
    TouchImageView imageView;
    String url, id, format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_image_full_screen);

        toolbar = findViewById(R.id.full_screen_image_view_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_done_white_24dp);

        imageView = findViewById(R.id.full_screen_image_view_image);

        Intent intent = getIntent();

        url = intent.getStringExtra("com.girish.aphotograph.URL");
        id = intent.getStringExtra("com.girish.aphotograph.ID");
        format = intent.getStringExtra("com.girish.aphotograph.FORMAT");

//        adjustImageAspect(2048, 2048);

        Glide.with(this)
            .load(url)
            .into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    int width = resource.getIntrinsicWidth();
                    int height = resource.getIntrinsicHeight();
                    adjustImageAspect(width, height);
                    imageView.setImageDrawable(resource);
                }
            });

    }

    private void adjustImageAspect(int bWidth, int bHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        if (bWidth == 0 || bHeight == 0)
            return;

        int sHeight = 0;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sHeight = size.y;

        int new_width = (int) Math.floor((double) bWidth * (double) sHeight
                / (double) bHeight);
        params.width = new_width;
        params.height = sHeight;

        Log.d("com.girish.aphotograph", "Fullscreen image new dimensions: w = " + new_width
                + ", h = " + sHeight);

        imageView.setLayoutParams(params);
    }
}
