package com.example.testtaskone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    ImageButton usersBtn, savedBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersBtn = (ImageButton) findViewById(R.id.usersBtn);
        savedBtn = (ImageButton) findViewById(R.id.savedBtn);

        Bundle arguments = getIntent().getExtras();

        if(arguments != null) {
            Boolean isSaved = arguments.getBoolean("saved");

            if(!isSaved)
                return;

            openSaved();
            return;
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_view, UserFragment.class, null)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!getSupportActionBar().getTitle().equals("Saved"))
            goToUsers();
    }

    @Override
    public void onBackPressed() {
        if(getSupportActionBar().getTitle().equals("Saved"))
            goToUsers();
        else
            this.finish();
    }

    public void getToUsers(View view) {
        goToUsers();
    }

    private void goToUsers() {
        usersBtn.setImageResource(R.drawable.tab_users_active);
        savedBtn.setImageResource(R.drawable.tab_saved);
        getSupportActionBar().setTitle("Users");
        setFragmentByClass(UserFragment.class);
    }

    public void getToSaved(View view) {
        openSaved();
    }

    private void openSaved() {
        usersBtn.setImageResource(R.drawable.tab_users);
        savedBtn.setImageResource(R.drawable.tab_saved_active);
        getSupportActionBar().setTitle("Saved");
        setFragmentByClass(SavedUserFragment.class);
    }

    private void setFragmentByClass(Class<? extends androidx.fragment.app.Fragment> _class) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, _class, null)
                .addToBackStack(null)
                .commit();
    }
}