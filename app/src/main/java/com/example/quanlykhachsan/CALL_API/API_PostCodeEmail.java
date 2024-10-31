package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.XacnhanMail.Model_XacNhanMail;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API_PostCodeEmail {
    @POST("api/Auth/active-account")
    Call<Model_XacNhanMail> postCodeEmail(@Query("email") String email, @Query("code") String code);
}
