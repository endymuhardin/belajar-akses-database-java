package com.muhardin.endy.training.java.aksesdb.service.plainjdbc;

import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import com.muhardin.endy.training.java.aksesdb.service.ProdukServiceTest;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;

public class ProdukServicePlainJdbcTest extends ProdukServiceTest {

    @Override
    public PenjualanService getPenjualanService() {
        PenjualanServicePlainJdbc service = new PenjualanServicePlainJdbc();
        service.setDataSource(getDataSource());
        return service;
    }

    @Override
    public DataSource getDataSource() {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setUrl("jdbc:mysql://localhost/belajar");
        ds.setUser("root");
        ds.setPassword("admin");
        return ds;
    }

}
