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

public class PetaParkiran2Fragment extends Fragment {

    private TextView textViewStatusLantai1Ke1;
    private TextView textViewStatusLantai1Ke2;
    private TextView textViewStatusLantai2Ke1;
    private TextView textViewStatusLantai2Ke2;
    private View parkiran2Lantai1Ke1;
    private View parkiran2Lantai1Ke2;
    private View parkiran2Lantai2Ke1;
    private View parkiran2Lantai2Ke2;

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peta_parkiran2, container, false);

        // Inisialisasi TextView
        textViewStatusLantai1Ke1 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran2Lantai1Ke1);
        textViewStatusLantai1Ke2 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran2Lantai1Ke2);
        textViewStatusLantai2Ke1 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran2Lantai2Ke1);
        textViewStatusLantai2Ke2 = view.findViewById(R.id.textViewAdaTidakAdaMobilParkiran2Lantai2Ke2);

        // Inisialisasi View parkiran
        parkiran2Lantai1Ke1 = view.findViewById(R.id.parkiran2_lanatai1_ke1);
        parkiran2Lantai1Ke2 = view.findViewById(R.id.parkiran2_lanatai1_ke2);
        parkiran2Lantai2Ke1 = view.findViewById(R.id.parkiran2_lanatai2_ke1);
        parkiran2Lantai2Ke2 = view.findViewById(R.id.parkiran2_lanatai2_ke2);

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

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                // Ambil data status dari masing-masing sensor
                String statusSensor5 = snapshot.child("SENSOR5").child("NAMA:G2-F1-1").child("STATUS").getValue(String.class);
                String statusSensor6 = snapshot.child("SENSOR6").child("NAMA:G2-F1-2").child("STATUS").getValue(String.class);
                String statusSensor7 = snapshot.child("SENSOR7").child("NAMA:G2-F2-1").child("STATUS").getValue(String.class);
                String statusSensor8 = snapshot.child("SENSOR8").child("NAMA:G2-F2-2").child("STATUS").getValue(String.class);

                // Update TextView dan View parkiran berdasarkan status
                updateStatus(parkiran2Lantai1Ke1, textViewStatusLantai1Ke1, statusSensor5);
                updateStatus(parkiran2Lantai1Ke2, textViewStatusLantai1Ke2, statusSensor6);
                updateStatus(parkiran2Lantai2Ke1, textViewStatusLantai2Ke1, statusSensor7);
                updateStatus(parkiran2Lantai2Ke2, textViewStatusLantai2Ke2, statusSensor8);
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
        parkiran2Lantai1Ke1.setBackgroundColor(Color.RED);
        parkiran2Lantai1Ke2.setBackgroundColor(Color.RED);
        parkiran2Lantai2Ke1.setBackgroundColor(Color.RED);
        parkiran2Lantai2Ke2.setBackgroundColor(Color.RED);

        textViewStatusLantai1Ke1.setText("Gagal memuat data");
        textViewStatusLantai1Ke2.setText("Gagal memuat data");
        textViewStatusLantai2Ke1.setText("Gagal memuat data");
        textViewStatusLantai2Ke2.setText("Gagal memuat data");
    }
}
