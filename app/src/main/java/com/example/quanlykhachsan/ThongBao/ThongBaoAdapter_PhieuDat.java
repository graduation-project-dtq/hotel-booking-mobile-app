package com.example.quanlykhachsan.ThongBao;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhachsan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ThongBaoAdapter_PhieuDat extends RecyclerView.Adapter<ThongBaoAdapter_PhieuDat.ViewHolder> {
    private Context context;
    private ArrayList<Model_ThongBao> phieuDatList;

    // Constructor
    public ThongBaoAdapter_PhieuDat(Context context, ArrayList<Model_ThongBao> phieuDatList) {
        this.context = context;
        this.phieuDatList = phieuDatList;
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView trangThaiTextView, noiDungTrangThaiTextView, thoiGianTrangThaiTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trangThaiTextView = itemView.findViewById(R.id.trangthai);
            noiDungTrangThaiTextView = itemView.findViewById(R.id.Noidungtrangthai);
            thoiGianTrangThaiTextView = itemView.findViewById(R.id.NgayXacNhantrangthai);
            imageView = itemView.findViewById(R.id.imgPhongPD);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customlistview_thongbao_datphong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_ThongBao thongBaoPhieuDat = phieuDatList.get(position);

        holder.trangThaiTextView.setText(thongBaoPhieuDat.getTitle());

        // Highlight booking ID
        String maPhieuDat = thongBaoPhieuDat.getId();
        SpannableString spannableString = new SpannableString("Phiếu đặt " + maPhieuDat + " " + thongBaoPhieuDat.getContent());
        spannableString.setSpan(
                new ForegroundColorSpan(Color.RED),
                "Phiếu đặt ".length(),
                "Phiếu đặt ".length() + maPhieuDat.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        holder.noiDungTrangThaiTextView.setText(spannableString);

        // Format and display the current date and time
//        Date currentTime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        String formattedDate = formatter.format(currentTime);
        String rawDate = thongBaoPhieuDat.getLastUpdatedTime(); // Ví dụ: "2024-10-19T12:34:03.977837+07:00"
        String formattedDate = formatDateTime(rawDate);

// Hiển thị ngày giờ đã định dạng trong TextView
        holder.thoiGianTrangThaiTextView.setText(formattedDate);
        // Set image resource
        holder.imageView.setImageResource(R.drawable.sut);
    }

    @Override
    public int getItemCount() {
        return phieuDatList.size();
    }
    public String formatDateTime(String rawDate) {
        try {
            // Định dạng gốc của chuỗi thời gian
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSXXX", Locale.getDefault());

            // Chuyển chuỗi rawDate thành đối tượng Date
            Date date = inputFormat.parse(rawDate);

            // Định dạng ngày giờ mới để hiển thị
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

            // Trả về chuỗi ngày giờ đã định dạng
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Lỗi định dạng ngày!";
        }
    }
}
