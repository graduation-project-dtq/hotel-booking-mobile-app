package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking.Model_RoomTypeResponse;
import com.example.quanlykhachsan.ThongBao.Model_ThongBaoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_GetThongBao {
    @GET("api/Notification/{customerId}")
    Call<Model_ThongBaoResponse> getThongBaodatphong(@Path("customerId") String customerId);
}
