package com.example.proyekterparkir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BarcodeSide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_side);

        // Ikon panah ke belakang akan kembali ke Home
        ImageView arrowBack = findViewById(R.id.arrow_back_barcode);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke MainActivity (Home)
                Intent intent = new Intent(BarcodeSide.this, Home.class);
                startActivity(intent);
            }
        });

        // Ambil QR code dari SharedPreferences
        ImageView pictBarcode = findViewById(R.id.pictBarcode);
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String barcodeBase64 = sharedPreferences.getString("barcode_image", null);

        if (barcodeBase64 != null) {
            byte[] decodedString = Base64.decode(barcodeBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            pictBarcode.setImageBitmap(bitmap);
        }
    }
}
