package com.example.quanlykhachsan.Booking;

import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;

public class Model_RoomTypeDetail {
    private Model_RoomType data; // Thay đổi thành đối tượng thay vì danh sách
    private String message;
    private int statusCode;

    public Model_RoomType getData() {
        return data; // Thay đổi kiểu trả về
    }

    public void setData(Model_RoomType data) {
        this.data = data; // Thay đổi kiểu nhận vào
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

