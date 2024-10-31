package com.example.quanlykhachsan;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.quanlykhachsan.CALL_API.API_DangKy;
import com.example.quanlykhachsan.CALL_API.API_GetService;
import com.example.quanlykhachsan.CALL_API.API_Login;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.Login.Model_Login;
import com.example.quanlykhachsan.Login.Model_Response;
import com.example.quanlykhachsan.XacnhanMail.JavaMailAPI;
import com.example.quanlykhachsan.XacnhanMail.XacNhanEmail;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyTK extends AppCompatActivity {

    private EditText TenDN, Email, MatKhau;
    private Button btnDangKy;
    API_DangKy api_dangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_tk);

        // Ánh xạ các view
        TenDN = findViewById(R.id.usernameInput);
        Email = findViewById(R.id.emailInput);
        MatKhau = findViewById(R.id.passwordInput);
        btnDangKy = findViewById(R.id.signUpButton);

        api_dangKy = RetrofitLogin.DangKyTaiKhoan();

        // Sự kiện khi nhấn nút đăng ký
        btnDangKy.setOnClickListener(v -> {
            String tenDN = TenDN.getText().toString();
            String email = Email.getText().toString();
            String matKhau = MatKhau.getText().toString();

            if (tenDN.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(DangKyTK.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                Model_DangKy customer = new Model_DangKy(tenDN, email, matKhau);

                // Gọi API đăng ký
                Call<Model_DangKy> call = api_dangKy.postDangKy(customer);

                call.enqueue(new Callback<Model_DangKy>() {
                    @Override
                    public void onResponse(Call<Model_DangKy> call, Response<Model_DangKy> response) {
                        if (response.isSuccessful()) {
                            // Gửi email xác nhận đăng ký thành công
                            Log.e("Email","Email DK" + email);
                            //String verificationCode = generateVerificationCode(); // Mã xác thực
                            //sendConfirmationEmail(email, verificationCode);
                            //saveUserVerificationCode(verificationCode);
                            //saveUserVerificationStatus(false);
                            Toast.makeText(DangKyTK.this, "Đăng ký thành công! Vui lòng kiểm tra email.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), XacNhanEmail.class);
                            startActivity(intent);
                        } else {
                            check_account(email,tenDN);
                        }
                    }
                    @Override
                    public void onFailure(Call<Model_DangKy> call, Throwable t) {
                        Log.e("API Error", t.getMessage());
                        Toast.makeText(DangKyTK.this, "Đăng ký thất bại! Thử lại sau.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
//    private String generateVerificationCode() {
//        Random random = new Random();
//        // Tạo mã xác thực ngẫu nhiên từ 100000 đến 999999
//        int code = 100000 + random.nextInt(900000);
//        return String.valueOf(code); // Chuyển đổi thành chuỗi
//    }
//
//    private void saveUserVerificationStatus(boolean isVerified) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isVerified", isVerified);  // Lưu trạng thái xác thực
//        editor.apply();
//    }
//    private void saveUserVerificationCode(String code) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("verificationCode", code);  // Lưu mã xác thực
//        editor.apply();
//    }


    //    private void sendConfirmationEmail(String email) {
//        if (email == null || email.isEmpty()) {
//            Log.e("Email Error", "Email rỗng");
//            Toast.makeText(this, "Email không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//            return; // Không tiếp tục nếu email không hợp lệ
//        }
//
//        String subject = "Xác nhận đăng ký tài khoản";
//        String message = "Chào mừng bạn! Nhấp vào liên kết sau để xác nhận tài khoản của bạn: \n" +
////                "http://127.0.0.1:8000/verify?email=" + email;
//                "https://localhost:7227/swagger/index.html";
//
//        JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, message);
//        javaMailAPI.execute(); // Gửi email xác nhận
//    }
//    private void sendConfirmationEmail(String email, String verificationCode) {
//        if (email == null || email.isEmpty()) {
//            Log.e("Email Error", "Email rỗng");
//            Toast.makeText(this, "Email không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//            return; // Không tiếp tục nếu email không hợp lệ
//        }
//
//        String subject = "Xác nhận đăng ký tài khoản";
//        String message = "<html>" +
//                "<head>" +
//                "<style>" +
//                "body { font-family: Arial, sans-serif; margin: 20px; padding: 0; background-color: #f9f9f9; }" +
//                ".container { max-width: 600px; margin: auto; padding: 20px; background-color: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); }" +
//                "h2 { color: #333; }" +
//                "a { display: inline-block; padding: 10px 15px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; }" +
//                "a:hover { background-color: #45a049; }" +
//                "p { color: #555; line-height: 1.6; }" +
//                ".verification-code { font-size: 20px; font-weight: bold; color: #333; }" +
//                "</style>" +
//                "</head>" +
//                "<body>" +
//                "<div class='container'>" +
//                "<h2>Chào mừng bạn đến với dịch vụ của chúng tôi!</h2>" +
////                "<a href='http://example.com/verify?email=" + email + "'>Xác nhận tài khoản</a>" +
//                "<a href='#" + email + "'>Xác nhận tài khoản</a>" +
//                "<p>Mã xác thực của bạn là: <span class='verification-code'>" + verificationCode + "</span></p>" +
//                "<p>Xin cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!</p>" +
//                "</div>" +
//                "</body>" +
//                "</html>";
//
//
//
//        JavaMailAPI javaMailAPI = new JavaMailAPI(email, subject, message);
//        javaMailAPI.execute(); // Gửi email xác nhận
//    }

    private void handleAPIError(Response<Model_DangKy> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("API Error", errorBody);
            String email = Email.getText().toString();
            String matKhau = MatKhau.getText().toString();
            Toast.makeText(this, "Email đã tồn tại! Vui lòng tạo email khác", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Đăng ký thất bại và không thể lấy chi tiết lỗi!", Toast.LENGTH_SHORT).show();
        }
    }
    private void check_account(String email, String password) {
        API_Login apiService = RetrofitLogin.getApiLoginService();
        Model_Login loginRequest = new Model_Login(email, password);
        Call<Model_Response> call1 = apiService.login(loginRequest);
        call1.enqueue(new Callback<Model_Response>() {
            @Override
            public void onResponse(Call<Model_Response> call, Response<Model_Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_Response loginResponse = response.body();
                    // Xử lý khi đăng nhập thành công nếu cần
                } else {
                    try {
                        // Lấy lỗi từ response
                        String errorBody = response.errorBody().string();
                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("errorMessage", "Lỗi không xác định");

                        if (response.code() == 406) {
                            // Trường hợp tài khoản chưa được kích hoạt
                            Toast.makeText(DangKyTK.this, errorMessage + " Vui lòng xác thực!!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), XacNhanEmail.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DangKyTK.this, "Email đã tồn tại! Vui lòng tạo email khác", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error parsing response", e);
                        Toast.makeText(DangKyTK.this, "Có lỗi xảy ra. Vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model_Response> call, Throwable t) {
                Toast.makeText(DangKyTK.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Loi", t.getMessage());
            }
        });
    }
}
