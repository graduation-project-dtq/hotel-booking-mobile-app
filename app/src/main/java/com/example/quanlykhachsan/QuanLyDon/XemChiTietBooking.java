package com.example.quanlykhachsan.QuanLyDon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanlykhachsan.Booking.BookingChoXacNhan_Adapter;
import com.example.quanlykhachsan.CALL_API.API_GetChiTietooking;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemChiTietBooking extends AppCompatActivity {
    private List<Model_XemChiTietBooking> bookingList;
    private BookingChoXacNhan_Adapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet_booking);

        Toolbar back=findViewById(R.id.back);
        listView = findViewById(R.id.listviewXemChiTiet);
        bookingList = new ArrayList<>();
        adapter = new BookingChoXacNhan_Adapter(this, bookingList);
        listView.setAdapter(adapter);
        String IdSearch = getIntent().getStringExtra("Id_Search");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadBookingDetails(IdSearch);
    }

    private void loadBookingDetails(String IdSearch) {
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        // Lấy token từ TokenManager
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("UpdateCustomer", "Access Token không hợp lệ hoặc đã hết hạn!");
            // TODO: Thực hiện refresh token ở đây
            return;
        }
        API_GetChiTietooking api = RetrofitLogin.XemChiTietBooking();
        Call<Model_XemChiTietBookingResponse> call = api.getXemChiTietBooking(IdSearch,"Bearer " + accessToken);

        Log.d("API_CALL", "Calling API with IdSearch: " + IdSearch);

        call.enqueue(new Callback<Model_XemChiTietBookingResponse>() {
            @Override
            public void onResponse(Call<Model_XemChiTietBookingResponse> call, Response<Model_XemChiTietBookingResponse> response) {
                Log.d("API_RESPONSE", "Response received. Code: " + response.code());

                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Response is successful");
                    if (response.body() != null) {
                        Log.d("API_RESPONSE", "Response body is not null");
                        Model_XemChiTietBookingResponse.Data data = response.body().getData();
                        if (data != null) {
                            Log.d("API_RESPONSE", "Data is not null");
                            if (data.getItems() != null && !data.getItems().isEmpty()) {
                                Log.d("API_RESPONSE", "Items found: " + data.getItems().size());
                                bookingList.clear();
                                bookingList.addAll(data.getItems());
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.w("API_RESPONSE", "Items list is null or empty");
                                Toast.makeText(XemChiTietBooking.this, "Không có dữ liệu đặt phòng", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w("API_RESPONSE", "Data is null");
                            Toast.makeText(XemChiTietBooking.this, "Không có dữ liệu trả về", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w("API_RESPONSE", "Response body is null");
                        Toast.makeText(XemChiTietBooking.this, "Phản hồi rỗng từ server", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_RESPONSE", "Response not successful. Code: " + response.code() + ", Message: " + response.message());
                    String errorBody = "Unable to fetch error body";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        Log.e("API_RESPONSE", "Error reading error body", e);
                    }
                    Log.e("API_RESPONSE", "Error body: " + errorBody);
                    Toast.makeText(XemChiTietBooking.this, "Lỗi: " + response.code() + " - " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Model_XemChiTietBookingResponse> call, Throwable t) {
                Log.e("API_FAILURE", "API call failed", t);
                Toast.makeText(XemChiTietBooking.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}