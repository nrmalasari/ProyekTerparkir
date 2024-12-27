package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHomeAdmin extends Fragment {

    private TextView totalUserTextView; // TextView untuk menampilkan total user

    public FragmentHomeAdmin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        // Inisialisasi TextView dari layout
        totalUserTextView = view.findViewById(R.id.totalUuser);

        // Panggil metode untuk menghitung total user
        fetchTotalUsers();

        // Inisialisasi tombol atau View yang akan memunculkan PopupMenu
        View menuButton = view.findViewById(R.id.menu_button); // Pastikan ada tombol dengan ID ini di layout

        // Mengatur PopupMenu untuk menampilkan menu
        menuButton.setOnClickListener(v -> showPopupMenu(v));

        return view;
    }

    private void fetchTotalUsers() {
        // Referensi ke Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");

        // Listener untuk membaca data dari Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Hitung jumlah anak (child) di node "users"
                long totalUsers = snapshot.getChildrenCount();

                // Tampilkan hasil di TextView
                totalUserTextView.setText(String.valueOf(totalUsers));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Tangani kesalahan jika ada
                totalUserTextView.setText("Error");
            }
        });
    }

    // Method untuk menampilkan PopupMenu
    public void showPopupMenu(View anchorView) {
        // Buat PopupMenu
        PopupMenu popupMenu = new PopupMenu(getContext(), anchorView);

        // Inflate menu dari XML ke dalam PopupMenu
        popupMenu.getMenuInflater().inflate(R.menu.dropdown_menu, popupMenu.getMenu());

        // Set listener ketika item diklik
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.profilAdminHome) {
                // Tindakan ketika "Profile" dipilih
                Toast.makeText(getContext(), "Profile dipilih", Toast.LENGTH_SHORT).show();

                // Intent untuk berpindah ke ProfileAdmin.class
                Intent intent = new Intent(getContext(), ProfileAdmin.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.logoutAdminHome) {
                // Tindakan ketika "Logout" dipilih
                Toast.makeText(getContext(), "Logout dipilih", Toast.LENGTH_SHORT).show();

                // Intent untuk berpindah ke Login Activity
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);

                // Menutup Fragment atau Activity saat ini jika diperlukan
                if (getActivity() != null) {
                    getActivity().finish(); // Menutup activity yang sedang berjalan
                }
                return true;
            }
            return false;
        });

        // Tampilkan PopupMenu
        popupMenu.show();
    }
}
