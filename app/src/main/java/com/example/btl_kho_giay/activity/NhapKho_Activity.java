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
import com.example.btl_kho_giay.adapter.NhapKho_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.NhapKho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NhapKho_Activity extends AppCompatActivity {
    private EditText edtSearch, edtMaNhap, edtSoLuongNhap, edtDonGiaNhap,
            edtNgayNhap, edtGhiChu;

    private Spinner spSanPham;

    private Button btnSearch, btnLamMoi, btnThem,
            btnDangXuat, btnTaiKhoan, btnTrangChu, btnCaiDat;

    private ListView lvNhapKho;

    private DatabaseHelper db;

    private ArrayList<NhapKho> dsNhap;
    private NhapKho_Adapter adapter;

    private ArrayList<SanPhamItem> dsSanPham;
    private ArrayAdapter<SanPhamItem> spinnerAdapter;

    private int maNhapDangSua = -1;
    private int maSPCuDangSua = -1;
    private int soLuongCuDangSua = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nhap_kho);

        anhXa();
        khoiTao();
        xuLySuKien();

        loadSanPhamSpinner();
        loadDanhSachNhapKho("");
        lamMoiForm();
    }

    private void anhXa() {
        edtSearch = findViewById(R.id.edtSearch);
        edtMaNhap = findViewById(R.id.edtMaNhap);
        edtSoLuongNhap = findViewById(R.id.edtSoLuongNhap);
        edtDonGiaNhap = findViewById(R.id.edtDonGiaNhap);
        edtNgayNhap = findViewById(R.id.edtNgayNhap);
        edtGhiChu = findViewById(R.id.edtGhiChu);

        spSanPham = findViewById(R.id.spSanPham);

        btnSearch = findViewById(R.id.btnSearch);
        btnLamMoi = findViewById(R.id.btnLamMoi_NK);
        btnThem = findViewById(R.id.btnThem_NK);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnTrangChu = findViewById(R.id.btnTrangChu);
        btnCaiDat = findViewById(R.id.btnCaiDat);

        lvNhapKho = findViewById(R.id.lvNhapKho);
    }

    private void khoiTao() {
        db = new DatabaseHelper(this);

        dsNhap = new ArrayList<>();
        dsSanPham = new ArrayList<>();

        adapter = new NhapKho_Adapter(
                this,
                dsNhap,
                R.layout.item_listview,
                new NhapKho_Adapter.OnNhapKhoActionListener() {
                    @Override
                    public void onSua(NhapKho nhapKho) {
                        duaDuLieuLenForm(nhapKho);
                    }

                    @Override
                    public void onXoa(NhapKho nhapKho) {
                        xoaPhieuNhap(nhapKho);
                    }
                }
        );

        lvNhapKho.setAdapter(adapter);

        spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                dsSanPham
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSanPham.setAdapter(spinnerAdapter);
    }

    private void xuLySuKien() {
        edtNgayNhap.setOnClickListener(v -> showDatePicker());

        btnSearch.setOnClickListener(v -> {
            String tuKhoa = edtSearch.getText().toString().trim();
            loadDanhSachNhapKho(tuKhoa);
        });

        btnLamMoi.setOnClickListener(v -> lamMoiForm());

        btnThem.setOnClickListener(v -> xuLyLuuPhieuNhap());

        btnDangXuat.setOnClickListener(v -> {
            Intent intent = new Intent(NhapKho_Activity.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btnTrangChu.setOnClickListener(v -> {
            Intent intent = new Intent(NhapKho_Activity.this, TrangChu_Activity.class);
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
                    edtNgayNhap.setText(ngay);
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

    private void loadDanhSachNhapKho(String tuKhoa) {
        dsNhap.clear();

        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor;

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            cursor = database.rawQuery(
                    "SELECT nk.MaNhap, nk.MaSP, sp.TenSP, nk.SoLuongNhap, nk.DonGiaNhap, nk.NgayNhap, IFNULL(nk.GhiChu,'') " +
                            "FROM NhapKho nk " +
                            "INNER JOIN SanPham sp ON nk.MaSP = sp.MaSP " +
                            "ORDER BY nk.MaNhap DESC",
                    null
            );
        } else {
            String key = "%" + tuKhoa + "%";

            cursor = database.rawQuery(
                    "SELECT nk.MaNhap, nk.MaSP, sp.TenSP, nk.SoLuongNhap, nk.DonGiaNhap, nk.NgayNhap, IFNULL(nk.GhiChu,'') " +
                            "FROM NhapKho nk " +
                            "INNER JOIN SanPham sp ON nk.MaSP = sp.MaSP " +
                            "WHERE CAST(nk.MaNhap AS TEXT) LIKE ? " +
                            "OR sp.TenSP LIKE ? " +
                            "OR IFNULL(nk.GhiChu,'') LIKE ? " +
                            "ORDER BY nk.MaNhap DESC",
                    new String[]{key, key, key}
            );
        }

        while (cursor.moveToNext()) {
            int maNhap = cursor.getInt(0);
            int maSP = cursor.getInt(1);
            String tenSP = cursor.getString(2);
            int soLuongNhap = cursor.getInt(3);
            double donGiaNhap = cursor.getDouble(4);
            String ngayNhap = cursor.getString(5);
            String ghiChu = cursor.getString(6);

            dsNhap.add(new NhapKho(
                    maNhap,
                    maSP,
                    tenSP,
                    soLuongNhap,
                    donGiaNhap,
                    ngayNhap,
                    ghiChu
            ));
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void xuLyLuuPhieuNhap() {
        if (dsSanPham.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm để nhập kho", Toast.LENGTH_SHORT).show();
            return;
        }

        String maNhapText = edtMaNhap.getText().toString().trim();
        String soLuongText = edtSoLuongNhap.getText().toString().trim();
        String donGiaText = edtDonGiaNhap.getText().toString().trim();
        String ngayNhap = edtNgayNhap.getText().toString().trim();
        String ghiChu = edtGhiChu.getText().toString().trim();

        if (maNhapText.isEmpty()) {
            edtMaNhap.setError("Nhập mã phiếu");
            return;
        }

        if (soLuongText.isEmpty()) {
            edtSoLuongNhap.setError("Nhập số lượng");
            return;
        }

        if (donGiaText.isEmpty()) {
            edtDonGiaNhap.setError("Nhập đơn giá");
            return;
        }

        if (ngayNhap.isEmpty()) {
            edtNgayNhap.setError("Nhập ngày nhập");
            return;
        }

        if (!isNgayHopLe(ngayNhap)) {
            edtNgayNhap.setError("Ngày phải đúng định dạng dd/MM/yyyy");
            return;
        }

        int maNhap;
        int soLuongNhap;
        double donGiaNhap;

        try {
            maNhap = Integer.parseInt(maNhapText);
            soLuongNhap = Integer.parseInt(soLuongText);
            donGiaNhap = Double.parseDouble(donGiaText);
        } catch (Exception e) {
            Toast.makeText(this, "Dữ liệu số không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soLuongNhap <= 0) {
            edtSoLuongNhap.setError("Số lượng nhập phải > 0");
            return;
        }

        if (donGiaNhap <= 0) {
            edtDonGiaNhap.setError("Đơn giá nhập phải > 0");
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
            if (maNhapDangSua == -1) {
                ContentValues values = new ContentValues();
                values.put("MaNhap", maNhap);
                values.put("MaSP", maSP);
                values.put("SoLuongNhap", soLuongNhap);
                values.put("DonGiaNhap", donGiaNhap);
                values.put("NgayNhap", ngayNhap);
                values.put("GhiChu", ghiChu);

                long result = database.insert("NhapKho", null, values);

                if (result == -1) {
                    Toast.makeText(this, "Mã phiếu nhập đã tồn tại hoặc dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.execSQL(
                        "UPDATE SanPham SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?",
                        new Object[]{soLuongNhap, maSP}
                );

                database.setTransactionSuccessful();
                thanhCong = true;

            } else {
                if (maSP == maSPCuDangSua) {
                    int tonHienTai = layTonKho(database, maSP);
                    int tonSauCapNhat = tonHienTai - soLuongCuDangSua + soLuongNhap;

                    if (tonSauCapNhat < 0) {
                        Toast.makeText(this, "Không thể cập nhật vì sẽ làm tồn kho âm", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put("MaSP", maSP);
                    values.put("SoLuongNhap", soLuongNhap);
                    values.put("DonGiaNhap", donGiaNhap);
                    values.put("NgayNhap", ngayNhap);
                    values.put("GhiChu", ghiChu);

                    int result = database.update(
                            "NhapKho",
                            values,
                            "MaNhap=?",
                            new String[]{String.valueOf(maNhapDangSua)}
                    );

                    if (result <= 0) {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    database.execSQL(
                            "UPDATE SanPham " +
                                    "SET SoLuongTon = SoLuongTon - ? + ? " +
                                    "WHERE MaSP = ?",
                            new Object[]{soLuongCuDangSua, soLuongNhap, maSP}
                    );

                    database.setTransactionSuccessful();
                    thanhCong = true;

                } else {
                    int tonSPCu = layTonKho(database, maSPCuDangSua);

                    if (tonSPCu - soLuongCuDangSua < 0) {
                        Toast.makeText(this, "Không thể đổi sản phẩm vì tồn kho sản phẩm cũ sẽ bị âm", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ContentValues values = new ContentValues();
                    values.put("MaSP", maSP);
                    values.put("SoLuongNhap", soLuongNhap);
                    values.put("DonGiaNhap", donGiaNhap);
                    values.put("NgayNhap", ngayNhap);
                    values.put("GhiChu", ghiChu);

                    int result = database.update(
                            "NhapKho",
                            values,
                            "MaNhap=?",
                            new String[]{String.valueOf(maNhapDangSua)}
                    );

                    if (result <= 0) {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    database.execSQL(
                            "UPDATE SanPham SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?",
                            new Object[]{soLuongCuDangSua, maSPCuDangSua}
                    );

                    database.execSQL(
                            "UPDATE SanPham SET SoLuongTon = SoLuongTon + ? WHERE MaSP = ?",
                            new Object[]{soLuongNhap, maSP}
                    );

                    database.setTransactionSuccessful();
                    thanhCong = true;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Có lỗi xảy ra khi lưu phiếu nhập", Toast.LENGTH_SHORT).show();
        } finally {
            database.endTransaction();
        }

        if (thanhCong) {
            Toast.makeText(
                    this,
                    maNhapDangSua == -1 ? "Thêm phiếu nhập thành công" : "Cập nhật phiếu nhập thành công",
                    Toast.LENGTH_SHORT
            ).show();

            loadSanPhamSpinner();
            loadDanhSachNhapKho(edtSearch.getText().toString().trim());
            lamMoiForm();
        }
    }

    private void xoaPhieuNhap(NhapKho nhapKho) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa phiếu nhập")
                .setMessage("Bạn có chắc muốn xóa phiếu nhập này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    SQLiteDatabase database = db.getWritableDatabase();
                    boolean thanhCong = false;

                    database.beginTransaction();

                    try {
                        int tonHienTai = layTonKho(database, nhapKho.getMaSP());

                        if (tonHienTai - nhapKho.getSoLuongNhap() < 0) {
                            Toast.makeText(this, "Không thể xóa vì sẽ làm tồn kho âm", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int result = database.delete(
                                "NhapKho",
                                "MaNhap=?",
                                new String[]{String.valueOf(nhapKho.getMaPN())}
                        );

                        if (result > 0) {
                            database.execSQL(
                                    "UPDATE SanPham SET SoLuongTon = SoLuongTon - ? WHERE MaSP = ?",
                                    new Object[]{nhapKho.getSoLuongNhap(), nhapKho.getMaSP()}
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

                        if (maNhapDangSua == nhapKho.getMaPN()) {
                            lamMoiForm();
                        }

                        loadSanPhamSpinner();
                        loadDanhSachNhapKho(edtSearch.getText().toString().trim());
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void duaDuLieuLenForm(NhapKho nhapKho) {
        maNhapDangSua = nhapKho.getMaPN();
        maSPCuDangSua = nhapKho.getMaSP();
        soLuongCuDangSua = nhapKho.getSoLuongNhap();

        edtMaNhap.setText(String.valueOf(nhapKho.getMaPN()));
        edtMaNhap.setEnabled(false);

        edtSoLuongNhap.setText(String.valueOf(nhapKho.getSoLuongNhap()));
        edtDonGiaNhap.setText(String.valueOf((long) nhapKho.getDonGiaNhap()));
        edtNgayNhap.setText(nhapKho.getNgayNhap());
        edtGhiChu.setText(nhapKho.getGhiChu());

        chonSanPhamTheoMa(nhapKho.getMaSP());

        btnThem.setText("Cập nhật phiếu nhập");
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
        maNhapDangSua = -1;
        maSPCuDangSua = -1;
        soLuongCuDangSua = 0;

        edtMaNhap.setText("");
        edtMaNhap.setEnabled(true);
        edtSoLuongNhap.setText("");
        edtDonGiaNhap.setText("");
        edtNgayNhap.setText("");
        edtGhiChu.setText("");

        btnThem.setText("Thêm phiếu nhập");

        if (!dsSanPham.isEmpty()) {
            spSanPham.setSelection(0);
        }
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
