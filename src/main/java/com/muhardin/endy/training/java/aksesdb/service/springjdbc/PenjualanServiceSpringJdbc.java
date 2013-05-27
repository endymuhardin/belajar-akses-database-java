package com.muhardin.endy.training.java.aksesdb.service.springjdbc;

import com.muhardin.endy.training.java.aksesdb.dao.springjdbc.PenjualanDao;
import com.muhardin.endy.training.java.aksesdb.dao.springjdbc.PenjualanDetailDao;
import com.muhardin.endy.training.java.aksesdb.dao.springjdbc.ProdukDao;
import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class PenjualanServiceSpringJdbc implements PenjualanService{
    @Autowired private ProdukDao produkDao;
    @Autowired private PenjualanDao penjualanDao;
    @Autowired private PenjualanDetailDao penjualanDetailDao;

    @Override
    public void simpan(Produk p) {
        produkDao.simpan(p);
    }

    @Override
    public Produk cariProdukById(Integer id) {
        return produkDao.cariById(id);
    }

    @Override
    public Produk cariProdukByKode(String kode) {
        return produkDao.cariByKode(kode);
    }

    @Override
    public Long hitungSemuaProduk() {
        return produkDao.hitungSemua();
    }

    @Override
    public List<Produk> cariSemuaProduk(Integer halaman, Integer baris) {
        return produkDao.cariSemua(halaman, baris);
    }

    @Override
    public Long hitungProdukByNama(String nama) {
        return produkDao.hitungByNama(nama);
    }

    @Override
    public List<Produk> cariProdukByNama(String nama, Integer halaman, Integer baris) {
        return produkDao.cariByNama(nama, halaman, baris);
    }

    @Override
    public void simpan(Penjualan p) {
        penjualanDao.simpan(p);
    }

    @Override
    public Penjualan cariPenjualanById(Integer id) {
        return penjualanDao.cariById(id);
    }

    @Override
    public Long hitungPenjualanByPeriode(Date mulai, Date sampai) {
        return penjualanDao.hitungByPeriode(mulai, sampai);
    }

    @Override
    public List<Penjualan> cariPenjualanByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris) {
        return penjualanDao.cariByPeriode(mulai, sampai, halaman, baris);
    }

    @Override
    public Long hitungPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai) {
        return penjualanDetailDao.hitungByProdukDanPeriode(p, mulai, sampai);
    }

    @Override
    public List<PenjualanDetail> cariPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris) {
        return penjualanDetailDao.cariByProdukDanPeriode(p, mulai, sampai, halaman, baris);
    }

}
