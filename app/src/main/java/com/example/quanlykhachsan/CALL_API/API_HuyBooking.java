package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhong;
import com.example.quanlykhachsan.QuanLyDon.Model_HuyPhong;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API_HuyBooking {
    @POST("api/Booking/{idHuy}")
    Call<Model_DonDatPhong> postHuyBooking(@Path("idHuy") String idHuy,@Header("Authorization") String authorization);
}
