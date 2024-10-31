package com.example.quanlykhachsan.TimKiem;

import com.example.quanlykhachsan.Booking.Model_RoomType;

import java.util.List;

public class SearchRoomResponse {
    private List<List<Model_RoomType>> data;
    private String message;
    private int statusCode;
    private String code;

    public List<List<Model_RoomType>> getData() {
        return data;
    }

    public void setData(List<List<Model_RoomType>> data) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
