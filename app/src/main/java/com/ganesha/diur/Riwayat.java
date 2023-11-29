package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Riwayat extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatAdapter riwayatAdapter;
    SessionManager sessionManager;
    TextView currentCalorie;
    TextView kaloriIdeal ;
    TextView tanggalSekarang;
    TextView userBBideal;
    private double calorieIdeal;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        getSupportActionBar().hide();

        //back to home
        ImageButton btnHome = findViewById(R.id.buttonBackToHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Riwayat.this, home.class);
                startActivity(i);
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerViewRiwayat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sessionManager = new SessionManager(Riwayat.this);
        String userId = sessionManager.getUserId();
        // Call the AsyncTask to get data from MongoDB

        //Ambil Layout dari actity_riwayat.xml :
        currentCalorie = findViewById(R.id.currentCalorie);
        kaloriIdeal = findViewById(R.id.kalori_ideal_user);
        userBBideal = findViewById(R.id.user_bb_ideal);
        progressBar = findViewById(R.id.progressBar);
        tanggalSekarang = findViewById(R.id.tanggal_sekarang);

        new FetchUserProfileTask().execute();
        new FetchRiwayatTask().execute(userId);
        // Update the UI with the current date
        updateTanggalSekarang();
    }

    //Tombol Back Android ditekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Riwayat.this, home.class);
        startActivity(i);
        finish();
    }

    private void updateTanggalSekarang() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        tanggalSekarang.setText(currentDate);
    }

    private class FetchUserProfileTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            String userId = sessionManager.getUserId();

            // Tambahkan log
            Log.d("Debug", "Fetching UserProfile - userId: " + userId);

            // URL endpoint API
            String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=" + userId;

            try {
                // Buat objek URL
                URL url = new URL(apiUrl);

                // Buka koneksi
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    // Dapatkan InputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // Baca respons
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    // Parse respons JSON
                    return new JSONObject(stringBuilder.toString());

                } catch (JSONException | IOException e) {
                    // Tambahkan log
                    Log.e("Error", "Error during JSON parsing or IO: " + e.getMessage());

                    // Tangani kesalahan parsing JSON atau IO
                    e.printStackTrace();
                } finally {
                    // Putuskan koneksi HttpURLConnection
                    urlConnection.disconnect();
                }

            } catch (IOException e) {
                // Tambahkan log
                Log.e("Error", "IO Error: " + e.getMessage());

                // Tangani kesalahan IO
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            // Setelah mendapatkan data profil pengguna, update UI
            updateUI(result);

            // Ambil total kalori dari API
            fetchTotalCalorieFromAPI();
        }
    }

    private void fetchTotalCalorieFromAPI() {
        String userId = sessionManager.getUserId(); // Ganti dengan ID yang sesuai

        // URL endpoint API
        String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getConsumeById?id_orangDiet=" + userId;

        new Riwayat.FetchTotalCalorieTask().execute(apiUrl);
    }

    private class FetchTotalCalorieTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return 0;
            }

            String apiUrl = urls[0];

            try {
                // Buat objek URL
                URL url = new URL(apiUrl);

                // Buka koneksi
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    // Dapatkan InputStream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    // Baca respons
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    // Parse respons JSON
                    JSONObject jsonResponse = new JSONObject(stringBuilder.toString());

                    if (jsonResponse.getBoolean("success")) {
                        return jsonResponse.getInt("total_kalori");
                    } else {
                        Log.e("Error", "Error fetching total calorie: " + jsonResponse.getString("message"));
                        return 0;
                    }

                } catch (JSONException | IOException e) {
                    // Tambahkan log
                    Log.e("Error", "Error during JSON parsing or IO: " + e.getMessage());

                    // Tangani kesalahan parsing JSON atau IO
                    e.printStackTrace();
                    return 0;
                } finally {
                    // Putuskan koneksi HttpURLConnection
                    urlConnection.disconnect();
                }

            } catch (IOException e) {
                // Tambahkan log
                Log.e("Error", "IO Error: " + e.getMessage());

                // Tangani kesalahan IO
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            // Set total kalori ke UI
            currentCalorie.setText(String.valueOf(result));

            // Perhitungan progress dari API
            double totalCalorieDouble = (int) result;
            double calorieIdealDouble = (int) calorieIdeal;
            int progress = (int) ((totalCalorieDouble / calorieIdealDouble) * 100);
            progressBar.setProgress(progress);
        }
    }

    private void updateUI(JSONObject userProfile) {
        if (userProfile != null) {
            try {
                //ambil data
                String username = userProfile.getString("username");
                int age = userProfile.getInt("usia");
                String gender = userProfile.getString("jenis_kelamin");
                int height = userProfile.getInt("tinggi_badan");
                int weight = userProfile.getInt("berat_badan");
                String activityLevel = userProfile.getString("level_aktivitas");

                double bmr = calculateBMR(age, weight, height, gender);
                calorieIdeal = calculateCalorieIdeal(bmr, activityLevel);

                // Update UI dengan data yang diambil dari API
                kaloriIdeal.setText(String.format("%.0f", calorieIdeal));

                // Calculate and update berat badan ideal
                double beratBadanIdeal = calculateBeratBadanIdeal(height, gender.equals("Laki-laki"));
                userBBideal.setText(String.format("%.2f", beratBadanIdeal));

            } catch (JSONException e) {
                // Tangani kesalahan parsing JSON
                e.printStackTrace();
            }
        } else {
            // Tangani respons null
            Toast.makeText(Riwayat.this, "Gagal mengambil profil pengguna", Toast.LENGTH_SHORT).show();
        }
    }


    private class FetchRiwayatTask extends AsyncTask<String, Void, List<RiwayatItem>> {

        @Override
        protected List<RiwayatItem> doInBackground(String... params) {
            String userId = params[0];
            List<RiwayatItem> riwayatList = new ArrayList<>();

            try {
                URL url = new URL("https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getRiwayat?id_orangDiet=" + userId);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject jsonResponse = new JSONObject(result.toString());

                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        JSONArray dataHarianArray = jsonResponse.getJSONArray("data_harian");

                        // Variable untuk grup tanggal
                        String currentTanggal = null;
                        int totalKaloriHarian = 0;

                        for (int i = 0; i < dataHarianArray.length(); i++) {
                            JSONObject konsumsiItem = dataHarianArray.getJSONObject(i);

                            String jamKonsumsi = konsumsiItem.getString("jam_konsumsi");
                            String tanggalKonsumsi = konsumsiItem.getString("tanggal_konsumsi");
                            String idResep = konsumsiItem.getString("id_resep");
                            String judulResep = konsumsiItem.getString("judul_resep");
                            String gambarResep = konsumsiItem.getString("gambar_resep");
                            int jumlahKaloriResep = konsumsiItem.getInt("jumlah_kalori_resep");

                            RiwayatModel riwayatModel = new RiwayatModel(jamKonsumsi, tanggalKonsumsi, idResep, judulResep, gambarResep, jumlahKaloriResep);

                            // Cek apakah tanggal berubah
                            if (currentTanggal == null || !currentTanggal.equals(tanggalKonsumsi)) {
                                // Tambahkan RiwayatHeaderModel
                                if (currentTanggal != null) {
                                    RiwayatHeaderModel headerModel = new RiwayatHeaderModel(currentTanggal, totalKaloriHarian);
                                    riwayatList.add(headerModel);
                                }

                                // Setel ulang variabel grup tanggal
                                currentTanggal = tanggalKonsumsi;
                                totalKaloriHarian = 0;
                            }

                            // Tambahkan RiwayatModel
                            riwayatList.add(riwayatModel);
                            totalKaloriHarian += jumlahKaloriResep;
                        }

                        // Tambahkan header untuk tanggal terakhir
                        if (currentTanggal != null) {
                            RiwayatHeaderModel headerModel = new RiwayatHeaderModel(currentTanggal, totalKaloriHarian);
                            riwayatList.add(headerModel);
                        }
                    }

                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return riwayatList;
        }

        @Override
        protected void onPostExecute(List<RiwayatItem> riwayatList) {
            super.onPostExecute(riwayatList);

            // Split riwayatList into headers and items
            List<RiwayatHeaderModel> headerList = new ArrayList<>();
            List<RiwayatModel> itemList = new ArrayList<>();

            for (RiwayatItem item : riwayatList) {
                if (item instanceof RiwayatHeaderModel) {
                    headerList.add((RiwayatHeaderModel) item);
                } else if (item instanceof RiwayatModel) {
                    itemList.add((RiwayatModel) item);
                }
            }

            // Sort the headerList based on tanggal (descending order)
            Collections.sort(headerList, new Comparator<RiwayatHeaderModel>() {
                @Override
                public int compare(RiwayatHeaderModel header1, RiwayatHeaderModel header2) {
                    return header2.getTanggal().compareTo(header1.getTanggal());
                }
            });

            // Merge headerList and itemList
            List<RiwayatItem> sortedRiwayatList = new ArrayList<>();
            for (RiwayatHeaderModel header : headerList) {
                sortedRiwayatList.add(header);

                // Add corresponding items based on tanggal
                for (RiwayatModel item : itemList) {
                    if (item.getTanggalKonsumsi().equals(header.getTanggal())) {
                        sortedRiwayatList.add(item);
                    }
                }
            }

            // Update the UI with the fetched and sorted data
            riwayatAdapter = new RiwayatAdapter(sortedRiwayatList, Riwayat.this);
            recyclerView.setAdapter(riwayatAdapter);

        }
    }

    private double calculateBMR(int age, int weight, int height, String gender) {
        double bmr;
        if (gender.equals("Laki-laki")) {
            bmr = 66 + (13.7 * weight) + (5 * height) - (6.8 * age);
        } else {
            bmr = 655 + (9.6 * weight) + (1.8 * height) - (4.7 * age);
        }
        return bmr;
    }

    private double calculateCalorieIdeal(double bmr, String activityLevel) {
        double calorieIdeal;
        switch (activityLevel) {
            case "Jarang":
                calorieIdeal = bmr * 1.2;
                break;
            case "Ringan":
                calorieIdeal = bmr * 1.375;
                break;
            case "Sedang":
                calorieIdeal = bmr * 1.55;
                break;
            case "Aktif":
                calorieIdeal = bmr * 1.725;
                break;
            case "Sangat Aktif":
                calorieIdeal = bmr * 1.9;
                break;
            default:
                calorieIdeal = bmr;
                break;
        }
        return calorieIdeal;
    }
    private double calculateBeratBadanIdeal(int tinggiBadan, boolean isPria) {
        // Asumsikan rumus yang diberikan adalah rumus yang diinginkan
        double persenPenurunan = isPria ? 0.10 : 0.15;

        // Hitung berat badan ideal berdasarkan rumus
        return (tinggiBadan - 100) - ((tinggiBadan - 100) * persenPenurunan);
    }
}
