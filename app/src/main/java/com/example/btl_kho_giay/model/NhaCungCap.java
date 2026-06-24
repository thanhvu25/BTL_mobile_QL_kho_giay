package com.example.btl_kho_giay.model;

public class NhaCungCap {

    private int maNCC;
    private String tenNCC;
    private String soDienThoai;
    private String email;
    private String diaChi;

    public NhaCungCap() {
    }

    public NhaCungCap(int maNCC,
                      String tenNCC,
                      String soDienThoai,
                      String email,
                      String diaChi) {

        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return tenNCC;
    }
}