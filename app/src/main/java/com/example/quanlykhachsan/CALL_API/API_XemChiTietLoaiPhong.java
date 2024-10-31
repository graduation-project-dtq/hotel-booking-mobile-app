package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryDetail;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_XemChiTietLoaiPhong {

    @GET("/api/RoomType/{id}") // Thay thế bằng đường dẫn API phù hợp
    Call<Model_RoomCategoryDetail> XemChiTietLoaiPhong(@Path("id") String id); // Nhận id là String
}