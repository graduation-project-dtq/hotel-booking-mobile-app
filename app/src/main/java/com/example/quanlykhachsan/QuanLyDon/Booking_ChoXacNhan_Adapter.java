package com.example.quanlykhachsan.QuanLyDon;

import static android.app.ProgressDialog.show;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhachsan.Booking.DatPhong_Activity;
import com.example.quanlykhachsan.Booking.Model_RoomType;
import com.example.quanlykhachsan.CALL_API.API_HuyBooking;
import com.example.quanlykhachsan.CALL_API.API_TimKiemPhong;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Index;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.R;
import com.example.quanlykhachsan.TimKiem.SearchRoomResponse;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking_ChoXacNhan_Adapter extends ArrayAdapter<Model_DonDatPhong> {
    private final Context context;
    private final List<Model_DonDatPhong> bookingList;
    private final OnBookingActionListener actionListener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private String idHuy;
    private  MaterialButton btnHuy;
    private TextView ghichu;

    public Booking_ChoXacNhan_Adapter(Context context, List<Model_DonDatPhong> bookingList, OnBookingActionListener actionListener) {
        super(context, 0, bookingList);
        this.context = context;
        this.bookingList = bookingList;
        this.actionListener = actionListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.customlistview_bookingchoxacnhan, parent, false);
        }

        Model_DonDatPhong booking = bookingList.get(position);

        TextView MaHoaDon = convertView.findViewById(R.id.MaHoaDon);
        TextView TongTien = convertView.findViewById(R.id.TongTien);
        ghichu = convertView.findViewById(R.id.ghichu);
        TextView ngaydat = convertView.findViewById(R.id.ngaydat);
        TextView ngayden = convertView.findViewById(R.id.ngayden);
        TextView ngaydi = convertView.findViewById(R.id.ngaydi);
        btnHuy = convertView.findViewById(R.id.btnHuy);
        MaterialButton btnxct = convertView.findViewById(R.id.btnXemChiTiet);

        MaHoaDon.setText("Hóa đơn đặt phòng: " + booking.getId());
        TongTien.setText(String.format(Locale.getDefault(), " %,.0f VNĐ", booking.getTotalAmount()));
        ghichu.setText("Cho phép hủy phòng");
        ngaydat.setText(booking.getBookingDate());
        ngayden.setText(booking.getCheckInDate());
        ngaydi.setText(booking.getCheckOutDate());
         idHuy=booking.getId();

//        btnHuy.setOnClickListener(v ->
//                handleCancelBooking(booking)
//        );
        HoaDonDaSD(booking);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idHuy=booking.getId();
                handleCancelBooking(booking);
            }
        });
        btnxct.setOnClickListener(v -> {
            Intent intent = new Intent(context, XemChiTietBooking.class);
            intent.putExtra("Id_Search", booking.getId());
            context.startActivity(intent);
        });

        return convertView;
    }
    private void HoaDonDaSD(Model_DonDatPhong booking)
    {
        Date currentDate = new Date();
        Date checkOutDate;
        try {
            checkOutDate = sdf.parse(booking.getCheckOutDate());
        } catch (ParseException e) {
            Toast.makeText(context, "Lỗi xử lý ngày tháng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentDate.after(checkOutDate)) {
            btnHuy.setVisibility(View.GONE);
            ghichu.setVisibility(View.GONE);
        }
        else
        {
            btnHuy.setVisibility(View.VISIBLE);
            ghichu.setVisibility(View.VISIBLE);
        }
    }
    private void handleCancelBooking(Model_DonDatPhong booking) {
        String checkInDateString = booking.getCheckInDate();
        Date currentDate = new Date();
        Date checkInDate;

        try {
            checkInDate = sdf.parse(checkInDateString);
        } catch (ParseException e) {
            Toast.makeText(context, "Lỗi xử lý ngày tháng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkInDate == null) {
            Toast.makeText(context, "Ngày check-in không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
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
            HuyPhong(idHuy);
        });
        builder.setNegativeButton("Không", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public interface OnBookingActionListener {
        void onCancelBooking(Model_DonDatPhong booking);
    }
    public void HuyPhong(String idHuy) {
        TokenManager tokenManager = new TokenManager(getContext());
        // Lấy token từ TokenManager
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("UpdateCustomer", "Access Token không hợp lệ hoặc đã hết hạn!");
            // TODO: Thực hiện refresh token ở đây
            return;
        }
        API_HuyBooking apiHuyBooking = RetrofitLogin.HuyDatPhong();
        Call<Model_DonDatPhong> call = apiHuyBooking.postHuyBooking(idHuy,"Bearer " + accessToken);

        call.enqueue(new Callback<Model_DonDatPhong>() {
            @Override
            public void onResponse(Call<Model_DonDatPhong> call, Response<Model_DonDatPhong> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(context, Index.class);
                    intent.putExtra("showAlertHuyphong", true);
                    context.startActivity(intent);
                    Toast.makeText(getContext(), "Hủy thành công", Toast.LENGTH_SHORT).show();
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorMessage = "Lỗi khi tìm kiếm phòng. Mã lỗi: " + response.code() + ". Chi tiết: " + errorBody;

                }
            }

            @Override
            public void onFailure(Call<Model_DonDatPhong> call, Throwable t) {
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                t.printStackTrace(); // In stack trace để debug
            }
        });
    }
}