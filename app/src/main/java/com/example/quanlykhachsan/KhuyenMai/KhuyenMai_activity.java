package com.example.quanlykhachsan.KhuyenMai;

import static com.example.quanlykhachsan.CALL_API.RetrofitLogin.GetKhuyenMai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.quanlykhachsan.CALL_API.API_GetKhuyenMai;
import com.example.quanlykhachsan.R;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KhuyenMai_activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private KhuyenMai_Adapter adapter;
    private List<Model_KhuyenMai> promotions;
    private MaterialButton btnChon;
    private Model_KhuyenMai selectedPromotion;
    private Toolbar back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyen_mai);

        recyclerView = findViewById(R.id.rvPromotions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        back=findViewById(R.id.toolbarback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChon = findViewById(R.id.btnChon);
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPromotion != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("promotionName", selectedPromotion.getName());
                    resultIntent.putExtra("promotionId", selectedPromotion.getId());
                    resultIntent.putExtra("promotionPrice", String.valueOf(selectedPromotion.getDiscountAmount()));
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("promotionName", "Chọn voucher ở đây");
                    resultIntent.putExtra("promotionId", "");
                    resultIntent.putExtra("promotionPrice","0");
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        promotions = new ArrayList<>();
        adapter = new KhuyenMai_Adapter(promotions, new KhuyenMai_Adapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Model_KhuyenMai promotion) {
                selectedPromotion = promotion;
                //Toast.makeText(KhuyenMai_activity.this, "Đã chọn: " + promotion.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        loadPromotions();
    }

    private void loadPromotions() {
        SharedPreferences sharedPreferences_id = getApplication().getSharedPreferences("Customer", Context.MODE_PRIVATE);
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

                    //promotions.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(KhuyenMai_activity.this, "Không thể tải danh sách khuyến mãi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_KhuyenMaiResponse> call, Throwable t) {
                Toast.makeText(KhuyenMai_activity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}