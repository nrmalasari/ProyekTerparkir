package com.example.proyekterparkir;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<HelperClass> userList;
    private Context context;
    private DatabaseReference database;

    public UserAdapter(Context context, ArrayList<HelperClass> userList) {
        this.context = context;
        this.userList = userList;
        this.database = FirebaseDatabase.getInstance().getReference("users");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout untuk setiap item list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_admin, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Ambil data user berdasarkan posisi
        HelperClass user = userList.get(position);

        // Set data ke dalam TextViews
        holder.tvNama.setText("Nama: " + user.getNama());
        holder.tvEmail.setText("Email: " + user.getEmail());
        holder.tvNoHp.setText("No HP: " + user.getNo_hp());
        holder.tvPass.setText("Password: " + user.getPass());

        // Button Hapus: Menghapus data berdasarkan email yang disanitasi
        holder.btnHapus.setOnClickListener(view -> {
            String sanitizedKey = user.getEmail().replace(".", ","); // Mengganti titik dengan koma untuk key Firebase
            database.child(sanitizedKey).removeValue()
                    .addOnSuccessListener(unused -> {
                        // Hapus data dari ArrayList lokal dan beri tahu adapter
                        userList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, userList.size());
                    })
                    .addOnFailureListener(e -> {
                        // Log error jika penghapusan gagal
                        System.err.println("Gagal menghapus data: " + e.getMessage());
                    });
        });

        // Button Edit: Kirim data user ke Activity Edit
        holder.btnEdit.setOnClickListener(view -> {
            Intent edit = new Intent(context, AdminEditDataUser.class);
            edit.putExtra("email", user.getEmail());
            edit.putExtra("nama", user.getNama());
            edit.putExtra("no_hp", user.getNo_hp());
            edit.putExtra("password", user.getPass());
            context.startActivity(edit);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size(); // Mengembalikan jumlah data
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvEmail, tvNoHp, tvPass;
        Button btnHapus, btnEdit;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inisialisasi komponen UI
            tvNama = itemView.findViewById(R.id.tvNama);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvNoHp = itemView.findViewById(R.id.tvNoHp);
            tvPass = itemView.findViewById(R.id.tvPassword);
            btnHapus = itemView.findViewById(R.id.btnHapus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
