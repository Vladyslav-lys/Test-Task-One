package com.example.testtaskone;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testtaskone.adapters.SavedUsersDatabaseAdapter;
import com.example.testtaskone.adapters.UserAdapter;
import com.example.testtaskone.entities.User;
import com.example.testtaskone.services.ImageDownloadTask;
import com.example.testtaskone.utils.EditProfileInvokerUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SavedUserFragment extends Fragment implements UserAdapter.OnItemListener {

    RecyclerView listView;
    List<User> userList = new ArrayList<User>();
    UserAdapter userAdapter;
    SavedUsersDatabaseAdapter savedUsersDatabaseAdapter;

    public SavedUserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_users, container, false);
        listView = (RecyclerView) result.findViewById(R.id.user_list);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                User user = (User) userList.get(position);
                savedUsersDatabaseAdapter.open();
                savedUsersDatabaseAdapter.delete(user.getId());
                savedUsersDatabaseAdapter.close();
                userList.remove(position);
                updateUserList();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(listView);

        savedUsersDatabaseAdapter = new SavedUsersDatabaseAdapter(getContext());
        getUsersOnPage();

        return result;
    }

    private void getUsersOnPage() {
        try{
            savedUsersDatabaseAdapter.open();
            userList = savedUsersDatabaseAdapter.getUsers();
            savedUsersDatabaseAdapter.close();
            userAdapter = new UserAdapter(getContext(), userList, this);
            updateUserList();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    @Override
    public void onItemClick(int position) {
        EditProfileInvokerUtility.goToEditProfile(getContext(),userList,position);
    }

    private void updateUserList() {
        listView.setAdapter(userAdapter);
    }
}
