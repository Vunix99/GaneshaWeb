package com.ganesha.diur;

public class RiwayatModel extends RiwayatItem {

    private String jamKonsumsi;
    private String tanggalKonsumsi;
    private String idResep;
    private String judulResep;
    private String gambarResep;
    private int jumlahKaloriResep;

    public RiwayatModel(String jamKonsumsi, String tanggalKonsumsi, String idResep, String judulResep, String gambarResep, int jumlahKaloriResep) {
        this.jamKonsumsi = jamKonsumsi;
        this.tanggalKonsumsi = tanggalKonsumsi;
        this.idResep = idResep;
        this.judulResep = judulResep;
        this.gambarResep = gambarResep;
        this.jumlahKaloriResep = jumlahKaloriResep;
    }

    @Override
    public boolean isHeader() {
        return false; // Karena ini adalah RiwayatModel, bukan RiwayatHeaderModel
    }

    @Override
    public int getItemType() {
        return TYPE_ITEM; // Tipe untuk RiwayatModel
    }


    public String getJamKonsumsi() {
        return jamKonsumsi;
    }

    public String getTanggalKonsumsi() {
        return tanggalKonsumsi;
    }

    public String getIdResep() {
        return idResep;
    }

    public String getJudulResep() {
        return judulResep;
    }

    public String getGambarResep() {
        return gambarResep;
    }

    public int getJumlahKaloriResep() {
        return jumlahKaloriResep;
    }
}
