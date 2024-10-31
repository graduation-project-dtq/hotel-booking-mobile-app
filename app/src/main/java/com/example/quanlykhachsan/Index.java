package com.example.quanlykhachsan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import com.example.quanlykhachsan.Booking.DatPhong_fragment;
import com.example.quanlykhachsan.Home.Home_fragment;
import com.example.quanlykhachsan.Profile.ThongTinCaNhan_fragment;
import com.example.quanlykhachsan.ThongBao.NotificationCountManager;
import com.example.quanlykhachsan.ThongBao.ThongBao;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Index extends AppCompatActivity  {
    private BottomNavigationView bottomNavigationView;
    private NotificationCountManager notificationManager;

    // Khai báo Handler và Runnable
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable notificationPollingRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Intent intent = getIntent();
        // Hiển thị thông báo dựa trên các tham số từ Intent
        showAlerts(intent);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        notificationManager = NotificationCountManager.getInstance(this);
        // Load số lượng thông báo ngay khi vào app
       // notificationManager.loadNotificationCount(bottomNavigationView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    replaceFragment(new Home_fragment());
                } else if (itemId == R.id.navigation_notification) {
                    replaceFragment(new ThongBao());
                    notificationManager.markNotificationsAsRead(bottomNavigationView);
                } else if (itemId == R.id.navigation_booking) {
                    replaceFragment(new DatPhong_fragment());
                } else if (itemId == R.id.navigation_profile) {
                    replaceFragment(new ThongTinCaNhan_fragment());
                }
                return true;
            }
        });

        // Khởi tạo fragment mặc định
        if (savedInstanceState == null) {
            replaceFragment(new Home_fragment());
        }
        // Khởi tạo Runnable cho polling
        notificationPollingRunnable = new Runnable() {
            @Override
            public void run() {
                // Chỉ load nếu không đang ở tab thông báo
                if (bottomNavigationView.getSelectedItemId() != R.id.navigation_notification) {
                    notificationManager.loadNotificationCount(bottomNavigationView);
                }
                handler.postDelayed(this, 3000);
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.post(notificationPollingRunnable); // Bắt đầu polling
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(notificationPollingRunnable); // Dừng polling khi Activity không còn hiển thị
    }

    private void showAlerts(Intent intent) {
        boolean showAlert = intent.getBooleanExtra("showAlert", false);
        boolean showAlertcancel = intent.getBooleanExtra("showAlertcancle", false);
        boolean showAlerterror = intent.getBooleanExtra("showAlerterror", false);
        boolean showAlertpay = intent.getBooleanExtra("showAlertpay", false);
        boolean showAlertHuyphong= intent.getBooleanExtra("showAlertHuyphong", false);

        if (showAlert) {
            showDialog("Xác nhận đặt phòng", "Đơn đặt phòng của bạn đã được thanh toán, Bạn vui lòng kiểm tra lại email để xác nhận thông tin, Xin cảm ơn quý khách.");
        } else if (showAlertcancel) {
            showDialog("Xác nhận đặt phòng", "Đơn đặt phòng của bạn chưa được thanh toán, Bạn vui lòng đặt phòng và thanh toán lại, Xin cảm ơn.");
        } else if (showAlerterror) {
            showDialog("Xác nhận đặt phòng", "Lỗi khi thanh toán, Xin lỗi bạn vì đang quá tải.");
        } else if (showAlertpay) {
            showDialog("Xác nhận đặt phòng", "Lỗi khi thanh toán, Xin lỗi bạn vì đang quá tải, Bạn vui lòng đặt phòng và thanh toán lại, Xin cảm ơn.");
        } else if (showAlertHuyphong) {
            showDialog("Xác nhận hủy phòng", "Bạn đã hủy phòng thành công, xin bạn đợi khách sạn xác nhận hóa đơn hủy này, Xin cảm ơn.");
        }
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("Xác nhận", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Fragmenthome, fragment)
                .commit();
    }

}