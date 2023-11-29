package com.ganesha.diur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private SessionManager sessionManager;
    private String[] texts = {"","#DIUR", "#DietSayur", "#DietForU", "#GoHealty"};
    private int index = 0;
    private int charIndex = 0;
    private int delayDelete = 200; // milliseconds untuk menghapus setiap karakter
    private int delayType = 200; // milliseconds untuk mengetik setiap karakter
    private Handler handler = new Handler();
    private boolean isDeleting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        textView = findViewById(R.id.textView);

        // Cek apakah pengguna sudah login
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            // Jika sudah login, arahkan ke home.java
            Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);
            finish(); // Tutup activity saat ini agar tidak dapat kembali ke sini dari tombol "Back"
        } else {
            // Jika belum login, tampilkan teks dan tombol "Mulai"
            animateText();

            Button buttonMulai = findViewById(R.id.buttonMulai);
            buttonMulai.setVisibility(View.VISIBLE); // Tampilkan tombol "Mulai"
            buttonMulai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Jika tombol "Mulai" diklik, arahkan ke halaman login
                    Intent intent = new Intent(MainActivity.this, login.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void animateText() {
        if (index >= texts.length) {
            index = 0;
        }

        String currentText = texts[index];
        textView.setText("");

        // Menghapus karakter
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isDeleting && charIndex >= 0 && charIndex <= currentText.length()) {
                    String displayedText = currentText.substring(0, charIndex);
                    textView.setText(displayedText);
                    charIndex++;
                    handler.postDelayed(this, delayDelete);
                } else {
                    charIndex = 0;
                    typeText(currentText);
                }
            }
        }, delayDelete);
    }

    private void typeText(final String currentText) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex <= currentText.length()) {
                    String displayedText = currentText.substring(0, charIndex);
                    textView.setText(displayedText);
                    charIndex++;
                    handler.postDelayed(this, delayType);
                } else {
                    index++;
                    charIndex = currentText.length(); // Atur kembali charIndex ke panjang currentText
                    // Set the flag to start deleting on the next iteration
                    isDeleting = true;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            deleteText(currentText);
                        }
                    }, delayType);
                }
            }
        }, delayType);
    }

    private void deleteText(final String currentText) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex >= 0) {
                    String displayedText = currentText.substring(0, charIndex);
                    textView.setText(displayedText);
                    charIndex--;
                    handler.postDelayed(this, delayDelete);
                } else {
                    // Reset the flag to allow typing in the next iteration
                    isDeleting = false;
                    // Introduce a delay before restarting the animation
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animateText(); // Panggil animateText() untuk melanjutkan loop
                        }
                    }, delayType);
                }
            }
        }, delayDelete);
    }
}
