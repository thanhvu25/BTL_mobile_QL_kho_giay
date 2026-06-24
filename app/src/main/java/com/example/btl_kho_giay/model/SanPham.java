package com.example.btl_kho_giay.model;

public class SanPham {
    public SanPham(int maSP, String tenSP, int maLoai, String tenLoai, String tenNCC, double giaNhap, String size, int maNCC, int soLuongTon, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.tenNCC = tenNCC;
        this.giaNhap = giaNhap;
        this.size = size;
        this.maNCC = maNCC;
        this.soLuongTon = soLuongTon;
        this.hinhAnh = hinhAnh;
    }

    private int maSP;
    private String tenSP;
    private int maLoai;
    private int maNCC;
    private String size;
    private double giaNhap;
    private int soLuongTon;
    private String hinhAnh;

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
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

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
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

    private String tenLoai;
    private String tenNCC;


}
