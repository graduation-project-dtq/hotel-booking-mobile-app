package com.example.quanlykhachsan.XacnhanMail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlykhachsan.CALL_API.API_DangKy;
import com.example.quanlykhachsan.CALL_API.API_PostCodeEmail;
import com.example.quanlykhachsan.CALL_API.API_Response_Code;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.DangKyTK;
import com.example.quanlykhachsan.MainActivity;
import com.example.quanlykhachsan.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XacNhanEmail extends AppCompatActivity {

    private EditText editTextCode;
    private Button buttonVerify;
    private ImageButton back;
    private TextInputEditText edtEmail;
    private TextView textViewResend;
    private  TextInputEditText editTextCode1, editTextCode2, editTextCode3, editTextCode4, editTextCode5, editTextCode6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_email);


        //editTextCode = findViewById(R.id.textInputLayoutCode);
        textViewResend = findViewById(R.id.textViewResend);
        edtEmail =findViewById(R.id.editTextEmail);
        editTextCode1 = findViewById(R.id.editTextCode1);
        editTextCode2 = findViewById(R.id.editTextCode2);
        editTextCode3 = findViewById(R.id.editTextCode3);
        editTextCode4 = findViewById(R.id.editTextCode4);
        editTextCode5 = findViewById(R.id.editTextCode5);
        editTextCode6 = findViewById(R.id.editTextCode6);

        buttonVerify = findViewById(R.id.buttonVerify);
        back =findViewById(R.id.back);

        textViewResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(XacNhanEmail.this, "Vui lòng nhập email để gửi lại mã code!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show loading
                ProgressDialog progressDialog = new ProgressDialog(XacNhanEmail.this);
                progressDialog.setMessage("Đang xác thực...");
                progressDialog.show();

                // Create request body
                Model_GuiLaiCode xnMail = new Model_GuiLaiCode(email);

                // Log request data
                Log.d("API_REQUEST", "Email: " + xnMail.getEmail());

                API_Response_Code apiResponseCode = RetrofitLogin.PostResponseCodeEmail();
                Call<Model_GuiLaiCode> call = apiResponseCode.postResponseCodeEmail(email);

                call.enqueue(new Callback<Model_GuiLaiCode>() {
                    @Override
                    public void onResponse(Call<Model_GuiLaiCode> call, Response<Model_GuiLaiCode> response) {
                        progressDialog.dismiss();

                        if (response.isSuccessful()) {
                            Toast.makeText(XacNhanEmail.this, "Đã gửi lại mã xác thực, kiểm tra lại email của bạn!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API_ERROR", "Status Code: " + response.code());
                                Log.e("API_ERROR", "Error Body: " + errorBody);

                                JSONObject errorJson = new JSONObject(errorBody);
                                String message = errorJson.optString("message", "Lỗi không xác định");

                                String displayMessage;
                                if (message.toLowerCase().contains("email")) {
                                        displayMessage = "Email không chính xác";
                                    } else {
                                        displayMessage = message;
                                    }

                                Toast.makeText(XacNhanEmail.this, displayMessage, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                Log.e("API_ERROR", "Error parsing response", e);
                                Toast.makeText(XacNhanEmail.this,
                                        "Có lỗi xảy ra. Vui lòng thử lại sau!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model_GuiLaiCode> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("API_ERROR", "Network error", t);
                        Toast.makeText(XacNhanEmail.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String code = getCombinedCode();

                if (code.isEmpty() || email.isEmpty()) {
                    Toast.makeText(XacNhanEmail.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show loading
                ProgressDialog progressDialog = new ProgressDialog(XacNhanEmail.this);
                progressDialog.setMessage("Đang xác thực...");
                progressDialog.show();

                // Create request body
                Model_XacNhanMail xnMail = new Model_XacNhanMail(email, code);

                // Log request data
                Log.d("API_REQUEST", "Email: " + xnMail.getEmail());
                Log.d("API_REQUEST", "Code: " + xnMail.getCode());

                API_PostCodeEmail apiPostCodeEmail = RetrofitLogin.PostCodeEmail();
                Call<Model_XacNhanMail> call = apiPostCodeEmail.postCodeEmail(email, code);

                call.enqueue(new Callback<Model_XacNhanMail>() {
                    @Override
                    public void onResponse(Call<Model_XacNhanMail> call, Response<Model_XacNhanMail> response) {
                        progressDialog.dismiss();

                        if (response.isSuccessful()) {
                            Toast.makeText(XacNhanEmail.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e("API_ERROR", "Status Code: " + response.code());
                                Log.e("API_ERROR", "Error Body: " + errorBody);

                                JSONObject errorJson = new JSONObject(errorBody);
                                String message = errorJson.optString("message", "Lỗi không xác định");

                                String displayMessage;
                                if (response.code() == 400) {
                                    if (message.toLowerCase().contains("code")) {
                                        displayMessage = "Mã xác thực không chính xác";
                                    } else if (message.toLowerCase().contains("email")) {
                                        displayMessage = "Email không chính xác";
                                    } else {
                                        displayMessage = message;
                                    }
                                } else {
                                    displayMessage = message;
                                }

                                Toast.makeText(XacNhanEmail.this, displayMessage, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                Log.e("API_ERROR", "Error parsing response", e);
                                Toast.makeText(XacNhanEmail.this,
                                        "Có lỗi xảy ra. Vui lòng thử lại sau!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model_XacNhanMail> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("API_ERROR", "Network error", t);
                        Toast.makeText(XacNhanEmail.this,
                                "Lỗi kết nối: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private String getCombinedCode() {
        StringBuilder combinedCode = new StringBuilder();

        combinedCode.append(editTextCode1.getText().toString());
        combinedCode.append(editTextCode2.getText().toString());
        combinedCode.append(editTextCode3.getText().toString());
        combinedCode.append(editTextCode4.getText().toString());
        combinedCode.append(editTextCode5.getText().toString());
        combinedCode.append(editTextCode6.getText().toString());

        return combinedCode.toString(); // Trả về chuỗi kết hợp
    }

//    private void verifyCode(String enteredCode) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        String verificationCode = sharedPreferences.getString("verificationCode", "");
//
//        if (enteredCode.equals(verificationCode)) {
//            saveUserVerificationStatus(true); // Cập nhật trạng thái xác thực
//            Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
//            // Chuyển hướng đến trang chính hoặc làm gì đó sau khi xác thực thành công
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish(); // Đóng Activity hiện tại
//        } else {
//            Toast.makeText(this, "Mã xác thực không chính xác! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void saveUserVerificationStatus(boolean isVerified) {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("isVerified", isVerified);  // Lưu trạng thái xác thực
//        editor.apply();
//    }
}