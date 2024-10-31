package com.example.quanlykhachsan.ThongBao;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.quanlykhachsan.Index;
import com.example.quanlykhachsan.R;

public class NotificationHelper {
    private Context context;
    private static final String CHANNEL_ID = "HOTEL_NOTIFICATION";
    private static final String CHANNEL_NAME = "Hotel Notifications";
    private static final String CHANNEL_DESC = "Notifications from Hotel App";
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String message, int notificationId) {
        // Tạo Intent để mở ứng dụng khi click vào notification
        Intent intent = new Intent(context, Index.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Xây dựng notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.hello) // Thêm icon nhỏ cho notification
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.hotel)) // Thêm logo khách sạn
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 200, 300, 400, 500})
                .setContentIntent(pendingIntent);

        // Hiển thị notification
        notificationManager.notify(notificationId, builder.build());
    }

    // Phương thức để hiển thị notification đặt phòng
    public void showBookingNotification(String roomNumber, String checkInDate) {
        String title = "Đặt phòng thành công";
        String message = "Phòng " + roomNumber + " - Check-in: " + checkInDate;
        showNotification(title, message, 1);
    }

    // Phương thức để hiển thị notification khuyến mãi
    public void showPromotionNotification(String promotionTitle, String discount) {
        String title = "Khuyến mãi mới";
        String message = promotionTitle + " - Giảm " + discount;
        showNotification(title, message, 2);
    }
}