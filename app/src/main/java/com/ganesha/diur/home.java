package com.ganesha.diur;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class home extends AppCompatActivity {

    private double calorieIdeal;
    private SessionManager sessionManager;
    private TextView currentCalorie;
    private ProgressBar progressBar;
    private TextView kaloriIdealUser;
    private TextView sayHiHome;

    private RecyclerView recyclerViewArtikelNew;
    private ArtikelNewAdapter artikelNewAdapter;
    private List<ArtikelModel> artikelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);

        // Inisialisasi SessionManager
        sessionManager = new SessionManager(this);

        // Cek status login
        if (!sessionManager.isLoggedIn()) {
            // Jika pengguna belum login, arahkan ke halaman login
            Intent intent = new Intent(home.this, login.class);
            startActivity(intent);
            finish(); // Tutup activity ini agar tidak dapat kembali ke home tanpa login
        } else {
            // Pengguna sudah login, lanjutkan dengan mengatur tampilan dan logika lainnya
            setContentView(R.layout.activity_home);

            // Inisialisasi komponen UI
            currentCalorie = findViewById(R.id.currentCalorie);
            kaloriIdealUser = findViewById(R.id.kalori_ideal_user);
            progressBar = findViewById(R.id.progressBar);
            sayHiHome = findViewById(R.id.sayHiHome);

            // Mulai AsyncTask untuk mendapatkan profil pengguna
            new FetchUserProfileTask().execute();

            //Mulai get artikel new
            new FetchAllArtikelTask().execute();

            //TombolKalku
            ImageButton buttonKalku = findViewById(R.id.kalkulator_button);
            buttonKalku.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(home.this, kalku_kalori.class);
                    startActivity(intent);
                    finish();
                }
            });

            // Tombol Profile
            ImageButton showProfileButton = findViewById(R.id.show_profile_button);
            showProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserProfile();
                }
            });

            //Tombol Selengkapnya (riwayat)
            TextView riwayat = findViewById(R.id.see_detail);
            riwayat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(home.this, Riwayat.class);
                    startActivity(i);
                    finish();
                }
            });

            //Tombol Resep
            ImageButton buttonToResep = findViewById(R.id.buttonToResep);
            buttonToResep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToResep();
                }
            });

            //Tombol Artikel
            ImageButton buttonToArtikel = findViewById(R.id.buttonToArticle);
            buttonToArtikel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToArtikel();
                }
            });

            //kasus artikel new (recent)
            // Inisialisasi RecyclerView dan Adapter
            recyclerViewArtikelNew = findViewById(R.id.recyclerViewArtikelNew);
            artikelNewAdapter = new ArtikelNewAdapter(this, artikelList);

            // Atur RecyclerView layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewArtikelNew.setLayoutManager(layoutManager);

            // Atur adapter ke RecyclerView
            recyclerViewArtikelNew.setAdapter(artikelNewAdapter);

            // Atur jumlah artikel yang ditampilkan
            artikelNewAdapter.setLimit(4);

            // Atur listener untuk menangani klik item
            artikelNewAdapter.setOnItemClickListener(new ArtikelNewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String idArtikel) {
                    // Implementasikan logika untuk pergi ke intent selanjutnya dengan id_artikel
                    Intent intent = new Intent(home.this, detail_artikel.class);
                    intent.putExtra("ARTIKEL_ID", idArtikel);
                    startActivity(intent);
                    finish();
                }
            });

            //Button Plus (Button Akses shortcut mudah resep)
            ImageButton btnPlus = findViewById(R.id.buttonPlus);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(home.this, resep.class);
                    startActivity(i);
                    finish();
                }
            });



        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor(int color) {
        // Check if the device is running Lollipop or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            // Clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // Add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set the status bar color
            window.setStatusBarColor(color);
        }
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
        String idOrangDiet = sessionManager.getUserId(); // Ganti dengan ID yang sesuai

        // URL endpoint API
        String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getConsumeById?id_orangDiet=" + idOrangDiet;

        new FetchTotalCalorieTask().execute(apiUrl);
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

                //Welcoming Message
                TextView saySelamat = findViewById(R.id.saySelamat);
                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                if (timeOfDay >= 0 && timeOfDay < 12) {
                    saySelamat.setText("Selamat Pagi!");
                } else if (timeOfDay >= 12 && timeOfDay < 16) {
                    saySelamat.setText("Selamat Siang!");
                } else if (timeOfDay >= 16 && timeOfDay < 18) {
                    saySelamat.setText("Selamat Sore!");
                } else if (timeOfDay >= 18 && timeOfDay < 24) {
                    saySelamat.setText("Selamat Malam!");
                }

                //ganti say hi homenya
                sayHiHome.setText("Hi, "+username);
                double bmr = calculateBMR(age, weight, height, gender);
                calorieIdeal = calculateCalorieIdeal(bmr, activityLevel);

                // Update UI dengan data yang diambil dari API
                kaloriIdealUser.setText(String.format("%.0f", calorieIdeal));

            } catch (JSONException e) {
                // Tangani kesalahan parsing JSON
                e.printStackTrace();
            }
        } else {
            // Tangani respons null
            Toast.makeText(home.this, "Gagal mengambil profil pengguna", Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchAllArtikelTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... voids) {
            // URL endpoint API untuk mendapatkan semua artikel
            String apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllArtikel";

            // Jumlah artikel yang ingin diambil (misalnya, 4 artikel)
            int numberOfArticles = 4; // Ubah sesuai kebutuhan

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
                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                    // Batasi jumlah artikel yang diambil
                    JSONArray limitedArray = new JSONArray();
                    for (int i = 0; i < numberOfArticles && i < jsonArray.length(); i++) {
                        limitedArray.put(jsonArray.getJSONObject(i));
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("artikel", limitedArray);

                    return jsonObject;


                } catch (JSONException | IOException e) {
                    // Tangani kesalahan parsing JSON atau IO
                    Log.e("Error", "Error during JSON parsing or IO: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    // Putuskan koneksi HttpURLConnection
                    urlConnection.disconnect();
                }

            } catch (IOException e) {
                // Tangani kesalahan IO
                Log.e("Error", "IO Error: " + e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            // Setelah mendapatkan data artikel, update UI
            updateArtikelUI(result);
        }
    }

    private void updateArtikelUI(JSONObject artikelData) {
        if (artikelData != null) {
            try {
                // Bersihkan artikelList sebelum menambahkan data baru
                artikelList.clear();

                // Loop untuk menambahkan setiap artikel ke artikelList
                JSONArray artikelArray = artikelData.getJSONArray("artikel");
                for (int i = 0; i < artikelArray.length(); i++) {
                    JSONObject artikelObject = artikelArray.getJSONObject(i);

                    // Ambil data artikel
                    String id_artikel = artikelObject.getString("id_artikel");
                    String judul = artikelObject.getString("judul");
                    String isi_artikel = artikelObject.getString("isi_artikel");
                    String gambar_artikel = artikelObject.getString("gambar_artikel");
                    String sumber = artikelObject.getString("sumber");
                    String tanggal_terbit = artikelObject.getString("tanggal_terbit");

                    // Buat objek ArtikelModel dan tambahkan ke artikelList
                    ArtikelModel artikel = new ArtikelModel(id_artikel, judul, isi_artikel, gambar_artikel, sumber, tanggal_terbit);
                    artikelList.add(artikel);
                }

                // Notifikasi adapter bahwa data telah berubah
                artikelNewAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                // Tangani kesalahan parsing JSON
                e.printStackTrace();
            }
        } else {
            // Tangani respons null
            Toast.makeText(home.this, "Gagal mengambil data artikel", Toast.LENGTH_SHORT).show();
        }
    }


    private void showUserProfile() {
        // Intent ke user_profile
        Intent intent = new Intent(home.this, user_profile.class);
        startActivity(intent);
        finish();
    }

    private void goToResep() {
        // Intent ke resep
        Intent intent = new Intent(home.this, resep.class);
        startActivity(intent);
        finish();
    }

    private void goToArtikel() {
        // Intent ke resep
        Intent intent = new Intent(home.this, artikel.class);
        startActivity(intent);
        finish();
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
}
