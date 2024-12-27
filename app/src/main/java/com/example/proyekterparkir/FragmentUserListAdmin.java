package com.example.proyekterparkir;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentUserListAdmin extends Fragment {

    private RecyclerView rvUser;
    private DatabaseReference database;
    private UserAdapter adapter;
    private ArrayList<HelperClass> userListAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list_admin, container, false);

        // Inisialisasi RecyclerView
        rvUser = view.findViewById(R.id.rvUser);
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rvUser.setItemAnimator(new DefaultItemAnimator());

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().getReference("users");

        // Memanggil fungsi untuk mengambil data user dari Firebase
        fetchUserData();

        return view;
    }

    private void fetchUserData() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userListAdmin = new ArrayList<>();
                // Melakukan looping terhadap data yang ada di Firebase
                for (DataSnapshot item : snapshot.getChildren()) {
                    // Debug log untuk memeriksa data yang diterima
                    System.out.println("Key: " + item.getKey());
                    System.out.println("Value: " + item.getValue());

                    // Membuat objek HelperClass dan mengisi data
                    HelperClass user = new HelperClass(
                            item.child("nama").getValue(String.class),
                            item.child("email").getValue(String.class),
                            item.child("pass").getValue(String.class),
                            "",  // Kosongkan konf_pass, jika Anda tidak membutuhkan data ini
                            item.child("no_hp").getValue(String.class)
                    );

                    // Pastikan data valid sebelum ditambahkan ke list
                    if (user.getEmail() != null && user.getNama() != null) {
                        userListAdmin.add(user);
                    } else {
                        // Log error jika data tidak valid
                        System.err.println("Data tidak valid: " + item.getKey());
                    }
                }

                // Debug log untuk memeriksa apakah list sudah terisi
                System.out.println("User List Size: " + userListAdmin.size());

                // Inisialisasi adapter dan set ke RecyclerView
                adapter = new UserAdapter(getContext(), userListAdmin);
                rvUser.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error jika ada masalah dengan database
                System.err.println("Database Error: " + error.getMessage());
            }
        });
    }
}
