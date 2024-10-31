package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhong;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhongRespones;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface API_GetBooking {
    @GET("/api/Booking/GetBooking")
    Call<Model_DonDatPhongRespones> getAllBooking(
            @Query("customerId") String customerId,
            @Query("enumBooking") int enumBooking,@Header("Authorization") String authorization
    );
}
