package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GantiPassword extends AppCompatActivity {

    private EditText etPasswordLama, etPasswordBaru;
    private Button btnUbahPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        getSupportActionBar().hide();

        sessionManager = new SessionManager(getApplicationContext());

        etPasswordLama = findViewById(R.id.password_lama_gantipassword);
        etPasswordBaru = findViewById(R.id.et_newpassword_ganti);
        btnUbahPassword = findViewById(R.id.ButtonUbahPassword);
        ImageButton btnBackProfile = findViewById(R.id.buttonBackToProfile);

        // back to profile
        btnBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GantiPassword.this, user_profile.class);
                startActivity(i);
            }
        });

        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = sessionManager.getUserId();
                String passwordLama = etPasswordLama.getText().toString().trim();
                String passwordBaru = etPasswordBaru.getText().toString().trim();

                new UbahPasswordTask(passwordLama, passwordBaru).execute();

            }
        });

        // Ambil username sebelumnya (sama seperti pada register_finishing)
        new GetUsernameTask().execute(sessionManager.getUserId());
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(GantiPassword.this, home.class);
        startActivity(i);
        finish();
    }

    private class GetUsernameTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params.length > 0) {
                String userId = params[0];
                String endpointUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile" +
                        "?id=" + userId;

                try {
                    URL url = new URL(endpointUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    // Dapatkan respons dari server
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();

                    return response.toString();

                } catch (Exception e) {
                    Log.e("GetUsernameTask", "Error during HTTP request", e);
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String username = jsonObject.optString("username", "");

                    // Update TextView di thread utama
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView usernameTextView = findViewById(R.id.username_gantipassword);
                            usernameTextView.setText(username);

                            // Gunakan data lain sesuai kebutuhan Anda
                        }
                    });

                } catch (JSONException e) {
                    Log.e("GetUsernameTask", "Error parsing JSON response", e);
                }
            } else {
                // Kesalahan saat mengambil data dari API
                // Tambahkan tindakan atau notifikasi yang sesuai
            }
        }
    }

    private class UbahPasswordTask extends AsyncTask<Void, Void, String> {

        private String passwordLama;
        private String passwordBaru;

        public UbahPasswordTask(String passwordLama, String passwordBaru) {
            this.passwordLama = passwordLama;
            this.passwordBaru = passwordBaru;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String userId = sessionManager.getUserId();

            String endpointUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/changePassword" +
                    "?id=" + userId +
                    "&password_lama=" + passwordLama +
                    "&password_baru=" + passwordBaru;

            try {
                URL url = new URL(endpointUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                // Mendapatkan respons dari server
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                Log.d("UbahPasswordTask", "Server Response: " + response.toString()); // Tambahkan baris ini

                return response.toString();

            } catch (Exception e) {
                Log.e("UbahPasswordTask", "Error during HTTP request", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                Log.d("UbahPasswordTask", "Realm Function Response: " + result);

                try {
                    JSONObject responseJson = new JSONObject(result);

                    if (responseJson.has("success")) {
                        String success = responseJson.optString("success", "failed");

                        if (success.trim().equals("success")) {
                            // Password berhasil diubah
                            Toast.makeText(GantiPassword.this, "Ganti Password berhasil!", Toast.LENGTH_SHORT).show();
                            // Pindahkan pengguna ke user_profile
                            Intent i = new Intent(GantiPassword.this, user_profile.class);
                            startActivity(i);
                            finish();
                        } else {
                            // Gagal mengubah password
                            Toast.makeText(GantiPassword.this, "Ganti Password gagal: " + responseJson.optString("error", ""), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Format respons tidak sesuai
                        Log.e("UbahPasswordTask", "Response does not contain 'success' key");
                        Toast.makeText(GantiPassword.this, "Format respons tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e("UbahPasswordTask", "Error parsing JSON response", e);
                    Toast.makeText(GantiPassword.this, "Terjadi kesalahan saat memproses respons", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Respons kosong atau null
                // Tambahkan tindakan atau notifikasi yang sesuai
                Log.e("UbahPasswordTask", "Empty or null response");
            }
        }

    }


}
