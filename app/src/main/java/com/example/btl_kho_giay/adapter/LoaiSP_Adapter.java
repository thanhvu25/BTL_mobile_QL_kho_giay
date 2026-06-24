package com.example.btl_kho_giay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.model.LoaiSP;

import java.util.ArrayList;

public class LoaiSP_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<LoaiSP> dsLoai;
    private int layout;

    private OnLoaiSPActionListener listener;

    public interface OnLoaiSPActionListener{
        void onSua(LoaiSP loaiSP);
        void onXoa(LoaiSP loaiSP);
    }

    public LoaiSP_Adapter(Context context,
                          ArrayList<LoaiSP> dsLoai,
                          int layout,
                          OnLoaiSPActionListener listener) {

        this.context = context;
        this.dsLoai = dsLoai;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dsLoai.size();
    }

    @Override
    public Object getItem(int position) {
        return dsLoai.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsLoai.get(position).getMaLoai();
    }

    static class ViewHolder{

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

            convertView =
                    LayoutInflater
                            .from(context)
                            .inflate(
                                    layout,
                                    parent,
                                    false
                            );

            holder.txtMa =
                    convertView.findViewById(R.id.txtMa);

            holder.txtTen =
                    convertView.findViewById(R.id.txtTen);

            holder.txtThongTin =
                    convertView.findViewById(R.id.txtThongTin);

            holder.btnSua =
                    convertView.findViewById(R.id.btnSua);

            holder.btnXoa =
                    convertView.findViewById(R.id.btnXoa);

            convertView.setTag(holder);

        }else{

            holder =
                    (ViewHolder) convertView.getTag();
        }

        LoaiSP loai =
                dsLoai.get(position);

        holder.txtMa.setText(
                loai.getMaLoai() + " - "
        );

        holder.txtTen.setText(
                loai.getTenLoai()
        );

        holder.txtThongTin.setText(
                loai.getMoTa() == null
                        ? ""
                        : loai.getMoTa()
        );

        holder.btnSua.setOnClickListener(v ->
                listener.onSua(loai));

        holder.btnXoa.setOnClickListener(v ->
                listener.onXoa(loai));

        return convertView;
    }
}

