package com.example.quanlykhachsan.Customer;

public class Model_CustomerReponse {
    private Model_Customer data;
    private Object additionalData;
    private String message;
    private int statusCode;
    private String code;

    public Model_CustomerReponse(Model_Customer data, Object additionalData, String message, int statusCode, String code) {
        this.data = data;
        this.additionalData = additionalData;
        this.message = message;
        this.statusCode = statusCode;
        this.code = code;
    }

    // Getters and setters
    public Model_Customer getData() {
        return data;
    }

    public void setData(Model_Customer data) {
        this.data = data;
    }

    public Object getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Object additionalData) {
        this.additionalData = additionalData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
