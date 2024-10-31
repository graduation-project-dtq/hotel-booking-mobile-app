package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking_Model.Model_Booking;
import com.example.quanlykhachsan.DangKy.Model_DangKy;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API_Booking {
    @POST("api/Booking")
    Call<Model_Booking> postBooking(@Body Model_Booking booking,@Header("Authorization") String authorization);
}
