package com.ganesha.diur;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class artikel extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArtikelAdapter artikelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artikel);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(artikel.this, home.class);
                startActivity(intent);
                finish();
            }
        });
        fetchDataFromApi();
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(artikel.this, home.class);
        startActivity(i);
        finish();
    }

    private void fetchDataFromApi() {
        // Ganti URL dengan URL endpoint MongoDB Realm Anda
        String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllArtikel";

        // Jalankan AsyncTask untuk mengambil data dari API secara asynchronous
        new FetchDataAsyncTask().execute(apiUrl);
    }

    private class FetchDataAsyncTask extends AsyncTask<String, Void, List<ArtikelModel>> {

        @Override
        protected List<ArtikelModel> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            String apiUrl = urls[0];
            List<ArtikelModel> artikelList = new ArrayList<>();

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    String jsonResponse = stringBuilder.toString();
                    artikelList = extractArtikelFromJson(jsonResponse);
                } else {
                    Log.e("HTTP Request", "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e("HTTP Request", "Problem retrieving the artikel JSON results.", e);
            }

            return artikelList;
        }

        @Override
        protected void onPostExecute(List<ArtikelModel> artikelList) {
            if (artikelList != null && !artikelList.isEmpty()) {
                artikelAdapter = new ArtikelAdapter(artikel.this, artikelList);
                recyclerView.setAdapter(artikelAdapter);
            } else {
                Toast.makeText(artikel.this, "No data available", Toast.LENGTH_SHORT).show();
            }
        }

        private List<ArtikelModel> extractArtikelFromJson(String jsonResponse) {
            List<ArtikelModel> artikelList = new ArrayList<>();

            try {
                JSONArray artikelArray = new JSONArray(jsonResponse);

                for (int i = 0; i < artikelArray.length(); i++) {
                    JSONObject artikelObject = artikelArray.getJSONObject(i);

                    String id_artikel = artikelObject.getString("id_artikel");
                    String judul = artikelObject.getString("judul");
                    String isi_artikel = artikelObject.getString("isi_artikel");
                    String gambar_artikel = artikelObject.getString("gambar_artikel");
                    String sumber = artikelObject.getString("sumber");
                    String tanggal_terbit = artikelObject.getString("tanggal_terbit");

                    ArtikelModel artikel = new ArtikelModel(id_artikel, judul, isi_artikel, gambar_artikel, sumber, tanggal_terbit);
                    artikelList.add(artikel);
                }
            } catch (JSONException e) {
                Log.e("JSON Parsing", "Error parsing JSON response", e);
            }

            return artikelList;
        }
    }
}
