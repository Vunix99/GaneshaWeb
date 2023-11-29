package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class detail_artikel extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);
        getSupportActionBar().hide();

        // Mendapatkan ID artikel dari Intent
        String idArtikel = getIntent().getStringExtra("ARTIKEL_ID");

        // Membuat URL berdasarkan ID artikel
        String endpointUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getArtikelById?id=" + idArtikel;

        ImageButton btnBackToArtikel = findViewById(R.id.btnBackToArtikel);
        btnBackToArtikel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_artikel.this, artikel.class);
                startActivity(intent);
                finish();
            }
        });
        // Memanggil AsyncTask untuk melakukan HTTP request secara asynchronous
        new GetArtikelDataTask().execute(endpointUrl);
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(detail_artikel.this, artikel.class);
        startActivity(i);
        finish();
    }

    private class GetArtikelDataTask extends AsyncTask<String, Void, String> {
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
                    JSONObject artikelJson = new JSONObject(result);

                    // Mendapatkan data dari objek JSON
                    String judulArtikel = artikelJson.getString("judul");
                    String isiArtikel = artikelJson.getString("isi_artikel");
                    String gambarArtikelUrl = artikelJson.getString("gambar_artikel");
                    String sumberArtikel = artikelJson.getString("sumber");
                    String tanggalTerbitArtikel = artikelJson.getString("tanggal_terbit");

                    // Setel data detail artikel ke UI
                    TextView titleTextView = findViewById(R.id.header_title);
                    TextView contentTextView = findViewById(R.id.isi_artikel);
                    ImageView imageView = findViewById(R.id.detail_image_artikel);
                    TextView sourceTextView = findViewById(R.id.sumber_artikel);
                    TextView dateTextView = findViewById(R.id.tanggal_terbit);

                    titleTextView.setText(judulArtikel);
                    contentTextView.setText(isiArtikel);
                    sourceTextView.setText(sumberArtikel);
                    dateTextView.setText(tanggalTerbitArtikel);

                    // Memuat gambar dari URL menggunakan Picasso
                    Picasso.get().load(gambarArtikelUrl).placeholder(R.drawable.image_artikel_1).into(imageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
