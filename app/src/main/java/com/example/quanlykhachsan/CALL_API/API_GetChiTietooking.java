package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhongRespones;
import com.example.quanlykhachsan.QuanLyDon.Model_XemChiTietBookingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface API_GetChiTietooking {
    @GET("/api/Booking")
    Call<Model_XemChiTietBookingResponse> getXemChiTietBooking(
            @Query("idSearch") String idSearch,@Header("Authorization") String authorization
    );
}
