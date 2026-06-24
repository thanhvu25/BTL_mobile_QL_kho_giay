package com.example.btl_kho_giay.model;

public class XuatKho {
    private int maXuat;
    private int maSP;
    private String tenSP;
    private int soLuongXuat;
    private String ngayXuat;
    private double donGiaXuat;
    private String nguoiNhan;
    private String lyDoXuat;
    private String ghiChu;

    public XuatKho() {
    }

    public XuatKho(int maXuat,
                   int maSP,
                   String tenSP,
                   int soLuongXuat,
                   String ngayXuat,
                   double donGiaXuat,
                   String nguoiNhan,
                   String lyDoXuat,
                   String ghiChu) {
        this.maXuat = maXuat;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuongXuat = soLuongXuat;
        this.ngayXuat = ngayXuat;
        this.donGiaXuat = donGiaXuat;
        this.nguoiNhan = nguoiNhan;
        this.lyDoXuat = lyDoXuat;
        this.ghiChu = ghiChu;
    }

    public int getMaXuat() {
        return maXuat;
    }

    public void setMaXuat(int maXuat) {
        this.maXuat = maXuat;
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

    public int getSoLuongXuat() {
        return soLuongXuat;
    }

    public void setSoLuongXuat(int soLuongXuat) {
        this.soLuongXuat = soLuongXuat;
    }

    public String getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(String ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public double getDonGiaXuat() {
        return donGiaXuat;
    }

    public void setDonGiaXuat(double donGiaXuat) {
        this.donGiaXuat = donGiaXuat;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public String getLyDoXuat() {
        return lyDoXuat;
    }

    public void setLyDoXuat(String lyDoXuat) {
        this.lyDoXuat = lyDoXuat;
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
