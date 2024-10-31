package com.example.quanlykhachsan.Booking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.quanlykhachsan.CALL_API.API_XemChiTietLoaiPhong;
import com.example.quanlykhachsan.CALL_API.API_XemChiTietRoomDetail;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryDetail;
import com.example.quanlykhachsan.LoaiPhong.XemChiTietLoaiPhong;
import com.example.quanlykhachsan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemChiTietLoaiPhong_Detail extends AppCompatActivity {

    private TextView nameTextView;
    private LinearLayout descriptionContainer;
    private ViewFlipper  imageView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_loai_phong_detail);

        nameTextView = findViewById(R.id.tenloai);
        descriptionContainer = findViewById(R.id.description_container);
        imageView = findViewById(R.id.image);
        back = findViewById(R.id.btnBack);

        back.setOnClickListener(v -> finish());

        String roomId = getIntent().getStringExtra("ROOM_ID");
        if (roomId != null && !roomId.isEmpty()) {
            Log.d("RoomCategoryAdapter", "roomId: " + roomId); // In log roomId
            loadRoomDetails(roomId);
        } else {
            Toast.makeText(this, "Không tìm phòng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadRoomDetails(String roomId) {
        API_XemChiTietRoomDetail apiService = RetrofitLogin.XemchiTietLoaiPhong_Detail();
        Call<Model_RoomTypeDetail> call = apiService.XemChiTietLoaiPhong_Detail(roomId);

        call.enqueue(new Callback<Model_RoomTypeDetail>() {
            @Override
            public void onResponse(Call<Model_RoomTypeDetail> call, Response<Model_RoomTypeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_RoomTypeDetail roomResponse = response.body();
                    Model_RoomType room = roomResponse.getData();

                    if (room != null) {
                        nameTextView.setText(room.getName());
                        if (room.getDescription() != null && !room.getDescription().isEmpty()) {
                            displayDescriptions(room.getDescription());
                        }
                        displayRoomImagesInFlipper(room.getimageRoomTypeDetailDTOs());
                    } else {
                        Log.e("XemChiTietLoaiPhong_Detail", "Room data is null");
                        Toast.makeText(XemChiTietLoaiPhong_Detail.this, "Dữ liệu phòng trống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("XemChiTietLoaiPhong_Detail", "Response not successful: " + response.code());
                    Toast.makeText(XemChiTietLoaiPhong_Detail.this, "Không tìm thấy thông tin phòng. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_RoomTypeDetail> call, Throwable t) {
                Log.e("XemChiTietLoaiPhong_Detail", "API call failed", t);
                Toast.makeText(XemChiTietLoaiPhong_Detail.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayRoomImagesInFlipper(List<Model_RoomType.imageRoomTypeDetailDTOs> imageRoomTypeDetailDTOs) {
        ViewFlipper viewFlipper = findViewById(R.id.image);

        if (imageRoomTypeDetailDTOs != null && !imageRoomTypeDetailDTOs.isEmpty()) {
            // Lặp qua danh sách hình ảnh và thêm từng ImageView vào ViewFlipper
            for (Model_RoomType.imageRoomTypeDetailDTOs imageDTO : imageRoomTypeDetailDTOs) {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // Sử dụng Picasso để tải hình ảnh từ URL
                Picasso.get()
                        .load(imageDTO.getUrl())
                        .into(imageView);

                // Thêm ImageView vào ViewFlipper
                viewFlipper.addView(imageView);
            }

            // Bắt đầu tự động chuyển đổi hình ảnh
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(3000); // 3 giây
            viewFlipper.startFlipping();
        } else {
            // Nếu không có hình ảnh, hiển thị một hình ảnh mặc định
            ImageView defaultImage = new ImageView(this);
            defaultImage.setImageResource(R.drawable.hello); // Thay thế bằng hình ảnh mặc định của bạn
            viewFlipper.addView(defaultImage);
        }
    }




    private void displayDescriptions(String description) {
        // Cắt chuỗi mô tả theo dấu phẩy
        String[] items = description.split(",");

        // Xóa nội dung cũ trong container
        descriptionContainer.removeAllViews();

        // Tạo mảng các biểu tượng
        int[] icons = {
                R.drawable.bedroom,
                R.drawable.kitchen,  // Thay bằng drawable icon 1 phù hợp
                R.drawable.bath,  // Thay bằng drawable icon 2 phù hợp
                R.drawable.balcony,
                R.drawable.ocean,
                R.drawable.airconditioner,
                R.drawable.television_1,
                R.drawable.washingmachine,
                R.drawable.muffler,
                R.drawable.outdoor,
                R.drawable.wifi,// Thay bằng drawable icon 3 phù hợp
                // Thêm nhiều biểu tượng hơn nếu cần
        };

        // Lặp qua từng mô tả và thêm chúng vào giao diện
        for (int i = 0; i < items.length; i++) {
            // Tạo view cho mỗi mô tả
            View descriptionItem = LayoutInflater.from(this).inflate(R.layout.item_mota_loaiphong, descriptionContainer, false);

            // Tìm các view trong item
            ImageView icon = descriptionItem.findViewById(R.id.icon);
            TextView text = descriptionItem.findViewById(R.id.mota);

            // Gán icon và mô tả
            if (i < icons.length) {
                icon.setImageResource(icons[i]);  // Gán biểu tượng tương ứng
            } else {
                icon.setImageResource(R.drawable.location); // Biểu tượng mặc định nếu không đủ biểu tượng
            }
            text.setText(items[i].trim());  // Cắt bỏ khoảng trắng
            // Thêm view vào container
            descriptionContainer.addView(descriptionItem);
        }
    }

}
