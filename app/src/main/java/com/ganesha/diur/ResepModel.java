package com.ganesha.diur;

import android.view.View;

import java.util.List;

public class ResepModel {
    private String id_resep;
    private String judul_resep;
    private String deskripsi;
    private String gambar_resep;
    private int jumlah_kalori_resep;

    // Konstruktor
    public ResepModel(String id_resep, String judul_resep, String deskripsi, String gambar_resep, int jumlah_kalori_resep) {
        this.id_resep = id_resep;
        this.judul_resep = judul_resep;
        this.deskripsi = deskripsi;
        this.gambar_resep = gambar_resep;
        this.jumlah_kalori_resep = jumlah_kalori_resep;
    }

    // Getter dan Setter
    public String getId_resep() {
        return id_resep;
    }

    public void setId_resep(String id_resep) {
        this.id_resep = id_resep;
    }

    public String getJudul_resep() {
        return judul_resep;
    }

    public void setJudul_resep(String judul_resep) {
        this.judul_resep = judul_resep;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar_resep() {
        return gambar_resep;
    }

    public void setGambar_resep(String gambar_resep) {
        this.gambar_resep = gambar_resep;
    }

    public int getJumlah_kalori_resep() {
        return jumlah_kalori_resep;
    }

    public void setJumlah_kalori_resep(int jumlah_kalori_resep) {
        this.jumlah_kalori_resep = jumlah_kalori_resep;
    }
}
