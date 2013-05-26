/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.muhardin.endy.training.java.aksesdb.service;

import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author endy
 */
public abstract class ProdukServiceTest {
    
    public abstract PenjualanService getPenjualanService();
    public abstract DataSource getDataSource();
    
    @Before
    public void bersihkanDataTest() throws Exception {
        DataSource ds = getDataSource();
        Connection conn = ds.getConnection();
        
        String sql = "delete from m_produk where kode=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1,"T-001");
        ps.executeUpdate();
        conn.close();
    }
    
    @Test
    public void testSimpanProduk() {
        Produk p = new Produk();
        p.setHarga(new BigDecimal(125000));
        p.setKode("T-001");
        p.setNama("Produk Test 001");
        
        PenjualanService service = getPenjualanService();
        service.simpan(p);
        assertNotNull(p.getId());
    }

    @Test
    public void testCariProdukById() {
        PenjualanService service = getPenjualanService();
        assertNotNull(service.cariProdukById(1));
        assertNull(service.cariProdukById(99));
    }

    @Test
    public void testCariProdukByKode() {
        PenjualanService service = getPenjualanService();
        assertNotNull(service.cariProdukByKode("K-001"));
        assertNull(service.cariProdukByKode("X-001"));
    }

    @Test
    public void testHitungSemuaProduk() {
        PenjualanService service = getPenjualanService();
        assertEquals(Long.valueOf(3), Long.valueOf(service.hitungSemuaProduk()));
    }

    @Test
    public void testCariSemuaProduk() {
        PenjualanService service = getPenjualanService();
        List<Produk> hasil = service.cariSemuaProduk(1, 100);
        assertNotNull(hasil);
        assertTrue(hasil.size() == 3);
        for (Produk produk : hasil) {
            assertNotNull(produk.getId());
            assertNotNull(produk.getKode());
            assertNotNull(produk.getNama());
            assertNotNull(produk.getHarga());
        }
    }

    @Test
    public void testHitungProdukByNama() {
        PenjualanService service = getPenjualanService();
        assertEquals(Long.valueOf(2), Long.valueOf(service.hitungProdukByNama("usb")));
        assertEquals(Long.valueOf(0), Long.valueOf(service.hitungProdukByNama("x")));
    }

    @Test
    public void testCariProdukByNama() {
        PenjualanService service = getPenjualanService();
        assertEquals(Long.valueOf(2), Long.valueOf(service.cariProdukByNama("usb", 1, 100).size()));
        assertEquals(Long.valueOf(0), Long.valueOf(service.cariProdukByNama("x", 1, 100).size()));
    }
}