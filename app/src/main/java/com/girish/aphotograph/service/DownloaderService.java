package com.girish.aphotograph.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Girish on 13-Dec-17.
 */

public class DownloaderService extends IntentService {

    final String downloadNotificationChannelID = "com.girish.aphotograph.DownloadNotification";

    public DownloaderService() {
        super("Downloader Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        File storeImage;
        FileOutputStream outputStream = null;
        NotificationManager manager = null;
        NotificationCompat.Builder builder = null;
        final int id = 1000;
        try {
            if (intent != null) {
                String baseUrl = intent.getStringExtra("com.girish.aphotograph.URL");
                String fileName = intent.getStringExtra("com.girish.aphotograph.ID");
                String format = intent.getStringExtra("com.girish.aphotograph.FORMAT");

                manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(getApplicationContext(), downloadNotificationChannelID);
                builder.setContentTitle("Image Downloading")
                        .setContentText("Download in progress")
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setAutoCancel(true)
                        .setShowWhen(true)
                        .setProgress(0, 0, true);

                Intent nIntent = getPackageManager().
                        getLaunchIntentForPackage("com.girish.aphotograph");
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                if (manager != null)
                    manager.notify(id, builder.build());

                url = new URL(baseUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();

                storeImage = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile()
                        + "/" + fileName + "." + format);
                outputStream = new FileOutputStream(storeImage);

                int totalSize = httpURLConnection.getContentLength();
                Log.i("Size", "" + totalSize);
                int progress = 0;
                int updateProgress;

                int read;
                byte[] data = new byte[1024];
                while ((read = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, read);
                    progress += read;
                    double temp = ((double) progress / totalSize) * 100;
                    updateProgress = (int) Math.floor(temp);
                    builder.setProgress(100, updateProgress, false)
                            .setContentText("" + updateProgress + "%");
                    if (manager != null)
                        manager.notify(id, builder.build());
                }
                Thread.sleep(100);
                builder.setContentText("Download complete")
                        .setSmallIcon(android.R.drawable.stat_sys_download_done)
                        .setProgress(0, 0, false);
                if (manager != null)
                    manager.notify(id, builder.build());

                Log.i("Download", "Done");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (builder != null && manager != null) {
                builder.setProgress(0, 0, false)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentText("Error cannot download");
                manager.notify(id, builder.build());
            }
        } finally {

            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }
}
