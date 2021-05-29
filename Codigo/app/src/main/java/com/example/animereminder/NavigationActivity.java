package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

public class NavigationActivity extends AppCompatActivity {
    TempAdminFragment tempAdminFragment = new TempAdminFragment();
    DashboardAdminFragment dashboardAdminFragment = new DashboardAdminFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_view);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Temporada</font>"));
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(tempAdminFragment);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NotNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstFragment:
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Temporada</font>"));
                    loadFragment(tempAdminFragment);
                    return true;
                case R.id.secondFragment:
                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#35424a'>Resumen</font>"));
                    loadFragment(dashboardAdminFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }


}