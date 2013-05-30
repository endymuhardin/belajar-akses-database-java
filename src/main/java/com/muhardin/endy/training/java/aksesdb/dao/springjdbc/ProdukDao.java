package com.muhardin.endy.training.java.aksesdb.dao.springjdbc;

import com.muhardin.endy.training.java.aksesdb.domain.Produk;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ProdukDao {

    private static final String SQL_CARI_BY_ID = "select * from m_produk where id = ?";
    private static final String SQL_CARI_BY_KODE = "select * from m_produk where kode = ?";
    private static final String SQL_HITUNG_SEMUA = "select count(*) from m_produk";
    private static final String SQL_CARI_SEMUA = "select * from m_produk limit ?,?";
    private static final String SQL_HITUNG_BY_NAMA = "select count(*) from m_produk where lower(nama) like ?";
    private static final String SQL_CARI_BY_NAMA = "select * from m_produk where lower(nama) like ? limit ?,?";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertProduk;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertProduk = new SimpleJdbcInsert(dataSource)
                .withTableName("m_produk")
                .usingGeneratedKeyColumns("id");
    }

    public void simpan(Produk p) {
        SqlParameterSource produkParameter = new BeanPropertySqlParameterSource(p);
        Number idBaru = insertProduk.executeAndReturnKey(produkParameter);
        p.setId(idBaru.intValue());
    }

    public Produk cariById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SQL_CARI_BY_ID, new ResultSetJadiProduk(), id);
        } catch (EmptyResultDataAccessException err) {
            return null;
        }
    }

    public Produk cariByKode(String kode) {
        try {
            return jdbcTemplate.queryForObject(SQL_CARI_BY_KODE, new ResultSetJadiProduk(), kode);
        } catch (EmptyResultDataAccessException err) {
            return null;
        }
    }

    public Long hitungSemua() {
        return jdbcTemplate.queryForObject(SQL_HITUNG_SEMUA, Long.class);
    }

    public List<Produk> cariSemua(Integer halaman, Integer baris) {
        return jdbcTemplate.query(SQL_CARI_SEMUA,
                new ResultSetJadiProduk(),
                PagingHelper.halamanJadiStart(halaman, baris),
                baris);
    }

    public Long hitungByNama(String nama) {
        return jdbcTemplate.queryForObject(SQL_HITUNG_BY_NAMA, Long.class, "%" + nama.toLowerCase() + "%");
    }

    public List<Produk> cariByNama(String nama, Integer halaman, Integer baris) {
        return jdbcTemplate.query(SQL_CARI_BY_NAMA,
                new ResultSetJadiProduk(),
                "%" + nama.toLowerCase() + "%",
                PagingHelper.halamanJadiStart(halaman, baris),
                baris);
    }

    private class ResultSetJadiProduk implements RowMapper<Produk> {

        @Override
        public Produk mapRow(ResultSet rs, int i) throws SQLException {
            Produk p = new Produk();
            p.setId((Integer) rs.getObject("id"));
            p.setKode(rs.getString("kode"));
            p.setNama(rs.getString("nama"));
            p.setHarga(rs.getBigDecimal("harga"));
            return p;
        }
    }
}
