package com.ganesha.diur;

public class ArtikelModel {
    private String id_artikel;
    private String judul;
    private String isi_artikel;
    private String gambar_artikel;
    private String sumber;
    private String tanggal_terbit;

    // Konstruktor
    public ArtikelModel(String id_artikel, String judul, String isi_artikel, String gambar_artikel, String sumber, String tanggal_terbit) {
        this.id_artikel = id_artikel;
        this.judul = judul;
        this.isi_artikel = isi_artikel;
        this.gambar_artikel = gambar_artikel;
        this.sumber = sumber;
        this.tanggal_terbit = tanggal_terbit;
    }

    // Getter dan Setter
    public String getId_artikel() {
        return id_artikel;
    }

    public void setId_artikel(String id_artikel) {
        this.id_artikel = id_artikel;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi_artikel() {
        return isi_artikel;
    }

    public void setIsi_artikel(String isi_artikel) {
        this.isi_artikel = isi_artikel;
    }

    public String getGambar_artikel() {
        return gambar_artikel;
    }

    public void setGambar_artikel(String gambar_artikel) {
        this.gambar_artikel = gambar_artikel;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getTanggal_terbit() {
        return tanggal_terbit;
    }

    public void setTanggal_terbit(String tanggal_terbit) {
        this.tanggal_terbit = tanggal_terbit;
    }
}
