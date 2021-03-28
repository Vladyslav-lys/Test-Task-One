package com.example.testtaskone.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.testtaskone.EditUserProfileActivity;
import com.example.testtaskone.entities.User;

import java.util.List;

public class EditProfileInvokerUtility {

    public static void goToEditProfile(Context context, List<User> userList, int position) {
        User user = (User) userList.get(position);
        Intent intent = new Intent(context, EditUserProfileActivity.class);
        intent.putExtra("id", user.getId());
        intent.putExtra("firstName", user.getFirstName());
        intent.putExtra("lastName", user.getLastName());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("phone", user.getPhone());
        intent.putExtra("picture", user.getPhoto());
        context.startActivity(intent);
    }
}
