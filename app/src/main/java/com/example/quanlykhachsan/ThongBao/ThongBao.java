package com.example.quanlykhachsan.ThongBao;

import static com.example.quanlykhachsan.CALL_API.RetrofitLogin.GetKhuyenMai;
import static com.example.quanlykhachsan.CALL_API.RetrofitLogin.ThongBaoDatPhong;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhachsan.CALL_API.API_GetKhuyenMai;
import com.example.quanlykhachsan.CALL_API.API_GetThongBao;
import com.example.quanlykhachsan.KhuyenMai.KhuyenMai;
import com.example.quanlykhachsan.KhuyenMai.KhuyenMai_Adapter;
import com.example.quanlykhachsan.KhuyenMai.KhuyenMai_activity;
import com.example.quanlykhachsan.KhuyenMai.Model_KhuyenMai;
import com.example.quanlykhachsan.KhuyenMai.Model_KhuyenMaiResponse;
import com.example.quanlykhachsan.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongBao extends Fragment {
    private RecyclerView listViewPhieuDat;
    private ThongBaoAdapter_PhieuDat adapterPhieuDat;
    private ArrayList<Model_ThongBao> phieuDatList;

    private List<Model_KhuyenMai> promotions;
    private RecyclerView recyclerView;
    private KhuyenMai_Adapter adapter;
    private BottomNavigationView bottomNavigationView;

    private int totalNotifications = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_notification_fragment, container, false);

        listViewPhieuDat = view.findViewById(R.id.rvBookings);
        listViewPhieuDat.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewPhieuDat.setHasFixedSize(true);
        phieuDatList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.rvPromotions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


        promotions = new ArrayList<>();
        adapter = new KhuyenMai_Adapter(promotions, new KhuyenMai_Adapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Model_KhuyenMai promotion) {
            }
        });
        adapter.setRadioButtonVisibility(false);
        recyclerView.setAdapter(adapter);

        adapterPhieuDat = new ThongBaoAdapter_PhieuDat(getContext(), phieuDatList);
        listViewPhieuDat.setAdapter(adapterPhieuDat);


        // Gọi cả hai API để lấy thông báo
        getThongBaoDatPhong();
        loadPromotions();

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        return view;
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        // Đánh dấu tất cả thông báo là đã xem khi vào fragment
//        NotificationCountManager.getInstance(getContext()).markNotificationsAsSeen();
//    }
    public void getThongBaoDatPhong() {
        SharedPreferences sharedPreferences_id = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String CustomerId = sharedPreferences_id.getString("customer", "Qúy khách");
        API_GetThongBao apiService = ThongBaoDatPhong();

        Call<Model_ThongBaoResponse> call = apiService.getThongBaodatphong(CustomerId);
        call.enqueue(new Callback<Model_ThongBaoResponse>() {
            @Override
            public void onResponse(Call<Model_ThongBaoResponse> call, Response<Model_ThongBaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    phieuDatList.clear();
                    phieuDatList.addAll(response.body().getData());
                    adapterPhieuDat.notifyDataSetChanged();

                    // Cập nhật tổng số thông báo
                  //  updateTotalNotifications();
                } else {
                    System.out.println("Lỗi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Model_ThongBaoResponse> call, Throwable t) {
                System.out.println("Gọi API thất bại: " + t.getMessage());
            }
        });
    }

    public void loadPromotions() {
        SharedPreferences sharedPreferences_id = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String CustomerId = sharedPreferences_id.getString("customer", "Qúy khách");

        API_GetKhuyenMai apiService = GetKhuyenMai();
        Call<Model_KhuyenMaiResponse> call = apiService.getkhuyenmaidatphong(CustomerId);

        call.enqueue(new Callback<Model_KhuyenMaiResponse>() {
            @Override
            public void onResponse(Call<Model_KhuyenMaiResponse> call, Response<Model_KhuyenMaiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    promotions.clear();

                    // Lọc khuyến mãi có ngày kết thúc lớn hơn ngày hiện tại
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date currentDate = new Date(); // Lấy thời gian hiện tại

                    for (Model_KhuyenMai khuyenMai : response.body().getData()) {
                        try {
                            Date endDate = sdf.parse(khuyenMai.getEndDate());
                            if (endDate != null && endDate.after(currentDate)) {
                                promotions.add(khuyenMai); // Thêm vào danh sách nếu còn hiệu lực
                            }
                        } catch (ParseException e) {
                            e.printStackTrace(); // Xử lý lỗi phân tích ngày
                        }
                    }

                    adapter.notifyDataSetChanged(); // Cập nhật adapter

                    // Cập nhật tổng số thông báo
                    // updateTotalNotifications();
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách khuyến mãi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_KhuyenMaiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}