package com.example.btl_kho_giay.activity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.adapter.XuatKho_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.XuatKho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class XuatKho_Activity extends AppCompatActivity {
    private EditText edtSearch, edtMaXuat, edtSoLuongXuat, edtNgayXuat,
            edtNguoiNhan, edtDonGiaXuat, edtGhiChu;

    private Spinner spSanPham;

    private Button btnSearch, btnLamMoi, btnThem,
            btnDangXuat, btnTaiKhoan, btnTrangChu, btnCaiDat;

    private ListView lvXuatKho;

    private DatabaseHelper db;

    private ArrayList<XuatKho> dsXuat;
    private XuatKho_Adapter adapter;

    private ArrayList<SanPhamItem> dsSanPham;
    private ArrayAdapter<SanPhamItem> spinnerAdapter;

    private int maXuatDangSua = -1;
    private int maSPCuDangSua = -1;
    private int soLuongCuDangSua = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuat_kho);

        anhXa();
        khoiTao();
        xuLySuKien();

        loadSanPhamSpinner();
        loadDanhSachXuatKho("");
        lamMoiForm();
    }

    private void anhXa() {
        edtSearch = findViewById(R.id.edtSearch);
        edtMaXuat = findViewById(R.id.edtMaXuat);
        edtSoLuongXuat = findViewById(R.id.edtSoLuongXuat);
        edtNgayXuat = findViewById(R.id.edtNgayXuat);
        edtNguoiNhan = findViewById(R.id.edtNguoiNhan);
        edtDonGiaXuat = findViewById(R.id.edtDonGiaXuat);
        edtGhiChu = findViewById(R.id.edtGhiChu);

        spSanPham = findViewById(R.id.spSanPham);

        btnSearch = findViewById(R.id.btnSearch);
        btnLamMoi = findViewById(R.id.btnLamMoi_XK);
        btnThem = findViewById(R.id.btnThem_XK);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnTrangChu = findViewById(R.id.btnTrangChu);
        btnCaiDat = findViewById(R.id.btnCaiDat);

        lvXuatKho = findViewById(R.id.lvXuatKho);
    }

    private void khoiTao() {
        db = new DatabaseHelper(this);

        dsXuat = new ArrayList<>();
        dsSanPham = new ArrayList<>();

        adapter = new XuatKho_Adapter(
                this,
                dsXuat,
                R.layout.item_listview,
                new XuatKho_Adapter.OnXuatKhoActionListener() {
                    @Override
                    public void onSua(XuatKho xuatKho) {
                        duaDuLieuLenForm(xuatKho);
                    }

                    @Override
                    public void onXoa(XuatKho xuatKho) {
                        xoaPhieuXuat(xuatKho);
                    }
                }
        );

        lvXuatKho.setAdapter(adapter);

        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsSanPham
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSanPham.setAdapter(spinnerAdapter);
    }

    private void xuLySuKien() {
        edtNgayXuat.setOnClickListener(v -> showDatePicker());

        btnSearch.setOnClickListener(v -> {
            String tuKhoa = edtSearch.getText().toString().trim();
            loadDanhSachXuatKho(tuKhoa);
        });

        btnLamMoi.setOnClickListener(v -> lamMoiForm());

        btnThem.setOnClickListener(v -> xuLyLuuPhieuXuat());

        btnDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(XuatKho_Activity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btnTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(XuatKho_Activity.this, TrangChu_Activity.class);
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

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String ngay = String.format(
                            Locale.getDefault(),
                            "%02d/%02d/%04d",
                            dayOfMonth,
                            month + 1,
                            year
                    );
                    edtNgayXuat.setText(ngay);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void loadSanPhamSpinner() {
        dsSanPham.clear();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT MaSP, TenSP, SoLuongTon FROM SanPham ORDER BY TenSP ASC",
                null
        );

        while (cursor.moveToNext()) {
            int maSP = cursor.getInt(0);
            String tenSP = cursor.getString(1);
            int soLuongTon = cursor.getInt(2);

            dsSanPham.add(new SanPhamItem(maSP, tenSP, soLuongTon));
        }

        cursor.close();
        spinnerAdapter.notifyDataSetChanged();
    }

    private void loadDanhSachXuatKho(String tuKhoa) {
        dsXuat.clear();

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor;

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            cursor = database.rawQuery(
                    "SELECT xk.MaXuat, xk.MaSP, sp.TenSP, xk.SoLuongXuat, xk.NgayXuat, " +
                            "xk.DonGiaXuat, IFNULL(xk.NguoiNhan,''), IFNULL(xk.LyDoXuat,''), IFNULL(xk.GhiChu,'') " +
                            "FROM XuatKho xk " +
                            "INNER JOIN SanPham sp ON xk.MaSP = sp.MaSP " +
                            "ORDER BY xk.MaXuat DESC",
                    null
            );
        } else {
            String key = "%" + tuKhoa + "%";

            cursor = database.rawQuery(
                    "SELECT xk.MaXuat, xk.MaSP, sp.TenSP, xk.SoLuongXuat, xk.NgayXuat, " +
                            "xk.DonGiaXuat, IFNULL(xk.NguoiNhan,''), IFNULL(xk.LyDoXuat,''), IFNULL(xk.GhiChu,'') " +
                            "FROM XuatKho xk " +
                            "INNER JOIN SanPham sp ON xk.MaSP = sp.MaSP " +
                            "WHERE CAST(xk.MaXuat AS TEXT) LIKE ? " +
                            "OR sp.TenSP LIKE ? " +
                            "OR IFNULL(xk.NguoiNhan,'') LIKE ? " +
                            "ORDER BY xk.MaXuat DESC",
                    new String[]{key, key, key}
            );
        }

        while (cursor.moveToNext()) {
            int maXuat = cursor.getInt(0);
            int maSP = cursor.getInt(1);
            String tenSP = cursor.getString(2);
            int soLuongXuat = cursor.getInt(3);
            String ngayXuat = cursor.getString(4);
            double donGiaXuat = cursor.getDouble(5);
            String nguoiNhan = cursor.getString(6);
            String lyDoXuat = cursor.getString(7);
            String ghiChu = cursor.getString(8);

            dsXuat.add(new XuatKho(
                    maXuat,
                    maSP,
                    tenSP,
                    soLuongXuat,
                    ngayXuat,
                    donGiaXuat,
                    nguoiNhan,
                    lyDoXuat,
                    ghiChu
            ));
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void xuLyLuuPhieuXuat() {
        if (dsSanPham.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm để xuất kho", Toast.LENGTH_SHORT).show();
            return;
        }

        String maXuatText = edtMaXuat.getText().toString().trim();
        String soLuongText = edtSoLuongXuat.getText().toString().trim();
        String ngayXuat = edtNgayXuat.getText().toString().trim();
        String nguoiNhan = edtNguoiNhan.getText().toString().trim();
        String donGiaText = edtDonGiaXuat.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();

        if (maXuatText.isEmpty()) {
            Toast.makeText(this, "Không lấy được mã phiếu xuất", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soLuongText.isEmpty()) {
            edtSoLuongXuat.setError("Nhập số lượng xuất");
            return;
        }

        if (ngayXuat.isEmpty()) {
            edtNgayXuat.setError("Chọn ngày xuất");
            return;
        }

        if (!isNgayHopLe(ngayXuat)) {
            edtNgayXuat.setError("Ngày phải đúng định dạng dd/MM/yyyy");
            return;
        }

        if (donGiaText.isEmpty()) {
            edtDonGiaXuat.setError("Nhập đơn giá xuất");
            return;
        }

        int maXuat;
        int soLuongXuat;
        double donGiaXuat;

        try {
            maXuat = Integer.parseInt(maXuatText);
            soLuongXuat = Integer.parseInt(soLuongText);
            donGiaXuat = Double.parseDouble(donGiaText);
        } catch (Exception e) {
            Toast.makeText(this, "Dữ liệu số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soLuongXuat <= 0) {
            edtSoLuongXuat.setError("Số lượng xuất phải > 0");
            return;
        }

        if (donGiaXuat <= 0) {
            edtDonGiaXuat.setError("Đơn giá xuất phải > 0");
            return;
        }

        SanPhamItem sanPhamChon = (SanPhamItem) spSanPham.getSelectedItem();
        if (sanPhamChon == null) {
            Toast.makeText(this, "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        int maSP = sanPhamChon.maSP;

        SQLiteDatabase database = db.getWritableDatabase();
        boolean thanhCong = false;

        database.beginTransaction();

        try {
            if (maXuatDangSua == -1) {
                int tonHienTai = layTonKho(database, maSP);

                if (soLuongXuat > tonHienTai) {
                    Toast.makeText(this, "Số lượng tồn kho không đủ để xuất", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues values = new ContentValues();
                values.put("MaXuat", maXuat);
                values.put("MaSP", maSP);
                values.put("SoLuongXuat", soLuongXuat);
                values.put("NgayXuat", ngayXuat);
                values.put("DonGiaXuat", donGiaXuat);
                values.put("NguoiNhan", nguoiNhan);
                values.put("LyDoXuat", "");
                values.put("GhiChu", ghiChu);

                long result = database.insert("XuatKho", null, values);

                if (result == -1) {
                    Toast.makeText(this, "Thêm phiếu xuất thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.execSQL(
                        "UPDATE SanPham SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?",
                        new Object[]{soLuongXuat, maSP}
                );

                database.setTransactionSuccessful();
                thanhCong = true;

            } else {
                if (maSP == maSPCuDangSua) {
                    int tonHienTai = layTonKho(database, maSP);
                    int tonSauHoan = tonHienTai + soLuongCuDangSua;

                    if (soLuongXuat > tonSauHoan) {
                        Toast.makeText(this, "Số lượng tồn kho không đủ để cập nhật", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put("MaSP", maSP);
                    values.put("SoLuongXuat", soLuongXuat);
                    values.put("NgayXuat", ngayXuat);
                    values.put("DonGiaXuat", donGiaXuat);
                    values.put("NguoiNhan", nguoiNhan);
                    values.put("LyDoXuat", "");
                    values.put("GhiChu", ghiChu);

                    int result = database.update(
                            "XuatKho",
                            values,
                            "MaXuat=?",
                            new String[]{String.valueOf(maXuatDangSua)}
                    );

                    if (result <= 0) {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    database.execSQL(
                            "UPDATE SanPham " +
                                    "SET SoLuongTon = SoLuongTon + ? - ? " +
                                    "WHERE MaSP = ?",
                            new Object[]{soLuongCuDangSua, soLuongXuat, maSP}
                    );

                    database.setTransactionSuccessful();
                    thanhCong = true;

                } else {
                    int tonSanPhamMoi = layTonKho(database, maSP);

                    if (soLuongXuat > tonSanPhamMoi) {
                        Toast.makeText(this, "Số lượng tồn kho của sản phẩm mới không đủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put("MaSP", maSP);
                    values.put("SoLuongXuat", soLuongXuat);
                    values.put("NgayXuat", ngayXuat);
                    values.put("DonGiaXuat", donGiaXuat);
                    values.put("NguoiNhan", nguoiNhan);
                    values.put("LyDoXuat", "");
                    values.put("GhiChu", ghiChu);

                    int result = database.update(
                            "XuatKho",
                            values,
                            "MaXuat=?",
                            new String[]{String.valueOf(maXuatDangSua)}
                    );

                    if (result <= 0) {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    database.execSQL(
                            "UPDATE SanPham SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?",
                            new Object[]{soLuongCuDangSua, maSPCuDangSua}
                    );

                    database.execSQL(
                            "UPDATE SanPham SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?",
                            new Object[]{soLuongXuat, maSP}
                    );

                    database.setTransactionSuccessful();
                    thanhCong = true;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Có lỗi xảy ra khi lưu phiếu xuất", Toast.LENGTH_SHORT).show();
        } finally {
            database.endTransaction();
        }

        if (thanhCong) {
            Toast.makeText(
                    this,
                    maXuatDangSua == -1 ? "Thêm phiếu xuất thành công" : "Cập nhật phiếu xuất thành công",
                    Toast.LENGTH_SHORT
            ).show();

            loadSanPhamSpinner();
            loadDanhSachXuatKho(edtSearch.getText().toString().trim());
            lamMoiForm();
        }
    }

    private void xoaPhieuXuat(XuatKho xuatKho) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa phiếu xuất")
                .setMessage("Bạn có chắc muốn xóa phiếu xuất này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    SQLiteDatabase database = db.getWritableDatabase();
                    boolean thanhCong = false;

                    database.beginTransaction();

                    try {
                        int result = database.delete(
                                "XuatKho",
                                "MaXuat=?",
                                new String[]{String.valueOf(xuatKho.getMaXuat())}
                        );

                        if (result > 0) {
                            database.execSQL(
                                    "UPDATE SanPham SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?",
                                    new Object[]{xuatKho.getSoLuongXuat(), xuatKho.getMaSP()}
                            );

                            database.setTransactionSuccessful();
                            thanhCong = true;
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    } finally {
                        database.endTransaction();
                    }

                    if (thanhCong) {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                        if (maXuatDangSua == xuatKho.getMaXuat()) {
                            lamMoiForm();
                        }

                        loadSanPhamSpinner();
                        loadDanhSachXuatKho(edtSearch.getText().toString().trim());
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void duaDuLieuLenForm(XuatKho xuatKho) {
        maXuatDangSua = xuatKho.getMaXuat();
        maSPCuDangSua = xuatKho.getMaSP();
        soLuongCuDangSua = xuatKho.getSoLuongXuat();

        edtMaXuat.setText(String.valueOf(xuatKho.getMaXuat()));
        edtSoLuongXuat.setText(String.valueOf(xuatKho.getSoLuongXuat()));
        edtNgayXuat.setText(xuatKho.getNgayXuat());
        edtNguoiNhan.setText(xuatKho.getNguoiNhan());
        edtDonGiaXuat.setText(String.valueOf((long) xuatKho.getDonGiaXuat()));
        edtGhiChu.setText(xuatKho.getGhiChu());

        chonSanPhamTheoMa(xuatKho.getMaSP());

        btnThem.setText("Cập nhật phiếu xuất");
    }

    private void chonSanPhamTheoMa(int maSP) {
        for (int i = 0; i < dsSanPham.size(); i++) {
            if (dsSanPham.get(i).maSP == maSP) {
                spSanPham.setSelection(i);
                break;
            }
        }
    }

    private void lamMoiForm() {
        maXuatDangSua = -1;
        maSPCuDangSua = -1;
        soLuongCuDangSua = 0;

        edtSoLuongXuat.setText("");
        edtNgayXuat.setText("");
        edtNguoiNhan.setText("Cửa hàng A");
        edtDonGiaXuat.setText("");
        edtGhiChu.setText("");

        btnThem.setText("Thêm phiếu xuất");
        taoMaXuatTuDong();

        if (!dsSanPham.isEmpty()) {
            spSanPham.setSelection(0);
        }
    }

    private void taoMaXuatTuDong() {
        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT IFNULL(MAX(MaXuat), 0) + 1 FROM XuatKho",
                null
        );

        int maMoi = 1;

        if (cursor.moveToFirst()) {
            maMoi = cursor.getInt(0);
        }

        cursor.close();

        edtMaXuat.setText(String.valueOf(maMoi));
    }

    private int layTonKho(SQLiteDatabase database, int maSP) {
        Cursor cursor = database.rawQuery(
                "SELECT SoLuongTon FROM SanPham WHERE MaSP=?",
                new String[]{String.valueOf(maSP)}
        );

        int ton = 0;

        if (cursor.moveToFirst()) {
            ton = cursor.getInt(0);
        }

        cursor.close();
        return ton;
    }

    private boolean isNgayHopLe(String ngay) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);
            return sdf.parse(ngay) != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static class SanPhamItem {
        int maSP;
        String tenSP;
        int soLuongTon;

        public SanPhamItem(int maSP, String tenSP, int soLuongTon) {
            this.maSP = maSP;
            this.tenSP = tenSP;
            this.soLuongTon = soLuongTon;
        }

        @Override
        public String toString() {
            return tenSP + " (Tồn: " + soLuongTon + ")";
        }
    }
}
