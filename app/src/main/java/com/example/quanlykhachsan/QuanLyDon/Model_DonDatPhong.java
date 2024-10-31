package com.example.quanlykhachsan.QuanLyDon;

public class Model_DonDatPhong {
    private String id;
    private String employeeId;
    private String customerId;
    private String customerName;
    private String phoneNumber;
    private double deposit;
    private double promotionalPrice;
    private double totalAmount;
    private double unpaidAmount;
    private String bookingDate;
    private String checkInDate;
    private String checkOutDate;

    // Constructor không tham số
    public Model_DonDatPhong() {
    }

    // Constructor đầy đủ tham số
    public Model_DonDatPhong(String id, String employeeId, String customerId, String customerName,
                             String phoneNumber, double deposit, double promotionalPrice,
                             double totalAmount, double unpaidAmount, String bookingDate,
                             String checkInDate, String checkOutDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.deposit = deposit;
        this.promotionalPrice = promotionalPrice;
        this.totalAmount = totalAmount;
        this.unpaidAmount = unpaidAmount;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    // Getter và Setter cho các thuộc tính
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getPromotionalPrice() {
        return promotionalPrice;
    }

    public void setPromotionalPrice(double promotionalPrice) {
        this.promotionalPrice = promotionalPrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(double unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

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
}
