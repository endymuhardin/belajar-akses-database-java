package com.muhardin.endy.training.java.aksesdb.service.plainjdbc;

import com.muhardin.endy.training.java.aksesdb.dao.plainjdbc.newpackage.PenjualanDao;
import com.muhardin.endy.training.java.aksesdb.dao.plainjdbc.newpackage.ProdukDao;
import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

public class PenjualanServicePlainJdbc implements PenjualanService {
    
    private ProdukDao produkDao;
    private PenjualanDao penjualanDao;

    public void setDataSource(DataSource dataSource) {
        produkDao = new ProdukDao();
        produkDao.setDataSource(dataSource);
        
        penjualanDao = new PenjualanDao();
        penjualanDao.setDataSource(dataSource);
    }

    @Override
    public void simpan(Produk p) {
        produkDao.simpan(p);
    }

    @Override
    public void hapus(Produk p) {
        produkDao.hapus(p);
    }

    @Override
    public Produk cariProdukById(Integer id) {
        return produkDao.cariProdukById(id);
    }

    @Override
    public Produk cariProdukByKode(String kode) {
        return produkDao.cariProdukByKode(kode);
    }

    @Override
    public Long hitungSemuaProduk() {
        return produkDao.hitungSemuaProduk();
    }

    @Override
    public List<Produk> cariSemuaProduk(Integer halaman, Integer baris) {
        return produkDao.cariSemuaProduk(halaman, baris);
    }

    @Override
    public Long hitungProdukByNama(String nama) {
        return produkDao.hitungProdukByNama(nama);
    }

    @Override
    public List<Produk> cariProdukByNama(String nama, Integer halaman, Integer baris) {
        return produkDao.cariProdukByNama(nama, halaman, baris);
    }

    @Override
    public void simpan(Penjualan p) {
        penjualanDao.simpan(p);
    }

    @Override
    public Penjualan cariPenjualanById(Integer id) {
        return penjualanDao.cariPenjualanById(id);
    }

    @Override
    public Long hitungPenjualanByPeriode(Date mulai, Date sampai) {
        return penjualanDao.hitungPenjualanByPeriode(mulai, sampai);
    }

    @Override
    public List<Penjualan> cariPenjualanByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris) {
        return penjualanDao.cariPenjualanByPeriode(mulai, sampai, halaman, baris);
    }

    @Override
    public Long hitungPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai) {
        return penjualanDao.hitungPenjualanByPeriode(mulai, sampai);
    }

    @Override
    public List<PenjualanDetail> cariPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris) {
        return penjualanDao.cariPenjualanDetailByProdukDanPeriode(p, mulai, sampai, halaman, baris);
    }
}
