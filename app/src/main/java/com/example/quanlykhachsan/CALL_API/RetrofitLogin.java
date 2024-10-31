package com.example.quanlykhachsan.CALL_API;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitLogin {
    private static final String BASE_URL = "https://10.0.2.2:7227";
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(UnsafeSSLHelper.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Tạo Retrofit instance cho API đăng nhập (an toàn)
    public static Retrofit getRetrofitInstanceForLogin() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(UnsafeSSLHelper.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // Sử dụng OkHttpClient mặc định (an toàn)
    }

    // Tạo Retrofit instance cho API lấy loại phòng (không an toàn)
    public static Retrofit getRetrofitInstanceForRoomTypes() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(UnsafeSSLHelper.getUnsafeOkHttpClient()) // Sử dụng UnsafeSSLHelper
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    //
    public static Retrofit getRetrofitInstanceForXemRoomTypes() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(UnsafeSSLHelper.getUnsafeOkHttpClient()) // Sử dụng UnsafeSSLHelper
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    //đăng nhập
    public static API_Login getApiLoginService() {
        return getRetrofitInstanceForLogin().create(API_Login.class);
    }

    //get loaiphong cho index
    public static API_GetLoaiPhong getApiGetLoaiPhongService() {
        return getRetrofitInstanceForRoomTypes().create(API_GetLoaiPhong.class);
    }
    //XemchiTiet
    public static API_XemChiTietLoaiPhong XemchiTietLoaiPhongService() {
        return getRetrofitInstanceForXemRoomTypes().create(API_XemChiTietLoaiPhong.class);
    }

    //XemchiTiet_Detail
    public static API_XemChiTietRoomDetail XemchiTietLoaiPhong_Detail() {
        return getRetrofitInstanceForXemRoomTypes().create(API_XemChiTietRoomDetail.class);
    }
    //load roomtypedetai theo roomtype
    public static API_GetRoomTypeDetail_RoomType getRoomTypeByCategoryService() {
        return getRetrofitInstanceForRoomTypes().create(API_GetRoomTypeDetail_RoomType.class);
    }

    //Tim kiem phong theo số người và loại phòng đã chọn
    public static API_TimKiemPhong TimKiemPhongSoNguoiTheoLoai() {
        return getRetrofitInstanceForRoomTypes().create(API_TimKiemPhong.class);
    }

    //Dang Ky Tai Khoan
    public static API_DangKy DangKyTaiKhoan() {
        return getRetrofitInstance().create(API_DangKy.class);
    }

    //Xem chi tiết thông tin khách hàng
    public static API_GetCustomer XemChiTietThongTin() {
        return getRetrofitInstance().create(API_GetCustomer.class);
    }

    //sửa thông tin khách hàng
    public static API_UpdateCustomer SuaThongTinKhachHang() {
        return getRetrofitInstance().create(API_UpdateCustomer.class);
    }


    //Đặt phòng
    public static API_Booking DatPhong() {
        return getRetrofitInstance().create(API_Booking.class);
    }

    //Kiểm tra phòng trống
    public static API_KiemTraPhongTrong KiemTraPhongTRong() {
        return getRetrofitInstance().create(API_KiemTraPhongTrong.class);
    }

    //lấy dữ liệu dịch vụ
    public static API_GetService LayDLService() {
        return getRetrofitInstance().create(API_GetService.class);
    }

    //load ds booking theo tab
    public static API_GetBooking LoadHoaDonChoXacNhan() {
        return getRetrofitInstance().create(API_GetBooking.class);
    }

    //xem chi tiết booking
    public static API_GetChiTietooking XemChiTietBooking() {
        return getRetrofitInstance().create(API_GetChiTietooking.class);
    }

    //Hủy phòng
    public static API_HuyBooking HuyDatPhong() {
        return getRetrofitInstance().create(API_HuyBooking.class);
    }
    //get thông báo khi đặt phòng
    public static API_GetThongBao ThongBaoDatPhong() {
        return getRetrofitInstance().create(API_GetThongBao.class);
    }
    //get khuyến mãi
    public static API_GetKhuyenMai GetKhuyenMai() {
        return getRetrofitInstance().create(API_GetKhuyenMai.class);
    }

    //xác nhan mail bằng mã code
    public static API_PostCodeEmail PostCodeEmail() {
        return getRetrofitInstance().create(API_PostCodeEmail.class);
    }
    //gửi lại mã code
    public static API_Response_Code PostResponseCodeEmail() {
        return getRetrofitInstance().create(API_Response_Code.class);
    }

    //đăng nhập = gg
    public static API_PostLoginGG PostLoginGG() {
        return getRetrofitInstance().create(API_PostLoginGG.class);
    }
}