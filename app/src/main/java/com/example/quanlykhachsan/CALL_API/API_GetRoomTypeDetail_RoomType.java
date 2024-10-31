package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking.Model_RoomTypeResponse; // Import đúng kiểu trả về
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_GetRoomTypeDetail_RoomType {
    @GET("api/RoomTypeDetail/{roomTypeID}")
    Call<Model_RoomTypeResponse> getRoomTypesByCategory(@Path("roomTypeID") String roomTypeID); // Thay đổi categoryId thành roomTypeID
}
