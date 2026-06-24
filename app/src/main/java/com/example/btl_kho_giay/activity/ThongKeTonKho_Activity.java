package com.example.btl_kho_giay.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.adapter.ThongKeTonKho_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.ThongKeTonKho;

import java.util.ArrayList;

public class ThongKeTonKho_Activity extends AppCompatActivity {

    private static final int NGUONG_SAP_HET_HANG = 10;
    private static final int MODE_TON_KHO_HIEN_TAI = 1;
    private static final int MODE_SAP_HET_HANG = 2;

    private EditText edtSearch;
    private Button btnSearch, btnTonKho, btnSapHetHang;
    private Button btnDangXuat, btnTaiKhoan, btnTrangChu, btnCaiDat;
    private TextView txtTongSanPham, txtTongTonKho, txtSapHetHang;
    private ListView lvThongKeTonKho;

    private DatabaseHelper db;
    private ArrayList<ThongKeTonKho> dsTonKho;
    private ThongKeTonKho_Adapter adapter;

    private int currentMode = MODE_TON_KHO_HIEN_TAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_ke);

        anhXa();
        khoiTao();
        xuLySuKien();

        capNhatTongQuan();
        loadDuLieuTonKho("", currentMode);
    }

    private void anhXa() {
        edtSearch = findViewById(R.id.edtSearch);

        btnSearch = findViewById(R.id.btnSearch);
        btnTonKho = findViewById(R.id.btnTonKho);
        btnSapHetHang = findViewById(R.id.btnSapHetHang);

        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnTrangChu = findViewById(R.id.btnTrangChu);
        btnCaiDat = findViewById(R.id.btnCaiDat);

        txtTongSanPham = findViewById(R.id.txtTongSanPham);
        txtTongTonKho = findViewById(R.id.txtTongTonKho);
        txtSapHetHang = findViewById(R.id.txtSapHetHang);

        lvThongKeTonKho = findViewById(R.id.lvThongKeTonKho);
    }

    private void khoiTao() {
        db = new DatabaseHelper(this);
        dsTonKho = new ArrayList<>();
        adapter = new ThongKeTonKho_Adapter(this, dsTonKho, R.layout.item_listview);
        lvThongKeTonKho.setAdapter(adapter);
    }

    private void xuLySuKien() {
        btnSearch.setOnClickListener(v -> {
            String tuKhoa = edtSearch.getText().toString().trim();
            loadDuLieuTonKho(tuKhoa, currentMode);
        });

        btnTonKho.setOnClickListener(v -> {
            currentMode = MODE_TON_KHO_HIEN_TAI;
            loadDuLieuTonKho(edtSearch.getText().toString().trim(), currentMode);
        });

        btnSapHetHang.setOnClickListener(v -> {
            currentMode = MODE_SAP_HET_HANG;
            loadDuLieuTonKho(edtSearch.getText().toString().trim(), currentMode);
        });

        btnDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(ThongKeTonKho_Activity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btnTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(ThongKeTonKho_Activity.this, TrangChu_Activity.class);
            startActivity(intent);
            finish();
        });

        btnTaiKhoan.setOnClickListener(v ->
                Toast.makeText(this, "Chưa kết nối màn hình Tài khoản", Toast.LENGTH_SHORT).show()
        );

        btnCaiDat.setOnClickListener(v ->
                Toast.makeText(this, "Chưa kết nối màn hình Cài đặt", Toast.LENGTH_SHORT).show()
        );
    }

    private void capNhatTongQuan() {
        SQLiteDatabase database = db.getReadableDatabase();

        int tongSanPham = 0;
        int tongTonKho = 0;
        int tongSapHetHang = 0;

        Cursor c1 = database.rawQuery(
                "SELECT COUNT(*) FROM SanPham",
                null
        );
        if (c1.moveToFirst()) {
            tongSanPham = c1.getInt(0);
        }
        c1.close();

        Cursor c2 = database.rawQuery(
                "SELECT IFNULL(SUM(SoLuongTon), 0) FROM SanPham",
                null
        );
        if (c2.moveToFirst()) {
            tongTonKho = c2.getInt(0);
        }
        c2.close();

        Cursor c3 = database.rawQuery(
                "SELECT COUNT(*) FROM SanPham WHERE SoLuongTon <= ?",
                new String[]{String.valueOf(NGUONG_SAP_HET_HANG)}
        );
        if (c3.moveToFirst()) {
            tongSapHetHang = c3.getInt(0);
        }
        c3.close();

        txtTongSanPham.setText("Tổng sản phẩm: " + tongSanPham);
        txtTongTonKho.setText("Tổng tồn kho: " + tongTonKho);
        txtSapHetHang.setText("Sản phẩm sắp hết hàng: " + tongSapHetHang);
    }

    private void loadDuLieuTonKho(String tuKhoa, int mode) {
        dsTonKho.clear();

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor;

        String key = "%" + tuKhoa + "%";

        if (mode == MODE_SAP_HET_HANG) {
            cursor = database.rawQuery(
                    "SELECT MaSP, TenSP, Size, SoLuongTon " +
                            "FROM SanPham " +
                            "WHERE SoLuongTon <= ? " +
                            "AND (CAST(MaSP AS TEXT) LIKE ? OR TenSP LIKE ?) " +
                            "ORDER BY SoLuongTon ASC, MaSP DESC",
                    new String[]{
                            String.valueOf(NGUONG_SAP_HET_HANG),
                            key,
                            key
                    }
            );
        } else {
            cursor = database.rawQuery(
                    "SELECT MaSP, TenSP, Size, SoLuongTon " +
                            "FROM SanPham " +
                            "WHERE SoLuongTon > 0 " +
                            "AND (CAST(MaSP AS TEXT) LIKE ? OR TenSP LIKE ?) " +
                            "ORDER BY SoLuongTon DESC, MaSP DESC",
                    new String[]{
                            key,
                            key
                    }
            );
        }

        while (cursor.moveToNext()) {
            int maSP = cursor.getInt(0);
            String tenSP = cursor.getString(1);
            String size = cursor.getString(2);
            int soLuongTon = cursor.getInt(3);

            dsTonKho.add(new ThongKeTonKho(
                    maSP,
                    tenSP,
                    size,
                    soLuongTon
            ));
        }

        cursor.close();
        adapter.notifyDataSetChanged();

        if (dsTonKho.isEmpty()) {
            if (mode == MODE_SAP_HET_HANG) {
                Toast.makeText(this, "Không có sản phẩm nào sắp hết hàng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Không tìm thấy dữ liệu tồn kho phù hợp", Toast.LENGTH_SHORT).show();
            }
        }

        capNhatTongQuan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capNhatTongQuan();
        loadDuLieuTonKho(edtSearch.getText().toString().trim(), currentMode);
    }
}
