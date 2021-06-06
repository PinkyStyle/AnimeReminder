package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.animereminder.controllers.AnimeController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class NavigationUserActivity extends AppCompatActivity {
    TempUserFragment tempUserFragment = new TempUserFragment();
    ListUserFragment listUserFragment = new ListUserFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_view_user);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Temporada</font>"));
        BottomNavigationView navigationUser = findViewById(R.id.bottom_navigation_user);
        navigationUser.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(tempUserFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NotNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment_user:
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Temporada</font>"));
                    loadFragment(tempUserFragment);
                    return true;
                case R.id.secondFragment_user:
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Mi lista</font>"));
                    loadFragment(listUserFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container_user, fragment);
        transaction.commit();
    }
}