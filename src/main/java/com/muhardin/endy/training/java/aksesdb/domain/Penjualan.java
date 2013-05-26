package com.muhardin.endy.training.java.aksesdb.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Penjualan {
    private Integer id;
    private Date waktuTransaksi;
    private List<PenjualanDetail> daftarPenjualanDetail = new ArrayList<PenjualanDetail>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getWaktuTransaksi() {
        return waktuTransaksi;
    }

    public void setWaktuTransaksi(Date waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }

    public List<PenjualanDetail> getDaftarPenjualanDetail() {
        return daftarPenjualanDetail;
    }

    public void setDaftarPenjualanDetail(List<PenjualanDetail> daftarPenjualanDetail) {
        this.daftarPenjualanDetail = daftarPenjualanDetail;
    }
}
