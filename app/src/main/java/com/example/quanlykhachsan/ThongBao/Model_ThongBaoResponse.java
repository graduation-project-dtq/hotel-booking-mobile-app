package com.example.quanlykhachsan.ThongBao;

import java.util.List;

public class Model_ThongBaoResponse {
    private List<Model_ThongBao> data;
    private Object additionalData;
    private String message;
    private int statusCode;
    private String code;

    // Constructor
    public Model_ThongBaoResponse(List<Model_ThongBao> data, Object additionalData,
                                  String message, int statusCode, String code) {
        this.data = data;
        this.additionalData = additionalData;
        this.message = message;
        this.statusCode = statusCode;
        this.code = code;
    }

    // Getters và Setters
    public List<Model_ThongBao> getData() { return data; }
    public void setData(List<Model_ThongBao> data) { this.data = data; }

    public Object getAdditionalData() { return additionalData; }
    public void setAdditionalData(Object additionalData) { this.additionalData = additionalData; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
