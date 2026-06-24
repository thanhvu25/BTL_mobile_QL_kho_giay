package com.example.btl_kho_giay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.btl_kho_giay.model.NhapKho;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "QLKhoGiay.db";

    private static final int DATABASE_VERSION = 8;

    public DatabaseHelper(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE TaiKhoan(" +
                        "SoDienThoai TEXT PRIMARY KEY," +
                        "TenDangNhap TEXT NOT NULL UNIQUE," +
                        "MatKhau TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE LoaiSP(" +
                        "MaLoai INTEGER PRIMARY KEY," +
                        "TenLoai TEXT NOT NULL UNIQUE," +
                        "MoTa TEXT)"
        );

        db.execSQL(
                "CREATE TABLE NhaCungCap(" +
                        "MaNCC INTEGER PRIMARY KEY," +
                        "TenNCC TEXT NOT NULL," +
                        "SoDienThoai TEXT NOT NULL UNIQUE," +
                        "Email TEXT UNIQUE," +
                        "DiaChi TEXT)"
        );

        db.execSQL(
                "CREATE TABLE SanPham(" +
                        "MaSP INTEGER PRIMARY KEY," +
                        "TenSP TEXT NOT NULL," +
                        "MaLoai INTEGER NOT NULL," +
                        "MaNCC INTEGER NOT NULL," +
                        "Size TEXT NOT NULL," +
                        "GiaNhap REAL NOT NULL CHECK(GiaNhap > 0)," +
                        "SoLuongTon INTEGER DEFAULT 0 CHECK(SoLuongTon >= 0)," +
                        "HinhAnh TEXT," +

                        "FOREIGN KEY(MaLoai) REFERENCES LoaiSP(MaLoai) ON DELETE RESTRICT," +
                        "FOREIGN KEY(MaNCC) REFERENCES NhaCungCap(MaNCC) ON DELETE RESTRICT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE NhapKho(" +
                        "MaNhap INTEGER PRIMARY KEY," +
                        "MaSP INTEGER NOT NULL," +
                        "SoLuongNhap INTEGER NOT NULL CHECK(SoLuongNhap > 0)," +
                        "DonGiaNhap REAL NOT NULL CHECK(DonGiaNhap > 0)," +
                        "NgayNhap TEXT NOT NULL," +
                        "GhiChu TEXT," +

                        "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP) ON DELETE RESTRICT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE XuatKho(" +
                        "MaXuat INTEGER PRIMARY KEY," +
                        "MaSP INTEGER NOT NULL," +
                        "SoLuongXuat INTEGER NOT NULL CHECK(SoLuongXuat > 0)," +
                        "NgayXuat TEXT NOT NULL," +
                        "DonGiaXuat REAL NOT NULL CHECK(DonGiaXuat > 0)," +
                        "NguoiNhan TEXT," +
                        "LyDoXuat TEXT," +
                        "GhiChu TEXT," +

                        "FOREIGN KEY(MaSP) REFERENCES SanPham(MaSP) ON DELETE RESTRICT" +
                        ")"
        );

        insertAdmin(db);
    }


    private void insertAdmin(SQLiteDatabase db){

        db.execSQL(
                "INSERT INTO TaiKhoan VALUES(" +
                        "'0123456789'," +
                        "'admin'," +
                        "'123')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS XuatKho");
        db.execSQL("DROP TABLE IF EXISTS NhapKho");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS NhaCungCap");
        db.execSQL("DROP TABLE IF EXISTS LoaiSP");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");

        onCreate(db);
    }

    public void capNhatTonKhoNhap(int maSP,int soLuongNhap){

        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(
                "UPDATE SanPham " +
                        "SET SoLuongTon = SoLuongTon + ? " +
                        "WHERE MaSP = ?",
                new Object[]{
                        soLuongNhap,
                        maSP
                }
        );
    }

    public boolean themNhapKho(NhapKho nk){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("MaNhap", nk.getMaPN());
        values.put("MaSP", nk.getMaSP());
        values.put("SoLuongNhap", nk.getSoLuongNhap());
        values.put("DonGiaNhap",nk.getDonGiaNhap());
        values.put("NgayNhap", nk.getNgayNhap());
        values.put("GhiChu", nk.getGhiChu());

        db.beginTransaction();

        try {

            long kq = db.insert("NhapKho", null, values);

            if (kq > 0) {

                capNhatTonKhoNhap(
                        nk.getMaSP(),
                        nk.getSoLuongNhap());

                db.setTransactionSuccessful();
                return true;
            }

        } finally {
            db.endTransaction();
        }

        return false;
    }

    public Cursor docDuLieuNhapKho() {

        SQLiteDatabase db = getReadableDatabase();

        String sql =
                "SELECT nk.MaNhap, " +
                        "nk.MaSP, " +
                        "sp.TenSP, " +
                        "nk.SoLuongNhap, " +
                        "nk.NgayNhap, " +
                        "nk.DonGiaNhap, " +
                        "IFNULL(nk.GhiChu,'') " +
                        "FROM NhapKho nk " +
                        "INNER JOIN SanPham sp ON nk.MaSP = sp.MaSP ";

        return db.rawQuery(sql,null);
    }
}
