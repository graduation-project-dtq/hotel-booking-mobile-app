package com.example.quanlykhachsan.Booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.quanlykhachsan.QuanLyDon.Booking_ChoXacNhan_Adapter;
import com.example.quanlykhachsan.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookingTab extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    private int currentTab = 0;
    Booking_ChoXacNhan_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_tab);


        ImageButton backButton = findViewById(R.id.back); // Use ImageButton, correct ID
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_layoutb);

        backButton.setOnClickListener(v -> finish()); // Simplified click listener

        BookingTabViewpageAdapter adapter = new BookingTabViewpageAdapter(this,getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Chờ xác nhận");
                            break;
                        case 1:
                            tab.setText("Đã xác nhận");
                            break;
                        case 2:
                            tab.setText("Yêu cầu hủy");
                            break;
                        case 3:
                            tab.setText("Xác nhận hủy");
                            break;
                        case 4:
                            tab.setText("Đã checkin");
                            break;
                        case 5:
                            tab.setText("Đã checkout");
                            break;
                    }
                }).attach();


        int tabPosition = getIntent().getIntExtra("tabPosition", 0);
        viewPager2.setCurrentItem(tabPosition);
        viewPager2.setOffscreenPageLimit(6); // Giữ 4 trang bên cạnh trong bộ nhớ
        viewPager2.setUserInputEnabled(true); // Cho phép vuốt ngang để chuyển tab

        // Thêm hiệu ứng chuyển trang mượt mà
        viewPager2.setPageTransformer(new MarginPageTransformer(8));
       // updateAdapterForTab(tabPosition);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }
//    private void updateAdapterForTab(int tabPosition) {
//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("f" + viewPager2.getCurrentItem());
//        if (fragment instanceof BookingChoXacNhan_fragment) {
//            ((BookingChoXacNhan_fragment) fragment).updateAdapterTab(tabPosition);
//        } else if (fragment instanceof BookingDaXacNhan_fragment) {
//            ((BookingDaXacNhan_fragment) fragment).updateAdapterTab(tabPosition);
//        } else if (fragment instanceof BookingYeuCauHuy_fragment) {
//            ((BookingYeuCauHuy_fragment) fragment).updateAdapterTab(tabPosition);
//        } else if (fragment instanceof BookingXacNhanHuy_fragment) {
//            ((BookingXacNhanHuy_fragment) fragment).updateAdapterTab(tabPosition);
//        }
//    }
    private class BookingTabViewpageAdapter extends FragmentStateAdapter {

        public BookingTabViewpageAdapter(@NonNull AppCompatActivity activity, FragmentManager fm, Lifecycle lc) {
            super(fm, lc);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new BookingChoXacNhan_fragment();
                case 1:
                    return new BookingDaXacNhan_fragment();
                case 2:
                    return new BookingYeuCauHuy_fragment();
                case 3:
                    return new BookingXacNhanHuy_fragment();
                case 4:
                    return new BookingDaCheckin_fragment();
                case 5:
                    return new BookingDaCheckout_fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 6;
        }
    }
}