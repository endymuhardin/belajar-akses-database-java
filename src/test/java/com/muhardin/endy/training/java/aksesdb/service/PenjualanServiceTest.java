/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muhardin.endy.training.java.aksesdb.service;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.joda.time.DateTime;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author endy
 */
public abstract class PenjualanServiceTest {
    
    public abstract PenjualanService getPenjualanService();
    public abstract DataSource getDataSource();
    
    @Before
    public void bersihkanDataTest() throws Exception {
        DataSource ds = getDataSource();
        Connection conn = ds.getConnection();
        
        String sqlHapusPenjualanDetail = "delete from t_penjualan_detail where id_penjualan > ?";
        PreparedStatement psHapusPenjualanDetail = conn.prepareStatement(sqlHapusPenjualanDetail);
        psHapusPenjualanDetail.setInt(1,90);
        psHapusPenjualanDetail.executeUpdate();
        
        String sqlHapusPenjualan = "delete from t_penjualan where id > ?";
        PreparedStatement psHapusPenjualan = conn.prepareStatement(sqlHapusPenjualan);
        psHapusPenjualan.setInt(1,90);
        psHapusPenjualan.executeUpdate();
        
        conn.close();
    }
    
    @Test
    public void testSimpanPenjualan() throws Exception {
        PenjualanService service = getPenjualanService();
        
        // data produk
        Produk p1 = service.cariProdukById(1);
        Produk p2 = service.cariProdukById(2);
        Produk p3 = service.cariProdukById(3);
        
        Penjualan p = new Penjualan();
        p.setWaktuTransaksi(new Date());
        
        PenjualanDetail pd1 = new PenjualanDetail();
        pd1.setHarga(p1.getHarga());
        pd1.setJumlah(1);
        pd1.setProduk(p1);
        p.getDaftarPenjualanDetail().add(pd1);
        pd1.setPenjualan(p);
        
        PenjualanDetail pd2 = new PenjualanDetail();
        pd2.setHarga(p2.getHarga());
        pd2.setJumlah(2);
        pd2.setProduk(p2);
        p.getDaftarPenjualanDetail().add(pd2);
        pd2.setPenjualan(p);
        
        PenjualanDetail pd3 = new PenjualanDetail();
        pd3.setHarga(p3.getHarga());
        pd3.setJumlah(3);
        pd3.setProduk(p3);
        p.getDaftarPenjualanDetail().add(pd3);
        pd3.setPenjualan(p);
        
        service.simpan(p);

        // cek generate id penjualan
        assertNotNull(p.getId());
        
        // cek generate id penjualan detail
        for (PenjualanDetail detail : p.getDaftarPenjualanDetail()) {
            assertNotNull(detail.getId());
        }
        
        Connection conn = getDataSource().getConnection();
        
        String sqlCekPenjualanDetail = "select count(*) from t_penjualan_detail where id_penjualan = ?";
        PreparedStatement psCekPenjualanDetail = conn.prepareStatement(sqlCekPenjualanDetail);
        psCekPenjualanDetail.setInt(1, p.getId());
        ResultSet rs = psCekPenjualanDetail.executeQuery();
        assertTrue(rs.next());
        assertEquals(Long.valueOf(3), Long.valueOf(rs.getLong(1)));
        
        conn.close();
    }
    
    @Test
    public void testCariPenjualanById(){
        Penjualan p = getPenjualanService().cariPenjualanById(1);
        verifikasiPenjualan(p);
    }
    
    @Test
    public void testHitungPenjualanByPeriode(){
        Date mulai = new DateTime(2013,1,1,0,0,0,0).toDate();
        Date sampai = new DateTime(2013,2,1,0,0,0,0).toDate();
        
        Long hasil = getPenjualanService().hitungPenjualanByPeriode(mulai, sampai);
        assertNotNull(hasil);
        assertEquals(Long.valueOf(2), Long.valueOf(hasil));
    }
    
    @Test
    public void testCariPenjualanByPeriode(){
        Date mulai = new DateTime(2013,1,1,0,0,0,0).toDate();
        Date sampai = new DateTime(2013,2,1,0,0,0,0).toDate();
        
        List<Penjualan> hasil = getPenjualanService()
                .cariPenjualanByPeriode(mulai, sampai, 1, 100);
        
        assertTrue(hasil.size() == 2);
        
        for (Penjualan penjualan : hasil) {
            verifikasiPenjualan(penjualan);
        }
    }
    
    @Test
    public void testHitungPenjualanDetailByProdukDanPeriode(){
        Produk p = new Produk();
        p.setId(1);
        
        Date mulai = new DateTime(2013,1,1,0,0,0,0).toDate();
        Date sampai = new DateTime(2013,2,1,0,0,0,0).toDate();
        
        Long hasil = getPenjualanService().hitungPenjualanDetailByProdukDanPeriode(p, mulai, sampai);
        assertNotNull(hasil);
        assertEquals(Long.valueOf(2), Long.valueOf(hasil));
    }
    
    @Test
    public void testCariPenjualanDetailByProdukDanPeriode(){
        Produk p = new Produk();
        p.setId(1);
        
        Date mulai = new DateTime(2013,1,1,0,0,0,0).toDate();
        Date sampai = new DateTime(2013,2,1,0,0,0,0).toDate();
        
        List<PenjualanDetail> hasil = getPenjualanService()
                .cariPenjualanDetailByProdukDanPeriode(p, mulai, sampai, 1, 100);
        
        assertNotNull(hasil);
        assertTrue(hasil.size() == 2);
        
        for (PenjualanDetail penjualanDetail : hasil) {
            verifikasiPenjualanDetail(penjualanDetail);
        }
    }

    private void verifikasiPenjualan(Penjualan p) {
        assertNotNull(p);
        assertNotNull(p.getId());
        assertNotNull(p.getWaktuTransaksi());
        assertNotNull(p.getDaftarPenjualanDetail());
        assertTrue(p.getDaftarPenjualanDetail().size() > 0);
        
        // cek relasi
        for (PenjualanDetail detail : p.getDaftarPenjualanDetail()) {
            assertNotNull(detail.getProduk().getHarga());
        }
    }

    private void verifikasiPenjualanDetail(PenjualanDetail penjualanDetail) {
        assertNotNull(penjualanDetail.getProduk().getHarga());
        assertNotNull(penjualanDetail.getPenjualan().getWaktuTransaksi());
    }
}