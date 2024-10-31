package com.example.quanlykhachsan.Booking_Model;

public class Model_KiemTraPhongTrong {
    private String checkInDate;
    private String checkOutDate;
    private String roomTypeDetailID;

    public Model_KiemTraPhongTrong(String checkInDate, String checkOutDate, String roomTypeDetailID) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomTypeDetailID = roomTypeDetailID;
    }

    // Getter và Setter
    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getRoomTypeDetailID() {
        return roomTypeDetailID;
    }

    public void setRoomTypeDetailID(String roomTypeDetailID) {
        this.roomTypeDetailID = roomTypeDetailID;
    }
}
