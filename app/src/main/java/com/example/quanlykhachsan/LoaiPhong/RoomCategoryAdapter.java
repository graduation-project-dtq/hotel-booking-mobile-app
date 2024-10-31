package com.example.quanlykhachsan.LoaiPhong;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlykhachsan.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RoomCategoryAdapter extends RecyclerView.Adapter<RoomCategoryAdapter.ViewHolder> {
    private List<Model_RoomCategory> roomTypes;
    private OnFavoriteClickListener onFavoriteClickListener;
    private Context context; // Thêm biến context

    public interface OnFavoriteClickListener {
        void onFavoriteClick(int position);
    }

    public RoomCategoryAdapter(Context context, List<Model_RoomCategory> roomTypes, OnFavoriteClickListener onFavoriteClickListener) {
        this.context = context; // Khởi tạo biến context
        this.roomTypes = roomTypes;
        this.onFavoriteClickListener = onFavoriteClickListener;
    }

    public void updateRoomTypes(List<Model_RoomCategory> newRoomTypes) {
        this.roomTypes.clear(); // Xóa dữ liệu cũ
        this.roomTypes.addAll(newRoomTypes); // Thêm dữ liệu mới
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_cricleview_phong, parent, false);
        return new ViewHolder(itemView, onFavoriteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_RoomCategory roomType = roomTypes.get(position);

        holder.roomName.setText(roomType.getName());

        if (roomType.getImageRoomTypes() != null && !roomType.getImageRoomTypes().isEmpty()) {
           String imageUrl = roomType.getImageRoomTypes().get(0).getUrl();
            //String imageUrl="https://cf.bstatic.com/xdata/images/hotel/max1280x900/590832188.jpg?k=69307328db0257c93f40c9c116d2839f819b182de509541f059167ef9b1d23d7&o=&hp=1";

            // In ra URL để kiểm tra
            Log.d("ImageURL", imageUrl);

            // Sử dụng Glide để tải ảnh
            Picasso.get()
                    .load(imageUrl)
                    //.placeholder(R.drawable.sup) // Hình ảnh thay thế khi tải
                    .error(R.drawable.location) // Hình ảnh hiển thị khi có lỗi
                    .into(holder.roomImage);
        }


        holder.XemchiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), XemChiTietLoaiPhong.class);
                String roomId = roomType.getId();
                Log.d("RoomCategoryAdapter", "roomId: " + roomId); // In log roomId
                intent.putExtra("ROOM_ID", roomId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomTypes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView roomName;
        public ShapeableImageView roomImage;
        public ImageView XemchiTiet;

        public ViewHolder(View itemView, OnFavoriteClickListener onFavoriteClickListener) {
            super(itemView);
            roomName = itemView.findViewById(R.id.txt_room_type);
            roomImage = itemView.findViewById(R.id.img_room);
            XemchiTiet = itemView.findViewById(R.id.xemchitiet);
        }
    }
}
