package com.example.animereminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.botton_action_all_users, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.cerrar_sesion){
            Toast.makeText(this,"Cerrar sesión",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(NavigationActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
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