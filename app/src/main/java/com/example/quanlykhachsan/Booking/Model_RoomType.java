package com.example.quanlykhachsan.Booking;

import java.util.List;

public class Model_RoomType {
    private String id;
    private String name;
    private int capacityMax;
    private int area;
    private String description;
    private double averageStart;
    private double discountPrice;
    private List<imageRoomTypeDetailDTOs> imageRoomTypeDetailDTOs;

    // Constructor
    public Model_RoomType(String id, String name, int capacityMax, int area, String description, double averageStart, List<imageRoomTypeDetailDTOs> imageRoomTypes,double discountPrice) {
        this.id = id;
        this.name = name;
        this.capacityMax = capacityMax;
        this.area = area;
        this.description = description;
        this.averageStart = averageStart;
        this.imageRoomTypeDetailDTOs = imageRoomTypes;
        this.discountPrice=discountPrice;
    }

    // Getters and setters


    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacityMax() {
        return capacityMax;
    }

    public void setCapacityMax(int capacityMax) {
        this.capacityMax = capacityMax;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAverageStart() {
        return averageStart;
    }

    public void setAverageStart(double averageStart) {
        this.averageStart = averageStart;
    }

    public List<imageRoomTypeDetailDTOs> getimageRoomTypeDetailDTOs() {
        return imageRoomTypeDetailDTOs;
    }

    public void setimageRoomTypeDetailDTOs(List<imageRoomTypeDetailDTOs> imageRoomTypeDetailDTOs) {
        this.imageRoomTypeDetailDTOs = imageRoomTypeDetailDTOs;
    }


    // Inner class for image details
    public static class imageRoomTypeDetailDTOs {
        private String url;

        public imageRoomTypeDetailDTOs(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}