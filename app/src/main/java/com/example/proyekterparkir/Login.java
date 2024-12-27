package com.example.proyekterparkir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class Login extends AppCompatActivity {

    EditText loginEmail, loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi EditText
        loginEmail = findViewById(R.id.email);
        loginPass = findViewById(R.id.pass);

        // Mengambil email yang disimpan di SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email_user", "");

        if (!TextUtils.isEmpty(email)) {
            loginEmail.setText(email); // Menampilkan email yang sudah didaftarkan
        }

        // Tombol masuk
        Button btnMasuk = findViewById(R.id.btn_masuk_login);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String pass = loginPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Masukkan Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(Login.this, "Masukkan Kata Sandi", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkUser();
            }
        });

        TextView linkDaftar = findViewById(R.id.textView4);
        linkDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });
    }

    private void checkUser() {
        String userEmail = loginEmail.getText().toString();
        String userPass = loginPass.getText().toString();

        // Cek apakah username dan password adalah admin
        if (userEmail.equals("admin") && userPass.equals("admin123")) {
            Toast.makeText(Login.this, "Login sebagai Admin", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, HomeAdmin.class);
            startActivity(intent);
            return;
        }

        // Jika bukan admin, lanjutkan proses validasi ke database Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(userEmail);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                    String passFromDB = userSnapshot.child("pass").getValue(String.class);

                    if (passFromDB != null && passFromDB.equals(userPass)) {
                        String emailFromDB = userSnapshot.child("email").getValue(String.class);

                        // Simpan email ke SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email_user", emailFromDB);  // Simpan email untuk digunakan di EditProfile
                        editor.putLong("login_time", System.currentTimeMillis()); // Simpan waktu login
                        editor.apply();

                        // Generate QR code dengan email atau data unik lainnya
                        generateQRCode(emailFromDB);

                    } else {
                        loginPass.setError("Kata sandi salah");
                        loginPass.requestFocus();
                    }
                } else {
                    loginEmail.setError("Email salah");
                    loginEmail.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateQRCode(String data) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(data, com.google.zxing.BarcodeFormat.QR_CODE, 222, 227);

            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Menyimpan QR Code dalam format Base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            String barcodeBase64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            editor.putString("barcode_image", barcodeBase64); // Menyimpan QR Code di SharedPreferences
            editor.apply();

            // Kirim QR Code ke halaman Barcode
            Intent intent = new Intent(Login.this, BarcodeSide.class);
            intent.putExtra("qr_code_bitmap", bitmap);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Login.this, "Failed to generate QR Code", Toast.LENGTH_SHORT).show();
        }
    }

}
