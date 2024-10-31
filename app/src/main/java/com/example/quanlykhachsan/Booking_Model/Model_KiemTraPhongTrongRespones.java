package com.example.quanlykhachsan.Booking_Model;

public class Model_KiemTraPhongTrongRespones {
    private Data data;
    private String message;
    private int statusCode;

    public Model_KiemTraPhongTrongRespones(Data data, String message, int statusCode) {
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    // Getter và Setter
    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
