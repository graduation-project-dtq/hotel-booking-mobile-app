package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.KhuyenMai.Model_KhuyenMaiResponse;
import com.example.quanlykhachsan.ThongBao.Model_ThongBaoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API_GetKhuyenMai {
    @GET("api/Voucher/{customerId}")
    Call<Model_KhuyenMaiResponse> getkhuyenmaidatphong(@Path("customerId") String customerId);
}
