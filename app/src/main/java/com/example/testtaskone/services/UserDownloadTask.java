package com.example.testtaskone.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.testtaskone.EditUserProfileActivity;
import com.example.testtaskone.adapters.UserAdapter;
import com.example.testtaskone.entities.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UserDownloadTask extends AsyncTask<String, Void, String> {

    private ProgressDialog dialog;
    private Context context;

    public UserDownloadTask(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("Wait...");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            URLConnection connection = url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());

            return streamToString(in);

        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
    }

    private String streamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
        } catch ( IOException e) {
            Log.d("Error", e.toString());
        } finally {
            reader.close();
        }
        return stringBuilder.toString();
    }
}


