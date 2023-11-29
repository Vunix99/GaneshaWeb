package com.ganesha.diur;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class AlertAfterRegister extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button finishingRegisterButton;
    private boolean isSecurityKeyCopied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_after_register);
        getSupportActionBar().hide();
        sessionManager = new SessionManager(this);

        // Pembaruan profil pengguna setelah berhasil update
        updateProfile();

        ImageButton copyToClipboardButton = findViewById(R.id.copyToClipboard);
        copyToClipboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logika untuk menyalin ke clipboard
                copyToClipboard();
                isSecurityKeyCopied = true;
                // Aktifkan tombol finishing register setelah tombol clipboard ditekan
                finishingRegisterButton.setEnabled(true);
            }
        });

        finishingRegisterButton = findViewById(R.id.ButtonfinishingRegister);
        finishingRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSecurityKeyCopied) {
                    // Cek apakah security key sudah disalin ke clipboard
                    TextView securityKeyTextView = findViewById(R.id.securityKey);
                    String securityKey = securityKeyTextView.getText().toString();

                    if (!securityKey.isEmpty()) {
                        // Security key sudah disalin, pindah ke home
                        Intent nextIntent = new Intent(AlertAfterRegister.this, home.class);
                        startActivity(nextIntent);
                        finish(); // Tutup activity saat ini agar tidak bisa kembali dengan tombol back
                    } else {
                        // Security key belum disalin, tampilkan pesan toast
                        Toast.makeText(AlertAfterRegister.this, "Harap salin security-key anda", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Jika tombol clipboard belum diklik, tampilkan pesan toast
                    Toast.makeText(AlertAfterRegister.this, "Harap klik clipboard terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class GetUserProfileTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error: " + e.getMessage();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);

                if (responseJson.optString("security_key").isEmpty()) {
                    Log.e("GetUserProfile", "Invalid JSON response");
                } else {
                    String securityKey = responseJson.getString("security_key");
                    TextView securityKeyTextView = findViewById(R.id.securityKey);
                    securityKeyTextView.setText(securityKey);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("GetUserProfile", "Error processing response: " + e.getMessage());
            }
        }
    }

    private void updateProfile() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        String userId = sessionManager.getUserId();
        String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=" + URLEncoder.encode(userId);
        new GetUserProfileTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, apiUrl);
    }

    private void copyToClipboard() {
        TextView securityKeyTextView = findViewById(R.id.securityKey);
        String securityKey = securityKeyTextView.getText().toString();

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Security Key", securityKey);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(this, "Security Key disalin ke Clipboard", Toast.LENGTH_SHORT).show();
    }
}
