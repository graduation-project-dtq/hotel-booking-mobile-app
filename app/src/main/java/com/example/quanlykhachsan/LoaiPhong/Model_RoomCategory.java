package com.example.quanlykhachsan.LoaiPhong;

import java.util.List;

public class Model_RoomCategory {
    private String id;
    private String name;
    private String description;
    private List<ImageRoomType> imageRoomTypes;

    public Model_RoomCategory(String id, String name, String description, List<ImageRoomType> imageRoomTypes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageRoomTypes = imageRoomTypes;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageRoomType> getImageRoomTypes() {
        return imageRoomTypes;
    }

    public void setImageRoomTypes(List<ImageRoomType> imageRoomTypes) {
        this.imageRoomTypes = imageRoomTypes;
    }

    // Inner class cho hình ảnh loại phòng
    public static class ImageRoomType {
        private String url;

        public ImageRoomType(String url) {
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
