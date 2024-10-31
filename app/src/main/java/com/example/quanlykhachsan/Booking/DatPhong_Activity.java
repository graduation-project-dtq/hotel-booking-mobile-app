package com.example.quanlykhachsan.Booking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlykhachsan.Api.CreateOrder;
import com.example.quanlykhachsan.Booking_Model.Model_Booking;
import com.example.quanlykhachsan.Booking_Model.Model_BookingRoomTypeDetail;
import com.example.quanlykhachsan.Booking_Model.Model_BookingService;
import com.example.quanlykhachsan.CALL_API.API_Booking;
import com.example.quanlykhachsan.CALL_API.API_UpdateCustomer;
import com.example.quanlykhachsan.CALL_API.API_XemChiTietRoomDetail;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.Customer.Model_Customer;
import com.example.quanlykhachsan.Customer.Model_CustomerReponse;
import com.example.quanlykhachsan.DangKy.Model_DangKy;
import com.example.quanlykhachsan.DangKyTK;
import com.example.quanlykhachsan.Helper.HMac.HMacUtil;
import com.example.quanlykhachsan.Home.Home_fragment;
import com.example.quanlykhachsan.Index;
import com.example.quanlykhachsan.KhuyenMai.KhuyenMai_activity;
import com.example.quanlykhachsan.Login.TokenManager;
import com.example.quanlykhachsan.R;
import com.example.quanlykhachsan.Service.Model_Service;
import com.example.quanlykhachsan.Service.Model_ServiceResponse;
import com.example.quanlykhachsan.Service.Service_Adapter;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.momo.momo_partner.AppMoMoLib;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import androidx.appcompat.widget.Toolbar;



//hoàn tiền zalopay
import org.apache.http.NameValuePair;
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
// https://mvnrepository.com/artifact/org.json/json

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
public class DatPhong_Activity extends AppCompatActivity {

    private DatPhong_ActivytyAdapter danhSachPhong;
    private List<Model_RoomType> roomTypes;
    private ListView selectedRoomsView;
    Map<String, Integer> selectedRooms = new HashMap<>();
    private double totalAmount = 0.0;
    private RadioButton tiencoc;
    private RadioButton thanhtoanhet;
    private  TextView tttt;
    private static final int MOMO_APP_REQUEST_CODE = 1000;
    private String EmployeeId=null;
    private String CustomerId ;
//    private Date ngayDen;
//    private Date ngayDi;

    private  String CheckInDate ;
    private  String CheckOutDate  ;
    private String PhoneNumber;

    private  Bundle roomsBundle;

    //private List Services ;
    private double Deposit ;
    private   double TongtienDP;
    private ListView list_dv;
    private Service_Adapter serviceAdapter;
    private double initialTotalAmount;
    List<Model_BookingService> Services;
    List<Model_BookingRoomTypeDetail> BookingDetails = new ArrayList<>();

    //khuyến mãi
    private EditText edtKhuyenMai;
    private String VoucherId;
    private String selectedPromotionPrice;
    private static final int REQUEST_CODE_KHUYEN_MAI = 1;
    Double tienkm = 0.0;
    private  TextView TienKM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_phong);

        //ZaloPay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(553, Environment.SANDBOX);


        // Lấy dữ lieu
        CustomerId  = getIntent().getStringExtra("CustomerId");
        CheckInDate  = getIntent().getStringExtra("NgayDen");
        CheckOutDate   = getIntent().getStringExtra("NgayDi");

        String ThanhTien = getIntent().getStringExtra("TongTien");
        TongtienDP = Double.parseDouble(ThanhTien);
        initialTotalAmount = TongtienDP;

       // Deposit =Double.parseDouble(ThanhTien);

        roomsBundle = getIntent().getBundleExtra("SelectedRooms");


        for (Map.Entry<String, Integer> entry : selectedRooms.entrySet()) {
            String roomName = entry.getKey();
            Integer quantity = entry.getValue();
            for(int i=0;i<quantity;i++)
            {
                Model_BookingRoomTypeDetail bookingRoomDetail = new Model_BookingRoomTypeDetail();
                bookingRoomDetail.setRoomTypeDetailID(roomName);
                BookingDetails.add(bookingRoomDetail);
            }
            // In ra
            System.out.println("Room: "+BookingDetails);
        }

        // Convert Bundle back to Map
        if (roomsBundle != null) {
            for (String key : roomsBundle.keySet()) {
                selectedRooms.put(key, roomsBundle.getInt(key));
            }
        }

        // Lấy tên khách hàng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TenKH", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("tenkh", "Không có tên");

        // Bind data to views
        TextView customerIdView = findViewById(R.id.TenKH);
        TextView ngayDenView = findViewById(R.id.checkin);
        TextView ngayDiView = findViewById(R.id.checkout);
        TextView ngayo = findViewById(R.id.songayo);
        TextView TongTien=findViewById(R.id.total_amount);
        TienKM=findViewById(R.id.tienkm);
        tttt=findViewById(R.id.tttt);
        selectedRoomsView = findViewById(R.id.room_list);
        EditText edtSDT=findViewById(R.id.edtSoDienThoai);
        list_dv=findViewById(R.id.service_list);
        RadioGroup chontt=findViewById(R.id.payment_method_group);
        chontt.check(R.id.deposit_radio);

        Toolbar back=findViewById(R.id.back);

         tiencoc=findViewById(R.id.deposit_radio);
         thanhtoanhet=findViewById(R.id.full_payment_radio);

        tinhTongTien();
        //Load dịch vụ
        LoadService();

        // Trong DatPhong_Activity
        tiencoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinhTongTien(); // Cập nhật lại tổng tiền
            }
        });

        thanhtoanhet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinhTongTien(); // Cập nhật lại tổng tiền
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //khuyến mãi
        edtKhuyenMai = findViewById(R.id.edtKhuyenMai);
        edtKhuyenMai.setFocusable(false);
        edtKhuyenMai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatPhong_Activity.this, KhuyenMai_activity.class);
                startActivityForResult(intent, REQUEST_CODE_KHUYEN_MAI);
            }
        });
       // Log.e("Loi","TongTien"+Deposit);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);



        Button payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PromotionInfo", "Promotion ID: " + VoucherId);
                PhoneNumber = edtSDT.getText().toString();
                if (PhoneNumber == null || PhoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                    return;
                }
                //chuyen sang zalo pay
                CreateOrder orderApi = new CreateOrder();
                try {
                    String TongTienPhaiThanhToanZALOPAY=String.format("%.0f",Deposit);
                    JSONObject data = orderApi.createOrder(TongTienPhaiThanhToanZALOPAY);

                    Log.d("Amount", TongTienPhaiThanhToanZALOPAY);
                    String code = data.getString("returncode");
                    Toast.makeText(getApplicationContext(), "return_code: " + code, Toast.LENGTH_LONG).show();

                    if (code.equals("1")) {
                        String token = data.getString("zptranstoken");
                        ZaloPaySDK.getInstance().payOrder(DatPhong_Activity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String transactionId, String transactionToken, String appTransID) {
                                saveTransactionInfo(transactionId, transactionToken, appTransID);
                                //tham so post api
                                for (Map.Entry<String, Integer> entry : selectedRooms.entrySet()) {
                                    String roomName = entry.getKey();
                                    Integer quantity = entry.getValue();
                                    for(int i=0;i<quantity;i++)
                                    {
                                        Model_BookingRoomTypeDetail bookingRoomDetail = new Model_BookingRoomTypeDetail();
                                        bookingRoomDetail.setRoomTypeDetailID(roomName);
                                        BookingDetails.add(bookingRoomDetail);
                                    }
                                }
                                Services = serviceAdapter.getSelectedServicesall();
                                for (Model_BookingService service : Services) {
                                    Log.e("Service ID: ","Ds dịch vụ" + service.getServiceID()
                                            + ", Quantity: " + service.getQuantity());
                                }

                                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                                Model_Booking booking = new Model_Booking(
                                        EmployeeId ,
                                        CustomerId ,
                                        CheckInDate ,
                                        CheckOutDate ,
                                        VoucherId,
                                        Deposit ,
                                        PhoneNumber ,
                                        BookingDetails,
                                        Services
                                );
                                Log.e("PromotionInfo", "Promotion ID: " + booking.getVoucherId());
                                // Tiến hành thanh toán
                                ThanhToan(booking);
                                //Intent intent=new Intent(DatPhong_Activity.this, Home_fragment.class);
                                //startActivity(intent);
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Intent intent = new Intent(DatPhong_Activity.this, Index.class);
                                intent.putExtra("showAlertcancle", true);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Intent intent = new Intent(DatPhong_Activity.this, Index.class);
                                intent.putExtra("showAlerterror", true);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        int soNgayO = tinhSoNgayO(CheckInDate , CheckOutDate  );
        ngayo.setText("Bạn ở: " + soNgayO + " ngày " +soNgayO + " đêm");
        TongTien.setText(String.format("Tổng tiền đặt phòng: %,.0f VNĐ",TongtienDP));
        customerIdView.setText("Tên khách hàng:" + savedName);
        ngayDenView.setText("Ngày đến: " + CheckInDate );
        ngayDiView.setText("Ngày đi: " + CheckOutDate  );
        TienKM.setText(String.format("Tiền khuyến mãi: %,.0f VNĐ",tienkm));
        // Khởi tạo danh sách phòng
        roomTypes = new ArrayList<>();

        // Gọi loadRoomTypeDetail cho từng phòng đã chọn
        for (String roomTypeID : selectedRooms.keySet()) {
            loadRoomTypeById(roomTypeID);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_KHUYEN_MAI && resultCode == RESULT_OK && data != null) {
            String promotionName = data.getStringExtra("promotionName");
            VoucherId = data.getStringExtra("promotionId");
            selectedPromotionPrice = data.getStringExtra("promotionPrice");
            edtKhuyenMai.setText(promotionName);
            Log.e("PromotionInfo", "Promotion Name: " + promotionName);
            Log.e("PromotionInfo", "Promotion ID: " + VoucherId);
            Log.e("PromotionInfo", "Promotion Price: " + selectedPromotionPrice);
            tinhTongTien();
            TienKM.setText(String.format("Tiền khuyến mãi: - %,.0f VNĐ",tienkm));
        }
    }

    // Phương thức để lấy ID khuyến mãi đã chọn (nếu cần)
    public String getSelectedPromotionId() {
        return VoucherId;
    }
    public String getSelectedPromotionPrice() {
        return selectedPromotionPrice;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private static Logger logger = Logger.getLogger(DatPhong_Activity.class.getName());

    private static Map<String, String> config = new HashMap<String, String>(){{
        put("app_id", "2553");
        put("key1", "PcY4iZIKFCIdgZvA6ueMcMHHUbRLYjPL");
        put("key2", "kLtgPl8HHhfvMuDHPwKfgfsY4Ydm9eIz");
        put("refund_url", "https://sb-openapi.zalopay.vn/v2/refund");
    }};

    private static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
    private void ThanhToan(Model_Booking booking) {
        TokenManager tokenManager = new TokenManager(getApplicationContext());
        // Lấy token từ TokenManager
        String accessToken = tokenManager.getAccessToken();
        if (accessToken == null || tokenManager.isAccessTokenExpired()) {
            Log.e("UpdateCustomer", "Access Token không hợp lệ hoặc đã hết hạn!");
            // TODO: Thực hiện refresh token ở đây
            return;
        }
        API_Booking apiBooking;
        apiBooking = RetrofitLogin.DatPhong();

        // Tạo đối tượng booking với các tham số cần thiết
        // Gọi API
        Call<Model_Booking> call = apiBooking.postBooking(booking ,"Bearer " + accessToken);

        call.enqueue(new Callback<Model_Booking>() {
            @Override
            public void onResponse(Call<Model_Booking> call, Response<Model_Booking> response) {
                String jsonResponse = new Gson().toJson(response.body());
                Log.d("API Success", "JSON Response: " + jsonResponse);
                if (response.isSuccessful()) {
                    // Nếu thành công, hiển thị thông báo cho người dùng
                    Intent intent = new Intent(DatPhong_Activity.this, Index.class);
                    intent.putExtra("showAlert", true);  // Truyền giá trị để báo hiệu cần hiển thị dialog
                    startActivity(intent);
                    finish();
                } else {
                    handleBookingFailure(response);
                    //hoanTien(Deposit);
//                    Intent intent=new Intent(DatPhong_Activity.this, Index.class);
//                    intent.putExtra("showAlertpay", true);
//                    startActivity(intent);
//                    finish();


//                    try {
//                        // Lấy nội dung lỗi từ API
//                        String errorBody = response.errorBody().string();
//                        // In log chi tiết về lỗi API
//                        Log.e("API Error", "Lỗi chi tiết: " + errorBody);
//
//                        // Hiển thị thông báo lỗi cho người dùng
//                        Toast.makeText(DatPhong_Activity.this, "Thanh Toán thất bại! Lỗi: " + errorBody, Toast.LENGTH_LONG).show();
//                        Intent intent=new Intent(DatPhong_Activity.this, Index.class);
//                        intent.putExtra("showAlertpay", true);
//                        startActivity(intent);
//                        finish();
//                    } catch (Exception e) {
//                        // In ra log nếu có lỗi trong khi xử lý response
//                        Log.e("API Error", "Lỗi khi xử lý phản hồi: " + e.getMessage(), e);
//                        Toast.makeText(DatPhong_Activity.this, "Thanh Toán thất bại và không thể lấy chi tiết lỗi!", Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(DatPhong_Activity.this, Index.class);
//                        intent.putExtra("showAlertpay", true);
//                        startActivity(intent);
//                        finish();
//                    }
                }
            }

            @Override
            public void onFailure(Call<Model_Booking> call, Throwable t) {
                // Ghi lại lỗi nếu API gọi thất bại
                Log.e("API Error", "Gọi API thất bại: " + t.getMessage(), t);

                // Hiển thị thông báo lỗi cho người dùng
                Toast.makeText(DatPhong_Activity.this, "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveTransactionInfo(String transactionId, String transactionToken, String appTransID) {
        // Lưu thông tin giao dịch vào SharedPreferences hoặc cơ sở dữ liệu local
        SharedPreferences prefs = getSharedPreferences("TransactionInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("transactionId", transactionId);
        editor.putString("transactionToken", transactionToken);
        editor.putString("appTransID", appTransID);
        editor.apply();
    }
    private void handleBookingFailure(Response<Model_Booking> response) {
        // Xử lý khi booking thất bại
        try {
            String errorBody = response.errorBody().string();
            Log.e("API Error", "Lỗi chi tiết: " + errorBody);
            Toast.makeText(DatPhong_Activity.this, "Đặt phòng thất bại! Lỗi: " + errorBody, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("API Error", "Lỗi khi xử lý phản hồi: " + e.getMessage(), e);
        }
        initiateRefund();
    }
    private void initiateRefund() {
        // Lấy thông tin giao dịch đã lưu
        SharedPreferences prefs = getSharedPreferences("TransactionInfo", MODE_PRIVATE);
        String transactionId = prefs.getString("transactionId", null);

        if (transactionId != null) {
            // Gọi phương thức hoàn tiền
            hoanTien(transactionId,Deposit);
        } else {
            Log.e("Refund Error", "Không tìm thấy thông tin giao dịch để hoàn tiền");
            Toast.makeText(this, "Không thể hoàn tiền do thiếu thông tin giao dịch", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(DatPhong_Activity.this, Index.class);
        intent.putExtra("showAlertpay", true);
        startActivity(intent);
        finish();
    }

    public static void hoanTien(String transactionId,double soTienHoan) {
        HttpURLConnection conn = null;
        BufferedReader rd = null;

        try {
            String appid = config.get("app_id");
            long timestamp = System.currentTimeMillis();
            String uid = timestamp + "" + (111 + new Random().nextInt(888));

            Log.d("Loi", "Bắt đầu hoàn tiền với số tiền: " + soTienHoan);
            Log.d("Loi", "AppID: " + appid + ", UID: " + uid);

            // Tạo dữ liệu đơn hàng cần hoàn tiền
            Map<String, Object> order = new HashMap<String, Object>(){{
                put("app_id", appid);
                put("zp_trans_id", "123456789"); // Thay bằng mã giao dịch thực tế
                put("m_refund_id", getCurrentTimeString("yyMMdd") + "_" + appid + "_" + uid);
                put("timestamp", timestamp);
                put("amount", soTienHoan);
                put("description", "Hoàn tiền giao dịch ZaloPay Demo");
            }};

            Log.d("Loi", "Dữ liệu đơn hàng: " + order.toString());

            // Tạo chữ ký HMAC
            String data = String.join("|",
                    order.get("app_id").toString(),
                    order.get("zp_trans_id").toString(),
                    order.get("amount").toString(),
                    order.get("description").toString(),
                    order.get("timestamp").toString()
            );

            String mac = HMacUtil.HMacHexStringEncode(
                    HMacUtil.HMACSHA256, config.get("key1"), data
            );
            order.put("mac", mac);

            Log.d("Loi", "Chữ ký MAC được tạo: " + mac);

            // Tạo yêu cầu POST
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> entry : order.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append('=')
                        .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }

            // Khởi tạo kết nối HTTP
            URL url = new URL(config.get("refund_url"));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Gửi dữ liệu
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(postData.toString());
                wr.flush();
            }

            // Đọc phản hồi từ server
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            Log.d("Loi", "Phản hồi từ server: " + resultJsonStr.toString());

            // Phân tích phản hồi JSON
            JSONObject jsonResponse = new JSONObject(resultJsonStr.toString());
            int returnCode = jsonResponse.getInt("return_code");
            String returnMessage = jsonResponse.optString("return_message", "Không có thông điệp");

            if (returnCode == 1) {
                Log.d("Loi", "Hoàn tiền thành công: " + returnMessage);
            } else {
                int subReturnCode = jsonResponse.optInt("sub_return_code", 0);
                String subReturnMessage = jsonResponse.optString("sub_return_message", "Không có thông tin chi tiết");
                Log.e("Loi", "Hoàn tiền thất bại: " + returnMessage);
                Log.e("Loi", "Chi tiết lỗi: " + subReturnCode + " - " + subReturnMessage);
            }

        } catch (IOException | JSONException e) {
            Log.e("Loi", "Lỗi trong quá trình hoàn tiền: " + e.getMessage(), e);
        } finally {
            // Đảm bảo đóng kết nối và luồng đọc
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    Log.e("Loi", "Lỗi khi đóng BufferedReader: " + e.getMessage(), e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    private void tinhTongTien() {
          Deposit=TongtienDP ;
        if (selectedPromotionPrice != null && !selectedPromotionPrice.trim().isEmpty()) {
            tienkm = Double.parseDouble(selectedPromotionPrice.trim());
        }
        if (tiencoc.isChecked()) {
            Deposit = TongtienDP * 0.5 - tienkm; // Giảm 50% nếu chọn đặt cọc
            Log.e("PromotionInfo", "Promotion Price: " + tienkm);
            Log.e("PromotionInfo", "Promotion Price: " + Deposit);

        } else if (thanhtoanhet.isChecked()) {
            Deposit = TongtienDP - tienkm; // Giữ nguyên tổng tiền nếu chọn thanh toán hết
        }

        // Định dạng số tiền
        String formattedText = String.format("Tổng tiền cần thanh toán: <font color='#FF0000'>%,.0f VNĐ</font> ", Deposit);
        tttt.setText(Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY));

    }
    private void loadRoomTypeDetail(String roomTypeID) {
        API_XemChiTietRoomDetail apiService = RetrofitLogin.XemchiTietLoaiPhong_Detail();
        Call<Model_RoomTypeDetail> call = apiService.XemChiTietLoaiPhong_Detail(roomTypeID);

        call.enqueue(new Callback<Model_RoomTypeDetail>() {
            @Override
            public void onResponse(Call<Model_RoomTypeDetail> call, Response<Model_RoomTypeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_RoomTypeDetail roomTypeResponse = response.body();
                    if (roomTypeResponse.getStatusCode() == 200) {
                        // Thêm phòng vào danh sách
                        roomTypes.add(roomTypeResponse.getData());

                        // Cập nhật Adapter khi đã tải xong tất cả phòng
                        DatPhong_ActivytyAdapter adapter = new DatPhong_ActivytyAdapter(getApplicationContext(), roomTypes,selectedRooms);
                        selectedRoomsView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi: " + roomTypeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể tải thông tin phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_RoomTypeDetail> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm mới để tải thông tin phòng dựa trên ID
    private void loadRoomTypeById(String roomTypeID) {
        API_XemChiTietRoomDetail apiService = RetrofitLogin.XemchiTietLoaiPhong_Detail();
        Call<Model_RoomTypeDetail> call = apiService.XemChiTietLoaiPhong_Detail(roomTypeID);

        call.enqueue(new Callback<Model_RoomTypeDetail>() {
            @Override
            public void onResponse(Call<Model_RoomTypeDetail> call, Response<Model_RoomTypeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_RoomTypeDetail roomTypeResponse = response.body();
                    if (roomTypeResponse.getStatusCode() == 200) {
                        // Thêm phòng vào danh sách
                        roomTypes.add(roomTypeResponse.getData());

                        // Cập nhật Adapter khi đã tải xong tất cả phòng
                        DatPhong_ActivytyAdapter adapter = new DatPhong_ActivytyAdapter(getApplicationContext(), roomTypes,selectedRooms);
                        selectedRoomsView.setAdapter(adapter);

                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi: " + roomTypeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không thể tải thông tin phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_RoomTypeDetail> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int tinhSoNgayO(String ngayDen, String ngayDi) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dateDen = sdf.parse(CheckInDate );
            Date dateDi = sdf.parse(CheckOutDate );

            long diffInMillies = Math.abs(dateDi.getTime() - dateDen.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            return (int) diff;
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tính số ngày ở", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

//    private void requestPayment() {
//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // Sử dụng PRODUCTION cho môi trường thật
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//
//        Map<String, Object> eventValue = new HashMap<>();
//        eventValue.put("merchantname", "Tên Đối Tác Của Bạn"); // Thay bằng tên đối tác thật của bạn
//        eventValue.put("merchantcode", "MOMO12345678"); // Thay bằng mã đối tác thật của bạn
//        eventValue.put("amount", totalAmount);
//        eventValue.put("orderId", String.valueOf(System.currentTimeMillis()));
//        eventValue.put("orderLabel", "Mã đơn hàng");
//        eventValue.put("merchantnamelabel", "Dịch vụ");
//        eventValue.put("fee", 0);
//        eventValue.put("description", "Thanh toán đặt phòng khách sạn");
//        eventValue.put("requestId", String.valueOf(System.currentTimeMillis()));
//        eventValue.put("partnerCode", "MOMO12345678"); // Thay bằng mã đối tác thật của bạn
//
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("key1", "value1");
//            objExtraData.put("key2", "value2");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put("extraData", objExtraData.toString());
//
//        eventValue.put("extra", "");
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if(data != null) {
//                if(data.getIntExtra("status", -1) == 0) {
//                    // Thanh toán thành công
//                    String token = data.getStringExtra("data"); // Token mã hóa giao dịch
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String message = "Thanh toán thành công. Token: " + token + ", SĐT: " + phoneNumber;
//                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//                    // Xử lý logic sau khi thanh toán thành công (ví dụ: cập nhật trạng thái đặt phòng)
//                } else {
//                    // Thanh toán thất bại
//                    String message = "Thanh toán thất bại: " + data.getStringExtra("message");
//                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Toast.makeText(this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
//        }
//    }

    //Lấy dữ liệu dịch vụ
    private void LoadService() {
        Call<Model_ServiceResponse> call = RetrofitLogin.LayDLService().getAllService();

        call.enqueue(new Callback<Model_ServiceResponse>() {
            @Override
            public void onResponse(Call<Model_ServiceResponse> call, Response<Model_ServiceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Model_Service> serviceList = response.body().getData().getItems();
                    serviceAdapter = new Service_Adapter(DatPhong_Activity.this, serviceList, new Service_Adapter.OnServiceSelectedListener() {
                        @Override
                        public void onServiceSelected(double totalServicePrice,List<String> selectedIds) {
                            updateTotalAmount(totalServicePrice);
                        }
                    });
                    list_dv.setAdapter(serviceAdapter);
                } else {
                    Toast.makeText(DatPhong_Activity.this, "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_ServiceResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(DatPhong_Activity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateTotalAmount(double totalServicePrice) {
        // Lưu tổng tiền dịch vụ hiện tại
        double currentServicePrice = TongtienDP - initialTotalAmount;
        // Cập nhật TongtienDP bằng cách trừ đi tổng tiền dịch vụ cũ và cộng tổng tiền dịch vụ mới
        TongtienDP = initialTotalAmount + totalServicePrice;

        TextView TongTien = findViewById(R.id.total_amount);
        TongTien.setText(String.format("Tổng tiền đặt phòng: %,.0f VNĐ", TongtienDP));
        tinhTongTien(); // Cập nhật lại tổng tiền cần thanh toán
    }

}