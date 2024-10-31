package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking_Model.Model_KiemTraPhongTrong;
import com.example.quanlykhachsan.Booking_Model.Model_KiemTraPhongTrongRespones;
import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface API_KiemTraPhongTrong {
    @POST("api/Room/FindRoom")
    Call<Model_KiemTraPhongTrongRespones> postCheckPhongTrong(@Body Model_KiemTraPhongTrong phongTrong);
}

