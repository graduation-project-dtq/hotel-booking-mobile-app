package com.example.quanlykhachsan.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhachsan.R;

public class GioiThieu_Activity extends AppCompatActivity {

    private Toolbar back;
    private static final String HOTEL_ADDRESS = "140 Đ. Lê Trọng Tấn, Tây Thạnh, Tân Phú, Hồ Chí Minh";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gioi_thieu);

        TextView mapView=findViewById(R.id.map);
        back=findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });
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
            if (mapIntent.resolveActivity(getApplication().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Nếu không có Google Maps, mở trong trình duyệt
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/search/?api=1&query=" + encodedAddress));
                startActivity(browserIntent);
            }

        } catch (Exception e) {
            // Xử lý lỗi nếu có
            Toast.makeText(getApplicationContext(), "Không thể mở bản đồ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}