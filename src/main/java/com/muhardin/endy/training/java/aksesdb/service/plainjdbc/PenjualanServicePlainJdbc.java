package com.muhardin.endy.training.java.aksesdb.service.plainjdbc;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenjualanServicePlainJdbc implements PenjualanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PenjualanServicePlainJdbc.class);
    private static final String SQL_INSERT_PRODUK = "insert into m_produk (kode, nama, harga) values (?,?,?)";
    private static final String SQL_UPDATE_PRODUK = "update m_produk set kode = ?, nama = ?, harga = ? where id = ?";
    private static final String SQL_HAPUS_PRODUK = "delete from m_produk where id = ?";
    
    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void simpan(Produk p) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            
            if(p.getId() == null) {
            
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PRODUK, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, p.getKode());
                ps.setString(2, p.getNama());
                ps.setBigDecimal(3, p.getHarga());

                ps.executeUpdate();

                ResultSet rsKey = ps.getGeneratedKeys();
                if(rsKey.next()) {
                    p.setId(((Long) rsKey.getObject(1)).intValue());
                }
            } else {
                PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PRODUK);

                ps.setString(1, p.getKode());
                ps.setString(2, p.getNama());
                ps.setBigDecimal(3, p.getHarga());
                ps.setInt(4, p.getId());

                ps.executeUpdate();
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public void hapus(Produk p) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HAPUS_PRODUK);
            
            ps.setInt(1, p.getId());
            ps.executeUpdate();
            
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception err) {
            LOGGER.error(err.getMessage(), err);
            if(conn !=  null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public Produk cariProdukById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Produk cariProdukByKode(String kode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long hitungSemuaProduk() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Produk> cariSemuaProduk(Integer halaman, Integer baris) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long hitungProdukByNama(String nama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Produk> cariProdukByNama(String nama, Integer halaman, Integer baris) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void simpan(Penjualan p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Penjualan cariPenjualanById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long hitungPenjualanByPeriode(Date mulai, Date sampai) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Penjualan> cariPenjualanByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long hitungPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<PenjualanDetail> cariPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
