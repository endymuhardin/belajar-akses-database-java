package com.muhardin.endy.training.java.aksesdb.dao.springjdbc;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PenjualanDetailDao {

    private static final String SQL_INSERT = "insert into t_penjualan_detail (id_penjualan,id_produk,harga,jumlah) values (?,?,?,?)";
    private static final String SQL_CARI_BY_ID_PENJUALAN = "select pd.*, p.waktu_transaksi, "
            + "produk.kode as kode_produk, produk.nama as nama_produk, produk.harga as harga_produk "
            + "from t_penjualan_detail pd "
            + "inner join t_penjualan p on pd.id_penjualan = p.id "
            + "inner join m_produk produk on pd.id_produk = produk.id "
            + "where id_penjualan = ?";
    
    private static final String SQL_HITUNG_BY_PRODUK_DAN_PERIODE = "select count(*) " +
            "from t_penjualan_detail pd " +
            "inner join t_penjualan p on pd.id_penjualan = p.id " +
            "inner join m_produk produk on pd.id_produk = produk.id " +
            "where pd.id_produk = ? " +
            "and (p.waktu_transaksi between ? and ?) ";
    
    private static final String SQL_CARI_BY_PRODUK_DAN_PERIODE = "select pd.*, p.waktu_transaksi, " +
            "produk.kode as kode_produk, produk.nama as nama_produk, produk.harga as harga_produk " +
            "from t_penjualan_detail pd " +
            "inner join t_penjualan p on pd.id_penjualan = p.id " +
            "inner join m_produk produk on pd.id_produk = produk.id " +
            "where pd.id_produk = ? " +
            "and (p.waktu_transaksi between ? and ?) " +
            "limit ?,?";
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired ProdukDao produkDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void simpan(final PenjualanDetail p) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT, new String[]{"id"});
                ps.setInt(1, p.getPenjualan().getId());
                ps.setInt(2, p.getProduk().getId());
                ps.setBigDecimal(3, p.getHarga());
                ps.setInt(4, p.getJumlah());
                return ps;
            }
        }, keyHolder);
        p.setId(keyHolder.getKey().intValue());
    }
    
    public List<PenjualanDetail> cariByPenjualan(Penjualan p){
        List<PenjualanDetail> hasil = jdbcTemplate.query(SQL_CARI_BY_ID_PENJUALAN, new ResultSetJadiPenjualanDetail(), p.getId());
        
        // set relasi ke penjualan
        for (PenjualanDetail penjualanDetail : hasil) {
            penjualanDetail.setPenjualan(p);
        }
        
        return hasil;
    }

    public Long hitungByProdukDanPeriode(Produk p, Date mulai, Date sampai) {
        return jdbcTemplate.queryForObject(SQL_HITUNG_BY_PRODUK_DAN_PERIODE, Long.class,
                p.getId(), mulai, sampai);
    }

    public List<PenjualanDetail> cariByProdukDanPeriode(Produk p, Date mulai, Date sampai, Integer halaman, Integer baris) {
        return jdbcTemplate.query(SQL_CARI_BY_PRODUK_DAN_PERIODE, new ResultSetJadiPenjualanDetail(), 
                p.getId(), 
                mulai, 
                sampai, 
                PagingHelper.halamanJadiStart(halaman, baris), 
                baris);
    }


    private class ResultSetJadiPenjualanDetail implements RowMapper<PenjualanDetail> {

        @Override
        public PenjualanDetail mapRow(ResultSet rs, int i) throws SQLException {
            PenjualanDetail p = new PenjualanDetail();
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
}
