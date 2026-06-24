package com.example.btl_kho_giay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.btl_kho_giay.model.NhapKho;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QLKhoGiay.db";

    // Tăng version để DB tạo lại sau khi thêm dữ liệu mẫu
    private static final int DATABASE_VERSION = 9;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        insertSampleData(db);
    }

    private void insertAdmin(SQLiteDatabase db) {
        db.execSQL(
                "INSERT INTO TaiKhoan VALUES(" +
                        "'0123456789'," +
                        "'admin'," +
                        "'123')"
        );
    }


    // ========= Sample ===============================
    private void insertSampleData(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            // ===== Loại sản phẩm =====
            db.execSQL("INSERT INTO LoaiSP(MaLoai, TenLoai, MoTa) VALUES (1, 'Giày chạy bộ', 'Giày dành cho chạy bộ')");
            db.execSQL("INSERT INTO LoaiSP(MaLoai, TenLoai, MoTa) VALUES (2, 'Giày bóng rổ', 'Giày dành cho bóng rổ')");
            db.execSQL("INSERT INTO LoaiSP(MaLoai, TenLoai, MoTa) VALUES (3, 'Giày sneaker', 'Giày thời trang hằng ngày')");

            // ===== Nhà cung cấp =====
            db.execSQL("INSERT INTO NhaCungCap(MaNCC, TenNCC, SoDienThoai, Email, DiaChi) VALUES (1, 'Nike Việt Nam', '0901234567', 'nike@gmail.com', 'Hà Nội')");
            db.execSQL("INSERT INTO NhaCungCap(MaNCC, TenNCC, SoDienThoai, Email, DiaChi) VALUES (2, 'Adidas Việt Nam', '0912345678', 'adidas@gmail.com', 'TP.HCM')");
            db.execSQL("INSERT INTO NhaCungCap(MaNCC, TenNCC, SoDienThoai, Email, DiaChi) VALUES (3, 'Puma Việt Nam', '0923456789', 'puma@gmail.com', 'Đà Nẵng')");

            // ===== Sản phẩm =====
            // Ban đầu để SoLuongTon = 0, sau đó cộng/trừ theo phiếu nhập/xuất
            db.execSQL("INSERT INTO SanPham(MaSP, TenSP, MaLoai, MaNCC, Size, GiaNhap, SoLuongTon, HinhAnh) VALUES (101, 'Nike Air Zoom', 1, 1, '42', 2200000, 0, '')");
            db.execSQL("INSERT INTO SanPham(MaSP, TenSP, MaLoai, MaNCC, Size, GiaNhap, SoLuongTon, HinhAnh) VALUES (102, 'Adidas Ultraboost', 1, 2, '41', 2500000, 0, '')");
            db.execSQL("INSERT INTO SanPham(MaSP, TenSP, MaLoai, MaNCC, Size, GiaNhap, SoLuongTon, HinhAnh) VALUES (103, 'Puma RS-X', 3, 3, '40', 1800000, 0, '')");
            db.execSQL("INSERT INTO SanPham(MaSP, TenSP, MaLoai, MaNCC, Size, GiaNhap, SoLuongTon, HinhAnh) VALUES (104, 'Nike Jordan One', 2, 1, '43', 3200000, 0, '')");

            // ===== Phiếu nhập kho =====
            db.execSQL("INSERT INTO NhapKho(MaNhap, MaSP, SoLuongNhap, DonGiaNhap, NgayNhap, GhiChu) VALUES (1, 101, 20, 2200000, '01/06/2026', 'Nhập đợt 1')");
            db.execSQL("INSERT INTO NhapKho(MaNhap, MaSP, SoLuongNhap, DonGiaNhap, NgayNhap, GhiChu) VALUES (2, 102, 15, 2500000, '02/06/2026', 'Nhập đợt 1')");
            db.execSQL("INSERT INTO NhapKho(MaNhap, MaSP, SoLuongNhap, DonGiaNhap, NgayNhap, GhiChu) VALUES (3, 103, 30, 1800000, '03/06/2026', 'Nhập đợt 1')");
            db.execSQL("INSERT INTO NhapKho(MaNhap, MaSP, SoLuongNhap, DonGiaNhap, NgayNhap, GhiChu) VALUES (4, 104, 12, 3200000, '04/06/2026', 'Nhập đợt 1')");

            // Cộng tồn kho sau nhập
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon + 20 WHERE MaSP = 101");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon + 15 WHERE MaSP = 102");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon + 30 WHERE MaSP = 103");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon + 12 WHERE MaSP = 104");

            // ===== Phiếu xuất kho =====
            db.execSQL("INSERT INTO XuatKho(MaXuat, MaSP, SoLuongXuat, NgayXuat, DonGiaXuat, NguoiNhan, LyDoXuat, GhiChu) VALUES (1, 101, 2, '05/06/2026', 2600000, 'Cửa hàng A', 'Bán hàng', 'Xuất bán')");
            db.execSQL("INSERT INTO XuatKho(MaXuat, MaSP, SoLuongXuat, NgayXuat, DonGiaXuat, NguoiNhan, LyDoXuat, GhiChu) VALUES (2, 102, 7, '06/06/2026', 2900000, 'Cửa hàng B', 'Bán hàng', 'Xuất bán')");
            db.execSQL("INSERT INTO XuatKho(MaXuat, MaSP, SoLuongXuat, NgayXuat, DonGiaXuat, NguoiNhan, LyDoXuat, GhiChu) VALUES (3, 103, 5, '07/06/2026', 2100000, 'Cửa hàng C', 'Bán hàng', 'Xuất bán')");
            db.execSQL("INSERT INTO XuatKho(MaXuat, MaSP, SoLuongXuat, NgayXuat, DonGiaXuat, NguoiNhan, LyDoXuat, GhiChu) VALUES (4, 104, 2, '08/06/2026', 3500000, 'Cửa hàng D', 'Bán hàng', 'Xuất bán')");

            // Trừ tồn kho sau xuất
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon - 2 WHERE MaSP = 101");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon - 7 WHERE MaSP = 102");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon - 5 WHERE MaSP = 103");
            db.execSQL("UPDATE SanPham SET SoLuongTon = SoLuongTon - 2 WHERE MaSP = 104");

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS XuatKho");
        db.execSQL("DROP TABLE IF EXISTS NhapKho");
        db.execSQL("DROP TABLE IF EXISTS SanPham");
        db.execSQL("DROP TABLE IF EXISTS NhaCungCap");
        db.execSQL("DROP TABLE IF EXISTS LoaiSP");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");

        onCreate(db);
    }

    public void capNhatTonKhoNhap(int maSP, int soLuongNhap) {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(
                "UPDATE SanPham SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?",
                new Object[]{soLuongNhap, maSP}
        );
    }

    public boolean themNhapKho(NhapKho nk) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("MaNhap", nk.getMaPN());
        values.put("MaSP", nk.getMaSP());
        values.put("SoLuongNhap", nk.getSoLuongNhap());
        values.put("DonGiaNhap", nk.getDonGiaNhap());
        values.put("NgayNhap", nk.getNgayNhap());
        values.put("GhiChu", nk.getGhiChu());

        db.beginTransaction();

        try {
            long kq = db.insert("NhapKho", null, values);

            if (kq > 0) {
                capNhatTonKhoNhap(nk.getMaSP(), nk.getSoLuongNhap());
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

        return db.rawQuery(sql, null);
    }
}
