package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.TimKiem.SearchRoomResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_TimKiemPhong {
    @GET("/api/RoomTypeDetail/{soNguoi},{roomTypeDetailID}")
    Call<SearchRoomResponse> TimKiemPhong(
            @Path("soNguoi") String soNguoi,
            @Path("roomTypeDetailID") String roomTypeDetailID
    );
}