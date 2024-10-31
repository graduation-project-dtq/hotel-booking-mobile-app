package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;
import com.example.quanlykhachsan.Service.Model_Service;
import com.example.quanlykhachsan.Service.Model_ServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API_GetService {
        @GET("/api/Service")
        Call<Model_ServiceResponse> getAllService();
}

