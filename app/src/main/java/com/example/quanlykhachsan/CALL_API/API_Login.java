package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Login.Model_Login;
import com.example.quanlykhachsan.Login.Model_Response;
import com.example.quanlykhachsan.Login.RefreshTokenRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API_Login {
    @POST("/api/Auth/login")
    Call<Model_Response> login(@Body Model_Login modelLogin);

    @POST("/api/Auth/refresh")
    Call<Model_Response> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);
}