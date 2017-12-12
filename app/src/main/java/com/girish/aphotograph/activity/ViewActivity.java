package com.girish.aphotograph.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.girish.aphotograph.R;

public class ViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ViewImageActivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        toolbar = findViewById(R.id.view_tool_bar);
        setSupportActionBar(toolbar);

        imageView = findViewById(R.id.view_image);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("com.girish.aphotograph.URL"))
                .into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
