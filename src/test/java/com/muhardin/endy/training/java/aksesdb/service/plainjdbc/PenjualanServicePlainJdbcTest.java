package com.muhardin.endy.training.java.aksesdb.service.plainjdbc;

import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import com.muhardin.endy.training.java.aksesdb.service.PenjualanServiceTest;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.sql.DataSource;

public class PenjualanServicePlainJdbcTest extends PenjualanServiceTest {

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
