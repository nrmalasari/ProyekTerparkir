package com.example.proyekterparkir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiwayatParkir extends AppCompatActivity {

    private TextView textViewID, textViewRiwayatWaktuParkir, textViewRiwayatBiaya, textViewPosisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_riwayat_parkir);

        // Inisialisasi Views
        textViewID = findViewById(R.id.textViewID);
        textViewRiwayatWaktuParkir = findViewById(R.id.textViewRiwayatWaktuParkir);
        textViewRiwayatBiaya = findViewById(R.id.textViewRiwayatBiaya);
        textViewPosisi = findViewById(R.id.textViewPosisi);
        ImageView arrowBack = findViewById(R.id.arrow_backRiwayat);

        // Fungsi tombol kembali
        arrowBack.setOnClickListener(view -> {
            Intent intent = new Intent(RiwayatParkir.this, SettingsFragment.class);
            startActivity(intent);
        });

        // Referensi Firebase
        DatabaseReference detectionsRef = FirebaseDatabase.getInstance().getReference("detections");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("DATA_SENSOR");

        // Ambil email pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email_user", "");
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ganti karakter "." di email dengan "," sesuai format Firebase
        String sanitizedEmail = userEmail.replace(".", ",");

        // Ambil data plat nomor terakhir dari "detections"
        fetchLatestPlatNomor(detectionsRef, textViewID);

        // Ambil data waktu parkir dan biaya dari "users"
        fetchUserData(usersRef, sanitizedEmail, textViewRiwayatWaktuParkir, textViewRiwayatBiaya);

        // Ambil data posisi parkir dari "DATA_SENSOR"
        fetchSensorData(sensorRef, textViewPosisi);
    }

    private void fetchLatestPlatNomor(DatabaseReference detectionsRef, TextView textView) {
        detectionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String latestPlatNomor = null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    String platNomor = child.child("plat_nomor").getValue(String.class);
                    if (platNomor != null) {
                        latestPlatNomor = platNomor; // Update dengan data terakhir yang ditemukan
                    }
                }
                if (latestPlatNomor != null) {
                    textView.setText("Plat  : " + latestPlatNomor);
                } else {
                    textView.setText("Plat  : Tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RiwayatParkir.this, "Gagal mengambil data plat: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserData(DatabaseReference usersRef, String sanitizedEmail, TextView waktuView, TextView biayaView) {
        usersRef.child(sanitizedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String waktuParkir = snapshot.child("waktu_parkir").getValue(String.class);
                Long biayaParkir = snapshot.child("biaya_parkir").getValue(Long.class);

                if (waktuParkir != null) {
                    waktuView.setText("Waktu Parkir: " + waktuParkir);
                } else {
                    waktuView.setText("Waktu Parkir: Tidak ditemukan");
                }

                if (biayaParkir != null) {
                    biayaView.setText("Biaya : Rp." + biayaParkir);
                } else {
                    biayaView.setText("Biaya : Tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RiwayatParkir.this, "Gagal mengambil data pengguna: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSensorData(DatabaseReference sensorRef, TextView textView) {
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String namaTerisi = null;
                for (DataSnapshot sensor : snapshot.getChildren()) {
                    String status = sensor.child("STATUS").getValue(String.class);
                    String nama = sensor.child("NAMA").getValue(String.class);

                    if ("Parkiran Terisi".equalsIgnoreCase(status) && nama != null) {
                        namaTerisi = nama;
                        break; // Keluar dari loop setelah menemukan data
                    }
                }
                if (namaTerisi != null) {
                    textView.setText("Posisi : " + namaTerisi);
                } else {
                    textView.setText("Posisi : Tidak ditemukan");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(RiwayatParkir.this, "Gagal mengambil data sensor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
