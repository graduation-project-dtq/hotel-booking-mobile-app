package com.example.quanlykhachsan.QuanLyDon;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhachsan.R;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Booking_YeuCauHuy_Adapter extends ArrayAdapter<Model_DonDatPhong> {
    private final Context context;
    private final List<Model_DonDatPhong> bookingList;
    private final OnBookingActionListener actionListener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public Booking_YeuCauHuy_Adapter(Context context, List<Model_DonDatPhong> bookingList, OnBookingActionListener actionListener) {
        super(context, 0, bookingList);
        this.context = context;
        this.bookingList = bookingList;
        this.actionListener = actionListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.customlistview_booking_yeucauhuy, parent, false);
        }

        Model_DonDatPhong booking = bookingList.get(position);

        TextView MaHoaDon = convertView.findViewById(R.id.MaHoaDon);
        TextView TongTien = convertView.findViewById(R.id.TongTien);
        TextView ngaydat = convertView.findViewById(R.id.ngaydat);
        TextView ngayden = convertView.findViewById(R.id.ngayden);
        TextView ngaydi = convertView.findViewById(R.id.ngaydi);
        MaterialButton btnxct = convertView.findViewById(R.id.btnXemChiTiet);

        MaHoaDon.setText("Hóa đơn đặt phòng: " + booking.getId());
        TongTien.setText(String.format(Locale.getDefault(), " %,.0f VNĐ", booking.getTotalAmount()));
        ngaydat.setText(booking.getBookingDate());
        ngayden.setText(booking.getCheckInDate());
        ngaydi.setText(booking.getCheckOutDate());


        btnxct.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemChiTietBooking.class);
            intent.putExtra("Id_Search", booking.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
    private void handleCancelBooking(Model_DonDatPhong booking) {
        String checkInDateString = booking.getCheckInDate();
        Date currentDate = new Date();
        Date checkInDate;
        Date checkOutDate;

        try {
            checkInDate = sdf.parse(checkInDateString);
            checkOutDate = sdf.parse(booking.getCheckOutDate());
        } catch (ParseException e) {
            Toast.makeText(context, "Lỗi xử lý ngày tháng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkInDate == null) {
            Toast.makeText(context, "Ngày check-in không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentDate.after(checkOutDate)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Xác nhận hủy đặt phòng");
            // Ngày hiện tại lớn hơn ngày checkout
            builder.setMessage("Đơn đặt phòng đã quá hạn hủy.Bạn không được hủy");
            builder.setNegativeButton("Hủy", null);

            AlertDialog dialog = builder.create();
            dialog.show();
            return; // Thoát khỏi hàm
        }
        long diffInMillies = Math.abs(checkInDate.getTime() - currentDate.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận hủy đặt phòng");

        if (diffInDays > 2) {
            builder.setMessage("Bạn được phép hủy phòng và sẽ được hoàn lại tiền đã cọc. Khách sạn sẽ liên hệ để xác nhận việc hoàn tiền. Bạn có chắc chắn muốn hủy không?");
        } else {
            builder.setMessage("Bạn được phép hủy phòng nhưng sẽ mất tiền đã cọc. Bạn có chắc chắn muốn hủy không?");
        }

        builder.setPositiveButton("Xác nhận hủy", (dialog, which) -> {
            if (actionListener != null) {
                actionListener.onCancelBooking(booking);
            }
        });
        builder.setNegativeButton("Không", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public interface OnBookingActionListener {
        void onCancelBooking(Model_DonDatPhong booking);
    }
}