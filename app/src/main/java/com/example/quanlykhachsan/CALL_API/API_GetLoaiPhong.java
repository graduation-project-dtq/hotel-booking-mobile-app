package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API_GetLoaiPhong {
    @GET("/api/RoomType")
    Call<Model_RoomCategoryResponse> getAllRoomTypes();
}
