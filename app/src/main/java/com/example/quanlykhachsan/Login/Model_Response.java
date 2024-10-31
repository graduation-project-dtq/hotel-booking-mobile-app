package com.example.quanlykhachsan.Login;

import com.google.gson.annotations.SerializedName;

public class Model_Response {
    public Data data;
    private String additionalData;
    private String message;
    @SerializedName("statusCode")
    private int statusCode;
    private String code;

    public static class Data {
        public TokenResponse tokenResponse;
        public TokenResponse getTokenResponse() {
            return tokenResponse;
        }

        public void setTokenResponse(TokenResponse tokenResponse) {
            this.tokenResponse = tokenResponse;
        }
    }

    public static class TokenResponse {
        public String accessToken;
        public String refreshToken;
        private Account account;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }
    }

    public static class Account {
        private String id;
        private String email;
        private String role;
        private String createdTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
    }

}