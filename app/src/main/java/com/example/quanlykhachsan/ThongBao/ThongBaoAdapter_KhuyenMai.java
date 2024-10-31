package com.example.quanlykhachsan.ThongBao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlykhachsan.KhuyenMai.KhuyenMai;
import com.example.quanlykhachsan.R;

import java.util.ArrayList;

public class ThongBaoAdapter_KhuyenMai extends BaseAdapter {
    private Context context;
    private ArrayList<KhuyenMai> khuyenMaiList;

    public ThongBaoAdapter_KhuyenMai(Context context, ArrayList<KhuyenMai> khuyenMaiList) {
        this.context = context;
        this.khuyenMaiList = khuyenMaiList;
    }

    @Override
    public int getCount() {
        return khuyenMaiList.size();
    }

    @Override
    public KhuyenMai getItem(int position) {
        return khuyenMaiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.customlistview_thongbao_khuyenmai, parent, false);
        }

        KhuyenMai khuyenMai = getItem(position);

        TextView triGiaTextView = convertView.findViewById(R.id.TenVC);
        TextView ngayTaoTextView = convertView.findViewById(R.id.NgayTaoVC);
        TextView ngayKetThucTextView = convertView.findViewById(R.id.ThoiGianKTVC);
        TextView voucherCodeTextView = convertView.findViewById(R.id.MaVC);
        ImageView imageView = convertView.findViewById(R.id.imgKhuyenMai);

        triGiaTextView.setText("Bạn vừa nhận được Voucher trị giá  " + khuyenMai.getTriGia() + "K");
        ngayTaoTextView.setText( khuyenMai.getNgayTao());
        ngayKetThucTextView.setText("Sử dụng trước ngày " + khuyenMai.getNgayKetThuc());
        voucherCodeTextView.setText("Mã voucher: " + khuyenMai.getVoucherCode());
        imageView.setImageResource(khuyenMai.getImageResource());

        return convertView;
    }
}