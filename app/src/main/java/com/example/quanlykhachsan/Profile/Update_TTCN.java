package com.example.quanlykhachsan.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.quanlykhachsan.CALL_API.API_GetCustomer;
import com.example.quanlykhachsan.CALL_API.API_UpdateCustomer;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Update_TTCN extends AppCompatActivity {

    private TextView txtEmail;
    private TextView txtFullName;
    private TextView txtBirthday;
    private TextView txtsex;
    private TextView txtsdt;
    private TextView txtcccd;
    private TextView txtdiachi;
    private Spinner spinnerGioiTinh;
    private ImageView dropdownButton;
    private TextInputEditText edtGioiTinh;
    private TextInputEditText edtNgaySinh;
    private TextInputEditText edtFullname;
    private TextInputEditText edtcccd;
    private TextInputEditText edtdiachi;
    private TextInputEditText edtsdt;

    private Handler handler = new Handler();
    private Runnable pollingRunnable;
    private static final long POLLING_INTERVAL = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ttcn);

        ImageView imgiconarrow = findViewById(R.id.iconarrow);

         txtFullName=findViewById(R.id.txtFullName);
         txtEmail=findViewById(R.id.txtEmail);
         txtBirthday=findViewById(R.id.txtBirthday);
         txtsex=findViewById(R.id.txtsex);
         txtsdt=findViewById(R.id.txtsdt);
         txtcccd=findViewById(R.id.txtcccd);
         txtdiachi=findViewById(R.id.txtdiachi);

        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        edtGioiTinh = findViewById(R.id.edtGioiTinh);
        edtsdt=findViewById(R.id.edtSDT);
        edtdiachi=findViewById(R.id.edtDiaChi);
        edtcccd=findViewById(R.id.edtcccd);
        edtFullname=findViewById(R.id.edtTenKH);
        spinnerGioiTinh = findViewById(R.id.spinnerGioiTinh);
        Button btnCapnhat=findViewById(R.id.btnCapNhat);

        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateCustomer();
            }
        });
       // dropdownButton = findViewById(R.id.dropdownButton);

        String[] gioiTinhArray = {"Chọn giới tính", "Nam", "Nữ"};

// Tạo adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, gioiTinhArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Gán adapter cho Spinner
        spinnerGioiTinh.setAdapter(adapter);

// Thiết lập sự kiện cho Spinner để hiển thị lựa chọn
        spinnerGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy giá trị được chọn (từ mảng gioiTinhArray)
                String selectedGioiTinh = gioiTinhArray[position];

                // Kiểm tra xem giá trị đã chọn có phải là "Chọn giới tính" hay không
                if (!selectedGioiTinh.equals("Chọn giới tính")) {
                    // Hiển thị giá trị đã chọn lên TextInputEditText
                    edtGioiTinh.setText(selectedGioiTinh);
                }

                // Thiết lập lại Spinner về trạng thái mặc định
                spinnerGioiTinh.setSelection(0); // Chọn vị trí đầu tiên ("Chọn giới tính")
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có lựa chọn
            }
        });

        edtNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Update_TTCN.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Định dạng ngày tháng theo yêu cầu của server (YYYY-MM-DD)
                            calendar.set(selectedYear, selectedMonth, selectedDay);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            String formattedDate = sdf.format(calendar.getTime());
                            edtNgaySinh.setText(formattedDate);
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        imgiconarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quay lại Activity trước đó
                finish(); // Sử dụng finish() cho Activity
            }
        });
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Qúy khách");
        LoadThongTinKhachHang(username);
        startPolling(username);

    }
    private void startPolling(String email) {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                LoadThongTinKhachHang(email); // Gọi hàm kiểm tra dữ liệu
                handler.postDelayed(this, POLLING_INTERVAL); // Lặp lại sau mỗi POLLING_INTERVAL
            }
        };
        handler.post(pollingRunnable); // Bắt đầu polling
    }
    private void LoadThongTinKhachHang(String email) {
        // Gọi API từ Retrofit
        API_GetCustomer api_getCustomer = RetrofitLogin.XemChiTietThongTin();

        // Thực hiện gọi API với email khách hàng
        Call<Model_CustomerReponse> call = api_getCustomer.XemChiTietThongTinCustomer(email);

        // Xử lý kết quả trả về từ API
        call.enqueue(new Callback<Model_CustomerReponse>() {
            @Override
            public void onResponse(Call<Model_CustomerReponse> call, Response<Model_CustomerReponse> response) {
                if (response.isSuccessful()) {
                    // Lấy thông tin khách hàng từ phản hồi
                    Model_CustomerReponse customerResponse = response.body();
                    if (customerResponse != null) {
                        // Bạn có thể xử lý thông tin khách hàng tại đây
                        Model_Customer customer = customerResponse.getData();
                        txtFullName.setText(customer.getName() != null ? customer.getName() : "Không có tên");
                        txtEmail.setText(customer.getEmail() != null ? customer.getEmail() : "Không có email");
                        txtBirthday.setText(customer.getDateOfBirth() != null ? customer.getDateOfBirth() : "Chưa cập nhật");
                        txtsex.setText(customer.getSex() != null ? customer.getSex() : "Chưa cập nhật");
                        txtsdt.setText(customer.getPhone() != null ? customer.getPhone() : "Chưa cập nhật ");
                        txtcccd.setText(customer.getIdentityCard() != null ? customer.getIdentityCard() : "Chưa cập nhật ");
                        txtdiachi.setText(customer.getAddress() != null ? customer.getAddress() : "Chưa cập nhật ");
                        // Ví dụ hiển thị thông tin khách hàng lên log
                        Log.d("Customer Info", "Tên: " + customer.getName());
                        Log.d("Customer Info", "Email: " + customer.getEmail());
                        Log.d("Customer Info", "Điểm tín nhiệm: " + customer.getCredibilityScore());

                        // Có thể cập nhật UI để hiển thị thông tin khách hàng
                        // Ví dụ: txtName.setText(customer.getName());
                    }
                } else {
                    // Xử lý lỗi nếu API trả về không thành công
                    Log.e("API Error", "Lỗi API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Model_CustomerReponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.e("API Error", t.getMessage());
            }
        });
    }



    private void UpdateCustomer() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("username", "Qúy khách");

        // Lấy thông tin từ các trường nhập liệu
        String name = edtFullname.getText().toString().trim();
        String dateOfBirth = edtNgaySinh.getText().toString().trim();
        String sex = edtGioiTinh.getText().toString().trim();
        String phone = edtsdt.getText().toString().trim();
        String identityCard = edtcccd.getText().toString().trim();
        String address = edtdiachi.getText().toString().trim();

        // Tạo một đối tượng Model_Customer với thông tin cập nhật
        Model_Customer updatedCustomer = new Model_Customer(
                name,
                identityCard,
                sex,
                dateOfBirth,
                phone,
                address
        );

        // Khởi tạo TokenManager
        TokenManager tokenManager = new TokenManager(getApplicationContext());

        // Lấy token từ TokenManager
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("UpdateCustomer", "Access Token không hợp lệ hoặc đã hết hạn!");
            // TODO: Thực hiện refresh token ở đây
            return;
        }

        // Gọi API để cập nhật thông tin khách hàng
        API_UpdateCustomer apiUpdateCustomer = RetrofitLogin.SuaThongTinKhachHang();
        Call<Model_Customer> call = apiUpdateCustomer.upDateCustomer(email, updatedCustomer, "Bearer " + accessToken);

        // Xử lý kết quả từ API
        call.enqueue(new Callback<Model_Customer>() {
            @Override
            public void onResponse(Call<Model_Customer> call, Response<Model_Customer> response) {
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    Log.d("UpdateCustomer", "Cập nhật thông tin khách hàng thành công!");
                    Toast.makeText(Update_TTCN.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý lỗi nếu API trả về không thành công
                    Log.e("UpdateCustomer", "Lỗi cập nhật: " + response.code());
                    if (response.code() == 400) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("UpdateCustomer", "Chi tiết lỗi 400: " + errorBody);
                            // Phân tích errorBody để lấy thông tin lỗi cụ thể
                            // Ví dụ: sử dụng JSONObject để parse errorBody nếu nó là JSON
                            JSONObject errorJson = new JSONObject(errorBody);
                            String errorMessage = errorJson.optString("message", "Lỗi không xác định");
                            Toast.makeText(Update_TTCN.this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("UpdateCustomer", "Lỗi khi đọc chi tiết lỗi: " + e.getMessage());
                            Toast.makeText(Update_TTCN.this, "Có lỗi xảy ra khi cập nhật thông tin", Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.code() == 401) {
                        // Token hết hạn hoặc không hợp lệ
                        Log.e("UpdateCustomer", "Token hết hạn hoặc không hợp lệ. Cần refresh token.");
                        Toast.makeText(Update_TTCN.this, "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
                        // TODO: Thực hiện refresh token hoặc đăng xuất người dùng
                    } else {
                        Toast.makeText(Update_TTCN.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model_Customer> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.e("UpdateCustomer", "Lỗi: " + t.getMessage());
                Toast.makeText(Update_TTCN.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}