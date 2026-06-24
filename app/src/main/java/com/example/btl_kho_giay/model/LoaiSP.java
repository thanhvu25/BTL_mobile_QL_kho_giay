package com.example.btl_kho_giay.model;

public class LoaiSP {

    private int maLoai;
    private String tenLoai;
    private String moTa;

    public LoaiSP() {
    }

    public LoaiSP(int maLoai,
                  String tenLoai,
                  String moTa) {

        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return tenLoai;
    }
}
