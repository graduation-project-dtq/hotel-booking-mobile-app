package com.example.quanlykhachsan.Service;

import java.util.List;

public class Model_Service {
    private String id;
    private String name;
    private int price;
    private String description;
    private List<ImageDTO> getImageServiceDTOs;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<ImageDTO> getGetImageServiceDTOs() { return getImageServiceDTOs; }
    public void setGetImageServiceDTOs(List<ImageDTO> getImageServiceDTOs) { this.getImageServiceDTOs = getImageServiceDTOs; }
}
