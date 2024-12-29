package com.example.proyekterparkir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminEditDataUser extends AppCompatActivity {

    private EditText editNama, editEmail, editPass, editNoHp;
    private Button btnSave;
    private String emailKey;  // Menyimpan email untuk referensi data yang akan diedit
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_data_user);

        // Inisialisasi EditText dan Button
        editNama = findViewById(R.id.editNama);
        editEmail = findViewById(R.id.editEmail);
        editPass = findViewById(R.id.editPassword);
        editNoHp = findViewById(R.id.editNoHp);
        btnSave = findViewById(R.id.btnSave);

        // Ambil data email yang diteruskan melalui intent
        emailKey = getIntent().getStringExtra("email");

        // Normalisasi email untuk kompatibilitas dengan Firebase
        String normalizedEmail = emailKey.replace(".", ",");

        // Referensi ke Firebase
        database = FirebaseDatabase.getInstance().getReference("users").child(normalizedEmail);

        // Ambil data pengguna dari Firebase
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Ambil data dari Firebase
                    String nama = snapshot.child("nama").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String password = snapshot.child("pass").getValue(String.class);
                    String phone = snapshot.child("no_hp").getValue(String.class);

                    // Set data ke EditText
                    editNama.setText(nama);
                    editEmail.setText(email);
                    editPass.setText(password);
                    editNoHp.setText(phone);

                } else {
                    Toast.makeText(AdminEditDataUser.this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminEditDataUser.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ImageView arrowBack = findViewById(R.id.arrow_backAdmin);
        arrowBack.setOnClickListener(view -> {
            Intent intent = new Intent(AdminEditDataUser.this, FragmentUserListAdmin.class);
            startActivity(intent);
        });


        // Simpan perubahan data pengguna
        btnSave.setOnClickListener(v -> {
            String updatedNama = editNama.getText().toString().trim();
            String updatedEmail = editEmail.getText().toString().trim();
            String updatedPass = editPass.getText().toString().trim();
            String updatedNoHp = editNoHp.getText().toString().trim();


            // Validasi input
            if (TextUtils.isEmpty(updatedNama) || TextUtils.isEmpty(updatedEmail) ||
                    TextUtils.isEmpty(updatedPass) || TextUtils.isEmpty(updatedNoHp)) {
                Toast.makeText(AdminEditDataUser.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update data di Firebase
            // Asumsi konf_pass tidak diubah dan memberikan nilai kosong
            HelperClass updatedUser = new HelperClass(updatedNama, updatedEmail, updatedPass, "", updatedNoHp);
            database.setValue(updatedUser);

            // Menyimpan perubahan email di SharedPreferences (jika diperlukan)
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email_user", updatedEmail);
            editor.apply();

            // Memberi notifikasi bahwa data berhasil diperbarui
            Toast.makeText(AdminEditDataUser.this, "Data berhasil diperbarui!", Toast.LENGTH_SHORT).show();

            // Kembali ke aktivitas sebelumnya
            finish();
        });

    }
}
