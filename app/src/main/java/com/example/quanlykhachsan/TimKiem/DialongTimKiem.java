package com.example.quanlykhachsan.TimKiem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.quanlykhachsan.Booking.Model_RoomType;
import com.example.quanlykhachsan.R;

import java.util.ArrayList;
import java.util.List;

public class DialongTimKiem {

    private List<List<Model_RoomType>> roomSuggestions;
    private Context context;
    private DialongTimKiemAdapter adapter;

    public DialongTimKiem(@NonNull Context context, List<List<Model_RoomType>> roomSuggestions) {
        this.context = context;
        this.roomSuggestions = roomSuggestions;
    }

    public void show(int requiredCapacity) {  // Thêm tham số requiredCapacity
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Tạo view từ layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialong_timkiemphong, null);
        builder.setView(view);

        ListView listView = view.findViewById(R.id.room_type_list_timkiem);
        TextView tvSuggestion = view.findViewById(R.id.goiy);

        List<Model_RoomType> flattenedList = new ArrayList<>();
        for (List<Model_RoomType> suggestion : roomSuggestions) {
            flattenedList.addAll(suggestion);
        }

        adapter = new DialongTimKiemAdapter(context, flattenedList, null);
        listView.setAdapter(adapter);

        // Gọi getSuggestionText với requiredCapacity
        if (roomSuggestions.size() > 1) {
            tvSuggestion.setVisibility(View.VISIBLE);
            tvSuggestion.setText(getSuggestionText(roomSuggestions, requiredCapacity)); // Truyền vào requiredCapacity
        } else {
            tvSuggestion.setVisibility(View.GONE);
        }

        // Thêm nút hủy
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog khi nhấn nút hủy
            }
        });

        // Tạo dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private String getSuggestionText(List<List<Model_RoomType>> suggestions, int requiredCapacity) {
        StringBuilder sb = new StringBuilder();
        boolean foundSuggestion = false;
        sb.append("Gợi ý tìm kiếm cho bạn: \n");
        // Kiểm tra xem có phòng nào đủ số người yêu cầu không
        for (List<Model_RoomType> suggestion : suggestions) {
            if (suggestion.size() > 0) {
                Model_RoomType room = suggestion.get(0);
                int capacity = room.getCapacityMax();

                // Nếu có phòng đủ số người yêu cầu
                if (capacity == requiredCapacity) {
                    sb.append("Chọn 1 phòng ").append(capacity).append(" người\n");
                    foundSuggestion = true;
                }
            }
        }

        // Tìm các tổ hợp phòng
        List<Integer> availableCapacities = new ArrayList<>();
        for (List<Model_RoomType> suggestion : suggestions) {
            for (Model_RoomType room : suggestion) {
                int capacity = room.getCapacityMax();
                if (!availableCapacities.contains(capacity)) {
                    availableCapacities.add(capacity);
                }
            }
        }

        // Tạo gợi ý cho tổ hợp phòng
        List<String> combinations = new ArrayList<>();
        for (int i = 0; i < availableCapacities.size(); i++) {
            for (int j = i + 1; j < availableCapacities.size(); j++) {
                int sum = availableCapacities.get(i) + availableCapacities.get(j);
                if (sum == requiredCapacity) {
                    combinations.add("Chọn 1 phòng " + availableCapacities.get(i) + " người và 1 phòng " + availableCapacities.get(j) + " người.");
                    foundSuggestion = true;
                }
            }
        }

        // Xử lý kết quả gợi ý
        if (foundSuggestion) {
           //sb.append("Gợi ý tìm kiếm cho bạn: \n");
            if (combinations.size() > 0) {
                sb.append(",").append(String.join(" hoặc ", combinations)).append("\n");
            }
        } else {
            return "Gợi ý tìm kiếm cho bạn.";
        }

        return sb.toString();
    }


}
