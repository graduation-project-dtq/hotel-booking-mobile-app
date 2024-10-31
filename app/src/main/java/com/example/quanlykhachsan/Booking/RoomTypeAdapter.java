package com.example.quanlykhachsan.Booking;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.quanlykhachsan.Booking_Model.Model_KiemTraPhongTrong;
import com.example.quanlykhachsan.CALL_API.API_KiemTraPhongTrong;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.LoaiPhong.XemChiTietLoaiPhong;
import com.example.quanlykhachsan.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.RoomTypeViewHolder> {

    private Context context;
    private List<Model_RoomType> roomTypes;
    private OnFavoriteClickListener onFavoriteClickListener;
    private List<Integer> selectedQuantities; // Danh sách lưu số lượng tạm thời
//    public interface OnRoomSelectedListener {
//        void onRoomSelected(String roomTypeId, int quantity);
//    }
    private OnRoomSelectedListener onRoomSelectedListener;
    private OnRoomAvailabilityCheckListener roomAvailabilityCheckListener;
    // Interface để xử lý sự kiện khi click vào "favorite"
    public interface OnFavoriteClickListener {
        void onFavoriteClick(int position);
    }
    public interface OnRoomAvailabilityCheckListener {
        void onCheckRoomAvailability(String roomTypeId, int requestedQuantity);
    }
    public interface OnRoomSelectedListener {
        void onRoomSelected(String roomTypeId, int quantity, double  price);
    }
    // Constructor
    public RoomTypeAdapter(Context context, List<Model_RoomType> roomTypes, OnFavoriteClickListener onFavoriteClickListener,
                           OnRoomSelectedListener onRoomSelectedListener, OnRoomAvailabilityCheckListener roomAvailabilityCheckListener) {
        this.context = context;
        this.roomTypes = roomTypes;
        this.onFavoriteClickListener = onFavoriteClickListener;
        this.selectedQuantities = new ArrayList<>(); // Khởi tạo danh sách số lượng
        for (int i = 0; i < roomTypes.size(); i++) {
            selectedQuantities.add(0); // Khởi tạo số lượng ban đầu là 0 cho mỗi loại phòng
        }
        this.onRoomSelectedListener = onRoomSelectedListener;
        this.roomAvailabilityCheckListener = roomAvailabilityCheckListener;
    }

    @NonNull
    @Override
    public RoomTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item
        View view = LayoutInflater.from(context).inflate(R.layout.custom_lisview_booking_loaiphong, parent, false);
        return new RoomTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomTypeViewHolder holder, int position) {
        // Lấy thông tin loại phòng hiện tại
        Model_RoomType roomType = roomTypes.get(position);

        // Load hình ảnh phòng
        if (roomType.getimageRoomTypeDetailDTOs() != null && !roomType.getimageRoomTypeDetailDTOs().isEmpty()) {
            String imageUrl = roomType.getimageRoomTypeDetailDTOs().get(0).getUrl();
            Picasso.get().load(imageUrl).into(holder.imagePhong);
        } else {
            // Đặt một hình ảnh mặc định hoặc để trống nếu không có hình ảnh
            holder.imagePhong.setImageResource(R.drawable.sup);
        }

        // Gán dữ liệu cho các TextView
        holder.tenPhong.setText(roomType.getName()); // Tên phòng
        holder.giaPhong.setText(String.format("Giá phòng: %,.0f VNĐ", roomType.getDiscountPrice()));
        holder.slKhach.setText(roomType.getCapacityMax() + " khách"); // Số lượng khách tối đa

        // Hiển thị số lượng đã chọn
        holder.soLuong.setText(String.valueOf(selectedQuantities.get(position)));

        // Xử lý click vào icon tăng giảm số lượng
        holder.icon1.setOnClickListener(v -> {
            // Kiểm tra xem ngày đến và ngày đi đã được chọn hay chưa
            if (DatPhong_fragment.CheckInDate == null || DatPhong_fragment.CheckOutDate == null) {
                Toast.makeText(context, "Vui lòng chọn ngày đến và ngày đi trước", Toast.LENGTH_SHORT).show();
                return;
            }

            String roomId = roomType.getId();
            SharedData.RoomTypeDetaiID = roomId;

            int newQuantity = selectedQuantities.get(position) - 1;

            // Kiểm tra xem số lượng mới có nhỏ hơn 0 hay không
            if (newQuantity < 0) {
                return;
            }

            SharedData.Quantity = newQuantity;
            // Gọi kiểm tra phòng trống
            roomAvailabilityCheckListener.onCheckRoomAvailability(roomId, newQuantity);
        });

        holder.icon2.setOnClickListener(v -> {
            if (DatPhong_fragment.CheckInDate == null || DatPhong_fragment.CheckOutDate == null) {
                Toast.makeText(context, "Vui lòng chọn ngày đến và ngày đi trước", Toast.LENGTH_SHORT).show();
                return;
            }

            String roomId = roomType.getId();
            SharedData.RoomTypeDetaiID = roomId;

            int newQuantity = selectedQuantities.get(position) + 1;
            SharedData.Quantity=newQuantity;
            // Gọi kiểm tra phòng trống
            roomAvailabilityCheckListener.onCheckRoomAvailability(roomId, newQuantity);
        });


        holder.buttonBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), XemChiTietLoaiPhong_Detail.class);
                String roomId = roomType.getId();
               // Log.d("RoomCategoryAdapter", "roomId: " + roomId); // In log roomId
                intent.putExtra("ROOM_ID", roomId);
                view.getContext().startActivity(intent);
            }
        });
    }

    public void updateRoomQuantity(String roomTypeId, int newQuantity, int availableRooms) {
        for (int i = 0; i < roomTypes.size(); i++) {
            if (roomTypes.get(i).getId().equals(roomTypeId)) {
                if (newQuantity <= availableRooms) {
                    selectedQuantities.set(i, newQuantity);
                } else {
                    selectedQuantities.set(i, availableRooms);
                    Toast.makeText(context, "Chỉ còn " + availableRooms + " phòng trống.", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(i);

                if (onRoomSelectedListener != null) {
                    onRoomSelectedListener.onRoomSelected(roomTypeId, selectedQuantities.get(i), roomTypes.get(i).getDiscountPrice());
                }
                break;
            }
        }
    }
    interface RoomAvailabilityCallback {
        void onResult(boolean available, String message);
    }
    @Override
    public int getItemCount() {
        return roomTypes != null ? roomTypes.size() : 0;  // Tránh NullPointerException
    }

    // Class ViewHolder để giữ các view
    public class RoomTypeViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePhong;
        TextView tenPhong, giaPhong;
        MaterialButton soLuong, icon1, icon2,buttonBookmark;
        Chip slKhach;

        public RoomTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            // Liên kết các view với layout
            imagePhong = itemView.findViewById(R.id.imagephong);
            tenPhong = itemView.findViewById(R.id.Tenphong);
            giaPhong = itemView.findViewById(R.id.GiaPhong);
            soLuong = itemView.findViewById(R.id.soLuong);
            icon1 = itemView.findViewById(R.id.icon1);
            icon2 = itemView.findViewById(R.id.icon2);
            slKhach = itemView.findViewById(R.id.SLKhach);
            buttonBookmark=itemView.findViewById(R.id.buttonBookmark);
        }
    }

    // Phương thức để cập nhật danh sách RoomType
    public void updateRoomTypes(List<Model_RoomType> newRoomTypes) {
        this.roomTypes = newRoomTypes;
        this.selectedQuantities.clear(); // Xóa danh sách số lượng cũ
        for (int i = 0; i < newRoomTypes.size(); i++) {
            this.selectedQuantities.add(0); // Khởi tạo số lượng mới cho mỗi loại phòng
        }
        notifyDataSetChanged();  // Cập nhật lại RecyclerView
    }

    // Phương thức lấy danh sách số lượng đã chọn
    public List<Integer> getSelectedQuantities() {
        return selectedQuantities;
    }
}