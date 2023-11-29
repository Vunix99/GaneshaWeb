package com.ganesha.diur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class kalku_bb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalku_bb);
        getSupportActionBar().hide();

        // Ambil referensi ke elemen UI
        EditText etTinggiBadan = findViewById(R.id.et_tinggibadan_register);
        Button btnHitung = findViewById(R.id.buttonHitung);
        TextView tvHasilBB = findViewById(R.id.tv_beratbadan_ideal);
        ImageButton btnBack = findViewById(R.id.buttonBackToHome);
        ImageButton btnChangeKalku = findViewById(R.id.buttonChangeKalku);

        // Spinner Jenis Kelamin
        Spinner spinnerKelamin = findViewById(R.id.spinner_kelamin);
        String[] jenisKelaminOptions = {"Pilih Jenis Kelamin", "Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapterKelamin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisKelaminOptions);
        adapterKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelamin.setAdapter(adapterKelamin);

        // Set default selection for Jenis Kelamin
        spinnerKelamin.setSelection(0); // Pilih "Pilih Jenis Kelamin"

        // Set listener untuk Spinner Jenis Kelamin
        spinnerKelamin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Ketika jenis kelamin dipilih, lakukan apa yang diperlukan
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Kosongkan karena tidak ada yang dilakukan ketika tidak ada item yang dipilih
            }
        });

        //Back to Home
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(kalku_bb.this, home.class);
                // Tambahkan flag untuk membersihkan tumpukan aktivitas
                startActivity(i);
                finish();
            }
        });

        //Change kalku
        btnChangeKalku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(kalku_bb.this, kalku_kalori.class);
                startActivity(i);
                finish();
            }
        });

        // Set listener untuk tombol hitung
        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil nilai tinggi badan dari EditText
                String tinggiBadanStr = etTinggiBadan.getText().toString();
                if (tinggiBadanStr.isEmpty()) {
                    etTinggiBadan.setError("Masukkan tinggi badan");
                    return;
                }

                int tinggiBadan = Integer.parseInt(tinggiBadanStr);

                // Ambil jenis kelamin yang dipilih dari Spinner
                String jenisKelamin = spinnerKelamin.getSelectedItem().toString();

                // Validasi jenis kelamin
                if (jenisKelamin.equals("Pilih Jenis Kelamin")) {
                    ((TextView)spinnerKelamin.getSelectedView()).setError("Pilih jenis kelamin");
                    return;
                }

                // Hitung berat badan ideal berdasarkan rumus
                double beratBadanIdeal = calculateBeratBadanIdeal(tinggiBadan, jenisKelamin.equals("Laki-laki"));

                // Tampilkan hasil di TextView
                tvHasilBB.setText("Berat Badan Ideal: " + beratBadanIdeal + " kg");
            }
        });
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(kalku_bb.this, home.class);
        startActivity(i);
        finish();
    }

    private double calculateBeratBadanIdeal(int tinggiBadan, boolean isPria) {
        // Asumsikan rumus yang diberikan adalah rumus yang diinginkan
        double persenPenurunan = isPria ? 0.10 : 0.15;

        // Hitung berat badan ideal berdasarkan rumus
        return (tinggiBadan - 100) - ((tinggiBadan - 100) * persenPenurunan);
    }
}
