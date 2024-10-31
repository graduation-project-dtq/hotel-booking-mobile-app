package com.example.quanlykhachsan.KhuyenMai;

import java.util.List;

public class Model_KhuyenMaiResponse {
    private List<Model_KhuyenMai> data;
    private Object additionalData;
    private String message;
    private int statusCode;
    private String code;

    // Constructor
    public Model_KhuyenMaiResponse(List<Model_KhuyenMai> data, Object additionalData,
                          String message, int statusCode, String code) {
        this.data = data;
        this.additionalData = additionalData;
        this.message = message;
        this.statusCode = statusCode;
        this.code = code;
    }

    // Getters và Setters
    public List<Model_KhuyenMai> getData() { return data; }
    public void setData(List<Model_KhuyenMai> data) { this.data = data; }

    public Object getAdditionalData() { return additionalData; }
    public void setAdditionalData(Object additionalData) { this.additionalData = additionalData; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

