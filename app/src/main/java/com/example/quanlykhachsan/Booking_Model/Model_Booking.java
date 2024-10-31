package com.example.quanlykhachsan.Booking_Model;

import java.util.Date;
import java.util.List;

public class Model_Booking {

    String EmloyeeId;
    String CustomerId;
    String CheckInDate;
    String CheckOutDate;
    String VoucherId;
    Double Deposit;
    String PhoneNumber;

    List<Model_BookingRoomTypeDetail> BookingDetails;
    List<Model_BookingService> Services;

    public Model_Booking(String emloyeeId, String customerId, String checkInDate, String checkOutDate,String voucherId, Double deposit, String phoneNumber, List<Model_BookingRoomTypeDetail> bookingDetails, List<Model_BookingService> services) {
        EmloyeeId = emloyeeId;
        CustomerId = customerId;
        CheckInDate = checkInDate;
        CheckOutDate = checkOutDate;
        VoucherId=voucherId;
        Deposit = deposit;
        PhoneNumber = phoneNumber;
        BookingDetails = bookingDetails;
        Services = services;
    }

    public String getEmloyeeId() {
        return EmloyeeId;
    }

    public void setEmloyeeId(String emloyeeId) {
        EmloyeeId = emloyeeId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCheckInDate() {
        return CheckInDate;
    }

    public void setCheckInDate(String checkInDate) {
        CheckInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return CheckOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        CheckOutDate = checkOutDate;
    }

    public String getVoucherId() {
        return VoucherId;
    }

    public void setVoucherId(String voucherId) {
        VoucherId = voucherId;
    }

    public Double getDeposit() {
        return Deposit;
    }

    public void setDeposit(Double deposit) {
        Deposit = deposit;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public List<Model_BookingRoomTypeDetail> getBookingDetails() {
        return BookingDetails;
    }

    public void setBookingDetails(List<Model_BookingRoomTypeDetail> bookingDetails) {
        BookingDetails = bookingDetails;
    }

    public List<Model_BookingService> getServices() {
        return Services;
    }

    public void setServices(List<Model_BookingService> services) {
        Services = services;
    }
}
