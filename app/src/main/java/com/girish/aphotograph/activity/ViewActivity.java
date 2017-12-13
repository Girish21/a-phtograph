package com.girish.aphotograph.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.girish.aphotograph.MyApplication;
import com.girish.aphotograph.R;
import com.girish.aphotograph.extra.CheckConnection;
import com.girish.aphotograph.extra.QueryParamsUtil;
import com.girish.aphotograph.service.DownloaderService;
import com.girish.aphotograph.util.ParseImageModel;
import com.girish.aphotograph.util.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    Button downloadImage;
    long id;
    String url;
    Intent downloaderService;
    Activity activity;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0)
            if (requestCode == 100)
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    startService(downloaderService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ViewImageActivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        activity = this;

        toolbar = findViewById(R.id.view_tool_bar);
        setSupportActionBar(toolbar);

        downloadImage = findViewById(R.id.download_image);
        imageView = findViewById(R.id.view_image);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getLongExtra("com.girish.aphotograph.ID", 0);
        url = intent.getStringExtra("com.girish.aphotograph.URL");

        if (savedInstanceState != null) {
            Glide.with(this)
                    .load(savedInstanceState.getString("com.girish.aphotograph.ReturnURL"))
                    .error(R.drawable.ic_error_black_24dp)
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(url)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(imageView);
        }

        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = MyApplication.getRetrofitApiClient();
                RetrofitUtil util = retrofit.create(RetrofitUtil.class);
                if (CheckConnection.isInternetAvailable(getApplicationContext())) {
                    Call<ParseImageModel> imageModelCall = util.getImageHighRes(
                            id,
                            QueryParamsUtil.getPhotoQuery()
                    );
                    if (imageModelCall != null) {
                        imageModelCall.enqueue(new Callback<ParseImageModel>() {
                            @Override
                            public void onResponse(@NonNull Call<ParseImageModel> call, @NonNull Response<ParseImageModel> response) {
                                ParseImageModel model = response.body();
                                downloaderService = new Intent(getApplicationContext(), DownloaderService.class);
                                downloaderService.putExtra("com.girish.aphotograph.URL", model.image.details.get(0).getUrl());
                                downloaderService.putExtra("com.girish.aphotograph.ID", String.valueOf(id));
                                downloaderService.putExtra("com.girish.aphotograph.FORMAT", model.image.details.get(0).getFormat());
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    startService(downloaderService);
                                } else
                                    ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                            }

                            @Override
                            public void onFailure(@NonNull Call<ParseImageModel> call, @NonNull Throwable t) {
                                Log.i("Error", t.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("com.girish.aphotograph.ReturnURL", url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
