package com.example.btl_kho_giay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;

public class TrangChu_Activity extends AppCompatActivity {

    LinearLayout btnLoaiSP;
    LinearLayout btnSanPham;
    LinearLayout btnNCC;
    LinearLayout btnNhapKho;
    LinearLayout btnXuatKho;
    LinearLayout btnThongKe;

    Button btnDangXuat;
    Button btnTaiKhoan;
    Button btnTrangChu;
    Button btnCaiDat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_chu);

        btnLoaiSP = findViewById(R.id.btnLoaiSP);
        btnNCC = findViewById(R.id.btnNCC);
        btnSanPham = findViewById(R.id.btnSanPham);
        btnNhapKho = findViewById(R.id.btnNhapKho);
        btnXuatKho = findViewById(R.id.btnXuatKho);
        btnThongKe = findViewById(R.id.btnThongKe);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTaiKhoan = findViewById(R.id.btnTaiKhoan);
        btnTrangChu = findViewById(R.id.btnTrangChu);
        btnCaiDat = findViewById(R.id.btnCaiDat);

        btnLoaiSP.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                LoaiSP_Activity.class
                        )
                ));

        btnNCC.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                NhaCungCap_Activity.class
                        )
                ));

        btnSanPham.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                SanPham_Activity.class
                        )
                ));

        btnNhapKho.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                NhapKho_Activity.class
                        )
                ));

        btnXuatKho.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                XuatKho_Activity.class
                        )
                ));

        btnThongKe.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                TrangChu_Activity.this,
                                ThongKeTonKho_Activity.class
                        )
                ));

        btnDangXuat.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            TrangChu_Activity.this,
                            MainActivity.class
                    );

            startActivity(intent);

            finish();
        });

    }
}
