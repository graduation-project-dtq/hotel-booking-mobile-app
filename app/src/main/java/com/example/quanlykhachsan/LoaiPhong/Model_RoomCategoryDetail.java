package com.example.quanlykhachsan.LoaiPhong;

public class Model_RoomCategoryDetail {
    private Model_RoomCategory data; // Thay đổi thành đối tượng thay vì danh sách
    private String message;
    private int statusCode;

    public Model_RoomCategory getData() {
        return data; // Thay đổi kiểu trả về
    }

    public void setData(Model_RoomCategory data) {
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
