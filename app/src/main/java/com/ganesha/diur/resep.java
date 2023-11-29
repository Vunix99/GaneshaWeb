package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class resep extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ResepAdapter resepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);
        getSupportActionBar().hide();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //back
        ImageButton buttonBackToHome = findViewById(R.id.buttonBackToHome);

        // Menambahkan listener klik untuk tombol back
        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(resep.this, home.class);
                startActivity(intent);
                finish();
            }
        });

        // Panggil AsyncTask untuk mendapatkan data dari API
        new FetchDataTask().execute();
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(resep.this, home.class);
        startActivity(i);
        finish();
    }

    private class FetchDataTask extends AsyncTask<Void, Void, List<ResepModel>> {

        @Override
        protected List<ResepModel> doInBackground(Void... voids) {
            List<ResepModel> resepList = new ArrayList<>();

            try {
                // URL endpoint API
                URL url = new URL("https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllResep");

                // Buka koneksi HTTP
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    // Baca data dari InputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Parse JSON
                    resepList = parseJson(response.toString());
                } finally {
                    // Tutup koneksi setelah selesai
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                Log.e("ResepActivity", "Error in doInBackground: " + e.getMessage());
            }

            return resepList;
        }

        @Override
        protected void onPostExecute(List<ResepModel> resepList) {
            // Tampilkan data dalam RecyclerView setelah selesai
            if (resepList != null && resepList.size() > 0) {
                showDataInRecyclerView(resepList);
            } else {
                Log.e("ResepActivity", "No data received from API.");
            }
        }

        private List<ResepModel> parseJson(String json) {
            List<ResepModel> resepList = new ArrayList<>();

            try {
                // Array JSON tidak memerlukan objek JSON
                JSONArray resepArray = new JSONArray(json);

                // Iterasi melalui array resep
                for (int i = 0; i < resepArray.length(); i++) {
                    JSONObject resepObject = resepArray.getJSONObject(i);

                    // Ambil informasi resep
                    String idResep = resepObject.getString("id_resep");
                    String title = resepObject.getString("judul_resep");
                    String description = resepObject.getString("deskripsi");
                    String imageUrl = resepObject.getString("gambar_resep");
                    int calorie = resepObject.getInt("jumlah_kalori_resep");

                    // Buat objek ResepModel dan tambahkan ke daftar
                    ResepModel resepModel = new ResepModel(idResep, title, description, imageUrl, calorie);
                    resepModel.setId_resep(idResep);
                    resepModel.setJudul_resep(title);
                    resepModel.setDeskripsi(description);
                    resepModel.setGambar_resep(imageUrl);
                    resepModel.setJumlah_kalori_resep(calorie);

                    resepList.add(resepModel);
                }
            } catch (JSONException e) {
                Log.e("ResepActivity", "Error parsing JSON: " + e.getMessage());
            }

            return resepList;
        }
    }

    private void showDataInRecyclerView(List<ResepModel> resepList) {
        // Inisialisasi dan atur adapter untuk RecyclerView
        resepAdapter = new ResepAdapter(resepList, this);
        recyclerView.setAdapter(resepAdapter);
    }
}
