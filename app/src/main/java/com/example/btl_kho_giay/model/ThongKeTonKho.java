package com.example.btl_kho_giay.model;

public class ThongKeTonKho {
    private int maSP;
    private String tenSP;
    private String size;
    private int soLuongTon;

    public ThongKeTonKho() {
    }

    public ThongKeTonKho(int maSP, String tenSP, String size, int soLuongTon) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.size = size;
        this.soLuongTon = soLuongTon;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
