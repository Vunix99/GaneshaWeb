package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class register_finishing extends AppCompatActivity {
    EditText etUmur, etBeratBadan, etTinggiBadan;
    Spinner spinnerKelamin, spinnerAktivitas;
    Button finishingButton;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finishing);
        sessionManager = new SessionManager(this);

        // Inisialisasi elemen UI
        etUmur = findViewById(R.id.et_umur_register);
        etBeratBadan = findViewById(R.id.et_beratbadan_register);
        etTinggiBadan = findViewById(R.id.et_tinggibadan_register);
        spinnerKelamin = findViewById(R.id.spinner_kelamin);
        spinnerAktivitas = findViewById(R.id.spinner_aktivitas);
        finishingButton = findViewById(R.id.ButtonfinishingRegister);

        // Array string untuk item spinner
        String[] kelamin = new String[]{"Pilih jenis kelamin", "Laki-laki", "Perempuan"};
        String[] aktivitas = new String[]{"Pilih level aktivitas", "Jarang", "Ringan", "Sedang", "Aktif", "Sangat Aktif"};

        // Adapter untuk spinner
        ArrayAdapter<String> adapterKelamin = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kelamin);
        adapterKelamin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapterAktivitas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aktivitas);
        adapterAktivitas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Tetapkan adapter ke spinner
        spinnerKelamin.setAdapter(adapterKelamin);
        spinnerAktivitas.setAdapter(adapterAktivitas);

        // Menerima ID dari SharedPreferences
        String userId = sessionManager.getUserId();

        finishingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input sebelum menyimpan data
                if (isAllInputFilled()) {
                    // Konversi nilai EditText menjadi integer
                    int umur = Integer.parseInt(etUmur.getText().toString());
                    int beratBadan = Integer.parseInt(etBeratBadan.getText().toString());
                    int tinggiBadan = Integer.parseInt(etTinggiBadan.getText().toString());
                    String jenisKelamin = spinnerKelamin.getSelectedItem().toString();
                    String levelAktivitas = spinnerAktivitas.getSelectedItem().toString();

                    // Bangun URL dengan parameter
                    String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateProfile";
                    apiUrl += "?id=" + URLEncoder.encode(userId) +
                            "&age=" + URLEncoder.encode(String.valueOf(umur)) +
                            "&weight=" + URLEncoder.encode(String.valueOf(beratBadan)) +
                            "&height=" + URLEncoder.encode(String.valueOf(tinggiBadan)) +
                            "&activity=" + URLEncoder.encode(levelAktivitas) +
                            "&gender=" + URLEncoder.encode(jenisKelamin);

                    // Save user ID to SharedPreferences
                    sessionManager.setUserId(userId);

                    // Kirim permintaan PUT
                    new UpdateProfileTask().execute(apiUrl);
                } else {
                    Toast.makeText(register_finishing.this, "Harap lengkapi semua input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validasi apakah semua input telah diisi
    private boolean isAllInputFilled() {
        return !etUmur.getText().toString().isEmpty() &&
                !etBeratBadan.getText().toString().isEmpty() &&
                !etTinggiBadan.getText().toString().isEmpty() &&
                spinnerKelamin.getSelectedItemPosition() != 0 &&
                spinnerAktivitas.getSelectedItemPosition() != 0;
    }

    private class UpdateProfileTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                // Membuat URL
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");

                // Mengecek respons dari server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    // Membaca respons dari server
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    in.close();
                    return response.toString();
                } else {
                    return "Update gagal. Response Code: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("Update", "Response from server: " + result);

            // Pemrosesan respons
            try {
                // Memeriksa apakah respons kosong
                if (result.trim().isEmpty()) {
                    // Respons kosong, menganggap bahwa respons berhasil
                    Toast.makeText(register_finishing.this, "Register berhasil, Silahkan Login", Toast.LENGTH_LONG).show();

                    // Pindah ke activity selanjutnya (misalnya, home)
                    Intent nextIntent = new Intent(register_finishing.this, home.class);
                    startActivity(nextIntent);
                    finish(); // Tutup activity saat ini agar tidak bisa kembali dengan tombol back
                } else {
                    // Memeriksa apakah respons JSON valid
                    JSONObject responseJson = new JSONObject(result);

                    // Mengecek apakah respons berisi key 'success'
                    if (responseJson.has("success")) {
                        // Mendapatkan nilai string dari key 'success'
                        String successValue = responseJson.getString("success");

                        if (successValue.equals("success")) {
                            // Pembaruan berhasil tanpa ada pesan
                            Toast.makeText(register_finishing.this, "Register berhasil", Toast.LENGTH_LONG).show();

                            // Pindah ke activity selanjutnya (misalnya, home)
                            Intent nextIntent = new Intent(register_finishing.this, AlertAfterRegister.class);
                            // Ambil userId yang telah diperbarui dari response
                            String updatedUserId = responseJson.optString("updatedId", "");
                            // Atur userId baru ke SessionManager
                            sessionManager.setUserId(updatedUserId);
                            sessionManager.setIsLogged(true);
                            startActivity(nextIntent);
                            finish(); // Tutup activity saat ini agar tidak bisa kembali dengan tombol back
                        } else {
                            // Pembaruan gagal dengan pesan dari server
                            Toast.makeText(register_finishing.this, "Update gagal", Toast.LENGTH_SHORT).show();
                            Log.e("Update", "Update failed: " + successValue);
                        }
                    } else {
                        // Format respons tidak sesuai
                        Toast.makeText(register_finishing.this, "Format respons tidak sesuai", Toast.LENGTH_SHORT).show();
                        Log.e("Update", "Format respons tidak sesuai");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(register_finishing.this, "Terjadi kesalahan saat memproses respons", Toast.LENGTH_SHORT).show();
                Log.e("Update", "Error processing response: " + e.getMessage());
            }
        }
    }
}
