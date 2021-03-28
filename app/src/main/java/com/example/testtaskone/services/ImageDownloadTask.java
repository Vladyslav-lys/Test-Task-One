package com.example.testtaskone.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        try {
            URL imageUrl = new URL(url);
            InputStream is = imageUrl.openConnection().getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(is);
            return imageBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
