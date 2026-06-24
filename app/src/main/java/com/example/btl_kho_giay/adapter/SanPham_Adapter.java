package com.example.btl_kho_giay.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.model.SanPham;

import java.util.ArrayList;

public class SanPham_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<SanPham> dsSP;
    private int layout;

    private OnSanPhamActionListener listener;

    public interface OnSanPhamActionListener{
        void onSua(SanPham sp);
        void onXoa(SanPham sp);
    }

    public SanPham_Adapter(Context context,
                           ArrayList<SanPham> dsSP,
                           int layout,
                           OnSanPhamActionListener listener) {

        this.context = context;
        this.dsSP = dsSP;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dsSP.size();
    }

    @Override
    public Object getItem(int position) {
        return dsSP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsSP.get(position).getMaSP();
    }

    static class ViewHolder{
        ImageView imgAnh;
        TextView txtMa;
        TextView txtTen;
        TextView txtThongTin;
        Button btnSua;
        Button btnXoa;
    }

    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            holder = new ViewHolder();

            convertView = LayoutInflater
                    .from(context)
                    .inflate(layout,parent,false);

            holder.imgAnh = convertView.findViewById(R.id.imgAnh);
            holder.txtMa = convertView.findViewById(R.id.txtMa);
            holder.txtTen = convertView.findViewById(R.id.txtTen);
            holder.txtThongTin = convertView.findViewById(R.id.txtThongTin);
            holder.btnSua = convertView.findViewById(R.id.btnSua);
            holder.btnXoa = convertView.findViewById(R.id.btnXoa);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        SanPham sp = dsSP.get(position);

        holder.txtMa.setText(
                "Mã: " + sp.getMaSP()
        );

        holder.txtTen.setText(
                "Tên: " + sp.getTenSP()
        );

        holder.txtThongTin.setText(
                "Loại: " + sp.getTenLoai()
                        + "\nNCC: " + sp.getTenNCC()
                        + "\nSize: " + sp.getSize()
                        + "\nGiá nhập: " + String.format("%,.0f", sp.getGiaNhap())
                        + "\nTồn kho: " + sp.getSoLuongTon()
        );

        if(sp.getHinhAnh() != null
                && !sp.getHinhAnh().isEmpty()){

            try{

                holder.imgAnh.setImageURI(
                        Uri.parse(
                                sp.getHinhAnh()
                        )
                );

            }catch (Exception e){

                holder.imgAnh.setImageResource(
                        R.drawable.ic_launcher_background
                );
            }

        }else{

            holder.imgAnh.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        holder.btnSua.setOnClickListener(v -> {

            if(listener != null){
                listener.onSua(sp);
            }

        });

        holder.btnXoa.setOnClickListener(v -> {

            if(listener != null){
                listener.onXoa(sp);
            }

        });

        return convertView;
    }
}