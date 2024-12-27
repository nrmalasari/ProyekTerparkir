package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

public class HasilPencarian extends AppCompatActivity {

    ViewPager2 viewPager2;
    int[] images = {R.drawable.parkir1,R.drawable.parkir1a,R.drawable.parkir1b};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hasil_pencarian);


        // Ikon panah ke belakang akan kembali ke Home
        ImageView arrowBack = findViewById(R.id.arrow_backSearch);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity
                Intent intent = new Intent(HasilPencarian.this, Home.class);
                startActivity(intent);
            }
        });
    }
}