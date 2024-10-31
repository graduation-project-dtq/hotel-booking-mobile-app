package com.example.quanlykhachsan.LoaiPhong;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlykhachsan.CALL_API.API_XemChiTietLoaiPhong;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemChiTietLoaiPhong extends AppCompatActivity {

    private TextView nameTextView;
    private LinearLayout descriptionContainer;
    private ImageView imageView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_loai_phong);

        nameTextView = findViewById(R.id.tenloai);
        descriptionContainer = findViewById(R.id.description_container);
        imageView = findViewById(R.id.image);
        back=findViewById(R.id.btnBack);

        back.setOnClickListener(v -> finish());
        // Lấy roomId từ Intent
        String roomId = getIntent().getStringExtra("ROOM_ID");
        if (roomId != null && !roomId.isEmpty()) {
            loadRoomDetails(roomId);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin phòng", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void loadRoomDetails(String roomId) {
        API_XemChiTietLoaiPhong apiService = RetrofitLogin.XemchiTietLoaiPhongService();
        Call<Model_RoomCategoryDetail> call = apiService.XemChiTietLoaiPhong(roomId);

        call.enqueue(new Callback<Model_RoomCategoryDetail>() {
            @Override
            public void onResponse(Call<Model_RoomCategoryDetail> call, Response<Model_RoomCategoryDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lấy dữ liệu từ phản hồi
                    Model_RoomCategoryDetail roomResponse = response.body();
                    Model_RoomCategory room = roomResponse.getData(); // Lấy đối tượng Model_RoomCategory

                    if (room != null) { // Kiểm tra nếu room không phải là null
                        // Hiển thị tên phòng
                        nameTextView.setText(room.getName());

                        // Hiển thị mô tả phòng
                        if (room.getDescription() != null && !room.getDescription().isEmpty()) {
                            displayDescriptions(room.getDescription());
                        }

                        // Kiểm tra và tải ảnh từ danh sách
                        List<Model_RoomCategory.ImageRoomType> imageList = room.getImageRoomTypes();
                        if (imageList != null && !imageList.isEmpty()) {
                            // Tải ảnh đầu tiên từ danh sách
                            Picasso.get().load(imageList.get(0).getUrl()).into(imageView);
                        }
                    } else {
                        // Xử lý khi không có dữ liệu phòng
                        Toast.makeText(XemChiTietLoaiPhong.this, "Không có thông tin phòng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi không tải được thông tin phòng
                    Toast.makeText(XemChiTietLoaiPhong.this, "Không thể tải thông tin phòng", Toast.LENGTH_SHORT).show();
                    Log.e("API Error", "Response unsuccessful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Model_RoomCategoryDetail> call, Throwable t) {
                // Xử lý lỗi khi kết nối API thất bại
                Toast.makeText(XemChiTietLoaiPhong.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API Error", "Call failed: ", t);
            }
        });
    }



    private void displayDescriptions(String description) {
        // Cắt chuỗi mô tả theo dấu phẩy
        String[] items = description.split("\\.");

        // Xóa nội dung cũ trong container
        descriptionContainer.removeAllViews();

        // Tạo mảng các biểu tượng
        int[] icons = {
                R.drawable.hotel_1,
                R.drawable.bedroom,  // Thay bằng drawable icon 1 phù hợp
                R.drawable.television_1,  // Thay bằng drawable icon 2 phù hợp
                R.drawable.bath,
                R.drawable.fridge,
                R.drawable.steam,
                R.drawable.checkmark,// Thay bằng drawable icon 3 phù hợp
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
