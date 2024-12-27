package com.example.proyekterparkir;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initializing widgets
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Setting default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
        }

        // Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Getting MenuItem id in a variable
                int id = menuItem.getItemId();

                // If else if statement for fragment
                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                } else if (id == R.id.booking) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ParkirFragment()).commit();
                } else if (id == R.id.settings) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
                }
                return true;
            }
        });
    }
}