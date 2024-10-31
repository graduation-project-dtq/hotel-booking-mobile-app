package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.XacnhanMail.Model_GuiLaiCode;
import com.example.quanlykhachsan.XacnhanMail.Model_XacNhanMail;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API_Response_Code {
    @POST("api/Auth/reponse-code")
    Call<Model_GuiLaiCode> postResponseCodeEmail(@Query("email") String email);
}
