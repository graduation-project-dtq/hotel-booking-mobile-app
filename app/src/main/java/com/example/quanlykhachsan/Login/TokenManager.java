package com.example.quanlykhachsan.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;

public class TokenManager {
    private static final String PREF_NAME = "AuthTokens";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String ACCESS_TOKEN_EXPIRY = "access_token_expiry";
    private static final String REFRESH_TOKEN_EXPIRY = "refresh_token_expiry";

    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putString(REFRESH_TOKEN, refreshToken);
        editor.putLong(ACCESS_TOKEN_EXPIRY, System.currentTimeMillis() + 55 * 60 * 1000); // 55 phút
        editor.putLong(REFRESH_TOKEN_EXPIRY, System.currentTimeMillis() + 14 * 24 * 60 * 60 * 1000); // 14 ngày
        editor.apply();
    }

    public void saveAccessToken(String accessToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ACCESS_TOKEN, accessToken);
        editor.putLong(ACCESS_TOKEN_EXPIRY, System.currentTimeMillis() + 55 * 60 * 1000); // 55 phút
        editor.apply();
    }
    public String getTokenDebugInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Access Token: ").append(getAccessToken()).append("\n");
        info.append("Refresh Token: ").append(getRefreshToken()).append("\n");
        info.append("Access Token Expiry: ").append(new Date(prefs.getLong(ACCESS_TOKEN_EXPIRY, 0))).append("\n");
        info.append("Refresh Token Expiry: ").append(new Date(prefs.getLong(REFRESH_TOKEN_EXPIRY, 0))).append("\n");
        info.append("Is Access Token Expired: ").append(isAccessTokenExpired()).append("\n");
        info.append("Is Refresh Token Expired: ").append(isRefreshTokenExpired()).append("\n");
        return info.toString();
    }
    // Thêm phương thức này để kiểm tra nhanh xem có token hay không
    public boolean hasToken() {
        return prefs.getString(ACCESS_TOKEN, null) != null;
    }

    public String getAccessToken() {
        return prefs.getString(ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return prefs.getString(REFRESH_TOKEN, null);
    }

    public boolean isAccessTokenExpired() {
        long expiryTime = prefs.getLong(ACCESS_TOKEN_EXPIRY, 0);
        return new Date().getTime() > expiryTime;
    }

    public boolean isRefreshTokenExpired() {
        long expiryTime = prefs.getLong(REFRESH_TOKEN_EXPIRY, 0);
        return new Date().getTime() > expiryTime;
    }
        public void clearTokens() {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove(ACCESS_TOKEN);
            editor.remove(REFRESH_TOKEN);
            editor.remove(ACCESS_TOKEN_EXPIRY);
            editor.remove(REFRESH_TOKEN_EXPIRY);
            editor.apply();
        }
}