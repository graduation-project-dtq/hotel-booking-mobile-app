package com.example.quanlykhachsan.ThongBao;

import static com.example.quanlykhachsan.CALL_API.RetrofitLogin.GetKhuyenMai;
import static com.example.quanlykhachsan.CALL_API.RetrofitLogin.ThongBaoDatPhong;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.quanlykhachsan.CALL_API.API_GetKhuyenMai;
import com.example.quanlykhachsan.CALL_API.API_GetThongBao;
import com.example.quanlykhachsan.KhuyenMai.Model_KhuyenMai;
import com.example.quanlykhachsan.KhuyenMai.Model_KhuyenMaiResponse;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.quanlykhachsan.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationCountManager {
    private static NotificationCountManager instance;
    private Context context;
    private NotificationHelper notificationHelper;
    private SharedPreferences notificationPrefs;
    private static final String LAST_CHECKED_TIME = "last_checked_time";
    private final Set<String> processedNotifications = new HashSet<>();

    private NotificationCountManager(Context context) {
        this.context = context.getApplicationContext();
        this.notificationPrefs = context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        this.notificationHelper = new NotificationHelper(context);
    }
    public void clearNotificationBadge(BottomNavigationView bottomNavigationView) {
        BadgeDrawable badge = bottomNavigationView.getBadge(R.id.navigation_notification);
        if (badge != null) {
            badge.setNumber(0);
            badge.setVisible(false);
        }
    }
    public static NotificationCountManager getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationCountManager(context);
        }
        return instance;
    }

    // Lưu thời gian khi người dùng vào tab thông báo
        public void markNotificationsAsRead(BottomNavigationView bottomNavigationView) {
            // Lưu thời gian hiện tại cộng thêm offset để match với endDateTime
            long currentTime = System.currentTimeMillis();
            notificationPrefs.edit()
                    .putLong(LAST_CHECKED_TIME, currentTime)
                    .apply();
            clearNotificationBadge(bottomNavigationView);
        }

    public void loadNotificationCount(BottomNavigationView bottomNavigationView) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String customerId = sharedPreferences.getString("customer", "Qúy khách");
        long lastCheckedTime = notificationPrefs.getLong(LAST_CHECKED_TIME,0);

        API_GetThongBao apiThongBao = ThongBaoDatPhong();
        apiThongBao.getThongBaodatphong(customerId).enqueue(new Callback<Model_ThongBaoResponse>() {
            @Override
            public void onResponse(Call<Model_ThongBaoResponse> call, Response<Model_ThongBaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Model_ThongBao> notifications = response.body().getData();
                    int newCount = countNewNotifications(notifications, lastCheckedTime);

                    // Kiểm tra và chỉ thông báo nếu thông báo chưa được xử lý
                    for (Model_ThongBao notification : notifications) {
                        String notificationId = notification.getId();
                        if (!processedNotifications.contains(notificationId) &&
                                isNewNotification(notification.getLastUpdatedTime(), lastCheckedTime)) {
                            notificationHelper.showNotification(
                                    "Thông báo mới",
                                    notification.getTitle(),
                                    notifications.indexOf(notification)
                            );
                            processedNotifications.add(notificationId); // Đánh dấu đã xử lý
                        }
                    }

                    loadPromotionCount(bottomNavigationView, newCount, lastCheckedTime);
                } else {
                    Log.e("NotificationCountManager", "Không nhận được dữ liệu từ API");
                    loadPromotionCount(bottomNavigationView, 0, lastCheckedTime);
                }
            }

            @Override
            public void onFailure(Call<Model_ThongBaoResponse> call, Throwable t) {
                Log.e("NotificationCountManager", "Lỗi khi gọi API: " + t.getMessage());
                loadPromotionCount(bottomNavigationView, 0, lastCheckedTime);
            }
        });
    }

    private int countNewNotifications(List<Model_ThongBao> notifications, long lastCheckedTime) {
        if (lastCheckedTime == 0) return notifications.size();

        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", java.util.Locale.getDefault());

        for (Model_ThongBao notification : notifications) {
            try {
                // Tiền xử lý chuỗi ngày: cắt bớt phần mili giây, chỉ giữ lại 3 chữ số
                String rawDate = notification.getLastUpdatedTime();
                String trimmedDate = rawDate.substring(0, rawDate.indexOf("+07:00")).split("\\.")[0] + "." +
                        rawDate.split("\\.")[1].substring(0, 3) + "+07:00";
                // Phân tích chuỗi ngày tháng
                Date endDate = sdf.parse(trimmedDate);
                long endDateTime = endDate.getTime();

                if (endDateTime > lastCheckedTime) {
                    count++;

                }
            } catch (Exception e) {
                Log.e("NotificationCountManager", "Lỗi khi phân tích ngày: " + notification.getLastUpdatedTime(), e);
            }
        }
        return count;
    }

    private void loadPromotionCount(BottomNavigationView bottomNavigationView, int bookingCount, long lastCheckedTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String customerId = sharedPreferences.getString("customer", "Qúy khách");

        API_GetKhuyenMai apiKhuyenMai = GetKhuyenMai();
        apiKhuyenMai.getkhuyenmaidatphong(customerId).enqueue(new Callback<Model_KhuyenMaiResponse>() {
            @Override
            public void onResponse(Call<Model_KhuyenMaiResponse> call, Response<Model_KhuyenMaiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Model_KhuyenMai> promotions = response.body().getData();
                    int newPromotions = countNewPromotions(promotions, lastCheckedTime);

                    // Kiểm tra và chỉ thông báo nếu thông báo chưa được xử lý
                    for (Model_KhuyenMai promotion : promotions) {
                        String notificationId = promotion.getId();
                        if (!processedNotifications.contains(notificationId) &&
                                isNewNotification(promotion.getLastUpdatedTime(), lastCheckedTime)) {
                            notificationHelper.showNotification(
                                    "Thông báo mới",
                                    promotion.getName(),
                                    promotions.indexOf(promotion)
                            );
                            processedNotifications.add(notificationId); // Đánh dấu đã xử lý
                        }
                    }

                    updateBadge(bottomNavigationView, bookingCount + newPromotions);
                } else {
                    updateBadge(bottomNavigationView, bookingCount);
                }
            }

            @Override
            public void onFailure(Call<Model_KhuyenMaiResponse> call, Throwable t) {
                Log.e("NotificationCountManager", "Lỗi khi gọi API khuyến mãi: " + t.getMessage());
                updateBadge(bottomNavigationView, bookingCount);
            }
        });
    }

    private int countNewPromotions(List<Model_KhuyenMai> promotions, long lastCheckedTime) {
        if (lastCheckedTime == 0) return promotions.size();

        int count = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", java.util.Locale.getDefault());

        for (Model_KhuyenMai promotion : promotions) {
            try {
                // Tiền xử lý chuỗi ngày: cắt bớt phần mili giây, chỉ giữ lại 3 chữ số
                String rawDate = promotion.getLastUpdatedTime();
                String trimmedDate = rawDate.substring(0, rawDate.indexOf("+07:00")).split("\\.")[0] + "." +
                        rawDate.split("\\.")[1].substring(0, 3) + "+07:00";

                // Phân tích chuỗi ngày tháng
                Date endDate = sdf.parse(trimmedDate);
                long endDateTime = endDate.getTime();

                if (endDateTime > lastCheckedTime) {
                    count++;
                    Log.e("NotificationCountManager", "count: " + count);
                    Log.e("NotificationCountManager", "endDateTime: " + endDateTime);
                    Log.e("NotificationCountManager", "lastCheckedTime: " + lastCheckedTime);
                }
            } catch (Exception e) {
                Log.e("NotificationCountManager", "Lỗi khi phân tích ngày: " + promotion.getLastUpdatedTime(), e);
            }
        }
        return count;
    }

    private boolean isNewNotification(String notificationTime, long lastCheckedTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", java.util.Locale.getDefault());
            String trimmedDate = notificationTime.substring(0, notificationTime.indexOf("+07:00")).split("\\.")[0] + "." +
                    notificationTime.split("\\.")[1].substring(0, 3) + "+07:00";
            Date date = sdf.parse(trimmedDate);
            return date.getTime() > lastCheckedTime;
        } catch (ParseException e) {
            return false;
        }
    }
    private void updateBadge(BottomNavigationView bottomNavigationView, int count) {
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_notification);
        badge.setNumber(count);
        badge.setVisible(count > 0);
    }
}