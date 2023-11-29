package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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

public class user_profile extends AppCompatActivity {
    private static final String[] ACTIVITY_LEVELS = {"Jarang", "Ringan", "Sedang", "Aktif", "Sangat Aktif"};

    String gender;
    EditText profileAge;
    EditText profileHeight;
    EditText profileWeight;
    Spinner profileActivityLevel;
    private SessionManager sessionManager;
    private String userId; // Variable untuk menyimpan userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(this);

        // Cek status login
        if (!sessionManager.isLoggedIn()) {
            // Jika pengguna belum login, arahkan ke halaman login
            Intent intent = new Intent(user_profile.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tutup activity ini agar tidak dapat kembali ke home tanpa login
        } else {
            // Pengguna sudah login
            setContentView(R.layout.activity_user_profile);
            userId = sessionManager.getUserId(); // Ambil userId sebagai string

            profileAge = findViewById(R.id.profile_usia);
            profileHeight = findViewById(R.id.profile_tinggibadan);
            profileWeight = findViewById(R.id.profile_beratbadan);
            profileActivityLevel = findViewById(R.id.profile_aktivitas);

            // Update kode pada onClickListener untuk tombol update
            Button updateButton = findViewById(R.id.update_profile_button);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Konversi nilai EditText menjadi integer
                    int newAge = Integer.parseInt(profileAge.getText().toString());
                    int newHeight = Integer.parseInt(profileHeight.getText().toString());
                    int newWeight = Integer.parseInt(profileWeight.getText().toString());
                    String newActivityLevel = profileActivityLevel.getSelectedItem().toString();

                    // Bangun URL dengan parameter
                    String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateProfile";
                    apiUrl += "?id=" + URLEncoder.encode(userId) +
                            "&age=" + URLEncoder.encode(String.valueOf(newAge)) +
                            "&weight=" + URLEncoder.encode(String.valueOf(newWeight)) +
                            "&height=" + URLEncoder.encode(String.valueOf(newHeight)) +
                            "&activity=" + URLEncoder.encode(newActivityLevel);

                    // Memulai AsyncTask untuk memperbarui profil
                    new UpdateUserProfileTask().execute(apiUrl);
                }
            });

            //back to home
            ImageButton backToHomeButton = findViewById(R.id.buttonBackToHome);
            backToHomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(user_profile.this, home.class);
                    startActivity(intent);
                    finish();
                }
            });

            //button ganti password
            Button buttonGantiPassword = findViewById(R.id.buttonToGantiPassword);
            buttonGantiPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(user_profile.this, GantiPassword.class);
                    startActivity(i);
                    finish();
                }
            });

            //button get security key
            Button buttonToGetSecurityKey = findViewById(R.id.buttonToGetSecurityKey);
            buttonToGetSecurityKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(user_profile.this, GetSecurityKey.class);
                    startActivity(i);
                    finish();
                }
            });

            //logout
            Button logoutButton = findViewById(R.id.logout_button);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear the user session
                    sessionManager.logoutUser();

                    // Create a new task stack with MainActivity and clear the current task stack
                    Intent intent = new Intent(user_profile.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    // Tampilkan pesan Toast bahwa proses logout berhasil
                    Toast.makeText(getApplicationContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
                }
            });

            // Memulai AsyncTask untuk mengambil profil pengguna
            new FetchUserProfileTask().execute();
        }
    }

    @Override
    public void onBackPressed() {
        // Check if the user is logged in
        if (!sessionManager.isLoggedIn()) {
            // If the user is not logged in, do not allow going back to the previous activity
            Intent intent = new Intent(user_profile.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close this activity
        } else {
            // If the user is logged in, proceed with the default back press behavior
            super.onBackPressed();
            Intent i = new Intent(user_profile.this, home.class);
            startActivity(i);
            finish();
        }
    }

    private class UpdateUserProfileTask extends AsyncTask<String, Void, String> {
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
                // Handle exceptions
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
                    Toast.makeText(user_profile.this, "Update berhasil", Toast.LENGTH_SHORT).show();

                    // Pindah ke activity selanjutnya (misalnya, home)
                    Intent nextIntent = new Intent(user_profile.this, home.class);
                    startActivity(nextIntent);
                    finish(); // Tutup activity saat ini agar tidak bisa kembali dengan tombol back
                    return; // Keluar dari metode karena tidak ada respons JSON untuk diproses
                }

                // Memeriksa apakah respons JSON valid
                JSONObject responseJson = new JSONObject(result);

                // Mengecek apakah respons berisi key 'success'
                if (responseJson.has("success")) {
                    // Mendapatkan nilai string dari key 'success'
                    String successValue = responseJson.getString("success");

                    if (successValue.equals("success")) {
                        // Pembaruan berhasil tanpa ada pesan
                        Toast.makeText(user_profile.this, "Update berhasil", Toast.LENGTH_SHORT).show();

                        // Pindah ke activity selanjutnya (misalnya, home)
                        Intent nextIntent = new Intent(user_profile.this, home.class);
                        startActivity(nextIntent);
                        finish(); // Tutup activity saat ini agar tidak bisa kembali dengan tombol back
                    } else {
                        // Pembaruan gagal dengan pesan dari server
                        Toast.makeText(user_profile.this, "Update gagal", Toast.LENGTH_SHORT).show();
                        Log.e("Update", "Update failed: " + successValue);
                    }
                } else {
                    // Format respons tidak sesuai
                    Toast.makeText(user_profile.this, "Format respons tidak sesuai", Toast.LENGTH_SHORT).show();
                    Log.e("Update", "Format respons tidak sesuai");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(user_profile.this, "Terjadi kesalahan saat memproses respons", Toast.LENGTH_SHORT).show();
                Log.e("Update", "Error processing response: " + e.getMessage());
            }
        }
    }

    private class FetchUserProfileTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            // URL endpoint API
            String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=" + userId;

            try {
                // Buat objek URL
                URL url = new URL(apiUrl);

                // Buka koneksi
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    // Dapatkan InputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // Baca respons
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    // Parse respons JSON
                    return new JSONObject(stringBuilder.toString());

                } catch (JSONException | IOException e) {
                    // Tambahkan log
                    Log.e("Error", "Error during JSON parsing or IO: " + e.getMessage());

                    // Tangani kesalahan parsing JSON atau IO
                    e.printStackTrace();
                    return null;
                } finally {
                    // Putuskan koneksi HttpURLConnection
                    urlConnection.disconnect();
                }

            } catch (IOException e) {
                // Tambahkan log
                Log.e("Error", "IO Error: " + e.getMessage());

                // Tangani kesalahan IO
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            // Setelah mendapatkan data profil pengguna, update UI
            updateUI(result);
        }
    }

    private void updateUI(JSONObject userProfile) {
        if (userProfile != null) {
            try {
                // Ambil data profil dari JSON
                String username = userProfile.getString("username");
                int age = userProfile.getInt("usia");
                String gender = userProfile.getString("jenis_kelamin");
                int height = userProfile.getInt("tinggi_badan");
                int weight = userProfile.getInt("berat_badan");
                String activityLevel = userProfile.getString("level_aktivitas");

                // Perbarui data profil pada UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(username, age, weight, height, activityLevel);
                    }
                });

            } catch (JSONException e) {
                // Tangani kesalahan parsing JSON
                e.printStackTrace();
            }
        } else {
            // Tangani respons null
            Toast.makeText(user_profile.this, "Gagal mengambil profil pengguna", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String username, int age, int weight, int height, String activityLevel) {
        // Perbarui data profil pada UI
        TextView profileUsername = findViewById(R.id.profile_username);
        profileUsername.setText(username + "\n(ID :"+userId+")");

        profileAge.setText(String.valueOf(age));
        profileHeight.setText(String.valueOf(height));
        profileWeight.setText(String.valueOf(weight));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ACTIVITY_LEVELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileActivityLevel.setAdapter(adapter);

        if (activityLevel != null) {
            int spinnerPosition = adapter.getPosition(activityLevel);
            profileActivityLevel.setSelection(spinnerPosition);
        }
    }
}
