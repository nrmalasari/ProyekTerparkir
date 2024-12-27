package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout untuk fragmen ini
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Inisialisasi tombol edit profil dan tambahkan click listener
        Button editProfileButton = view.findViewById(R.id.button_edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mulai aktivitas EditProfile
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        // Inisialisasi tombol edit profil dan tambahkan click listener
        Button riwayatParkirButton = view.findViewById(R.id.button_riwayat_parkir);
        riwayatParkirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mulai aktivitas EditProfile
                Intent intent = new Intent(getActivity(), RiwayatParkir.class);
                startActivity(intent);
            }
        });

        TextView textView_keluar = view.findViewById(R.id.button_logout);
        textView_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        // Tambahkan click listener untuk panah kembali jika diperlukan
        ImageView arrowBack = view.findViewById(R.id.arrow_backProfile);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke MainActivity
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
