package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ganesha.diur.register_finishing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class register extends AppCompatActivity {

    EditText etUsernameRegister, etPasswordRegister, etEmailRegister;
    Button registerButton;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);

        etUsernameRegister = findViewById(R.id.et_username_register);
        etPasswordRegister = findViewById(R.id.et_password_register);
        etEmailRegister = findViewById(R.id.et_email_register);
        registerButton = findViewById(R.id.register_button);

        etPasswordRegister.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Lakukan tindakan login di sini
                String username = etUsernameRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();
                String email = etEmailRegister.getText().toString();

                // Membuat objek JSON
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);
                    jsonBody.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Mengirim permintaan HTTP secara asynchronous
                new RegisterTask().execute(jsonBody.toString());
                return true;
            }
            return false;
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsernameRegister.getText().toString();
                String password = etPasswordRegister.getText().toString();
                String email = etEmailRegister.getText().toString();

                // Membuat objek JSON
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", username);
                    jsonBody.put("password", password);
                    jsonBody.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Mengirim permintaan HTTP secara asynchronous
                new RegisterTask().execute(jsonBody.toString());
            }
        });

        Button buttonToLoginPage = findViewById(R.id.buttonToLoginPage);
        buttonToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, login.class);
                startActivity(i);
                finish();
            }
        });
    }

    // Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(register.this, login.class);
        startActivity(i);
        finish();
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/registUser";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Mengirim data JSON
                OutputStream os = connection.getOutputStream();
                os.write(params[0].getBytes());
                os.flush();
                os.close();

                // Membaca respons dari server
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    in.close();
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                // Periksa apakah respons adalah string JSON yang valid
                if (isJSONValid(result)) {
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.has("status")) {
                        if (jsonResponse.getString("status").equals("success")) {
                            // Pendaftaran berhasil, simpan ID ke SharedPreferences
                            sessionManager.setUserId(jsonResponse.getString("_id")); //simpan _id ke session

                            // Pindah ke activity selanjutnya
                            Intent intent = new Intent(register.this, register_finishing.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(register.this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                        } else if (jsonResponse.getString("status").equals("failed") && jsonResponse.getString("message").equals("Pendaftaran gagal, username telah ada") && jsonResponse.getString("reason").equals("UsernameAlreadyExists")) {
                            // Pendaftaran gagal karena username sudah terdaftar
                            Toast.makeText(register.this, "Username telah terdaftar! Pilih username lain.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Pendaftaran gagal, tampilkan pesan kesalahan
                            String errorMessage = jsonResponse.optString("message", "Unknown error");
                            Log.e("RegisterTask", "Registration failed: " + errorMessage);
                            Toast.makeText(register.this, "Pendaftaran gagal: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Respon tidak sesuai format yang diharapkan
                        Log.e("RegisterTask", "Invalid response format");
                        Toast.makeText(register.this, "Respon tidak valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Respons tidak valid, tampilkan pesan kesalahan
                    Log.e("RegisterTask", "Invalid JSON response: " + result);

                    // Tampilkan pesan kesalahan sesuai dengan status code 400
                    if (result != null && result.equals("Error: 400")) {
                        Toast.makeText(register.this, "Username telah terdaftar! \nPilih username lain.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(register.this, "Respon JSON tidak valid", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Penanganan kesalahan umum
                Log.e("RegisterTask", "Error: " + e.getMessage());
                Toast.makeText(register.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }


        // Metode untuk memeriksa apakah string adalah JSON yang valid
        private boolean isJSONValid(String test) {
            try {
                new JSONObject(test);
            } catch (JSONException ex) {
                try {
                    new JSONArray(test);
                } catch (JSONException ex1) {
                    return false;
                }
            }
            return true;
        }
    }
}
