package com.example.btl_kho_giay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.model.NhaCungCap;

import java.util.ArrayList;

public class NhaCungCap_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<NhaCungCap> dsNCC;
    private int layout;

    private OnNCCActionListener listener;

    public interface OnNCCActionListener{
        void onSua(NhaCungCap ncc);
        void onXoa(NhaCungCap ncc);
    }

    public NhaCungCap_Adapter(Context context,
                              ArrayList<NhaCungCap> dsNCC,
                              int layout,
                              OnNCCActionListener listener) {

        this.context = context;
        this.dsNCC = dsNCC;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dsNCC.size();
    }

    @Override
    public Object getItem(int position) {
        return dsNCC.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsNCC.get(position).getMaNCC();
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

            convertView = LayoutInflater
                    .from(context)
                    .inflate(layout,parent,false);

            holder.txtMa = convertView.findViewById(R.id.txtMa);
            holder.txtTen = convertView.findViewById(R.id.txtTen);
            holder.txtThongTin = convertView.findViewById(R.id.txtThongTin);
            holder.btnSua = convertView.findViewById(R.id.btnSua);
            holder.btnXoa = convertView.findViewById(R.id.btnXoa);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        NhaCungCap ncc = dsNCC.get(position);

        holder.txtMa.setText("Mã: " + ncc.getMaNCC());

        holder.txtTen.setText("Tên: " + ncc.getTenNCC()
        );

        holder.txtThongTin.setText(
                "SĐT: " + ncc.getSoDienThoai()
                        + "\nEmail: " + ncc.getEmail()
                        + "\nĐịa chỉ: " + ncc.getDiaChi()
        );

        holder.btnSua.setOnClickListener(v -> {
            if(listener != null){
                listener.onSua(ncc);
            }
        });

        holder.btnXoa.setOnClickListener(v -> {
            if(listener != null){
                listener.onXoa(ncc);
            }
        });

        return convertView;
    }
}