package com.example.quanlykhachsan.Login;

import android.content.Context;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.example.quanlykhachsan.CALL_API.API_Login;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import retrofit2.Call;

public class AuthInterceptor implements Interceptor {
    private TokenManager tokenManager;
    private Context context;
    private API_Login apiService;

    public AuthInterceptor(Context context, OkHttpClient client) {
        this.context = context;
        this.tokenManager = new TokenManager(context);
        this.apiService = RetrofitLogin.getApiLoginService();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Kiểm tra xem access token có hết hạn không
        if (tokenManager.isAccessTokenExpired()) {
            synchronized (this) {
                // Kiểm tra lại để tránh race condition
                if (tokenManager.isAccessTokenExpired()) {
                    String newAccessToken = refreshToken();
                    if (newAccessToken != null) {
                        tokenManager.saveAccessToken(newAccessToken);
                    } else {
                        tokenManager.clearTokens();
                        // Có thể thêm logic để xử lý khi không thể refresh token
                    }
                }
            }
        }

        // Tạo request mới với access token mới nhất
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + tokenManager.getAccessToken())
                .build();

        return chain.proceed(newRequest);
    }
    private String refreshToken() {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(tokenManager.getRefreshToken());
        try {
            retrofit2.Response<Model_Response> response = apiService.refreshToken(refreshRequest).execute();
            if (response.isSuccessful() && response.body() != null) {
                Model_Response modelResponse = response.body();
                // Lấy accessToken từ 'modelResponse'
                return modelResponse.data.tokenResponse.accessToken;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}