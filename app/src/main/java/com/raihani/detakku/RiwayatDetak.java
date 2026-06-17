package com.raihani.detakku;

public class RiwayatDetak {

    private int bpm;
    private String status;
    private String waktu;
    private long timestamp;


    public RiwayatDetak() {
        // diperlukan Firestore
    }


    public RiwayatDetak(
            int bpm,
            String status,
            String waktu,
            long timestamp
    ) {
        this.bpm = bpm;
        this.status = status;
        this.waktu = waktu;
        this.timestamp = timestamp;
    }


    public int getBpm() {
        return bpm;
    }


    public String getStatus() {
        return status;
    }


    public String getWaktu() {
        return waktu;
    }


    public long getTimestamp() {
        return timestamp;
    }
}