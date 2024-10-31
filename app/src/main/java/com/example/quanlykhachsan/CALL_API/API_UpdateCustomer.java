package com.example.quanlykhachsan.CALL_API;

import com.example.quanlykhachsan.Booking.Model_RoomTypeResponse;
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface API_UpdateCustomer {

    @PUT("api/Customer/{email}")
    Call<Model_Customer> upDateCustomer(@Path("email") String email, @Body Model_Customer updatedCustomer, @Header("Authorization") String authorization);
}

