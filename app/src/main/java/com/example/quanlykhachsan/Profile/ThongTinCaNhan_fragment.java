package com.example.quanlykhachsan.Profile;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.quanlykhachsan.Booking.BookingTab;
import com.example.quanlykhachsan.CALL_API.API_GetBooking;
import com.example.quanlykhachsan.CALL_API.API_GetCustomer;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.DangKyTK;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.MainActivity;
import com.example.quanlykhachsan.QuanLyDon.Booking_ChoXacNhan_Adapter;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhong;
import com.example.quanlykhachsan.QuanLyDon.Model_DonDatPhongRespones;
import com.example.quanlykhachsan.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinCaNhan_fragment extends Fragment {

    private ImageView imgProfile;
    private TextView txtFullName;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtBirthday;
    private TextView txtsex;
    private TextView txtsdt;
    private ImageView iconbtnlogout;
    private Switch switchNotification;
    TokenManager tokenManager;
    private static final int REQUEST_BOOKING_TAB = 1;
    private Handler handler = new Handler();
    private Runnable pollingRunnable;
    private static final long POLLING_INTERVAL = 3000;
    private  TextView ChoxacnhanCount;
    private  TextView DaxacnhanCount;
    private  TextView YeucauhuyCount;
    private  TextView XacnhanhuyCount;
    private  TextView DaCheckinCount;
    private  TextView DaCheckoutCount;
    Booking_ChoXacNhan_Adapter adapter;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BOOKING_TAB && resultCode == Activity.RESULT_OK) {
            int tabPosition = data.getIntExtra("tabPosition", 0);

        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_profile_fragment, container, false);

        tokenManager = new TokenManager(getActivity());
        imgProfile = view.findViewById(R.id.imgProfile);
        txtFullName = view.findViewById(R.id.txtFullName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtBirthday = view.findViewById(R.id.txtBirthday);
        txtsex = view.findViewById(R.id.txtsex);
        txtsdt = view.findViewById(R.id.txtsdt);
      //  switchNotification = view.findViewById(R.id.switchNotification);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnUpdate=view.findViewById(R.id.btnUpdateTTCN);
        //iconbtnlogout=view.findViewById(R.id.iconlogout);
        ImageView Choxacnhan=view.findViewById(R.id.Choxacnhan);
        ImageView Daxacnhan=view.findViewById(R.id.Daxacnhan);
        ImageView Yeucauhuy=view.findViewById(R.id.Yeucauhuy);
        ImageView Xacnhanhuy=view.findViewById(R.id.Xacnhanhuy);
        ImageView DaCheckin=view.findViewById(R.id.Dacheckin);
        ImageView DaCheckout=view.findViewById(R.id.Dacheckout);
        ImageView Xemlichsu=view.findViewById(R.id.Xemls);


        //LinearLayoutCompat ChoxacnhanLayout = view.findViewById(R.id.Choxacnhan);
         ChoxacnhanCount = view.findViewById(R.id.Choxacnhan_count);
         DaxacnhanCount = view.findViewById(R.id.Daxacnhan_count);
         YeucauhuyCount=view.findViewById(R.id.Yeucauhuy_count);
         XacnhanhuyCount=view.findViewById(R.id.Xacnhanhuy_count);
         DaCheckinCount=view.findViewById(R.id.Dacheckin_count);
         DaCheckoutCount=view.findViewById(R.id.Dacheckout_count);

        Choxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 0); // Chuyển đến tab 0 (Chờ xác nhận)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);
            }
        });

        Daxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 1); // Chuyển đến tab 1 (Đã xác nhận)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);

            }
        });

        Yeucauhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 2); // Chuyển đến tab 2 (Yêu cầu hủy)
                startActivity(intent);
            }
        });

        Xacnhanhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 3); // Chuyển đến tab 3 (Xác nhận hủy)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);
            }
        });
        DaCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 4); // Chuyển đến tab 3 (Xác nhận hủy)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);
            }
        });
        DaCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 5); // Chuyển đến tab 3 (Xác nhận hủy)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);
            }
        });
        Xemlichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), BookingTab.class);
                intent.putExtra("tabPosition", 5); // Chuyển đến tab 3 (Xác nhận hủy)
                startActivityForResult(intent, REQUEST_BOOKING_TAB);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Xóa token
                tokenManager.clearTokens();

                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().finishAffinity(); // Đóng tất cả các Activity trong stack
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Update_TTCN.class);
                startActivity(intent);
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Username", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Qúy khách");
        LoadThongTinKhachHang(username);
        startPolling(username);

        SharedPreferences sharedPreferences_id = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String CustomerId = sharedPreferences_id.getString("customer", "Qúy khách");
        // Gọi API để lấy số lượng đơn booking cho mỗi trạng thái
        loadBookingCounts(CustomerId);
        return view;
    }
    private void startPolling(String email) {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                LoadThongTinKhachHang(email); // Gọi hàm kiểm tra dữ liệu
                handler.postDelayed(this, POLLING_INTERVAL); // Lặp lại sau mỗi POLLING_INTERVAL
            }
        };
        handler.post(pollingRunnable); // Bắt đầu polling
    }
    private void loadBookingCounts(String customerId) {
        TokenManager tokenManager = new TokenManager(getActivity());
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("LoadBookingCounts", "Access Token không hợp lệ hoặc đã hết hạn!");
            return;
        }



        // Gọi API cho mỗi trạng thái
        loadBookingCountForStatus(customerId, 3, ChoxacnhanCount);
        loadBookingCountForStatus(customerId, 1, DaxacnhanCount);
        loadBookingCountForStatus (customerId, 0, YeucauhuyCount);
        loadBookingCountForStatus( customerId, 2, XacnhanhuyCount);
        loadBookingCountForStatus( customerId, 4, DaCheckinCount);
        loadBookingCountForStatus( customerId, 5, DaCheckoutCount);
    }
    private void loadBookingCountForStatus(String customerId, int enumBooking, TextView countView) {
        API_GetBooking apiGetBooking = RetrofitLogin.LoadHoaDonChoXacNhan();
        Log.d("API_CALL", "Gọi API với customerId: " + customerId + ", enumBooking: " + enumBooking);
        Call<Model_DonDatPhongRespones> call = apiGetBooking.getAllBooking(customerId, enumBooking, "Bearer " + tokenManager.getAccessToken());
        call.enqueue(new Callback<Model_DonDatPhongRespones>() {
            @Override
            public void onResponse(Call<Model_DonDatPhongRespones> call, Response<Model_DonDatPhongRespones> response) {
                Log.d("API_RESPONSE", "Response Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Model_DonDatPhongRespones responseBody = response.body();
                    if (responseBody.getData() != null && responseBody.getData() != null) {
                        int count = responseBody.getData().size();
                        Log.d("API_DATA", "Số lượng booking: " + count);
                        updateCountView(countView, count);
                    } else {
                        Log.e("API_ERROR", "Danh sách booking rỗng hoặc null");
                        updateCountView(countView, 0);
                    }
                } else {
                    Log.e("API_ERROR", "Lỗi: " + response.message());
                    updateCountView(countView, 0);
                }
            }

            @Override
            public void onFailure(Call<Model_DonDatPhongRespones> call, Throwable t) {
                Log.e("API_FAILURE", "Lỗi khi gọi API: " + t.getMessage());
                updateCountView(countView, 0);
            }
        });
    }
    private void updateCountView(final TextView countView, final int count) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (countView != null) {
                    if (count > 0) {
                        countView.setVisibility(View.VISIBLE);
                        countView.setText(String.valueOf(count));
                        Log.d("UI_UPDATE", "Updating count view: " + count);
                    } else {
                        countView.setVisibility(View.GONE);
                        Log.d("UI_UPDATE", "Hiding count view");
                    }
                }
            }
        });
    }
    private void LoadThongTinKhachHang(String email) {
        // Gọi API từ Retrofit
        API_GetCustomer api_getCustomer = RetrofitLogin.XemChiTietThongTin();

        // Thực hiện gọi API với email khách hàng
        Call<Model_CustomerReponse> call = api_getCustomer.XemChiTietThongTinCustomer(email);

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<Model_CustomerReponse>() {
            @Override
            public void onResponse(Call<Model_CustomerReponse> call, Response<Model_CustomerReponse> response) {
                if (response.isSuccessful()) {
                    // Lấy thông tin khách hàng từ phản hồi
                    Model_CustomerReponse customerResponse = response.body();
                    if (customerResponse != null) {
                        // Bạn có thể xử lý thông tin khách hàng tại đây
                        Model_Customer customer = customerResponse.getData();
                        txtFullName.setText(customer.getName() != null ? customer.getName() : "Không có tên");
                        txtEmail.setText(customer.getEmail() != null ? customer.getEmail() : "Không có email");
                        txtBirthday.setText(customer.getDateOfBirth() != null ? customer.getDateOfBirth() : "Chưa cập nhật");
                        txtsex.setText(customer.getSex() != null ? customer.getSex() : "Chưa cập nhật");
                        txtsdt.setText(customer.getPhone() != null ? customer.getPhone() : "Chưa cập nhật số điện thoại");
                        // Ví dụ hiển thị thông tin khách hàng lên log
                        Log.d("Customer Info", "Tên: " + customer.getName());
                        Log.d("Customer Info", "Email: " + customer.getEmail());
                        Log.d("Customer Info", "Điểm tín nhiệm: " + customer.getCredibilityScore());

                        // Có thể cập nhật UI để hiển thị thông tin khách hàng
                        // Ví dụ: txtName.setText(customer.getName());
                    }
                } else {
                    // Xử lý lỗi nếu API trả về không thành công
                    Log.e("API Error", "Lỗi API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Model_CustomerReponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.e("API Error", t.getMessage());
            }
        });
    }

}