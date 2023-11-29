package com.ganesha.diur;

public class RiwayatHeaderModel extends RiwayatItem {

    private String tanggal;
    private int totalKalori;

    public RiwayatHeaderModel(String tanggal, int totalKalori) {
        this.tanggal = tanggal;
        this.totalKalori = totalKalori;
    }

    @Override
    public boolean isHeader() {
        return true; // Karena ini adalah RiwayatHeaderModel
    }

    @Override
    public int getItemType() {
        return TYPE_HEADER; // Tipe untuk RiwayatHeaderModel
    }

    // Tambahkan setter dan getter sesuai kebutuhan
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getTotalKalori() {
        return totalKalori;
    }

    public void setTotalKalori(int totalKalori) {
        this.totalKalori = totalKalori;
    }
}
