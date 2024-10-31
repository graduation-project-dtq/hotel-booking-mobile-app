package com.example.quanlykhachsan.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhachsan.Booking.DatPhong_fragment;
import com.example.quanlykhachsan.CALL_API.API_GetCustomer;
import com.example.quanlykhachsan.CALL_API.API_GetLoaiPhong;
import com.example.quanlykhachsan.CALL_API.API_Login;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.CALL_API.UnsafeSSLHelper;
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;
import com.example.quanlykhachsan.LoaiPhong.RoomCategoryAdapter;
import com.example.quanlykhachsan.Login.Model_Login;
import com.example.quanlykhachsan.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home_fragment extends Fragment {

    private ViewFlipper viewFlipper;
    private RecyclerView recyclerView;
    private RoomCategoryAdapter adapter;
    private List<Model_RoomCategory> roomTypes = new ArrayList<>();
    private RoomCategoryAdapter.OnFavoriteClickListener onFavoriteClickListener;
    API_GetLoaiPhong apiService;
    private static final String HOTEL_ADDRESS = "140 Đ. Lê Trọng Tấn, Tây Thạnh, Tân Phú, Hồ Chí Minh";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_home_fragment, container, false);

        apiService = RetrofitLogin.getApiGetLoaiPhongService();

        TextView descriptionText = view.findViewById(R.id.description_text);
        TextView readMoreText =view.findViewById(R.id.read_more);
        TextView TenKH=view.findViewById(R.id.TenKH);
        ImageView booking=view.findViewById(R.id.datphong);
        ImageView Gioithieu=view.findViewById(R.id.gioithieu);
        ImageView Hotro=view.findViewById(R.id.hotro);
        final boolean isExpanded = false; // Khai báo là final
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Qúy khách");
        TenKH.setText(username);
        readMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpanded = false; // Khai báo biến cục bộ

                if (isExpanded) {
                    descriptionText.setMaxLines(3);
                    readMoreText.setText("Đọc thêm");
                } else {
                    descriptionText.setMaxLines(Integer.MAX_VALUE);
                    readMoreText.setText("Thu gọn");
                }
                isExpanded = !isExpanded;
            }
        });

        TextView mapView = view.findViewById(R.id.map);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });

        Gioithieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo Intent để chuyển sang Activity khác
                Intent intent = new Intent(getContext(), GioiThieu_Activity.class);
                startActivity(intent); // Chuyển sang Activity mới
            }
        });

        Hotro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo Intent để chuyển sang Activity khác
                Intent intent = new Intent(getContext(), HoTro_Activity.class);
                startActivity(intent); // Chuyển sang Activity mới
            }
        });


        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo đối tượng của DatPhong_fragment
                DatPhong_fragment datPhongFragment = new DatPhong_fragment();

                // Sử dụng FragmentManager để chuyển sang DatPhong_fragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.Fragmenthome, datPhongFragment) // Thay thế Fragment hiện tại
                        .addToBackStack(null) // Cho phép quay lại Fragment trước đó khi nhấn nút Back
                        .commit(); // Thực thi giao dịch
            }
        });

        recyclerView = view.findViewById(R.id.recyphong);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        adapter = new RoomCategoryAdapter(getContext(),roomTypes, onFavoriteClickListener);
        recyclerView.setAdapter(adapter);

        viewFlipper = view.findViewById(R.id.viewFlipper);

        int[] imageResources = {
                R.drawable.banner2,
                R.drawable.banner3,
                R.drawable.banner1,
        };

        for (int imageResource : imageResources) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(imageResource);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(2000);
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);

        // Gọi API để lấy danh sách loại phòng
        LoadLoaiPhong();
        LoadThongTinKhachHang(username);
        return view;
    }
    private void openGoogleMaps() {
        try {
            // Encode địa chỉ để tránh các ký tự đặc biệt
            String encodedAddress = Uri.encode(HOTEL_ADDRESS);

            // Tạo Uri cho Google Maps
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + encodedAddress);

            // Tạo Intent để mở Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Kiểm tra xem thiết bị có ứng dụng Google Maps không
            if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Nếu không có Google Maps, mở trong trình duyệt
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=" + encodedAddress));
                startActivity(browserIntent);
            }

        } catch (Exception e) {
            // Xử lý lỗi nếu có
            Toast.makeText(getContext(), "Không thể mở bản đồ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void LoadLoaiPhong() {
        Call<Model_RoomCategoryResponse> call = apiService.getAllRoomTypes();
        call.enqueue(new Callback<Model_RoomCategoryResponse>() {
            @Override
            public void onResponse(Call<Model_RoomCategoryResponse> call, Response<Model_RoomCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Model_RoomCategory> roomCategories = response.body().getData();
                    adapter.updateRoomTypes(roomCategories);
                    for (Model_RoomCategory roomType : roomCategories) {
                            Log.d("API Response", "Room Type: " + roomType.getName());
                            Log.d("API Response", "Description: " + roomType.getDescription());
                            if (!roomType.getImageRoomTypes().isEmpty()) {
                                Log.d("API Response", "Image URL: " + roomType.getImageRoomTypes().get(0).getUrl());
                            }
                        }
                }
            }

            @Override
            public void onFailure(Call<Model_RoomCategoryResponse> call, Throwable t) {
                // Handle failure
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

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Context context = requireContext();
                                SharedPreferences sharedPreferences = context.getSharedPreferences("TenKH", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("tenkh", customer.getName()); // Lưu tên khách hàng
                                editor.apply(); // Áp dụng thay đổi

                            }
                        });
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