package com.example.btl_kho_giay.model;

public class NhapKho {

    private int maPN;
    private int maSP;
    private String tenSP;
    private int soLuongNhap;
    private double donGiaNhap;
    private String ngayNhap;
    private String ghiChu;

    public NhapKho() {
    }

    public NhapKho(int maPN,
                   int maSP,
                   String tenSP,
                   int soLuongNhap,
                   double donGiaNhap,
                   String ngayNhap,
                   String ghiChu) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongNhap = soLuongNhap;
        this.donGiaNhap = donGiaNhap;
        this.ngayNhap = ngayNhap;
        this.ghiChu = ghiChu;
    }

    public int getMaPN() {
        return maPN;
    }

    public void setMaPN(int maPN) {
        this.maPN = maPN;
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

    public int getSoLuongNhap() {
        return soLuongNhap;
    }

    public void setSoLuongNhap(int soLuongNhap) {
        this.soLuongNhap = soLuongNhap;
    }

    public double getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(double donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

    public String getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(String ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    @Override
    public String toString() {
        return tenSP;
    }
}
