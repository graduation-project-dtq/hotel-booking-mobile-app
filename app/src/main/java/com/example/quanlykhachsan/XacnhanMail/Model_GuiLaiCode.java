package com.example.quanlykhachsan.XacnhanMail;

import com.google.gson.annotations.SerializedName;

public class Model_GuiLaiCode {
    @SerializedName("email")
    private String email;


    public Model_GuiLaiCode(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
