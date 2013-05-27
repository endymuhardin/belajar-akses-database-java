package com.muhardin.endy.training.java.aksesdb.dao.springjdbc;

import com.muhardin.endy.training.java.aksesdb.domain.Penjualan;
import com.muhardin.endy.training.java.aksesdb.domain.PenjualanDetail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PenjualanDao {

    private static final String SQL_INSERT = "insert into t_penjualan (waktu_transaksi) values (:waktuTransaksi)";
    private static final String SQL_CARI_BY_ID = "select * from t_penjualan where id = ?";
    private static final String SQL_HITUNG_BY_PERIODE = "select count(*) from t_penjualan where waktu_transaksi between ? and ?";
    private static final String SQL_CARI_BY_PERIODE = "select * from t_penjualan where waktu_transaksi between ? and ? limit ?,?";
    
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    @Autowired private PenjualanDetailDao penjualanDetailDao;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void simpan(Penjualan p) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(p);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT, namedParameters, keyHolder);
        p.setId(keyHolder.getKey().intValue());
        
        for (PenjualanDetail detail : p.getDaftarPenjualanDetail()) {
            detail.setPenjualan(p);
            penjualanDetailDao.simpan(detail);
        }
    }

    public Penjualan cariById(Integer id) {
        try {
            Penjualan p = jdbcTemplate.queryForObject(SQL_CARI_BY_ID, new ResultSetJadiPenjualan(), id);
            List<PenjualanDetail> daftarDetail = penjualanDetailDao.cariByPenjualan(p);
            p.setDaftarPenjualanDetail(daftarDetail);
            return p;
        } catch (EmptyResultDataAccessException err) {
            return null;
        }
    }

    public Long hitungByPeriode(Date mulai, Date sampai) {
        return jdbcTemplate.queryForObject(SQL_HITUNG_BY_PERIODE, Long.class, mulai, sampai);
    }

    public List<Penjualan> cariByPeriode(Date mulai, Date sampai, Integer halaman, Integer baris) {
        List<Penjualan> hasil = jdbcTemplate.query(SQL_CARI_BY_PERIODE, new ResultSetJadiPenjualan(), 
                mulai, 
                sampai, 
                PagingHelper.halamanJadiStart(halaman, baris),
                baris);
        
        // inisialisasi relasi
        for (Penjualan p : hasil) {
            List<PenjualanDetail> daftarDetail = penjualanDetailDao.cariByPenjualan(p);
            p.setDaftarPenjualanDetail(daftarDetail);
        }
        
        return hasil;
    }


    private class ResultSetJadiPenjualan implements RowMapper<Penjualan> {

        @Override
        public Penjualan mapRow(ResultSet rs, int i) throws SQLException {
            Penjualan p = new Penjualan();
            p.setId((Integer) rs.getObject("id"));
            p.setWaktuTransaksi(rs.getDate("waktu_transaksi"));
            return p;
        }
    }
}
