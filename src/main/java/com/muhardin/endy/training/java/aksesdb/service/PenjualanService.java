package com.muhardin.endy.training.java.aksesdb.service;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import java.util.Date;
import java.util.List;

public interface PenjualanService {
    // service berkaitan dengan produk
    void simpan(Produk p);
    Produk cariProdukById(Integer id);
    Produk cariProdukByKode(String kode);
    Long hitungSemuaProduk();
    List<Produk> cariSemuaProduk(Integer halaman, Integer baris);
    Long hitungProdukByNama(String nama);
    List<Produk> cariProdukByNama(String nama, Integer halaman, Integer baris);
    
    // service yang berkaitan dengan transaksi
    void simpan(Penjualan p);
    Penjualan cariPenjualanById(Integer id);
    Long hitungPenjualanByPeriode(Date mulai, Date sampai);
    List<Penjualan> cariPenjualanByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris);
    Long hitungPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai);
    List<PenjualanDetail> cariPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris);
}
