package com.example.proyekterparkir;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ParkirFragment extends Fragment {

    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private long stopTime = 0;

    private TextView textViewBookingIn2, textViewBookingPrice2, textViewBookingPlat, textViewBookingSpot2;
    private Button btnWaktuBerhenti;
    private ImageView arrowBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parkir, container, false);

        // Inisialisasi Views
        textViewBookingIn2 = view.findViewById(R.id.textViewBookingIn2);
        textViewBookingPrice2 = view.findViewById(R.id.textViewBookingPrice2);
        textViewBookingPlat = view.findViewById(R.id.textViewBookingPlat);
        textViewBookingSpot2 = view.findViewById(R.id.textViewBookingSpot2);
        btnWaktuBerhenti = view.findViewById(R.id.btn_waktu_berhenti);
        arrowBack = view.findViewById(R.id.arrow_back1);

        // Firebase Realtime Database
        DatabaseReference detectionsRef = FirebaseDatabase.getInstance().getReference("detections");
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference sensorRef = FirebaseDatabase.getInstance().getReference("DATA_SENSOR");

        // Listener untuk mendeteksi data baru di "detections"
        detectionsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String platNomor = snapshot.child("plat_nomor").getValue(String.class);
                if (platNomor != null && textViewBookingPlat != null) {
                    textViewBookingPlat.setText("Plat Anda: " + platNomor);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        sensorRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Tidak ada logika di sini, karena kita hanya ingin memantau perubahan
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Ketika ada perubahan di salah satu sensor, cek anaknya
                for (DataSnapshot sensorSnapshot : snapshot.getChildren()) {
                    String namaSensor = sensorSnapshot.getKey(); // Ambil nama sensor
                    if (namaSensor != null) {
                        // Ambil status dari anak "NAMA"
                        String statusSensor = sensorSnapshot.child("STATUS").getValue(String.class);

                        // Jika status berubah menjadi "Parkiran Terisi", tampilkan nama sensor
                        if ("Parkiran Terisi".equalsIgnoreCase(statusSensor)) {
                            textViewBookingSpot2.setText(namaSensor);
                            break; // Keluar dari loop setelah menemukan sensor yang ter-update
                        } else {
                            // Jika status berubah ke selain "Parkiran Terisi", kosongkan textView
                            textViewBookingSpot2.setText("Tidak ada");
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Jika data dihapus, kosongkan textView
                textViewBookingSpot2.setText("Tidak ada");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Tidak diperlukan untuk skenario ini
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Gagal mengambil data sensor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });




        // Ambil email pengguna dari SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email_user", "");
        long loginTime = sharedPreferences.getLong("login_time", 0);

        // Validasi SharedPreferences
        if (userEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Jika waktu login valid, mulai timer
        if (loginTime > 0) {
            updateRunnable = new Runnable() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis();
                    long durationMillis = currentTime - loginTime;

                    // Format durasi dan hitung biaya parkir
                    String duration = formatDuration(durationMillis);
                    long hours = TimeUnit.MILLISECONDS.toHours(durationMillis) + 1; // Dibulatkan ke atas
                    long parkingFee = hours * 2000;

                    // Perbarui UI
                    if (textViewBookingIn2 != null && textViewBookingPrice2 != null) {
                        textViewBookingIn2.setText(duration);
                        textViewBookingPrice2.setText("Rp. " + parkingFee);
                    }

                    // Jalankan ulang setelah 1 detik
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(updateRunnable);
        }

        btnWaktuBerhenti.setOnClickListener(v -> {
            if (userEmail.isEmpty()) {
                Toast.makeText(getActivity(), "Email pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            stopTime = System.currentTimeMillis();
            long durationMillis = stopTime - loginTime;
            String duration = formatDuration(durationMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(durationMillis) + 1;
            long parkingFee = hours * 2000;

            // Simpan waktu berhenti di SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("stop_time", stopTime);
            editor.apply();

            // Hentikan pembaruan waktu
            if (handler != null && updateRunnable != null) {
                handler.removeCallbacks(updateRunnable);
            }

            // Update data waktu parkir dan biaya parkir ke Firebase
            String sanitizedEmail = userEmail.replace(".", ",");
            usersRef.child(sanitizedEmail).child("waktu_parkir").setValue(duration);
            usersRef.child(sanitizedEmail).child("biaya_parkir").setValue(parkingFee)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Data parkir diperbarui", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Kirim data buka palang ke Firebase
            DatabaseReference palangRef = FirebaseDatabase.getInstance().getReference("palang_status");
            palangRef.setValue(1); // Kirim nilai 1 untuk membuka palang

            // Tampilkan dialog notifikasi
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Konfirmasi Buka Palang")
                    .setMessage("Jika Telah Keluar Klik OKE")
                    .setPositiveButton("OKE", (dialog, which) -> {
                        palangRef.setValue(0); // Perbarui nilai menjadi 0
                        Toast.makeText(getActivity(), "Palang Keluar Tertutup", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .setNegativeButton("Batal", (dialog, which) -> {
                        Toast.makeText(getActivity(), "Aksi dibatalkan", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });

            // Buat dan tunjukkan dialog
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(dialogInterface -> {
                // Ubah warna background tombol OKE
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(requireContext().getResources().getColor(R.color.blue)); // Ganti dengan warna Anda
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(requireContext().getResources().getColor(android.R.color.white)); // Warna teks putih

                // Ubah warna background tombol Batal
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(requireContext().getResources().getColor(R.color.blue)); // Ganti dengan warna Anda
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(requireContext().getResources().getColor(android.R.color.white)); // Warna teks putih
            });
            dialog.show();
        });


            // Tombol kembali
        arrowBack.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Home.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }

    private String formatDuration(long durationMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
