package com.example.proyekterparkir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    EditText namaEdit, emailEdit, passEdit, phoneEdit;
    Button btnSave;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Inisialisasi EditText dan Button
        namaEdit = findViewById(R.id.nama_edit);
        emailEdit = findViewById(R.id.email_edit);
        passEdit = findViewById(R.id.pass_edit);
        phoneEdit = findViewById(R.id.phone_edit);
        btnSave = findViewById(R.id.btn_save);

        // Ambil email pengguna yang login dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String emailUser = sharedPreferences.getString("email_user", "");

        if (TextUtils.isEmpty(emailUser)) {
            Toast.makeText(this, "Tidak ada pengguna yang login", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ganti "." di email untuk kompatibilitas dengan Firebase
        String normalizedEmail = emailUser.replace(".", ",");

        // Referensi ke database Firebase
        reference = FirebaseDatabase.getInstance().getReference("users").child(normalizedEmail);

        // Ambil data pengguna dari Firebase
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nama = snapshot.child("nama").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("pass").getValue(String.class);
                    String phone = snapshot.child("no_hp").getValue(String.class);

                    // Set data ke EditText
                    namaEdit.setText(nama);
                    emailEdit.setText(email);
                    passEdit.setText(password);
                    phoneEdit.setText(phone);
                } else {
                    Toast.makeText(EditProfile.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Simpan perubahan
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNama = namaEdit.getText().toString();
                String updatedEmail = emailEdit.getText().toString();
                String updatedPass = passEdit.getText().toString();
                String updatedPhone = phoneEdit.getText().toString();

                if (TextUtils.isEmpty(updatedNama) || TextUtils.isEmpty(updatedEmail) ||
                        TextUtils.isEmpty(updatedPass) || TextUtils.isEmpty(updatedPhone)) {
                    Toast.makeText(EditProfile.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perbarui data di Firebase
                reference.child("nama").setValue(updatedNama);
                reference.child("email").setValue(updatedEmail);
                reference.child("pass").setValue(updatedPass);
                reference.child("no_hp").setValue(updatedPhone);

                // Perbarui email di SharedPreferences jika email berubah
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email_user", updatedEmail);
                editor.apply();

                Toast.makeText(EditProfile.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView arrowBack = findViewById(R.id.arrow_backProfile);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, SettingsFragment.class);
                startActivity(intent);
            }
        });

    }
}
