package com.example.quanlykhachsan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhachsan.CALL_API.API_GetCustomer;
import com.example.quanlykhachsan.CALL_API.API_Login;
import com.example.quanlykhachsan.CALL_API.API_PostLoginGG;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.CALL_API.UnsafeSSLHelper; // Nhập class UnsafeSSLHelper
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.Login.AuthInterceptor;
import com.example.quanlykhachsan.Login.Model_Login;
import com.example.quanlykhachsan.Login.Model_LoginGG;
import com.example.quanlykhachsan.Login.Model_Response;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.XacnhanMail.XacNhanEmail;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
public class MainActivity extends AppCompatActivity {
    private TokenManager tokenManager;
    private API_Login apiService;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        setupGoogleSignIn();
        checkExistingToken();
    }

    private void initializeComponents() {
        tokenManager = new TokenManager(this);
        apiService = RetrofitLogin.getApiLoginService();

        findViewById(R.id.loginButton).setOnClickListener(v -> attemptLogin());
        findViewById(R.id.signupText).setOnClickListener(v -> startActivity(new Intent(this, DangKyTK.class)));
        findViewById(R.id.googleBtn).setOnClickListener(v -> signIn());
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Đảm bảo sign out khi khởi động app
        mGoogleSignInClient.signOut();
    }

    private void checkExistingToken() {
        if (tokenManager.hasToken()) {
            startActivity(new Intent(this, Index.class));
            finish();
        }
    }

    private void attemptLogin() {
        String username = ((TextInputEditText) findViewById(R.id.TDNInput)).getText().toString();
        String password = ((TextInputEditText) findViewById(R.id.MKInput)).getText().toString();
        if (!username.isEmpty() && !password.isEmpty()) {
            loginUser(username, password);
        } else {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser(String email, String password) {
        apiService.login(new Model_Login(email, password)).enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulLogin(response.body(), email);
                } else {
                    handleLoginError(response);
                }
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Login Error", t.getMessage());
            }
        });
    }

    private void handleSuccessfulLogin(Model_Response loginResponse, String email) {
        String accessToken = loginResponse.data.tokenResponse.accessToken;
        String refreshToken = loginResponse.data.tokenResponse.refreshToken;
        String customerId = loginResponse.data.tokenResponse.getAccount().getId();

        saveUserInfo(email, customerId);
        tokenManager.saveTokens(accessToken, refreshToken);

        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Index.class));
        finish();
    }

    private void handleLoginError(Response<Model_Response> response) {
        try {
            JSONObject errorJson = new JSONObject(response.errorBody().string());
            String errorMessage = errorJson.optString("errorMessage", "Lỗi không xác định");
            Toast.makeText(this, response.code() == 406 ? "Lỗi: " + errorMessage : "Tên tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("API_ERROR", "Error parsing response", e);
            Toast.makeText(this, "Có lỗi xảy ra. Vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserInfo(String username, String customerId) {
        SharedPreferences.Editor editor = getSharedPreferences("Username", MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.apply();

        SharedPreferences.Editor editorId = getSharedPreferences("Customer", MODE_PRIVATE).edit();
        editorId.putString("customer", customerId);
        editorId.apply();
    }

    private void signIn() {
        // Sign out trước khi sign in để hiện dialog chọn tài khoản
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginWithGoogle(account);
        } catch (ApiException e) {
            Log.e("SignIn_Error", "Error code: " + e.getStatusCode());
            Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    private void loginWithGoogle(GoogleSignInAccount account) {
        Model_LoginGG modelLoginGG = new Model_LoginGG(account.getEmail(), account.getDisplayName());
        RetrofitLogin.PostLoginGG().postLoginGG(modelLoginGG).enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulLogin(response.body(), account.getEmail());
                } else {
                    Toast.makeText(MainActivity.this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}