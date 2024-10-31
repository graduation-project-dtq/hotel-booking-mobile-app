package com.example.quanlykhachsan.LoaiPhong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quanlykhachsan.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DialongLoaiPhongAdapter extends BaseAdapter {
    private Context context;
    private List<Model_RoomCategory> roomCategories;
    private OnRoomCategorySelectedListener listener;

    public interface OnRoomCategorySelectedListener {
        void onRoomCategorySelected(Model_RoomCategory selectedCategory);
    }

    public DialongLoaiPhongAdapter(Context context, List<Model_RoomCategory> roomCategories, OnRoomCategorySelectedListener listener) {
        this.context = context;
        this.roomCategories = roomCategories;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return roomCategories.size();
    }

    @Override
    public Model_RoomCategory getItem(int position) {
        return roomCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateRoomTypes(List<Model_RoomCategory> newRoomTypes) {
        this.roomCategories.clear();
        this.roomCategories.addAll(newRoomTypes);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customlistview_dialongloaiphong, parent, false);
        }

        Model_RoomCategory roomCategory = getItem(position);

        TextView roomName = view.findViewById(R.id.TenLoaiphong);
        Button btnChon = view.findViewById(R.id.btnchon);
        ImageView roomImage = view.findViewById(R.id.imageloaiphong);

        roomName.setText(roomCategory.getName());

        if (!roomCategory.getImageRoomTypes().isEmpty()) {
            Picasso.get().load(roomCategory.getImageRoomTypes().get(0).getUrl()).into(roomImage);
        } else {
            roomImage.setImageResource(R.drawable.ic_launcher_background);
        }

        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRoomCategorySelected(roomCategory);
                }
            }
        });

        return view;
    }
}