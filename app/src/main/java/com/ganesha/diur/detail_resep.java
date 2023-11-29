package com.ganesha.diur;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class detail_resep extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep);
        getSupportActionBar().hide();

        SessionManager sessionManager = new SessionManager(this);

        // Mendapatkan ID resep dari Intent
        String idResep = getIntent().getStringExtra("id_resep");

        // Membuat URL berdasarkan ID resep
        String endpointUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getResepById?id=" + idResep;

        ImageButton buttonBackToResep = findViewById(R.id.buttonBackToResep);
        buttonBackToResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke activity resep.java
                finish();
            }
        });

        // Tombol selesai resep
        Button buttonDoneResep01 = findViewById(R.id.buttonDoneResep01);
        buttonDoneResep01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tampilkan pop-up konfirmasi
                showConfirmationPopup(idResep);
            }
        });

        // Memanggil AsyncTask untuk melakukan HTTP request secara asynchronous
        new GetResepDataTask().execute(endpointUrl);
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(detail_resep.this, resep.class);
        startActivity(i);
        finish();
    }

    private void showConfirmationPopup(String idResep) {
        // Membuat dialog pop-up
        Dialog dialog = new Dialog(detail_resep.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_confirm);

        // Mendapatkan elemen UI dari layout pop-up
        Button cancelButton = dialog.findViewById(R.id.button_batal_resep);
        Button doneButton = dialog.findViewById(R.id.button_done_resep);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup pop-up jika tombol "Batal" ditekan
                dialog.dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menutup pop-up dan memanggil AsyncTask jika tombol "Selesaikan" ditekan
                dialog.dismiss();
                SessionManager sessionManager = new SessionManager(detail_resep.this);
                new InsertKonsumsiHarianTask().execute(sessionManager.getUserId(), idResep);
            }
        });

        // Menampilkan pop-up
        dialog.show();
    }


    private class GetResepDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];

            try {
                // Membuat URL dari string URL
                URL url = new URL(urlString);

                // Membuat koneksi HTTP
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    // Membaca data dari InputStream
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } finally {
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
                    // Mendapatkan objek JSON dari respons
                    JSONObject resepJson = new JSONObject(result);

                    // Mendapatkan data dari objek JSON
                    String judulResep = resepJson.getString("judul_resep");
                    String deskripsiResep = resepJson.getString("deskripsi");
                    String gambarResepUrl = resepJson.getString("gambar_resep");
                    String jumlahKaloriResep = resepJson.getString("jumlah_kalori_resep");

                    // Setel data detail resep ke UI
                    TextView titleTextView = findViewById(R.id.title_resep);
                    TextView descriptionTextView = findViewById(R.id.deskripsi_resep);
                    ImageView imageView = findViewById(R.id.detail_resep_image);
                    TextView calorieTextView = findViewById(R.id.detail_calorie);

                    titleTextView.setText(judulResep);
                    descriptionTextView.setText(deskripsiResep);
                    calorieTextView.setText(jumlahKaloriResep + " kcal");

                    // Memuat gambar dari URL menggunakan Picasso
                    Picasso.get().load(gambarResepUrl).placeholder(R.drawable.image_resep_01).into(imageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class InsertKonsumsiHarianTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String idResep = params[1];

            try {
                // Membuat URL dari string URL
                URL url = new URL("https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/insertKonsumsiHarian");

                // Membuat koneksi HTTP
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    // Setel metode HTTP ke POST
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);

                    // Membuat data yang akan dikirim sebagai payload
                    String postData = "id_orangDiet=" + userId + "&id_resep=" + idResep;

                    // Mendapatkan OutputStream dari koneksi HTTP
                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    outputStream.close();

                    // Periksa status kode HTTP
                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        // Membaca data dari InputStream
                        InputStream in = urlConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        return result.toString();
                    } else {
                        // Handle kesalahan HTTP (status kode tidak OK)
                        return null;
                    }
                } finally {
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
                    // Mendapatkan objek JSON dari respons
                    JSONObject responseJson = new JSONObject(result);

                    // Mendapatkan status dari respons
                    boolean success = responseJson.getBoolean("success");

                    // Handle respons sesuai kebutuhan aplikasi Anda
                    if (success) {
                        // Tindakan yang diambil jika sukses
                        // Misalnya, menampilkan pesan sukses kepada pengguna
                        Toast.makeText(detail_resep.this, "Resep berhasil diselesaikan!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(detail_resep.this, home.class);
                        startActivity(i);
                        finish();
                    } else {
                        // Tindakan yang diambil jika gagal
                        // Misalnya, menampilkan pesan kesalahan kepada pengguna
                        Toast.makeText(detail_resep.this, "Gagal menyelesaikan resep", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Tindakan yang diambil jika result null
                Toast.makeText(detail_resep.this, "Tidak ada respons dari server", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
