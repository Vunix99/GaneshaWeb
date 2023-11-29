package com.ganesha.diur;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class kalku_kalori extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalku_kalori);
        getSupportActionBar().hide();

        // ButtonChangeKalku
        ImageButton buttonChangeKalku = findViewById(R.id.buttonChangeKalku);
        buttonChangeKalku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(kalku_kalori.this, kalku_bb.class);
                startActivity(intent);
                finish();
            }
        });

        // ButtonBackToHome
        ImageButton buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(kalku_kalori.this, home.class);
                startActivity(intent);
                finish();
            }
        });

        // Spinner Jenis Kelamin
        Spinner spinnerKelamin = findViewById(R.id.spinner_kelamin);
        String[] jenisKelaminOptions = {"Pilih Jenis Kelamin", "Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapterKelamin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenisKelaminOptions);
        adapterKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKelamin.setAdapter(adapterKelamin);

        // Set default selection for Jenis Kelamin
        spinnerKelamin.setSelection(0); // Pilih "Pilih Jenis Kelamin"

        // Spinner Level Aktivitas
        Spinner spinnerAktivitas = findViewById(R.id.spinner_aktivitas);
        String[] levelAktivitasOptions = {"Pilih Level Aktivitas", "Jarang", "Ringan", "Sedang", "Aktif", "Sangat Aktif"};
        ArrayAdapter<String> adapterAktivitas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levelAktivitasOptions);
        adapterAktivitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAktivitas.setAdapter(adapterAktivitas);

        // Set default selection for Level Aktivitas
        spinnerAktivitas.setSelection(0); // Pilih "Pilih Level Aktivitas"

        // Function untuk menghitung
        Button buttonHitung = findViewById(R.id.buttonHitung);
        buttonHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ambil nilai dari EditText atau Spinner sesuai kebutuhan
                int umur = Integer.parseInt(((EditText) findViewById(R.id.et_umur_register)).getText().toString());
                int beratBadan = Integer.parseInt(((EditText) findViewById(R.id.et_beratbadan_register)).getText().toString());
                int tinggiBadan = Integer.parseInt(((EditText) findViewById(R.id.et_tinggibadan_register)).getText().toString());
                String jenisKelamin = spinnerKelamin.getSelectedItem().toString();
                String levelAktivitas = spinnerAktivitas.getSelectedItem().toString();

                // Panggil fungsi hitung kalori ideal
                hitungKaloriIdeal(umur, beratBadan, tinggiBadan, jenisKelamin, levelAktivitas);
            }
        });
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(kalku_kalori.this, home.class);
        startActivity(i);
        finish();
    }
    // Function untuk menghitung kalori ideal
    private void hitungKaloriIdeal(int umur, int beratBadan, int tinggiBadan, String jenisKelamin, String levelAktivitas) {
        // Hitung BMR berdasarkan rumus yang diberikan
        double bmr = calculateBMR(umur, beratBadan, tinggiBadan, jenisKelamin);

        // Hitung kalori ideal berdasarkan rumus yang diberikan
        double calorieIdeal = calculateCalorieIdeal(bmr, levelAktivitas);
        long calorieIdealFix = Math.round(calorieIdeal);
        // Tampilkan hasil perhitungan kalori ideal di TextView
        TextView tvKaloriIdeal = findViewById(R.id.tv_kalori_ideal);
        tvKaloriIdeal.setText("Kalori Ideal: " + calorieIdealFix + " kcal");

        Toast.makeText(this, "Berhasil Menghitung!", Toast.LENGTH_SHORT).show();
    }

    // Fungsi untuk menghitung BMR
    private double calculateBMR(int age, int weight, int height, String gender) {
        double bmr;
        if (gender.equals("Laki-laki")) {
            bmr = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
        } else {
            bmr = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        }
        return bmr;
    }

    // Fungsi untuk menghitung kalori ideal
    private double calculateCalorieIdeal(double bmr, String activityLevel) {
        double calorieIdeal;
        switch (activityLevel) {
            case "Jarang":
                calorieIdeal = bmr * 1.2;
                break;
            case "Ringan":
                calorieIdeal = bmr * 1.375;
                break;
            case "Sedang":
                calorieIdeal = bmr * 1.55;
                break;
            case "Aktif":
                calorieIdeal = bmr * 1.725;
                break;
            case "Sangat Aktif":
                calorieIdeal = bmr * 1.9;
                break;
            default:
                calorieIdeal = bmr;
                break;
        }
        return calorieIdeal;
    }
}
