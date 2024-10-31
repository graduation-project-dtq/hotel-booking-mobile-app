package com.example.quanlykhachsan.Booking;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykhachsan.Booking_Model.Data;
import com.example.quanlykhachsan.Booking_Model.Model_KiemTraPhongTrong;
import com.example.quanlykhachsan.Booking_Model.Model_KiemTraPhongTrongRespones;
import com.example.quanlykhachsan.CALL_API.API_Booking;
import com.example.quanlykhachsan.CALL_API.API_GetLoaiPhong;
import com.example.quanlykhachsan.CALL_API.API_GetRoomTypeDetail_RoomType;
import com.example.quanlykhachsan.CALL_API.API_KiemTraPhongTrong;
import com.example.quanlykhachsan.CALL_API.API_TimKiemPhong;
import com.example.quanlykhachsan.CALL_API.RetrofitLogin;
import com.example.quanlykhachsan.LoaiPhong.DialongLoaiPhongAdapter;
import com.example.quanlykhachsan.LoaiPhong.Model_RoomCategory;
import com.example.quanlykhachsan.R;
import com.example.quanlykhachsan.TimKiem.DialongTimKiem;
import com.example.quanlykhachsan.TimKiem.SearchRoomResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatPhong_fragment extends Fragment {

    private RecyclerView roomListView;
    private RecyclerView roomListView2;
    private RecyclerView roomListView3;
    private List<Model_RoomType> roomTypes;
    private  ListView roomCategory;
    private RoomTypeAdapter roomTypeListAdapter;
    private DialongLoaiPhongAdapter dialongLoaiPhongAdapter;
    private TextView textKynghi;
    private Date ngayDi;
    private Date ngayDen;
    private int adultsCount = 0;
    private TextView tvAdultsCount;
    private TextView tvSummary;
    private CardView dropdownContent;
    private ImageView dropdownButton;

    private List<Model_RoomType> superiorRooms;
    private List<Model_RoomType> deluxeRooms;
    private List<Model_RoomType> suiteRooms;
    API_GetLoaiPhong apiService;
    private  TextView TenLoaiPhong;
    private  ImageView TimKiem;
    private String RoomTypeID;
    private String  RoomTypeDetaiID;
    private int newQuantity;
    private API_GetRoomTypeDetail_RoomType apiGetRoomTypeDetailRoomType;
    API_Booking apiBooking;
   // private String CustomerId;
    public static String CheckInDate;
    public static String CheckOutDate;
    private Button btnThemVaoDon;
    private Button TongTien;
    private  long tongTien = 0;
    private Map<String, Integer> selectedRooms = new HashMap<>();

    Model_KiemTraPhongTrong modelKiemTraPhongTrong;
    private  RoomTypeAdapter adapter;
    // Thêm biến để lưu trữ click listener
    private RoomTypeAdapter.OnFavoriteClickListener onFavoriteClickListener = new RoomTypeAdapter.OnFavoriteClickListener() {
        @Override
        public void onFavoriteClick(int position) {
            // Xử lý khi icon yêu thích được click
            // Toast.makeText(getContext(), "Yêu thích phòng " + roomTypes.get(position).getName(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.index_booking_fragment, container, false);

        apiService= RetrofitLogin.getApiGetLoaiPhongService();
        apiGetRoomTypeDetailRoomType=RetrofitLogin.getRoomTypeByCategoryService();


        // Khởi tạo roomCategories (đảm bảo roomCategories không null)
        List<Model_RoomCategory> roomCategories = new ArrayList<>();

        roomListView = view.findViewById(R.id.listviewbooking);
        //roomListView2 = view.findViewById(R.id.listviewbooking2);
        //roomListView3 = view.findViewById(R.id.listviewbooking3);
        TongTien=view.findViewById(R.id.TongTien);
        Button btnChonNgayDen = view.findViewById(R.id.btnChonNgayDen);
        Button btnChonNgayDi = view.findViewById(R.id.btnChonNgayDi);
        btnThemVaoDon=view.findViewById(R.id.btnThemVaoHoaDon);
        textKynghi = view.findViewById(R.id.textKynghi);
        tvAdultsCount = view.findViewById(R.id.tvAdultsCount);
        tvSummary = view.findViewById(R.id.tvSummary);
        ImageButton btnMinusAdults = view.findViewById(R.id.btnMinusAdults);
        ImageButton btnPlusAdults = view.findViewById(R.id.btnPlusAdults);
        Button btnDone = view.findViewById(R.id.btnDone);
        dropdownButton = view.findViewById(R.id.dropdownButton);
        dropdownContent = view.findViewById(R.id.dropdownContent);
        TenLoaiPhong=view.findViewById(R.id.TenLoaiPhong);
        TimKiem=view.findViewById(R.id.btnSearch);
        // Ẩn dropdown content ban đầu
        dropdownContent.setVisibility(View.GONE);
        //Thêm vào đơn
        btnThemVaoDon.setOnClickListener(v -> addToOrder());
        // Xử lý sự kiện click cho dropdownButton
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropdownContent.getVisibility() == View.GONE) {
                    // Hiển thị dropdown nếu nó đang ẩn
                    dropdownContent.setVisibility(View.VISIBLE);
                } else {
                    // Ẩn dropdown nếu nó đang hiển thị
                    dropdownContent.setVisibility(View.GONE);
                }
            }
        });

        // Mở DatePickerDialog để chọn ngày đến
        btnChonNgayDen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentCalendar = Calendar.getInstance();
                int currentYear = currentCalendar.get(Calendar.YEAR);
                int currentMonth = currentCalendar.get(Calendar.MONTH);
                int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
                int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

                // Kiểm tra nếu giờ hiện tại sau 14h
                if (currentHour >= 14) {
                    // Nếu sau 14h, chỉ cho đặt từ ngày mai trở về sau
                    currentCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                int year = currentCalendar.get(Calendar.YEAR);
                int month = currentCalendar.get(Calendar.MONTH);
                int day = currentCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        (view1, year1, monthOfYear, dayOfMonth) -> {
                            // Xử lý ngày được chọn
                            String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                            btnChonNgayDen.setText("Ngày đến: " + selectedDate);

                            // Lưu trữ ngày đến để kiểm tra cho ngày đi
                            Calendar ngayDenCalendar = Calendar.getInstance();
                            ngayDenCalendar.set(year1, monthOfYear, dayOfMonth);
                            ngayDen = ngayDenCalendar.getTime(); // lưu biến này để so sánh khi chọn ngày đi

                            // Định dạng ngày đến
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            CheckInDate = dateFormat.format(ngayDen);
                            Log.d("Formatted Date", "Ngày đến: " + CheckInDate);

                            updateKynghiText();
                            ngayDi = null;
                            btnChonNgayDi.setEnabled(true);
                            btnChonNgayDi.setText("Chọn ngày đi");
                            textKynghi.setText("");
                        }, year, month, day);

                // Thiết lập ngày bắt đầu là từ hôm nay hoặc ngày mai (nếu qua 14h)
                datePickerDialog.getDatePicker().setMinDate(currentCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        btnChonNgayDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ngày đến đã được chọn
                if (ngayDen != null) {
                    Calendar ngayDenCalendar = Calendar.getInstance();
                    ngayDenCalendar.setTime(ngayDen);

                    // Ngày đi phải lớn hơn ít nhất 1 ngày so với ngày đến
                    ngayDenCalendar.add(Calendar.DAY_OF_MONTH, 1);

                    int year = ngayDenCalendar.get(Calendar.YEAR);
                    int month = ngayDenCalendar.get(Calendar.MONTH);
                    int day = ngayDenCalendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            (view1, year1, monthOfYear, dayOfMonth) -> {
                                // Xử lý ngày đi được chọn
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                                btnChonNgayDi.setText("Ngày đi: " + selectedDate);
                                // Lưu trữ ngày đi
                                Calendar ngayDiCalendar = Calendar.getInstance();
                                ngayDiCalendar.set(year1, monthOfYear, dayOfMonth);
                                ngayDi = ngayDiCalendar.getTime(); // Lưu ngày đi

                                // Định dạng ngày đi
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                CheckOutDate = dateFormat.format(ngayDi);
                                Log.d("Formatted Date", "Ngày đi: " + CheckOutDate);

                                updateKynghiText();
                            }, year, month, day);

                    // Đặt ngày đi phải sau ngày đến ít nhất 1 ngày
                    datePickerDialog.getDatePicker().setMinDate(ngayDenCalendar.getTimeInMillis());
                    datePickerDialog.show();
                } else {
                    Toast.makeText(getContext(), "Vui lòng chọn ngày đến trước", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện tăng giảm số người lớn
        btnPlusAdults.setOnClickListener(v -> {
            adultsCount++;
            tvAdultsCount.setText(String.valueOf(adultsCount));
            //updateSummaryText();
        });

        btnMinusAdults.setOnClickListener(v -> {
            if (adultsCount > 0) {
                adultsCount--;
                tvAdultsCount.setText(String.valueOf(adultsCount));
               // updateSummaryText();
            }
        });

        btnDone.setOnClickListener(v -> {
            updateSummaryText();
            dropdownContent.setVisibility(View.GONE);
        });

        tvSummary.setOnClickListener(v -> dropdownContent.setVisibility(View.VISIBLE));

        TimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timKiemPhong();
            }
        });
        showRoomTypeSelectionDialog();
        tinhVaHienThiTongTien();

        //KiemTraPhongTrong(modelKiemTraPhongTrong);
        return view;
    }
    private void timKiemPhong() {
        if (adultsCount == 0 || RoomTypeID == null) {
            Toast.makeText(getContext(), "Vui lòng chọn số người và loại phòng", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("SearchRoom", "Số người: " + adultsCount + ", ID loại phòng: " + RoomTypeID);
        API_TimKiemPhong apiTimKiemPhong = RetrofitLogin.TimKiemPhongSoNguoiTheoLoai();
        Call<SearchRoomResponse> call = apiTimKiemPhong.TimKiemPhong(String.valueOf(adultsCount), RoomTypeID);

        call.enqueue(new Callback<SearchRoomResponse>() {
            @Override
            public void onResponse(Call<SearchRoomResponse> call, Response<SearchRoomResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchRoomResponse searchResponse = response.body();
                    if (searchResponse.getStatusCode() == 200) {
                        List<List<Model_RoomType>> roomSuggestions = searchResponse.getData();
                        // Giả sử bạn đang tìm kiếm cho 6 người
                        showSearchResultDialog(roomSuggestions, adultsCount);

                    } else {
                        String errorMessage = "Lỗi: " + (searchResponse.getMessage() != null ? searchResponse.getMessage() : "Không xác định");
                        showErrorMessage(errorMessage);
                    }
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String errorMessage = "Lỗi khi tìm kiếm phòng. Mã lỗi: " + response.code() + ". Chi tiết: " + errorBody;
                    showErrorMessage(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SearchRoomResponse> call, Throwable t) {
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                showErrorMessage(errorMessage);
                t.printStackTrace(); // In stack trace để debug
            }
        });
    }

    private void showErrorMessage(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
        // Log lỗi để dễ dàng debug
        Log.e("DatPhong_fragment", "Error: " + message);
    }

    private void showSearchResultDialog(List<List<Model_RoomType>> roomSuggestions, int requiredCapacity) {
        if (getContext() != null) {
            // Tạo dialog với số lượng người yêu cầu
            DialongTimKiem dialog = new DialongTimKiem(getContext(), roomSuggestions);
            dialog.show(requiredCapacity);  // Truyền số lượng người cần tìm vào show()
        } else {
            Log.e("DatPhong_fragment", "Context is null, cannot show dialog");
        }
    }

    private void updateKynghiText() {
        if (ngayDen != null && ngayDi != null) {
            long tinhNgay = ngayDi.getTime() - ngayDen.getTime();
            long soDem = tinhNgay / (1000 * 60 * 60 * 24);
            textKynghi.setText(Html.fromHtml("Bạn đã chọn cho kỳ nghỉ của mình là:" +
                    " <font color='#000000'>"+ " "+ "</font> <font color='#FF0000'>"+ soDem + " ngày</font>"));
        } else {
            textKynghi.setText("");
        }
    }

    // Cập nhật text tóm tắt trong header
    private void updateSummaryText() {
        tvSummary.setText("Bạn muốn tìm phòng cho " + adultsCount + " người");
    }

    private void showRoomTypeSelectionDialog() {
        List<Model_RoomCategory> roomCategories = new ArrayList<>();
        DialongDatPhong_fragment dialog = new DialongDatPhong_fragment(roomCategories, new DialongDatPhong_fragment.OnRoomCategorySelectedListener() {
            @Override
            public void onRoomCategorySelected(Model_RoomCategory selectedRoomCategory) {
                TenLoaiPhong.setText("Loại phòng: " + selectedRoomCategory.getName());
                RoomTypeID = selectedRoomCategory.getId();
                loadRoomTypeDetail(RoomTypeID);
            }
        });
        dialog.show(getChildFragmentManager(), "select_room_type");
    }

    private void loadRoomTypeDetail(String roomTypeID) {
        Call<Model_RoomTypeResponse> call = apiGetRoomTypeDetailRoomType.getRoomTypesByCategory(roomTypeID);
        call.enqueue(new Callback<Model_RoomTypeResponse>() {
            @Override
            public void onResponse(Call<Model_RoomTypeResponse> call, Response<Model_RoomTypeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_RoomTypeResponse roomTypeResponse = response.body();
                    if (roomTypeResponse.getStatusCode() == 200) {
                        roomTypes = roomTypeResponse.getData();
                        // Khởi tạo RoomTypeAdapter sau khi lấy dữ liệu
                         adapter = new RoomTypeAdapter(getContext(), roomTypes,
                                position -> {
                                    // Handle favorite click
                                },
                                (roomTypeId, quantity, price) -> {
                                    if (quantity > 0) {
                                        selectedRooms.put(roomTypeId, quantity);
                                    } else {
                                        selectedRooms.remove(roomTypeId);
                                    }
                                    tinhVaHienThiTongTien();
                                },
                                 (roomTypeId, requestedQuantity) -> {
                                     RoomTypeDetaiID = SharedData.RoomTypeDetaiID;
                                     newQuantity=SharedData.Quantity;
                                     Log.e("soluong","soluongchon"+newQuantity);
                                     modelKiemTraPhongTrong = new Model_KiemTraPhongTrong(CheckInDate, CheckOutDate, RoomTypeDetaiID);
                                     KiemTraPhongTrong(modelKiemTraPhongTrong, (available, message, availableRooms) -> {
                                         Log.e("soluong","soluongtrong"+availableRooms);
                                         if ( newQuantity <= availableRooms) {
                                             adapter.updateRoomQuantity(roomTypeId, requestedQuantity, availableRooms);
                                         } else {
                                             String thongBao = available ?
                                                     "Chỉ còn " + availableRooms + " phòng trống." :
                                                     "Không còn phòng trống.";
                                             Toast.makeText(getContext(), thongBao, Toast.LENGTH_SHORT).show();
                                             adapter.updateRoomQuantity(roomTypeId, Math.min(requestedQuantity - 1, availableRooms), availableRooms);
                                         }
                                     });
                                 }
                        );
                        roomListView.setLayoutManager(new LinearLayoutManager(getContext()));
                        roomListView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "Lỗi: " + roomTypeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải thông tin phòng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Model_RoomTypeResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToOrder() {
        SharedPreferences sharedPreferences_id = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        String CustomerId = sharedPreferences_id.getString("customer", "Qúy khách");

        if (CustomerId == null || CheckInDate == null || CheckOutDate == null) {
            Toast.makeText(getContext(), "Vui lòng chọn đầy đủ thông tin đặt phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRooms.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ít nhất một phòng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính tổng tiền một lần nữa để đảm bảo giá trị mới nhất
        tinhVaHienThiTongTien();

        // Create an Intent to start DatPhong_Activity
        Intent intent = new Intent(getActivity(), DatPhong_Activity.class);
        String strTongTien = String.valueOf(tongTien);

        // Pass data to the Intent
        intent.putExtra("CustomerId", CustomerId);
        intent.putExtra("NgayDen", CheckInDate);
        intent.putExtra("NgayDi", CheckOutDate);
        intent.putExtra("TongTien", strTongTien);
        Log.d("Thanhtien", "Tổng tiền: " + tongTien);

        // Convert selectedRooms map to a Bundle
        Bundle roomsBundle = new Bundle();
        for (Map.Entry<String, Integer> entry : selectedRooms.entrySet()) {
            roomsBundle.putInt(entry.getKey(), entry.getValue());
        }
        intent.putExtra("SelectedRooms", roomsBundle);

        // Start the DatPhong_Activity
        startActivity(intent);
    }
    private void tinhVaHienThiTongTien() {
        tongTien = 0; // Reset tổng tiền về 0 trước khi tính toán
        long soNgay = 0;
        if (ngayDen != null && ngayDi != null) {
            soNgay = (ngayDi.getTime() - ngayDen.getTime()) / (1000 * 60 * 60 * 24);
        }
        if(roomTypes != null && soNgay > 0) {
            for (Model_RoomType roomType : roomTypes) {
                int soLuong = selectedRooms.getOrDefault(roomType.getId(), 0);
                tongTien += roomType.getDiscountPrice() * soLuong * soNgay;
            }
        }
        TongTien.setText(String.format("Tổng tiền: %,d VNĐ", tongTien));
    }
    public String getFormattedNgayDen() {
        return CheckInDate;
    }

    public String getFormattedNgayDi() {
        return CheckOutDate;
    }

    public void KiemTraPhongTrong(Model_KiemTraPhongTrong phongTrong, RoomAvailabilityCallback callback) {
        Log.d("KiemTraPhongTrong", "CheckInDate: " + phongTrong.getCheckInDate());
        Log.d("KiemTraPhongTrong", "CheckOutDate: " + phongTrong.getCheckOutDate());
        Log.d("KiemTraPhongTrong", "RoomTypeDetailID: " + phongTrong.getRoomTypeDetailID());

        // Gọi API kiểm tra phòng trống
        API_KiemTraPhongTrong apiKiemTraPhongTrong = RetrofitLogin.KiemTraPhongTRong();
        Call<Model_KiemTraPhongTrongRespones> call = apiKiemTraPhongTrong.postCheckPhongTrong(phongTrong);

        call.enqueue(new Callback<Model_KiemTraPhongTrongRespones>() {
            @Override
            public void onResponse(Call<Model_KiemTraPhongTrongRespones> call, Response<Model_KiemTraPhongTrongRespones> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Model_KiemTraPhongTrongRespones phongTrongResponse = response.body();
                    Log.d("KiemTraPhongTrong", "Phản hồi API: " + phongTrongResponse.getMessage());

                    // Lấy giá trị availableRooms từ phản hồi
                    Data data = phongTrongResponse.getData();
                    if (data != null) {
                        int availableRooms = data.getAvailableRooms();
                        Log.d("KiemTraPhongTrong", "Số phòng trống: " + availableRooms);

                        // Trả về kết quả qua callback
                        callback.onResult(true, "Phòng có sẵn", availableRooms);
                    } else {
                        Log.e("KiemTraPhongTrong", "Không tìm thấy thông tin số phòng trống");
                        callback.onResult(false, "Không tìm thấy thông tin số phòng trống", 0);
                    }
                } else {
                    Log.e("KiemTraPhongTrong", "Phản hồi không thành công: " + response.code());
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("KiemTraPhongTrong", "Error body: " + errorBody);

                        JSONObject errorJson = new JSONObject(errorBody);
                        String errorMessage = errorJson.optString("errorMessage", "Không có phòng trống");

                        callback.onResult(false, errorMessage, 0);
                    } catch (Exception e) {
                        Log.e("KiemTraPhongTrong", "Lỗi khi đọc error body", e);
                        callback.onResult(false, "Lỗi khi kiểm tra phòng trống", 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model_KiemTraPhongTrongRespones> call, Throwable t) {
                Log.e("KiemTraPhongTrong", "Lỗi kết nối", t);
                callback.onResult(false, "Lỗi kết nối: " + t.getMessage(), 0);
            }
        });
    }

    interface RoomAvailabilityCallback {
        void onResult(boolean available, String message, int availableRooms);
    }
}