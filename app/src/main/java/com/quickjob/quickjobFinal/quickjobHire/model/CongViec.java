package com.quickjob.quickjobFinal.quickjobHire.model;

import org.json.JSONArray;

/**
 * Created by VinhNguyen on 6/25/2016.
 */
public class CongViec {
    public String tencongviec;
    public String tecongty;
    public String quymo;
    public String diachict;
    public String ngannghe;
    public String motact;
    public String diadiem;
    public String luong;
    public String dateup;
    public String bangcap;
    public String dotuoi;
    public String ngoaingu;
    public String gioitinh;
    public String khac;
    public String motacv;
    public String kn;
    public String idungtuyen;
    public String trangthai;
    public String phucloi;

    public String getPhucloi() {
        return phucloi;
    }

    public void setPhucloi(String phucloi) {
        this.phucloi = phucloi;
    }

    public String getMacv() {
        return macv;
    }

    public void setMacv(String macv) {
        this.macv = macv;
    }


    public String macv;
    public String url;
    public String nganhnghe;
    public String nganhNghe;
    int sohoso;
    public String chucdanh;
    public String soluong;
    public JSONArray profile_apply;

    public JSONArray getProfile_apply() {
        return profile_apply;
    }

    public void setProfile_apply(JSONArray profile_apply) {
        this.profile_apply = profile_apply;
    }

    public String getTecongty() {
        return tecongty;
    }

    public void setTecongty(String tecongty) {
        this.tecongty = tecongty;
    }

    public String getQuymo() {
        return quymo;
    }

    public void setQuymo(String quymo) {
        this.quymo = quymo;
    }

    public String getDiachict() {
        return diachict;
    }

    public void setDiachict(String diachict) {
        this.diachict = diachict;
    }

    public String getNgannghe() {
        return ngannghe;
    }

    public void setNgannghe(String ngannghe) {
        this.ngannghe = ngannghe;
    }

    public String getMotact() {
        return motact;
    }

    public void setMotact(String motact) {
        this.motact = motact;
    }

    public String getDiadiem() {
        return diadiem;
    }

    public void setDiadiem(String diadiem) {
        this.diadiem = diadiem;
    }

    public String getLuong() {
        return luong;
    }

    public void setLuong(String luong) {
        this.luong = luong;
    }

    public String getDateup() {
        return dateup;
    }

    public void setDateup(String dateup) {
        this.dateup = dateup;
    }

    public String getBangcap() {
        return bangcap;
    }

    public void setBangcap(String bangcap) {
        this.bangcap = bangcap;
    }

    public String getDotuoi() {
        return dotuoi;
    }

    public void setDotuoi(String dotuoi) {
        this.dotuoi = dotuoi;
    }

    public String getNgoaingu() {
        return ngoaingu;
    }

    public void setNgoaingu(String ngoaingu) {
        this.ngoaingu = ngoaingu;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getKhac() {
        return khac;
    }

    public void setKhac(String khac) {
        this.khac = khac;
    }

    public String getMotacv() {
        return motacv;
    }

    public void setMotacv(String motacv) {
        this.motacv = motacv;
    }

    public String getKn() {
        return kn;
    }

    public void setKn(String kn) {
        this.kn = kn;
    }

    public String getIdungtuyen() {
        return idungtuyen;
    }

    public void setIdungtuyen(String idungtuyen) {
        this.idungtuyen = idungtuyen;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNganhNghe() {
        return nganhNghe;
    }

    public void setNganhNghe(String nganhNghe) {
        this.nganhNghe = nganhNghe;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String mahs;
    public String sdt;

    public String getTencongviec() {
        return tencongviec;
    }

    public void setTencongviec(String tencongviec) {
        this.tencongviec = tencongviec;
    }

    public String getMahs() {
        return mahs;
    }

    public void setMahs(String mahs) {
        this.mahs = mahs;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getChucdanh() {
        return chucdanh;
    }

    public void setChucdanh(String chucdanh) {
        this.chucdanh = chucdanh;
    }

    public int getSohoso() {
        return sohoso;
    }

    public void setSohoso(int sohoso) {
        this.sohoso = sohoso;
    }

    public String getNganhnghe() {
        return nganhnghe;
    }

    public void setNganhnghe(String nganhnghe) {
        this.nganhnghe = nganhnghe;
    }
    public CongViec()
    {

    }
    public CongViec(String macv, String tencongviec, String tencongty, String diachict, String nganhnghe, String motact, String quymo, String nganhNghe, String chucdanh, String soluong, String luong, String diadiem, String dateup, String motacv, String bangcap, String dotuoi, String ngoaingu, String gioitinh, String khac, String kn, String url, String idungtuyen, String trangthai, String sdt)
    {
        this.sdt=sdt;
        this.trangthai=trangthai;
        this.macv=macv;
        this.nganhNghe=nganhNghe;
        this.chucdanh=chucdanh;
        this.soluong=soluong;
        this.tecongty=tencongty;
        this.diachict=diachict;
        this.nganhnghe=nganhnghe;
        this.motact=motact;
        this.quymo=quymo;
        this.tencongviec=tencongviec;
        this.diadiem=diadiem;
        this.luong=luong;
        this.dateup=dateup;
        this.bangcap=bangcap;
        this.dotuoi=dotuoi;
        this.ngoaingu=ngoaingu;
        this.gioitinh=gioitinh;
        this.khac=khac;
        this.motacv=motacv;
        this.kn=kn;
        this.url=url;
        this.idungtuyen=idungtuyen;
    }
}
