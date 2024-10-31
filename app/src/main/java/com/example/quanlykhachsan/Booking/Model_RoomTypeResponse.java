package com.example.quanlykhachsan.Booking;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;

import java.util.List;

public class Model_RoomTypeResponse {
    private List<Model_RoomType> data; // Thay đổi thành danh sách các loại phòng
    private String message;
    private int statusCode;

    public List<Model_RoomType> getData() {
        return data;
    }

    public void setData(List<Model_RoomType> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
