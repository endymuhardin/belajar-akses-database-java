package com.muhardin.endy.training.java.aksesdb.dao.plainjdbc.newpackage;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import com.muhardin.endy.training.java.aksesdb.service.plainjdbc.PagingHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenjualanDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(PenjualanDao.class);
    private static final String SQL_HITUNG_PENJUALAN_BY_PERIODE = "select count(*) from t_penjualan where waktu_transaksi between ? and ?";
    private static final String SQL_CARI_PENJUALAN_BY_PERIODE = "select * from t_penjualan where waktu_transaksi between ? and ? limit ?,?";
    private static final String SQL_INSERT_PENJUALAN = "insert into t_penjualan(waktu_transaksi) values (?)";
    private static final String SQL_INSERT_PENJUALAN_DETAIL = "insert into t_penjualan_detail (id_penjualan, id_produk, jumlah, harga) values (?,?,?,?)";
    private static final String SQL_CARI_PENJUALAN_BY_ID = "select * from t_penjualan where id = ?";
    private static final String SQL_CARI_PENJUALAN_DETAIL_BY_ID_PENJUALAN = "select pd.*, p.waktu_transaksi, "
            + "produk.kode as kode_produk, produk.nama as nama_produk, produk.harga as harga_produk "
            + "from t_penjualan_detail pd "
            + "inner join t_penjualan p on pd.id_penjualan = p.id "
            + "inner join m_produk produk on pd.id_produk = produk.id "
            + "where id_penjualan = ?";
    
    private static final String SQL_HITUNG_PENJUALAN_DETAIL_BY_PRODUK_DAN_PERIODE = "select count(*) " +
            "from t_penjualan_detail pd " +
            "inner join t_penjualan p on pd.id_penjualan = p.id " +
            "inner join m_produk produk on pd.id_produk = produk.id " +
            "where pd.id_produk = ? " +
            "and (p.waktu_transaksi between ? and ?) ";
    
    private static final String SQL_CARI_PENJUALAN_DETAIL_BY_PRODUK_DAN_PERIODE = "select pd.*, p.waktu_transaksi, " +
            "produk.kode as kode_produk, produk.nama as nama_produk, produk.harga as harga_produk " +
            "from t_penjualan_detail pd " +
            "inner join t_penjualan p on pd.id_penjualan = p.id " +
            "inner join m_produk produk on pd.id_produk = produk.id " +
            "where pd.id_produk = ? " +
            "and (p.waktu_transaksi between ? and ?) " +
            "limit ?,?";
    
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void simpan(Penjualan p) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            
            
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_PENJUALAN, Statement.RETURN_GENERATED_KEYS);

            ps.setDate(1, new java.sql.Date(p.getWaktuTransaksi().getTime()));
            ps.executeUpdate();

            ResultSet rsKey = ps.getGeneratedKeys();
            if(rsKey.next()) {
                p.setId(((Long) rsKey.getObject(1)).intValue());

                for (PenjualanDetail pd : p.getDaftarPenjualanDetail()) {
                    PreparedStatement psDetail = conn.prepareStatement(SQL_INSERT_PENJUALAN_DETAIL, Statement.RETURN_GENERATED_KEYS);
                    psDetail.setInt(1, p.getId());
                    psDetail.setInt(2, pd.getProduk().getId());
                    psDetail.setInt(3, pd.getJumlah());
                    psDetail.setBigDecimal(4, pd.getHarga());
                    psDetail.executeUpdate();

                    ResultSet rsKeyDetail = psDetail.getGeneratedKeys();
                    if(rsKeyDetail.next()){
                        pd.setId(((Long) rsKeyDetail.getObject(1)).intValue());
                    }
                }

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

    public Penjualan cariPenjualanById(Integer id) {
        Connection conn = null;
        Penjualan p = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PENJUALAN_BY_ID);
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = konversiResultSetJadiPenjualan(rs);
                List<PenjualanDetail> daftarDetail = cariPenjualanDetailByPenjualan(p);
                p.setDaftarPenjualanDetail(daftarDetail);
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
            return p;
        }
    }

    public Long hitungPenjualanByPeriode(Date mulai, Date sampai) {
        Connection conn = null;
        Long p = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_PENJUALAN_BY_PERIODE);
            ps.setDate(1, new java.sql.Date(mulai.getTime()));
            ps.setDate(2, new java.sql.Date(sampai.getTime()));
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                p = rs.getLong(1);
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
            return p;
        }
    }

    public List<Penjualan> cariPenjualanByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris) {
        Connection conn = null;
        List<Penjualan> hasil = new ArrayList<Penjualan>();
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PENJUALAN_BY_PERIODE);
            ps.setDate(1, new java.sql.Date(mulai.getTime()));
            ps.setDate(2, new java.sql.Date(sampai.getTime()));
            ps.setInt(3, PagingHelper.halamanJadiStart(halaman, baris));
            ps.setInt(4, baris);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Penjualan x = konversiResultSetJadiPenjualan(rs);
                hasil.add(x);
                List<PenjualanDetail> daftarDetail = cariPenjualanDetailByPenjualan(x);
                x.setDaftarPenjualanDetail(daftarDetail);
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
            return hasil;
        }
    }

    public Long hitungPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai) {
        Connection conn = null;
        Long hasil = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_PENJUALAN_DETAIL_BY_PRODUK_DAN_PERIODE);
            ps.setInt(1, p.getId());
            ps.setDate(2, new java.sql.Date(mulai.getTime()));
            ps.setDate(3, new java.sql.Date(sampai.getTime()));
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                hasil = rs.getLong(1);
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
            return hasil;
        }
    }

    public List<PenjualanDetail> cariPenjualanDetailByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris) {
        Connection conn = null;
        List<PenjualanDetail> hasil = new ArrayList<PenjualanDetail>();
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PENJUALAN_DETAIL_BY_PRODUK_DAN_PERIODE);
            ps.setInt(1, p.getId());
            ps.setDate(2, new java.sql.Date(mulai.getTime()));
            ps.setDate(3, new java.sql.Date(sampai.getTime()));
            ps.setInt(4, PagingHelper.halamanJadiStart(halaman, baris));
            ps.setInt(5, baris);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PenjualanDetail x = konversiResultSetJadiPenjualanDetail(rs);
                hasil.add(x);
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
            return hasil;
        }
    }

    private List<PenjualanDetail> cariPenjualanDetailByPenjualan(Penjualan p) {
        Connection conn = null;
        List<PenjualanDetail> hasil = new ArrayList<PenjualanDetail>();
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(SQL_CARI_PENJUALAN_DETAIL_BY_ID_PENJUALAN);
            
            ps.setInt(1, p.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PenjualanDetail pd = konversiResultSetJadiPenjualanDetail(rs);
                hasil.add(pd);
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
            return hasil;
        }
    }
    
    private Penjualan konversiResultSetJadiPenjualan(ResultSet rs) throws SQLException {
        Penjualan p = new Penjualan();
        p.setId((Integer) rs.getObject("id"));
        p.setWaktuTransaksi(rs.getDate("waktu_transaksi"));
        return p;
    }
    
    private PenjualanDetail konversiResultSetJadiPenjualanDetail(ResultSet rs) throws SQLException {
        PenjualanDetail p = new PenjualanDetail();
        p.setId((Integer) rs.getObject("id"));
        p.setId((Integer) rs.getObject("id"));
        p.setHarga(rs.getBigDecimal("harga"));
        p.setJumlah((Integer) rs.getObject("jumlah"));

        // relasi ke produk
        Produk px = new Produk();
        px.setId((Integer) rs.getObject("id_produk"));
        px.setKode(rs.getString("kode_produk"));
        px.setNama(rs.getString("nama_produk"));
        px.setHarga(rs.getBigDecimal("harga_produk"));
        p.setProduk(px);

        // relasi ke penjualan
        Penjualan jual = new Penjualan();
        jual.setId((Integer) rs.getObject("id_penjualan"));
        jual.setWaktuTransaksi(rs.getDate("waktu_transaksi"));
        p.setPenjualan(jual);
        
        return p;
    }
}
