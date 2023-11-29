package com.ganesha.diur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LupaPassword extends AppCompatActivity {

    ImageButton btnback;
    EditText etUsername, etSecurityKey, etNewpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);
        getSupportActionBar().hide();

        // Deklarasi edit text
        etUsername = findViewById(R.id.et_username_lupa);
        etSecurityKey = findViewById(R.id.et_securitykey_lupa);
        etNewpassword = findViewById(R.id.et_newpassword_lupa);

        // Tombol back
        btnback = findViewById(R.id.buttonBackToLogin);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LupaPassword.this, login.class);
                startActivity(i);
                finish();
            }
        });

        // Tombol reset password
        Button btnReset = findViewById(R.id.button_ubah_password);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        // Tambahkan listener untuk menangani aksi "Done"
        etNewpassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Aksi yang diambil saat "Done" ditekan pada keyboard
                    resetPassword();
                    return true;
                }
                return false;
            }
        });
    }

    // Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LupaPassword.this, login.class);
        startActivity(i);
        finish();
    }

    private void resetPassword() {
        String username = etUsername.getText().toString();
        String securityKey = etSecurityKey.getText().toString();
        String newPassword = etNewpassword.getText().toString();

        ResetPasswordTask resetPasswordTask = new ResetPasswordTask(username, securityKey, newPassword);
        resetPasswordTask.execute();
    }

    private class ResetPasswordTask extends AsyncTask<Void, Void, ResponseData> {
        private String username;
        private String securityKey;
        private String newPassword;

        public ResetPasswordTask(String username, String securityKey, String newPassword) {
            this.username = username;
            this.securityKey = securityKey;
            this.newPassword = newPassword;
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/lupaPassword";

            try {
                String apiUrlWithParams = apiUrl + "?username=" + URLEncoder.encode(username, "UTF-8") +
                        "&security_key=" + URLEncoder.encode(securityKey, "UTF-8") +
                        "&new_password=" + URLEncoder.encode(newPassword, "UTF-8");

                URL url = new URL(apiUrlWithParams);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Membuat objek JSON
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", username);
                jsonBody.put("security_key", securityKey);
                jsonBody.put("new_password", newPassword);

                // Mengirim data JSON
                OutputStream os = connection.getOutputStream();
                os.write(jsonBody.toString().getBytes());
                os.flush();
                os.close();

                // Membaca respons dari server
                int responseCode = connection.getResponseCode();
                String jsonResponse = readResponse(connection);

                return new ResponseData(responseCode, jsonResponse);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return new ResponseData(500, "Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(ResponseData result) {
            int responseCode = result.getResponseCode();
            String jsonResponse = result.getJsonResponse();

            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);
                String status = jsonObject.optString("status", "");

                if (status.equals("success")) {
                    // Reset password berhasil
                    Toast.makeText(LupaPassword.this, "Password berhasil direset", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LupaPassword.this, login.class);
                    startActivity(i);
                    finish();
                } else {
                    // Reset password gagal, tampilkan pesan kesalahan
                    String errorMessage = jsonObject.optString("message", "Unknown error");
                    Toast.makeText(LupaPassword.this,  errorMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Kesalahan parsing JSON
                Toast.makeText(LupaPassword.this, "Kesalahan parsing JSON", Toast.LENGTH_SHORT).show();
            }
        }

        private String readResponse(HttpURLConnection connection) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();
            return response.toString();
        }
    }

    // Kelas ResponseData untuk menangkap status code dan JSON response
    private static class ResponseData {
        private final int responseCode;
        private final String jsonResponse;

        public ResponseData(int responseCode, String jsonResponse) {
            this.responseCode = responseCode;
            this.jsonResponse = jsonResponse;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getJsonResponse() {
            return jsonResponse;
        }
    }
}
