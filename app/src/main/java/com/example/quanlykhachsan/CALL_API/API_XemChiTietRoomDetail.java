package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking.Model_RoomType;
import com.example.quanlykhachsan.Booking.Model_RoomTypeDetail;
import com.example.quanlykhachsan.Booking.Model_RoomTypeResponse;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_XemChiTietRoomDetail {
    @GET("/GetByID{id}") // Thay thế bằng đường dẫn API phù hợp
    Call<Model_RoomTypeDetail> XemChiTietLoaiPhong_Detail(@Path("id") String id); // Nhận id là String
}
