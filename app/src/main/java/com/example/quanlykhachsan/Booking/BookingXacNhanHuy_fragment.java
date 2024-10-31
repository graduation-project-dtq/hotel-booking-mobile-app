package com.example.quanlykhachsan.Booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quanlykhachsan.CALL_API.API_GetBooking;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.QuanLyDon.Booking_ChoXacNhan_Adapter;
import com.example.quanlykhachsan.QuanLyDon.Booking_YeuCauHuy_Adapter;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhong;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhongRespones;
import com.example.quanlykhachsan.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingXacNhanHuy_fragment extends Fragment {

    private ListView listView;
    private Booking_YeuCauHuy_Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookingchoxacnhan, container, false);

        listView = view.findViewById(R.id.listviewCXN);

        SharedPreferences sharedPreferences_id = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String CustomerId = sharedPreferences_id.getString("customer", "Qúy khách");

        LoadBookings(CustomerId,2);

        return view;

    }

    private void LoadBookings(String customerId, int enumBooking) {
        TokenManager tokenManager = new TokenManager(getActivity());
        // Lấy token từ TokenManager
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("UpdateCustomer", "Access Token không hợp lệ hoặc đã hết hạn!");
            // TODO: Thực hiện refresh token ở đây
            return;
        }
        API_GetBooking apiGetBooking = RetrofitLogin.LoadHoaDonChoXacNhan();
        Call<Model_DonDatPhongRespones> call = apiGetBooking.getAllBooking(customerId, enumBooking,"Bearer " + accessToken);

        Log.d("API_CALL", "Gọi API với customerId: " + customerId + ", enumBooking: " + enumBooking);

        call.enqueue(new Callback<Model_DonDatPhongRespones>() {
            @Override
            public void onResponse(Call<Model_DonDatPhongRespones> call, Response<Model_DonDatPhongRespones> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Model_DonDatPhongRespones bookingResponse = response.body();
                    List<Model_DonDatPhong> bookings = bookingResponse.getData();

                    Log.d("API_DATA", "Số lượng booking: " + (bookings != null ? bookings.size() : 0));

                    if (bookings != null && !bookings.isEmpty()) {
                        adapter = new Booking_YeuCauHuy_Adapter(getContext(), bookings, booking -> {
                            Log.d("API_BOOKING", "Hủy phòng: " + booking.getId());
                            Toast.makeText(getContext(), "Hủy phòng: " + booking.getId(), Toast.LENGTH_SHORT).show();
                        });
                        listView.setAdapter(adapter);
                    } else {
                        Log.w("API_WARNING", "Không có dữ liệu đặt phòng.");
                        //Toast.makeText(getContext(), "Không có dữ liệu đặt phòng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API_ERROR", "Lỗi: " + response.message());
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_DonDatPhongRespones> call, Throwable t) {
                Log.e("API_FAILURE", "Lỗi khi gọi API: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
