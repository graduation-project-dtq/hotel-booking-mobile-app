package com.example.quanlykhachsan.Booking;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlykhachsan.Booking_Model.BookingChoXacNhanItem;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhong;
import com.example.quanlykhachsan.QuanLyDon.Model_XemChiTietBooking;
import com.example.quanlykhachsan.QuanLyDon.XemChiTietBooking;
import com.example.quanlykhachsan.R;

import java.util.List;

public class BookingChoXacNhan_Adapter extends ArrayAdapter<Model_XemChiTietBooking> {

    private List<Model_XemChiTietBooking> bookingChoXacNhanItems;
    private Context context;

    public BookingChoXacNhan_Adapter(Context context, List<Model_XemChiTietBooking> bookingChoXacNhanItems) {
        super(context, R.layout.custom_lisview_bookingcxn, bookingChoXacNhanItems); // Pass layout and items to ArrayAdapter
        this.bookingChoXacNhanItems = bookingChoXacNhanItems;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Tạo view mới nếu convertView là null
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_lisview_bookingcxn, parent, false);
        }

        // Lấy item hiện tại
        Model_XemChiTietBooking currentItem = bookingChoXacNhanItems.get(position);

        // Ánh xạ các view
       // ImageView imagephong = convertView.findViewById(R.id.imagephong);
        TextView Tenphong = convertView.findViewById(R.id.Tenphong);
        TextView dv = convertView.findViewById(R.id.Tendv);

        StringBuilder roomNames = new StringBuilder(); // Sử dụng StringBuilder để xây dựng danh sách tên phòng
        for (Model_XemChiTietBooking.BookingDetail roomDetail : currentItem.getBookingDetail()) {
            roomNames.append("Phòng: ").append(roomDetail.getRoomName()).append("\n"); // Thêm tên phòng vào danh sách
        }
        Tenphong.setText(roomNames.toString());
        StringBuilder serviceNames = new StringBuilder(); // Sử dụng StringBuilder để xây dựng danh sách tên phòng
        for (Model_XemChiTietBooking.Service roomDetail : currentItem.getServices()) {
            serviceNames.append("Dịch vụ: ").append(roomDetail.getServiceName()).append(":");
            serviceNames.append("").append(roomDetail.getQuantity()).append("\n");

        }
        dv.setText(serviceNames.toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), XemChiTietBooking.class);
                String IdSearch = currentItem.getId();
                // Log.d("RoomCategoryAdapter", "roomId: " + roomId); // In log roomId
                intent.putExtra("Id_Search", IdSearch);
                view.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
