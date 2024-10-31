package com.example.quanlykhachsan.Booking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.quanlykhachsan.CALL_API.API_GetLoaiPhong;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.LoaiPhong.DialongLoaiPhongAdapter;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategoryResponse;
import com.example.quanlykhachsan.LoaiPhong.RoomCategoryAdapter;
import com.example.quanlykhachsan.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialongDatPhong_fragment extends DialogFragment {

    private ListView roomTypeList;
    private DialongLoaiPhongAdapter dialongLoaiPhongAdapter;
    private OnRoomCategorySelectedListener listener;
    private API_GetLoaiPhong apiService;
    private List<Model_RoomCategory> roomCategories; // Lưu danh sách loại phòng

    public interface OnRoomCategorySelectedListener {
        void onRoomCategorySelected(Model_RoomCategory selectedRoomCategory);
    }

    // Constructor để truyền danh sách loại phòng và listener
    public DialongDatPhong_fragment(List<Model_RoomCategory> roomCategories, OnRoomCategorySelectedListener listener) {
        this.roomCategories = roomCategories;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialong_loaiphong, null);

        apiService = RetrofitLogin.getApiGetLoaiPhongService();
        roomTypeList = view.findViewById(R.id.room_type_list);

        dialongLoaiPhongAdapter = new DialongLoaiPhongAdapter(getContext(), roomCategories, new DialongLoaiPhongAdapter.OnRoomCategorySelectedListener() {
            @Override
            public void onRoomCategorySelected(Model_RoomCategory selectedCategory) {
                if (listener != null) {
                    listener.onRoomCategorySelected(selectedCategory);
                }
                dismiss();
            }
        });

        roomTypeList.setAdapter(dialongLoaiPhongAdapter);

        if (roomCategories.isEmpty()) {
            LoadLoaiPhong();
        }

        builder.setView(view);
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void LoadLoaiPhong() {
        Call<Model_RoomCategoryResponse> call = apiService.getAllRoomTypes();
        call.enqueue(new Callback<Model_RoomCategoryResponse>() {
            @Override
            public void onResponse(Call<Model_RoomCategoryResponse> call, Response<Model_RoomCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    roomCategories = response.body().getData();
                    if (roomCategories != null) {
                        // Cập nhật danh sách loại phòng cho adapter
                        dialongLoaiPhongAdapter.updateRoomTypes(roomCategories);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model_RoomCategoryResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }
}
