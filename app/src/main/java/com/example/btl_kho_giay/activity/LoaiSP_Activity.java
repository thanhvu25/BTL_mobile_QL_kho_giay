package com.example.btl_kho_giay.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.MainActivity;
import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.adapter.LoaiSP_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.LoaiSP;

import java.util.ArrayList;

public class LoaiSP_Activity extends AppCompatActivity {

    EditText edtSearch,edtMaLoai,edtTenLoai,edtMoTa;

    Button btnThem,btnDangXuat, btnTrangchu;

    ListView lvLoai;

    DatabaseHelper db;

    ArrayList<LoaiSP> dsLoai;

    LoaiSP_Adapter adapter;

    private int maLoaiDangSua = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loai_hang);

        edtMaLoai = findViewById(R.id.edtMaLoai);
        edtTenLoai = findViewById(R.id.edtTenLoai);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnThem = findViewById(R.id.btnThem_LH);
        lvLoai = findViewById(R.id.lvLoaiSanPham);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTrangchu = findViewById(R.id.btnTrangChu);

        db = new DatabaseHelper(this);

        dsLoai = new ArrayList<>();

        adapter = new LoaiSP_Adapter(this,dsLoai,R.layout.item_listview,new LoaiSP_Adapter.OnLoaiSPActionListener() {

            @Override
            public void onSua(LoaiSP loaiSP) {

                maLoaiDangSua = loaiSP.getMaLoai();
                edtMaLoai.setText(String.valueOf(loaiSP.getMaLoai()));
                edtMaLoai.setEnabled(false);

                edtTenLoai.setText(loaiSP.getTenLoai());
                edtMoTa.setText(loaiSP.getMoTa());

                btnThem.setText("Cập nhật");
            }

            @Override
            public void onXoa(LoaiSP loaiSP) {

                new AlertDialog.Builder(LoaiSP_Activity.this)
                .setTitle("Xóa loại sản phẩm")
                .setMessage("Bạn có chắc muốn xóa?")
                .setPositiveButton("Có",(dialog, which) -> {

                    SQLiteDatabase database = db.getWritableDatabase();

                    int result = database.delete("LoaiSP","MaLoai=?",new String[]{String.valueOf(loaiSP.getMaLoai())});

                    if(result > 0){

                        Toast.makeText(LoaiSP_Activity.this,"Xóa thành công",Toast.LENGTH_SHORT).show();

                        loadLoaiSP();

                    }else{

                        Toast.makeText(
                                LoaiSP_Activity.this,
                                "Xóa thất bại",
                                Toast.LENGTH_SHORT
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
        });

        btnDangXuat.setOnClickListener(v -> {

            Toast.makeText(this, "Quay về Trang chủ để đăng xuất", Toast.LENGTH_SHORT).show();
        });

        btnTrangchu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            LoaiSP_Activity.this,
                            TrangChu_Activity.class
                    );

            startActivity(intent);

            finish();
        });

        lvLoai.setAdapter(adapter);

        loadLoaiSP();

        // Thêm
        btnThem.setOnClickListener(v -> {

            String maLoaiText =
                    edtMaLoai.getText()
                            .toString()
                            .trim();

            String tenLoai =
                    edtTenLoai.getText()
                            .toString()
                            .trim();

            String moTa =
                    edtMoTa.getText()
                            .toString()
                            .trim();

            if(maLoaiText.isEmpty()){

                edtMaLoai.setError(
                        "Nhập mã loại"
                );

                return;
            }

            if(tenLoai.isEmpty()){

                edtTenLoai.setError(
                        "Nhập tên loại sản phẩm"
                );

                return;
            }

            SQLiteDatabase database =
                    db.getWritableDatabase();

            try{

                if(maLoaiDangSua == -1){

                    ContentValues values =
                            new ContentValues();

                    values.put(
                            "MaLoai",
                            Integer.parseInt(maLoaiText)
                    );

                    values.put(
                            "TenLoai",
                            tenLoai
                    );

                    values.put(
                            "MoTa",
                            moTa
                    );

                    long result =
                            database.insert(
                                    "LoaiSP",
                                    null,
                                    values
                            );

                    if(result == -1){

                        Toast.makeText(
                                this,
                                "Mã loại hoặc tên loại đã tồn tại",
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
                            "TenLoai",
                            tenLoai
                    );

                    values.put(
                            "MoTa",
                            moTa
                    );

                    int result =
                            database.update(
                                    "LoaiSP",
                                    values,
                                    "MaLoai=?",
                                    new String[]{
                                            String.valueOf(
                                                    maLoaiDangSua
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

                    maLoaiDangSua = -1;

                    btnThem.setText(
                            "Thêm"
                    );
                }

                edtMaLoai.setText("");
                edtTenLoai.setText("");
                edtMoTa.setText("");

                edtMaLoai.setEnabled(true);

                loadLoaiSP();

            }catch (Exception e){

                Toast.makeText(
                        this,
                        "Mã loại hoặc tên loại đã tồn tại",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadLoaiSP(){

        dsLoai.clear();

        SQLiteDatabase database =
                db.getReadableDatabase();

        Cursor cursor =
                database.rawQuery(
                        "SELECT * FROM LoaiSP ORDER BY MaLoai DESC",
                        null
                );

        while(cursor.moveToNext()){

            int maLoai = cursor.getInt(0);
            String tenLoai = cursor.getString(1);
            String moTa = cursor.getString(2);

            dsLoai.add(
                    new LoaiSP(
                            maLoai,
                            tenLoai,
                            moTa
                    )
            );
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }
}
