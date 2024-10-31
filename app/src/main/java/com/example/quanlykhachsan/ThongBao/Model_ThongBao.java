package com.example.quanlykhachsan.ThongBao;

public class Model_ThongBao {
    private String id;
    private String customerId;
    private String title;
    private String content;
    private String lastUpdatedTime;

    // Constructor
    public Model_ThongBao(String id, String customerId, String title, String content) {
        this.id = id;
        this.customerId = customerId;
        this.title = title;
        this.content = content;
    }

    // Getters và Setters

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content;}
    public void setContent(String content) { this.content = content; }
}
