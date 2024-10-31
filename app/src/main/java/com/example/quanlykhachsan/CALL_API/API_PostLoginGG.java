package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Login.Model_Login;
import com.example.quanlykhachsan.Login.Model_LoginGG;
import com.example.quanlykhachsan.Login.Model_Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API_PostLoginGG {
    @POST("/api/Auth/google-signin")
    Call<Model_Response> postLoginGG(@Body Model_LoginGG modelLoginGG);
}
