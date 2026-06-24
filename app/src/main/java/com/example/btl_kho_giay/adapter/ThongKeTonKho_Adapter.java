package com.example.btl_kho_giay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.btl_kho_giay.R;
import com.example.btl_kho_giay.model.ThongKeTonKho;

import java.util.ArrayList;

public class ThongKeTonKho_Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<ThongKeTonKho> dsTonKho;
    private int layout;

    public ThongKeTonKho_Adapter(Context context,
                                 ArrayList<ThongKeTonKho> dsTonKho,
                                 int layout) {
        this.context = context;
        this.dsTonKho = dsTonKho;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return dsTonKho.size();
    }

    @Override
    public Object getItem(int position) {
        return dsTonKho.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dsTonKho.get(position).getMaSP();
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

        ThongKeTonKho tk = dsTonKho.get(position);

        holder.txtMa.setText("SP" + tk.getMaSP());
        holder.txtTen.setText(tk.getTenSP());

        String thongTin = "Size: " + tk.getSize() + " | Tồn kho: " + tk.getSoLuongTon();
        holder.txtThongTin.setText(thongTin);

        // Màn thống kê chỉ xem, không sửa/xóa
        holder.btnSua.setVisibility(View.GONE);
        holder.btnXoa.setVisibility(View.GONE);

        return convertView;
    }
}
