package com.example.proyekterparkir;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PetaParkiran3Fragment extends Fragment {

    private TextView textViewStatusLantai1Ke1;
    private TextView textViewStatusLantai1Ke2;
    private TextView textViewStatusLantai2Ke1;
    private TextView textViewStatusLantai2Ke2;
    private View parkiran3Lantai1Ke1;
    private View parkiran3Lantai1Ke2;
    private View parkiran3Lantai2Ke1;
    private View parkiran3Lantai2Ke2;

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peta_parkiran3, container, false);

        // Inisialisasi TextView
        textViewStatusLantai1Ke1 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran3Lantai1Ke1);
        textViewStatusLantai1Ke2 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran3Lantai1Ke2);
        textViewStatusLantai2Ke1 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran3Lantai2Ke1);
        textViewStatusLantai2Ke2 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran3Lantai2Ke2);

        // Inisialisasi View parkiran
        parkiran3Lantai1Ke1 = view.findViewById(R.id.parkiran3_lanatai1_ke1);
        parkiran3Lantai1Ke2 = view.findViewById(R.id.parkiran3_lanatai1_ke2);
        parkiran3Lantai2Ke1 = view.findViewById(R.id.parkiran3_lanatai2_ke1);
        parkiran3Lantai2Ke2 = view.findViewById(R.id.parkiran3_lanatai2_ke2);

        // Inisialisasi Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("DATA_SENSOR");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Menambahkan ValueEventListener ketika fragment mulai
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Menghapus ValueEventListener untuk menghindari memory leaks
        databaseReference.removeEventListener(valueEventListener);
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                // Ambil data status dari masing-masing sensor
                String statusSensor9 = snapshot.child("SENSOR9").child("NAMA:G3-F1-1").child("STATUS").getValue(String.class);
                String statusSensor10 = snapshot.child("SENSOR10").child("NAMA:G3-F1-2").child("STATUS").getValue(String.class);
                String statusSensor11 = snapshot.child("SENSOR11").child("NAMA:G3-F2-1").child("STATUS").getValue(String.class);
                String statusSensor12 = snapshot.child("SENSOR12").child("NAMA:G3-F2-2").child("STATUS").getValue(String.class);

                // Update TextView dan View parkiran berdasarkan status
                updateStatus(parkiran3Lantai1Ke1, textViewStatusLantai1Ke1, statusSensor9);
                updateStatus(parkiran3Lantai1Ke2, textViewStatusLantai1Ke2, statusSensor10);
                updateStatus(parkiran3Lantai2Ke1, textViewStatusLantai2Ke1, statusSensor11);
                updateStatus(parkiran3Lantai2Ke2, textViewStatusLantai2Ke2, statusSensor12);
            } else {
                // Jika data tidak tersedia
                setDefaultStatus();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            setDefaultStatus();
        }
    };

    private void updateStatus(View parkiranView, TextView statusTextView, String status) {
        if (status != null) {
            if (status.equalsIgnoreCase("Parkiran Terisi")) {
                parkiranView.setBackgroundColor(Color.GREEN); // Hijau jika terisi
                statusTextView.setText("Parkiran Terisi");
            } else {
                parkiranView.setBackgroundColor(Color.RED); // Merah jika kosong
                statusTextView.setText("Parkiran Kosong");
            }
        } else {
            // Jika status null, anggap parkiran kosong
            parkiranView.setBackgroundColor(Color.RED); // Merah
            statusTextView.setText("Data tidak tersedia");
        }
    }

    private void setDefaultStatus() {
        // Set default status saat gagal mengambil data
        parkiran3Lantai1Ke1.setBackgroundColor(Color.RED);
        parkiran3Lantai1Ke2.setBackgroundColor(Color.RED);
        parkiran3Lantai2Ke1.setBackgroundColor(Color.RED);
        parkiran3Lantai2Ke2.setBackgroundColor(Color.RED);

        textViewStatusLantai1Ke1.setText("Gagal memuat data");
        textViewStatusLantai1Ke2.setText("Gagal memuat data");
        textViewStatusLantai2Ke1.setText("Gagal memuat data");
        textViewStatusLantai2Ke2.setText("Gagal memuat data");
    }
}
