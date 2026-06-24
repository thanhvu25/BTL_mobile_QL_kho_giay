package com.example.btl_kho_giay.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.adapter.NhaCungCap_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.NhaCungCap;

import java.util.ArrayList;

public class NhaCungCap_Activity extends AppCompatActivity {

    EditText edtMaNCC;
    EditText edtTenNCC;
    EditText edtSoDienThoai;
    EditText edtEmail;
    EditText edtDiaChi;
    EditText edtSearch;

    Button btnThem;
    Button btnLamMoi;
    Button btnSearch, btnDangXuat, btnTrangchu;

    ListView lvNhaCungCap;

    DatabaseHelper db;

    ArrayList<NhaCungCap> dsNCC;

    NhaCungCap_Adapter adapter;

    private int maNCCDangSua = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nha_cung_cap);

        anhXa();

        db = new DatabaseHelper(this);

        dsNCC = new ArrayList<>();

        adapter = new NhaCungCap_Adapter(this,dsNCC,R.layout.item_listview,new NhaCungCap_Adapter.OnNCCActionListener() {

            @Override
            public void onSua(NhaCungCap ncc) {

                maNCCDangSua = ncc.getMaNCC();

                edtMaNCC.setText(
                        String.valueOf(
                                ncc.getMaNCC()
                        )
                );

                edtMaNCC.setEnabled(false);

                edtTenNCC.setText(
                        ncc.getTenNCC()
                );

                edtSoDienThoai.setText(
                        ncc.getSoDienThoai()
                );

                edtEmail.setText(
                        ncc.getEmail()
                );

                edtDiaChi.setText(
                        ncc.getDiaChi()
                );

                btnThem.setText(
                        "Cập nhật"
                );
            }

            @Override
            public void onXoa(NhaCungCap ncc) {

                new AlertDialog.Builder(
                        NhaCungCap_Activity.this
                )
                        .setTitle(
                                "Xóa nhà cung cấp"
                        )
                        .setMessage(
                                "Bạn có chắc muốn xóa?"
                        )
                        .setPositiveButton(
                                "Có",
                                (dialog, which) -> {

                                    SQLiteDatabase database =
                                            db.getWritableDatabase();

                                    try{

                                        int result = database.delete(
                                                "NhaCungCap",
                                                "MaNCC=?",
                                                new String[]{
                                                        String.valueOf(ncc.getMaNCC())
                                                });

                                        if(result > 0){
                                            Toast.makeText(
                                                    NhaCungCap_Activity.this,
                                                    "Xóa thành công",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            loadNCC();
                                        }

                                    }catch (Exception e){

                                        Toast.makeText(
                                                NhaCungCap_Activity.this,
                                                "Nhà cung cấp đang được sử dụng",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                }
                        )
                        .setNegativeButton(
                                "Không",
                                null
                        )
                        .show();
                }
            }
        );

        btnDangXuat.setOnClickListener(v -> {

            Toast.makeText(this, "Quay về Trang chủ để đăng xuất", Toast.LENGTH_SHORT).show();
        });

        btnTrangchu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            NhaCungCap_Activity.this,
                            TrangChu_Activity.class
                    );

            startActivity(intent);

            finish();
        });

        lvNhaCungCap.setAdapter(adapter);

        loadNCC();

        btnThem.setOnClickListener(v -> {

            String maNCCText =
                    edtMaNCC.getText()
                            .toString()
                            .trim();

            String tenNCC =
                    edtTenNCC.getText()
                            .toString()
                            .trim();

            String sdt =
                    edtSoDienThoai.getText()
                            .toString()
                            .trim();

            String email =
                    edtEmail.getText()
                            .toString()
                            .trim();

            String diaChi =
                    edtDiaChi.getText()
                            .toString()
                            .trim();

            if(maNCCText.isEmpty()){

                edtMaNCC.setError(
                        "Nhập mã nhà cung cấp"
                );

                return;
            }

            if(tenNCC.isEmpty()){

                edtTenNCC.setError(
                        "Nhập tên nhà cung cấp"
                );

                return;
            }

            if(!email.isEmpty()
                    && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                edtEmail.setError(
                        "Email không hợp lệ"
                );

                return;
            }

            if(sdt.isEmpty()){
                edtSoDienThoai.setError("Nhập số điện thoại");
                return;
            }

            if(!Patterns.PHONE.matcher(sdt).matches()
                    || sdt.length() < 10){
                edtSoDienThoai.setError("Số điện thoại không hợp lệ");
                return;
            }

            SQLiteDatabase database =
                    db.getWritableDatabase();

            try{

                if(maNCCDangSua == -1){

                    ContentValues values =
                            new ContentValues();

                    try{
                        Integer.parseInt(maNCCText);
                    }catch (NumberFormatException e){
                        edtMaNCC.setError("Mã NCC phải là số");
                        return;
                    }

                    values.put(
                            "MaNCC",
                            Integer.parseInt(maNCCText)
                    );

                    values.put(
                            "TenNCC",
                            tenNCC
                    );

                    values.put(
                            "SoDienThoai",
                            sdt
                    );

                    values.put(
                            "Email",
                            email
                    );

                    values.put(
                            "DiaChi",
                            diaChi
                    );

                    long result =
                            database.insert(
                                    "NhaCungCap",
                                    null,
                                    values
                            );

                    if(result == -1){

                        Toast.makeText(
                                this,
                                "Mã NCC hoặc tên NCC đã tồn tại",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    Toast.makeText(
                            this,
                            "Thêm thành công",
                            Toast.LENGTH_SHORT
                    ).show();

                }else{

                    ContentValues values =
                            new ContentValues();

                    values.put(
                            "TenNCC",
                            tenNCC
                    );

                    values.put(
                            "SoDienThoai",
                            sdt
                    );

                    values.put(
                            "Email",
                            email
                    );

                    values.put(
                            "DiaChi",
                            diaChi
                    );

                    int result =
                            database.update(
                                    "NhaCungCap",
                                    values,
                                    "MaNCC=?",
                                    new String[]{
                                            String.valueOf(
                                                    maNCCDangSua
                                            )
                                    }
                            );

                    if(result > 0){

                        Toast.makeText(
                                this,
                                "Cập nhật thành công",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    maNCCDangSua = -1;

                    btnThem.setText(
                            "Thêm NCC"
                    );
                }

                clearForm();

                loadNCC();

            }catch (Exception e){

                Toast.makeText(
                        this,
                        "Dữ liệu đã tồn tại",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnLamMoi.setOnClickListener(v -> {

            clearForm();

            loadNCC();

            btnThem.setText(
                    "Thêm NCC"
            );

            maNCCDangSua = -1;
        });

        btnSearch.setOnClickListener(v -> {

            String key =
                    edtSearch.getText()
                            .toString()
                            .trim();

            if(key.isEmpty()){
                loadNCC();
                return;
            }

            dsNCC.clear();

            SQLiteDatabase database =
                    db.getReadableDatabase();

            Cursor cursor =
                    database.rawQuery(
                            "SELECT * FROM NhaCungCap " +
                                    "WHERE CAST(MaNCC AS TEXT) LIKE ? " +
                                    "OR TenNCC LIKE ?",
                            new String[]{
                                    "%" + key + "%",
                                    "%" + key + "%"
                            }
                    );

            while(cursor.moveToNext()){

                dsNCC.add(
                        new NhaCungCap(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        )
                );
            }

            cursor.close();

            adapter.notifyDataSetChanged();
        });
    }

    private void anhXa(){

        edtMaNCC = findViewById(R.id.edtMaNCC);
        edtTenNCC = findViewById(R.id.edtTenNCC);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtEmail = findViewById(R.id.edtEmail);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtSearch = findViewById(R.id.edtSearch);

        btnThem = findViewById(R.id.btnThem_NCC);
        btnLamMoi = findViewById(R.id.btnLamMoi_NCC);
        btnSearch = findViewById(R.id.btnSearch);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTrangchu = findViewById(R.id.btnTrangChu);


        lvNhaCungCap =
                findViewById(R.id.lvNhaCungCap);
    }

    private void loadNCC(){

        dsNCC.clear();

        SQLiteDatabase database =
                db.getReadableDatabase();

        Cursor cursor =
                database.rawQuery(
                        "SELECT * FROM NhaCungCap ORDER BY MaNCC DESC",
                        null
                );

        while(cursor.moveToNext()){

            dsNCC.add(
                    new NhaCungCap(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    )
            );
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void clearForm(){

        edtMaNCC.setText("");
        edtTenNCC.setText("");
        edtSoDienThoai.setText("");
        edtEmail.setText("");
        edtDiaChi.setText("");

        edtMaNCC.setEnabled(true);
    }
}