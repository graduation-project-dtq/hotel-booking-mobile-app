package com.example.quanlykhachsan.Booking_Model;

public class BookingChoXacNhanItem {
    private int imagephong;
    private String tenLoaiphong;
    private String tangPhong;
    private String thanhTien;
    private String slPhong;
    private String tienPhong;


    public BookingChoXacNhanItem(int imagephong, String tenLoaiphong, String tangPhong, String thanhTien, String slPhong, String tienPhong) {
        this.imagephong = imagephong;
        this.tenLoaiphong = tenLoaiphong;
        this.tangPhong = tangPhong;
        this.thanhTien = thanhTien;
        this.slPhong = slPhong;
        this.tienPhong = tienPhong;
    }

    public int getImagephong() {
        return imagephong;
    }

    public String getTenLoaiphong() {
        return tenLoaiphong;
    }

    public String getTangPhong() {
        return tangPhong;
    }

    public String getThanhTien() {
        return thanhTien;
    }

    public String getSlPhong() {
        return slPhong;
    }

    public String getTienPhong() {
        return tienPhong;
    }
}