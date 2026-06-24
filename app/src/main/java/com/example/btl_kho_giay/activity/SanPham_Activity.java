package com.example.btl_kho_giay.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.adapter.NhaCungCap_Adapter;
import com.example.btl_kho_giay.adapter.SanPham_Adapter;
import com.example.btl_kho_giay.database.DatabaseHelper;
import com.example.btl_kho_giay.model.LoaiSP;
import com.example.btl_kho_giay.model.NhaCungCap;
import com.example.btl_kho_giay.model.SanPham;

import java.util.ArrayList;

public class SanPham_Activity extends AppCompatActivity {

    EditText edtSearch,edtMaSP,edtTenSP,edtGiaNhap,edtSoLuongTon;
    Spinner spLoai,spNCC;
    Spinner spSize;
    Button btnSearch,btnThem_SP, btnDangXuat, btnTrangchu;
    Button btnChonAnh;
    ListView lvSanPham;
    DatabaseHelper db;
    ArrayList<SanPham> dsSP;
    ArrayList<LoaiSP> dsLoai;
    ArrayList<NhaCungCap> dsNCC;
    SanPham_Adapter adapter; int maSPDangSua = -1;
    String selectedImageUri = "";
    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.san_pham);

        anhXa();

        db = new DatabaseHelper(this);

        dsSP = new ArrayList<>();
        dsLoai = new ArrayList<>();
        dsNCC = new ArrayList<>();

        loadLoaiSP();
        loadNCC();
        loadSize();

        adapter = new SanPham_Adapter(this,dsSP,R.layout.item_listview_sp,new SanPham_Adapter.OnSanPhamActionListener() {

            @Override
            public void onSua(SanPham sp) {

                maSPDangSua = sp.getMaSP();

                edtMaSP.setText(String.valueOf(sp.getMaSP()));
                edtTenSP.setText(sp.getTenSP());
                edtGiaNhap.setText(String.valueOf(sp.getGiaNhap()));
                edtSoLuongTon.setText(String.valueOf(sp.getSoLuongTon()));

                edtMaSP.setEnabled(false);
                edtSoLuongTon.setEnabled(false);

                selectedImageUri = sp.getHinhAnh();

                btnThem_SP.setText("Cập nhật");


                chonSpinnerLoai(sp.getMaLoai());
                chonSpinnerNCC(sp.getMaNCC());
                chonSpinnerSize(sp.getSize());
            }

            @Override
            public void onXoa(SanPham sp) {

                new AlertDialog.Builder(SanPham_Activity.this)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc muốn xóa?")
                        .setPositiveButton("Có", (dialog, which) -> {

                            try {

                                SQLiteDatabase database = db.getWritableDatabase();

                                int result =
                                        database.delete(
                                                "SanPham",
                                                "MaSP=?",
                                                new String[]{
                                                        String.valueOf(sp.getMaSP())
                                                });

                                if(result > 0){

                                    Toast.makeText(
                                            SanPham_Activity.this,
                                            "Xóa thành công",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    loadSanPham();
                                }

                            }catch (Exception e){

                                Toast.makeText(
                                        SanPham_Activity.this,
                                        "Sản phẩm đang được sử dụng",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        btnDangXuat.setOnClickListener(v -> {

            Toast.makeText(this, "Quay về Trang chủ để đăng xuất", Toast.LENGTH_SHORT).show();
        });

        btnTrangchu.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            SanPham_Activity.this,
                            TrangChu_Activity.class
                    );

            startActivity(intent);

            finish();
        });

        lvSanPham.setAdapter(adapter);

        loadSanPham();

        pickImageLauncher = registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {

                            if(result.getResultCode() == RESULT_OK
                                    && result.getData() != null){

                                Uri uri =
                                        result.getData().getData();

                                if(uri != null){
                                    selectedImageUri =
                                            uri.toString();
                                }
                            }
                        });

        btnChonAnh.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);

            intent.setType("image/*");

            pickImageLauncher.launch(intent);
        });

        // Btn thêm
        btnThem_SP.setOnClickListener(v -> {

            if(maSPDangSua == -1){
                themSP();
            }else{
                capNhatSP();
            }
        });

        btnSearch.setOnClickListener(v -> timKiem());


    }

    private void anhXa(){

        edtSearch = findViewById(R.id.edtSearch);
        edtMaSP = findViewById(R.id.edtMaSP);
        edtTenSP = findViewById(R.id.edtTenSP);
        edtGiaNhap = findViewById(R.id.edtGiaNhap);
        edtSoLuongTon = findViewById(R.id.edtSoLuongTon);
        spLoai = findViewById(R.id.spLoai);
        spNCC = findViewById(R.id.spNCC);
        spSize = findViewById(R.id.spSize);
        btnSearch = findViewById(R.id.btnSearch);
        btnThem_SP = findViewById(R.id.btnThem_SP);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnTrangchu = findViewById(R.id.btnTrangChu);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        lvSanPham = findViewById(R.id.lvSanPham);
    }

    private void loadSanPham() {

        dsSP.clear();

        SQLiteDatabase database = db.getReadableDatabase();

        String sql =
                "SELECT SP.MaSP, " +
                        "SP.TenSP, " +
                        "SP.MaLoai, " +
                        "SP.MaNCC, " +
                        "L.TenLoai, " +
                        "N.TenNCC, " +
                        "SP.Size, " +
                        "SP.GiaNhap, " +
                        "SP.SoLuongTon, " +
                        "SP.HinhAnh " +
                        "FROM SanPham SP " +
                        "INNER JOIN LoaiSP L ON SP.MaLoai = L.MaLoai " +
                        "INNER JOIN NhaCungCap N ON SP.MaNCC = N.MaNCC " +
                        "ORDER BY SP.MaSP DESC";

        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            dsSP.add(
                    new SanPham(
                            cursor.getInt(0),      // MaSP
                            cursor.getString(1),   // TenSP
                            cursor.getInt(2),      // MaLoai
                            cursor.getString(4),   // TenLoai
                            cursor.getString(5),   // TenNCC
                            cursor.getDouble(7),   // GiaNhap
                            cursor.getString(6),   // Size
                            cursor.getInt(3),      // MaNCC
                            cursor.getInt(8),      // SoLuongTon
                            cursor.getString(9)    // HinhAnh
                    )
            );
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void loadLoaiSP(){

        dsLoai.clear();

        SQLiteDatabase database =
                db.getReadableDatabase();

        Cursor cursor =
                database.rawQuery(
                        "SELECT * FROM LoaiSP ORDER BY TenLoai",
                        null
                );

        while(cursor.moveToNext()){

            dsLoai.add(
                    new LoaiSP(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2)
                    )
            );
        }

        cursor.close();

        ArrayAdapter<LoaiSP> adapterLoai =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        dsLoai
                );

        adapterLoai.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spLoai.setAdapter(adapterLoai);
    }

    private void loadNCC(){

        dsNCC.clear();

        SQLiteDatabase database =
                db.getReadableDatabase();

        Cursor cursor =
                database.rawQuery(
                        "SELECT * FROM NhaCungCap ORDER BY TenNCC",
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

        ArrayAdapter<NhaCungCap> adapterNCC =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        dsNCC
                );

        adapterNCC.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spNCC.setAdapter(adapterNCC);
    }

    private void themSP(){

        String maSP = edtMaSP.getText().toString().trim();
        String tenSP = edtTenSP.getText().toString().trim();

        String giaNhap = edtGiaNhap.getText().toString().trim();
        double gia;
        try{
            gia = Double.parseDouble(giaNhap);
        }catch (Exception e){
            edtGiaNhap.setError("Giá nhập không hợp lệ");
            return;
        }

        String soLuongTon = edtSoLuongTon.getText().toString().trim();

        if(maSP.isEmpty()){
            edtMaSP.setError("Nhập mã sản phẩm");
            return;
        }

        if(tenSP.isEmpty()){
            edtTenSP.setError("Nhập tên sản phẩm");
            return;
        }

        if(giaNhap.isEmpty()){
            edtGiaNhap.setError("Nhập giá nhập");
            return;
        }
        try{

            if(gia <= 0){
                edtGiaNhap.setError("Giá nhập phải lớn hơn 0");
                return;
            }

        }catch (Exception e){
            edtGiaNhap.setError("Giá nhập không hợp lệ");
            return;
        }

        LoaiSP loai =
                (LoaiSP) spLoai.getSelectedItem();

        NhaCungCap ncc =
                (NhaCungCap) spNCC.getSelectedItem();

        String size =
                spSize.getSelectedItem().toString();

        ContentValues values =
                new ContentValues();

        values.put(
                "MaSP",
                Integer.parseInt(maSP)
        );

        values.put(
                "TenSP",
                tenSP
        );

        values.put(
                "MaLoai",
                loai.getMaLoai()
        );

        values.put(
                "MaNCC",
                ncc.getMaNCC()
        );

        values.put(
                "Size",
                size
        );

        values.put(
                "GiaNhap",gia
        );

        values.put("SoLuongTon", 0);

        values.put(
                "HinhAnh",
                selectedImageUri
        );

        try{

            SQLiteDatabase database =
                    db.getWritableDatabase();

            long result =
                    database.insert(
                            "SanPham",
                            null,
                            values
                    );

            if(result == -1){

                Toast.makeText(
                        this,
                        "Mã sản phẩm đã tồn tại",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "Thêm thành công",
                    Toast.LENGTH_SHORT
            ).show();

            clearForm();
            loadSanPham();

        }catch (Exception e){

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void capNhatSP(){

        String tenSP =
                edtTenSP.getText().toString().trim();

        String giaNhap =
                edtGiaNhap.getText().toString().trim();

        LoaiSP loai =
                (LoaiSP) spLoai.getSelectedItem();

        NhaCungCap ncc =
                (NhaCungCap) spNCC.getSelectedItem();

        String size =
                spSize.getSelectedItem().toString();

        ContentValues values =
                new ContentValues();

        values.put(
                "TenSP",
                tenSP
        );

        values.put(
                "MaLoai",
                loai.getMaLoai()
        );

        values.put(
                "MaNCC",
                ncc.getMaNCC()
        );

        values.put(
                "Size",
                size
        );

        values.put(
                "GiaNhap",
                Double.parseDouble(giaNhap)
        );

        values.put(
                "HinhAnh",
                selectedImageUri
        );

        SQLiteDatabase database =
                db.getWritableDatabase();

        int result =
                database.update(
                        "SanPham",
                        values,
                        "MaSP=?",
                        new String[]{
                                String.valueOf(
                                        maSPDangSua
                                )
                        }
                );

        if(result > 0){

            Toast.makeText(
                    this,
                    "Cập nhật thành công",
                    Toast.LENGTH_SHORT
            ).show();

            maSPDangSua = -1;

            btnThem_SP.setText(
                    "Thêm sản phẩm"
            );

            clearForm();

            loadSanPham();
        }
    }

    private void timKiem(){

        String key =
                edtSearch.getText()
                        .toString()
                        .trim();

        if(key.isEmpty()){

            loadSanPham();
            return;
        }

        dsSP.clear();

        SQLiteDatabase database =
                db.getReadableDatabase();

        String sql =
                "SELECT SP.MaSP, SP.TenSP, SP.MaLoai, SP.MaNCC, " +
                        "L.TenLoai, N.TenNCC, SP.Size, SP.GiaNhap, " +
                        "SP.SoLuongTon, SP.HinhAnh " +
                        "FROM SanPham SP " +
                        "INNER JOIN LoaiSP L ON SP.MaLoai=L.MaLoai " +
                        "INNER JOIN NhaCungCap N ON SP.MaNCC=N.MaNCC " +
                        "WHERE CAST(SP.MaSP AS TEXT) LIKE ? " +
                        "OR SP.TenSP LIKE ?";

        Cursor cursor =
                database.rawQuery(
                        sql,
                        new String[]{
                                "%" + key + "%",
                                "%" + key + "%"
                        }
                );

        while(cursor.moveToNext()){

            dsSP.add(
                    new SanPham(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getInt(2),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getDouble(7),
                            cursor.getString(6),
                            cursor.getInt(3),
                            cursor.getInt(8),
                            cursor.getString(9)
                    )
            );
        }

        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void chonSpinnerLoai(int maLoai){

        for(int i = 0; i < dsLoai.size(); i++){

            if(dsLoai.get(i).getMaLoai() == maLoai){

                spLoai.setSelection(i);
                break;
            }
        }
    }

    private void chonSpinnerNCC(int maNCC){

        for(int i = 0; i < dsNCC.size(); i++){

            if(dsNCC.get(i).getMaNCC() == maNCC){

                spNCC.setSelection(i);
                break;
            }
        }
    }

    private void chonSpinnerSize(String size){

        for(int i = 0; i < spSize.getCount(); i++){

            if(spSize.getItemAtPosition(i)
                    .toString()
                    .equals(size)){

                spSize.setSelection(i);
                break;
            }
        }
    }

    private void loadSize(){

        ArrayAdapter<CharSequence> adapterSize =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.size_giay,
                        android.R.layout.simple_spinner_item
                );

        adapterSize.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spSize.setAdapter(adapterSize);
    }

    private void clearForm(){

        edtMaSP.setText("");
        edtTenSP.setText("");
        edtGiaNhap.setText("");
        edtSoLuongTon.setText("");

        edtMaSP.setEnabled(true);
        edtSoLuongTon.setEnabled(true);

        selectedImageUri = "";

        maSPDangSua = -1;

        btnThem_SP.setText(
                "Thêm sản phẩm"
        );
    }

}