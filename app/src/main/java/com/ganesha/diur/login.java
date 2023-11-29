package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class login extends AppCompatActivity {
    EditText etUsernameLogin, etPasswordLogin;
    Button loginButton;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        // Cek apakah pengguna sudah login
        if (sessionManager.isLoggedIn()) {
            // Jika sudah login, pindah ke activity home
            Intent intent = new Intent(login.this, home.class);
            startActivity(intent);
            finish(); // Tutup activity login agar tidak dapat dikembali
        }

        etUsernameLogin = findViewById(R.id.et_username_login);
        etPasswordLogin = findViewById(R.id.et_password_login);
        loginButton = findViewById(R.id.button_login);

        etPasswordLogin.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Lakukan tindakan login di sini
                String username = etUsernameLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                new AuthenticateUserTask().execute(username, password);
                return true;
            }
            return false;
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsernameLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Memulai AsyncTask untuk melakukan autentikasi melalui API
                new AuthenticateUserTask().execute(username, password);
            }
        });

        //btn lupa password
        Button btnLupa = findViewById(R.id.button_lupa);
        btnLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, LupaPassword.class);
                startActivity(i);
                finish();
            }
        });

        Button buttonToRegister = findViewById(R.id.buttonToRegister);
        buttonToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, register.class);
                startActivity(intent);
            }
        });
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(login.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private class AuthenticateUserTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... credentials) {
            if (credentials.length == 2) {
                try {
                    String username = credentials[0];
                    String password = credentials[1];
                    URL url = new URL("https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/loginUser?username=" + username + "&password=" + password);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");

                    // Membaca respons dari API
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }

                        // Parse respons JSON
                        return new JSONObject(response.toString());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
                try {
                    boolean success = result.getBoolean("success");

                    if (success) {
                        // Login berhasil
                        Toast.makeText(login.this, "Login berhasil", Toast.LENGTH_SHORT).show();

                        // Ambil ID pengguna dari respons
                        String userId = result.getString("id");

                        // Simpan userId ke SessionManager
                        sessionManager.setUserId(userId);

                        // Set isLogged ke true
                        sessionManager.setIsLogged(true);

                        // Lakukan tindakan jika login berhasil
                        // Misalnya, pindah ke activity home
                        Intent intent = new Intent(login.this, home.class);
                        startActivity(intent);
                        finish(); // Tutup activity login agar tidak dapat dikembali
                    } else {
                        // Login gagal, tampilkan pesan kesalahan
                        String message = result.getString("message");
                        Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // Tangani kesalahan parsing JSON
                    e.printStackTrace();
                }
            } else {
                // Respons null, tampilkan pesan kesalahan umum
                Toast.makeText(login.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
