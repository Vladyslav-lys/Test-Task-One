package com.example.testtaskone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testtaskone.adapters.UserAdapter;
import com.example.testtaskone.entities.User;
import com.example.testtaskone.services.ImageDownloadTask;
import com.example.testtaskone.services.UserDownloadTask;
import com.example.testtaskone.utils.EditProfileInvokerUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment implements UserAdapter.OnItemListener{
    RecyclerView listView;
    List<User> userList = new ArrayList<User>();
    List<User> curUserList = new ArrayList<User>();
    LinearLayout pageBtnLayout;
    UserAdapter adapter;
    int count, curListCount;

    public UserFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_users, container, false);
        listView = (RecyclerView) result.findViewById(R.id.user_list);
        pageBtnLayout = (LinearLayout) result.findViewById(R.id.pageBtnLayout);

        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsersOnPage();
    }

    @Override
    public void onItemClick(int position) {
        EditProfileInvokerUtility.goToEditProfile(getContext(),userList,position);
    }

    private void getUsersOnPage() {
        try{
            String res = new UserDownloadTask(getContext())
                    .execute("https://randomuser.me/api/?results=50&inc=name,email,phone,picture&nat=us")
                    .get();

            JSONObject json = new JSONObject(res);
            JSONArray resultArray = json.getJSONArray("results");
            count = resultArray.length();
            curListCount = 10;

            for(int i=0; i<resultArray.length(); i++) {
                JSONObject element = resultArray.getJSONObject(i);
                String firstName = element.getJSONObject("name").optString("first");
                String lastName = element.getJSONObject("name").optString("last");
                String email = element.optString("email");
                String phone = element.optString("phone");
                ImageDownloadTask task = new ImageDownloadTask();
                Bitmap pic = task
                        .execute(element.getJSONObject("picture").optString("large"))
                        .get();
                User newUser = new User(0, pic, firstName, lastName, email, phone);
                userList.add(newUser);
            }

            goToPage(0);

            adapter = new UserAdapter(getContext(), curUserList, this);
            listView.setAdapter(adapter);

            if(count > curListCount) {
                for (int i = 0; i < (count / curListCount); i++) {
                    createButton(i);
                }
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    private void createButton(int numOfBtn) {
        Button btnTag = new Button(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.setMargins(0,0,0,0);
        btnTag.setLayoutParams(params);
        btnTag.setText(String.valueOf(numOfBtn+1));
        btnTag.setTag(numOfBtn);
        btnTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goToPage((Integer) v.getTag());
                adapter.notifyDataSetChanged();
            }
        });

        pageBtnLayout.addView(btnTag);
    }

    private void goToPage(int pageNum) {
        curUserList.clear();
        listView.smoothScrollToPosition(0);
        int curPageNum = pageNum*curListCount;
        for (int j=curPageNum; j<(curPageNum+curListCount); j++)
            curUserList.add(userList.get(j));
    }
}
