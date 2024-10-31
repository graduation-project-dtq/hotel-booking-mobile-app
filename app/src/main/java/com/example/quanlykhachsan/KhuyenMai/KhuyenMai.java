package com.example.quanlykhachsan.KhuyenMai;

public class KhuyenMai {
    private int TriGia;
    private String NgayTao;
    private String NgayKetThuc;
    private String voucherCode;
    private int imageResource;

    public KhuyenMai(int triGia, String ngayTao, String ngayKetThuc, String voucherCode, int imageResource) {
        TriGia = triGia;
        NgayTao = ngayTao;
        NgayKetThuc = ngayKetThuc;
        this.voucherCode = voucherCode;
        this.imageResource = imageResource;
    }

    public int getTriGia() {
        return TriGia;
    }

    public void setTriGia(int triGia) {
        TriGia = triGia;
    }

    public String getNgayTao() {
        return NgayTao;
    }

    public void setNgayTao(String ngayTao) {
        NgayTao = ngayTao;
    }

    public String getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        NgayKetThuc = ngayKetThuc;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}