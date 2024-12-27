package com.example.proyekterparkir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    EditText editTextNama, editTextEmail, editTextPass, editTextKonfPass, editTextNomorHp;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        // Inisialisasi EditText
        editTextNama = findViewById(R.id.nama);
        editTextEmail = findViewById(R.id.emailReg);
        editTextPass = findViewById(R.id.passReg);
        editTextKonfPass = findViewById(R.id.confirm_pass);
        editTextNomorHp = findViewById(R.id.phoneReg);

        // Tombol Daftar
        Button buttonLogin = findViewById(R.id.btn_daftar_regis);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editTextNama.getText().toString();
                String email = editTextEmail.getText().toString();
                String pass = editTextPass.getText().toString();
                String konf_pass = editTextKonfPass.getText().toString();
                String no_hp = editTextNomorHp.getText().toString();

                if (TextUtils.isEmpty(nama)) {
                    editTextNama.setError("Masukkan nama");
                    editTextNama.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Masukkan email");
                    editTextEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    editTextPass.setError("Masukkan password");
                    editTextPass.requestFocus();
                    return;
                }

                if (!pass.equals(konf_pass)) {
                    editTextKonfPass.setError("Password tidak sama");
                    editTextKonfPass.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(no_hp)) {
                    editTextNomorHp.setError("Masukkan Nomor Hp");
                    editTextNomorHp.requestFocus();
                    return;
                }

                // Inisialisasi Firebase
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Simpan data ke Firebase
                String normalizedEmail = email.replace(".", ",");
                HelperClass helperClass = new HelperClass(nama, email, pass,"", no_hp);
                reference.child(normalizedEmail).setValue(helperClass);

                // Simpan email di SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email_user", email);
                editor.apply();

                // Tampilkan pesan bahwa registrasi berhasil
                Toast.makeText(Registration.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                // Berpindah ke LoginActivity
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        TextView linkMasuk = findViewById(R.id.link_login);
        linkMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });

        ImageView arrowBack = findViewById(R.id.arrowBackLogin);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
