package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.DangKy.Model_DangKy;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API_DangKy {
    @POST("api/Auth/register")
    Call<Model_DangKy> postDangKy(@Body Model_DangKy customer);
}
