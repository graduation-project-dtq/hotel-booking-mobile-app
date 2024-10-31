package com.example.quanlykhachsan.Login;

public class Model_LoginGG {
    private String email;
    private String TenND;

    public Model_LoginGG(String email, String TenND) {
        this.email = email;
        this.TenND = TenND;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenND() {
        return TenND;
    }

    public void setTenND(String tenND) {
        TenND = tenND;
    }
}
