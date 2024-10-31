package com.example.quanlykhachsan.TimKiem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quanlykhachsan.Booking.Model_RoomType;
import com.example.quanlykhachsan.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DialongTimKiemAdapter extends BaseAdapter {
    private Context context;
    private List<Model_RoomType> roomCategories; // Đổi thành Model_RoomType
    private OnRoomCategorySelectedListener listener;

    public interface OnRoomCategorySelectedListener {
        void onRoomCategorySelected(Model_RoomType selectedCategory);
    }

    public DialongTimKiemAdapter(Context context, List<Model_RoomType> roomCategories, OnRoomCategorySelectedListener listener) {
        this.context = context;
        this.roomCategories = roomCategories;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return roomCategories.size();
    }

    @Override
    public Model_RoomType getItem(int position) {
        return roomCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateRoomTypes(List<Model_RoomType> newRoomTypes) {
        this.roomCategories.clear();
        this.roomCategories.addAll(newRoomTypes);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customlistview_dialongtimkiem, parent, false);
        }

        Model_RoomType roomCategory = getItem(position);

        TextView roomName = view.findViewById(R.id.Tenphongtk);
        TextView giaPhong = view.findViewById(R.id.GiaPhongtk);
        TextView soNguoiO = view.findViewById(R.id.SLKhachtk);
        ImageView roomImage = view.findViewById(R.id.imagephongtk);

       // giaPhong.setText(String.valueOf(roomCategory.getArea()));
        roomName.setText(roomCategory.getName());
        soNguoiO.setText("Số người tối đa: " + roomCategory.getCapacityMax()); // Hiển thị số người tối đa
        roomImage.setImageResource(R.drawable.sut);
//        if (!roomCategory.getimageRoomTypeDetailDTOs().isEmpty()) {
//            Picasso.get().load(roomCategory.getimageRoomTypeDetailDTOs().get(0).getUrl()).into(roomImage);
//        } else {
//            roomImage.setImageResource(R.drawable.ic_launcher_background);
//        }

        return view;
    }
}
