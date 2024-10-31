package com.example.quanlykhachsan.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quanlykhachsan.R;
import com.google.android.material.card.MaterialCardView;

public class HoTro_Activity extends AppCompatActivity {
    private Toolbar back;
    private MaterialCardView emailSupport;
    private MaterialCardView phoneSupport;
    private static final String EMAIL = "qd2t.hotel.management@gmail.com";
    private static final String PHONE_NUMBER = "0123456789";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_tro);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        emailSupport = findViewById(R.id.emailSupport);
        emailSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        phoneSupport = findViewById(R.id.phoneSupport);
        phoneSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }
    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // chỉ email apps sẽ handle cái này
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hỗ trợ khách hàng");

        try {
            startActivity(Intent.createChooser(intent, "Chọn ứng dụng email..."));
        } catch (ActivityNotFoundException e) {
            // Hiển thị thông báo nếu không tìm thấy ứng dụng email nào
            Toast.makeText(this,
                    "Không tìm thấy ứng dụng email nào được cài đặt",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL); // Dùng ACTION_DIAL để mở ứng dụng gọi điện
        intent.setData(Uri.parse("tel:" + PHONE_NUMBER)); // Chuyển đổi số điện thoại thành định dạng URI
        startActivity(intent); // Khởi động hoạt động gọi điện
    }
}