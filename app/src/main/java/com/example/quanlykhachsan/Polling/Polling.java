package com.example.quanlykhachsan.Polling;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.example.quanlykhachsan.Profile.Update_TTCN;

public class Polling {
    private Handler handler = new Handler();
    private Runnable pollingRunnable;
    private static final long POLLING_INTERVAL = 3000;


    public void startPolling() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                //updateTtcn.LoadThongTinKhachHang(email);
                handler.postDelayed(this, POLLING_INTERVAL); // Lặp lại sau mỗi POLLING_INTERVAL
            }
        };
        handler.post(pollingRunnable); // Bắt đầu polling
    }
}
