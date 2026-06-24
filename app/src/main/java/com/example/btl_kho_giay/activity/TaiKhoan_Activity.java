package com.example.btl_kho_giay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.database.TaiKhoanRepository;
import com.example.btl_kho_giay.model.TaiKhoan;
import com.google.android.material.textfield.TextInputEditText;

public class TaiKhoan_Activity extends AppCompatActivity {

    private EditText edtname, edtSdt;
    private TextInputEditText edtPassword, edtCfPassword;
    private Button btnDangKy, btnDangNhap;

    private TaiKhoanRepository taiKhoanRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        anhXa();

        taiKhoanRepository = new TaiKhoanRepository(this);

        btnDangKy.setOnClickListener(v -> xuLyDangKy());

        btnDangNhap.setOnClickListener(v -> {
            Intent intent = new Intent(TaiKhoan_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void anhXa() {
        edtname = findViewById(R.id.edtname);
        edtSdt = findViewById(R.id.edtSdt);
        edtPassword = findViewById(R.id.edtPassword);
        edtCfPassword = findViewById(R.id.edtCfPassword);

        btnDangKy = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }

    private void xuLyDangKy() {
        String tenDangNhap = edtname.getText().toString().trim();
        String soDienThoai = edtSdt.getText().toString().trim();
        String matKhau = edtPassword.getText().toString().trim();
        String xacNhanMatKhau = edtCfPassword.getText().toString().trim();

        if (tenDangNhap.isEmpty()) {
            edtname.setError("Vui lòng nhập tên người dùng");
            edtname.requestFocus();
            return;
        }

        if (soDienThoai.isEmpty()) {
            edtSdt.setError("Vui lòng nhập số điện thoại");
            edtSdt.requestFocus();
            return;
        }

        if (!isSoDienThoaiHopLe(soDienThoai)) {
            edtSdt.setError("Số điện thoại phải gồm 10 chữ số");
            edtSdt.requestFocus();
            return;
        }

        if (matKhau.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return;
        }

        if (matKhau.length() < 3) {
            edtPassword.setError("Mật khẩu phải từ 3 ký tự trở lên");
            edtPassword.requestFocus();
            return;
        }

        if (xacNhanMatKhau.isEmpty()) {
            edtCfPassword.setError("Vui lòng nhập lại mật khẩu");
            edtCfPassword.requestFocus();
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            edtCfPassword.setError("Mật khẩu xác nhận không khớp");
            edtCfPassword.requestFocus();
            return;
        }

        if (taiKhoanRepository.kiemTraTenDangNhapTonTai(tenDangNhap)) {
            edtname.setError("Tên người dùng đã tồn tại");
            edtname.requestFocus();
            return;
        }

        if (taiKhoanRepository.kiemTraSoDienThoaiTonTai(soDienThoai)) {
            edtSdt.setError("Số điện thoại đã được đăng ký");
            edtSdt.requestFocus();
            return;
        }

        TaiKhoan taiKhoan = new TaiKhoan(
                soDienThoai,
                tenDangNhap,
                matKhau
        );

        boolean ketQua = taiKhoanRepository.dangKyTaiKhoan(taiKhoan);

        if (ketQua) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(TaiKhoan_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isSoDienThoaiHopLe(String soDienThoai) {
        return soDienThoai.matches("^0\\d{9}$");
    }
}
