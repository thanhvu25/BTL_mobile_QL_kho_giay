package com.example.btl_kho_giay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.model.NhapKho;

import java.util.ArrayList;
import java.util.Locale;

public class NhapKho_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<NhapKho> dsNhap;
    private int layout;
    private OnNhapKhoActionListener listener;

    public interface OnNhapKhoActionListener {
        void onSua(NhapKho nhapKho);
        void onXoa(NhapKho nhapKho);
    }

    public NhapKho_Adapter(Context context,
                           ArrayList<NhapKho> dsNhap,
                           int layout,
                           OnNhapKhoActionListener listener) {
        this.context = context;
        this.dsNhap = dsNhap;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dsNhap.size();
    }

    @Override
    public Object getItem(int position) {
        return dsNhap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsNhap.get(position).getMaPN();
    }

    static class ViewHolder {
        TextView txtMa;
        TextView txtTen;
        TextView txtThongTin;
        Button btnSua;
        Button btnXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater
                    .from(context)
                    .inflate(layout, parent, false);

            holder.txtMa = convertView.findViewById(R.id.txtMa);
            holder.txtTen = convertView.findViewById(R.id.txtTen);
            holder.txtThongTin = convertView.findViewById(R.id.txtThongTin);
            holder.btnSua = convertView.findViewById(R.id.btnSua);
            holder.btnXoa = convertView.findViewById(R.id.btnXoa);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NhapKho nk = dsNhap.get(position);

        holder.txtMa.setText("PN" + nk.getMaPN());
        holder.txtTen.setText("| SP: " + nk.getTenSP());

        String thongTin =
                "SL nhập: " + nk.getSoLuongNhap()
                        + " | Ngày: " + nk.getNgayNhap()
                        + "\nĐơn giá: " + String.format(Locale.getDefault(), "%,.0f", nk.getDonGiaNhap());

        if (nk.getGhiChu() != null && !nk.getGhiChu().trim().isEmpty()) {
            thongTin += "\nGhi chú: " + nk.getGhiChu();
        }

        holder.txtThongTin.setText(thongTin);

        holder.btnSua.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSua(nk);
            }
        });

        holder.btnXoa.setOnClickListener(v -> {
            if (listener != null) {
                listener.onXoa(nk);
            }
        });

        return convertView;
    }
}
