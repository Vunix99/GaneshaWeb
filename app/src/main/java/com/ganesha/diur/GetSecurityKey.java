package com.ganesha.diur;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetSecurityKey extends AppCompatActivity {

    private TextView profileUsername;
    private TextView userSecurityKey;
    private EditText etPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_security_key);
        getSupportActionBar().hide();

        // Inisialisasi TextView
        profileUsername = findViewById(R.id.profile_username);
        userSecurityKey = findViewById(R.id.userSecurityKey);
        etPassword = findViewById(R.id.et_password_getsecurity);
        ImageButton copyToClipboardButton = findViewById(R.id.copyToClipboard);
        ImageButton btnBackToProfile = findViewById(R.id.buttonBackToProfile);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(this);

        // Mendapatkan userId dari SessionManager
        String userId = sessionManager.getUserId();

        // Membuat dan menjalankan AsyncTask untuk mendapatkan username
        new FetchDataAsyncTask().execute(userId);


        //back to profile
        btnBackToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GetSecurityKey.this, user_profile.class);
                startActivity(i);
                finish();
            }
        });

        //Clipboard
        copyToClipboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan teks dari TextView userSecurityKey
                String securityKey = userSecurityKey.getText().toString();

                // Menyalin teks ke Clipboard
                copyToClipboard(securityKey);

                // Menampilkan Toast bahwa teks telah disalin
                showToast("Security key telah disalin ke Clipboard");
            }
        });

        // Menambahkan onClickListener untuk tombol
        Button btnGetSecurityKey = findViewById(R.id.ButtonGetSecurityKey);
        btnGetSecurityKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mendapatkan userId dari SessionManager
                String userId = sessionManager.getUserId();

                // Mendapatkan password dari EditText
                String password = etPassword.getText().toString();

                // Memastikan userId dan password tidak kosong
                if (!userId.isEmpty() && !password.isEmpty()) {
                    // Membuat dan menjalankan AsyncTask untuk mendapatkan security_key
                    new GetSecurityKeyAsyncTask().execute(userId, password);
                } else {
                    // Menangani situasi jika userId atau password kosong
                    // ... (Tambahkan logika penanganan sesuai kebutuhan)
                }
            }
        });
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(GetSecurityKey.this, user_profile.class);
        startActivity(i);
        finish();
    }


    private class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];

            try {
                // Membuat URL endpoint
                URL url = new URL("https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=" + userId);

                // Membuat koneksi HTTP
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    // Membaca data dari input stream
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } finally {
                    // Menutup koneksi setelah selesai
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Mengambil nilai username dari respons JSON
                    JSONObject jsonObject = new JSONObject(result);
                    String username = jsonObject.getString("username");

                    // Menetapkan nilai username ke TextView
                    profileUsername.setText(username);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Menangani kesalahan jika terjadi kesalahan pembacaan JSON
                }
            }
        }
    }

    private class GetSecurityKeyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String password = params[1];

            try {
                // Membuat URL endpoint dengan menyertakan userId dan password sebagai query parameters
                String endpoint = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getSecurityKey"
                        + "?id=" + userId
                        + "&password=" + password;

                // Membuat koneksi HTTP
                URL url = new URL(endpoint);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    // Membaca data dari input stream
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } finally {
                    // Menutup koneksi setelah selesai
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Mengambil nilai security_key dari respons JSON
                    JSONObject jsonObject = new JSONObject(result);
                    String securityKey = jsonObject.getString("security_key");

                    // Menetapkan nilai security_key ke TextView
                    userSecurityKey.setText(securityKey);

                    // Menampilkan Toast ketika security_key berhasil diubah
                    showToast("Berhasil membuka, \nsilahkan salin key anda");

                    // Menutup keyboard
                    closeKeyboard();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GetSecurityKey.this, "Password salah", Toast.LENGTH_SHORT).show();
                    // Menangani kesalahan jika terjadi kesalahan pembacaan JSON
                }
            }
        }
    }

    // Metode untuk menampilkan Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Metode untuk menutup keyboard
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("SecurityKey", text);
        clipboard.setPrimaryClip(clip);
    }
}
