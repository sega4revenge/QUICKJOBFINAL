package com.quickjob.quickjobFinal.quickjobFind.model;

/**
 * Created by VinhNguyen on 7/8/2016.
 */
public class Profile {
    public String ten;
    public String quequan;
    public String gioitinh;
    public String ngaysinh;
    public String email;
    public String sdt;
    public String diachi;
    public String hocvan;
    public String totnghiep;
    public String nganhnghe;
    public String mucluong;
    public String diadiem;
    public String kynang;
    public String ngoaingu;
    public String namkn;
    public String tencongty;
    public String chucdanh;
    public String motacv;
    public String thanhtuu;
    public String xeploai;
    public String chuyennganh;
    public String vitri;
    public String ngaydang;
    public String id;
    public String tentruong;
    public String tencv;
    public String uid;
    public String img;
    public Profile(String nganhnghe, String vitri, String mucluong, String diadiem, String ngaydang, String mahs, String ten, String gioitinh, String ngaysinh, String email, String sdt, String diachi, String quequan, String tentruong, String chuyennganh, String xeploai, String thanhtuu, String namkn, String tencongty, String chucdanh, String mota, String ngoaingu, String kynang, String tencv, String uid, String img){
        this.nganhnghe= nganhnghe;
        this.mucluong=mucluong;
        this.img=img;
        this.diadiem=diadiem;
        this.vitri=vitri;
        this.ngaydang=ngaydang;
        this.ten =ten;
        this.quequan =quequan;
        this.gioitinh=gioitinh;
        this.email=email;
        this.sdt=sdt;
        this.id=mahs;
        this.uid=uid;
        this.diachi=diachi;
        this.ngaysinh=ngaysinh;
        this.tentruong=tentruong;
        this.chuyennganh=chuyennganh;
        this.xeploai=xeploai;
        this.thanhtuu=thanhtuu;
        this.namkn=namkn;
        this.tencongty=tencongty;
        this.chucdanh=chucdanh;
        this.motacv=mota;
        this.ngoaingu=ngoaingu;
        this.kynang=kynang;
        this.tencv=tencv;
    }
    public Profile(){

    }
}