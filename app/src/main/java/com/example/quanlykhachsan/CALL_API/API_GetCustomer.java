package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryDetail;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_GetCustomer {
    @GET("/api/Customer/{email}") // Thay thế bằng đường dẫn API phù hợp
    Call<Model_CustomerReponse> XemChiTietThongTinCustomer(@Path("email") String email); // Nhận id là String
}
