package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class PetaParkir1 extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView arrowBackPeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peta_parkir1);

        // Inisialisasi View
        tabLayout = findViewById(R.id.tableLayout);
        viewPager = findViewById(R.id.viewPager);
        arrowBackPeta = findViewById(R.id.arrow_backPeta);

        // Setup TabLayout dengan ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Setup ViewPager dengan adapter
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new PetaParkiran1Fragment(), "Gedung 1");
        vpAdapter.addFragment(new PetaParkiran2Fragment(), "Grdung 2");
        vpAdapter.addFragment(new PetaParkiran3Fragment(), "Gedung 3");
        viewPager.setAdapter(vpAdapter);

        // Ambil data navigateTo dari Intent
        String navigateTo = getIntent().getStringExtra("navigateTo");
        if (navigateTo != null) {
            switch (navigateTo) {
                case "PetaParkiran1Fragment":
                    viewPager.setCurrentItem(0); // Tampilkan tab pertama
                    break;
                case "PetaParkiran2Fragment":
                    viewPager.setCurrentItem(1); // Tampilkan tab kedua
                    break;
                case "PetaParkiran3Fragment":
                    viewPager.setCurrentItem(2); // Tampilkan tab ketiga
                    break;
            }
        }

        // Fungsi tombol back ke HasilPencarian
        arrowBackPeta.setOnClickListener(v -> {
            Intent intent = new Intent(PetaParkir1.this, HomeFragment.class);
            startActivity(intent);
            finish(); // Tutup aktivitas saat ini
        });


        // Atur padding untuk sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
