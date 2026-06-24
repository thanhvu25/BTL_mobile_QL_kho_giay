package com.example.btl_kho_giay;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_kho_giay.activity.TaiKhoan_Activity;
import com.example.btl_kho_giay.activity.TrangChu_Activity;
import com.example.btl_kho_giay.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    EditText edtUser, edtPass;
    Button btnLogin;
    TextView btnDangKy;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnDangKy = findViewById(R.id.btnDangKy);

        // Khởi tạo database
        db = new DatabaseHelper(this);

        // Chuyển sang màn hình đăng ký
        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TaiKhoan_Activity.class);
            startActivity(intent);
        });

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(v -> {

            String user = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(
                        this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            SQLiteDatabase database = db.getReadableDatabase();

            Cursor cursor = database.rawQuery(
                    "SELECT * FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=?",
                    new String[]{
                            user,
                            pass
                    }
            );

            if (cursor.moveToFirst()) {
                Intent intent = new Intent(MainActivity.this, TrangChu_Activity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(
                        MainActivity.this,
                        "Sai tài khoản hoặc mật khẩu",
                        Toast.LENGTH_SHORT
                ).show();
            }

            cursor.close();
        });
    }
}
