package com.example.btl_kho_giay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.btl_kho_giay.model.TaiKhoan;

public class TaiKhoanRepository {

    private final DatabaseHelper dbHelper;

    public TaiKhoanRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean kiemTraSoDienThoaiTonTai(String soDienThoai) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM TaiKhoan WHERE SoDienThoai = ? LIMIT 1",
                new String[]{soDienThoai}
        );

        boolean tonTai = cursor.moveToFirst();
        cursor.close();
        return tonTai;
    }

    public boolean kiemTraTenDangNhapTonTai(String tenDangNhap) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM TaiKhoan WHERE TenDangNhap = ? LIMIT 1",
                new String[]{tenDangNhap}
        );

        boolean tonTai = cursor.moveToFirst();
        cursor.close();
        return tonTai;
    }

    public boolean dangKyTaiKhoan(TaiKhoan taiKhoan) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SoDienThoai", taiKhoan.getSoDienThoai());
        values.put("TenDangNhap", taiKhoan.getTenDangNhap());
        values.put("MatKhau", taiKhoan.getMatKhau());

        long result = db.insert("TaiKhoan", null, values);
        return result != -1;
    }
}
