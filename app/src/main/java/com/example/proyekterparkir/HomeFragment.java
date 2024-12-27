package com.example.proyekterparkir;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Referensi ke ImageView barcode
        View barcode = view.findViewById(R.id.barcode);

        // Listener untuk klik pada barcode
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke BarcodeSide
                Intent intent = new Intent(getActivity(), BarcodeSide.class);
                startActivity(intent);
            }
        });

        // Mengatur Spinner (dropdown) dengan placeholder
        Spinner spinner = view.findViewById(R.id.spinner);
        String[] opsi = {"Mau Kemana?", "XXI", "Matahari", "A&W"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, opsi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Variabel global untuk menyimpan pilihan Spinner
        final String[] selectedOption = {null};

        // Listener untuk item yang dipilih pada Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedOption[0] = null; // Jika "Mau Kemana?" dipilih, kosongkan pilihan
                    return;
                }
                selectedOption[0] = opsi[position]; // Simpan pilihan yang dipilih
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOption[0] = null; // Kosongkan pilihan jika tidak ada yang dipilih
            }
        });

        // Listener untuk tombol "Cari Parkiran"
        Button buttonSearch = view.findViewById(R.id.button_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOption[0] == null) {
                    Toast.makeText(getContext(), "Silakan pilih tujuan terlebih dahulu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Intent ke PetaParkir1 dengan data navigasi
                Intent intent = new Intent(getActivity(), PetaParkir1.class);

                // Tentukan fragment tujuan berdasarkan opsi yang dipilih
                switch (selectedOption[0]) {
                    case "XXI":
                        intent.putExtra("navigateTo", "PetaParkiran1Fragment");
                        break;
                    case "Matahari":
                        intent.putExtra("navigateTo", "PetaParkiran2Fragment");
                        break;
                    case "A&W":
                        intent.putExtra("navigateTo", "PetaParkiran3Fragment");
                        break;
                }

                startActivity(intent);
            }
        });

        return view;
    }
}
