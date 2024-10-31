package com.example.quanlykhachsan.LoaiPhong;

import java.util.List;

public class Model_RoomCategoryResponse {
    private List<Model_RoomCategory> data; // Thay đổi thành danh sách các loại phòng
    private String message;
    private int statusCode;

    public List<Model_RoomCategory> getData() {
        return data;
    }

    public void setData(List<Model_RoomCategory> data) {
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
