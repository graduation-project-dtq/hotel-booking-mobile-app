package com.example.quanlykhachsan.Booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;

// Import Glide để load ảnh
import com.example.quanlykhachsan.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatPhong_ActivytyAdapter extends BaseAdapter {
    private Context context;
    private List<Model_RoomType> roomList;  // Sử dụng List<Model_RoomType>
    private Map<String, Integer> selectedRoomQuantities = new HashMap<>();

    public DatPhong_ActivytyAdapter(Context context, List<Model_RoomType> roomList, Map<String, Integer> selectedRoomQuantities) { // Thêm selectedRoomQuantities vào constructor
        this.context = context;
        this.roomList = roomList;
        this.selectedRoomQuantities = selectedRoomQuantities;
    }
    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int position) {
        return roomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_listview_datphong_activity, parent, false);
        }

        // Lấy thông tin phòng hiện tại
        Model_RoomType room = roomList.get(position);

        // Liên kết dữ liệu với các view
        ImageView imageroom = convertView.findViewById(R.id.imagephongdadat);
        TextView roomName = convertView.findViewById(R.id.Tenphongdadat);
        TextView roomQuantity = convertView.findViewById(R.id.SLPhong);
        TextView Gia = convertView.findViewById(R.id.GiaPhong);

        // Set dữ liệu vào các TextView
        roomName.setText(room.getName());
        //roomQuantity.setText(String.valueOf(room.get()));
        Gia.setText(String.format("Giá phòng: %,.0f VNĐ",room.getDiscountPrice()));

        // Load ảnh phòng từ URL
        if (room.getimageRoomTypeDetailDTOs() != null && !room.getimageRoomTypeDetailDTOs().isEmpty()) {
            String imageUrl = room.getimageRoomTypeDetailDTOs().get(0).getUrl();
            Picasso.get().load(imageUrl).into(imageroom);
        } else {
            // Đặt một hình ảnh mặc định hoặc để trống nếu không có hình ảnh
            imageroom.setImageResource(R.drawable.sup);
        }
        int selectedQuantity = selectedRoomQuantities.getOrDefault(room.getId(), 0);
        roomQuantity.setText(String.valueOf(selectedQuantity)+" Phòng");
        return convertView;
    }
}