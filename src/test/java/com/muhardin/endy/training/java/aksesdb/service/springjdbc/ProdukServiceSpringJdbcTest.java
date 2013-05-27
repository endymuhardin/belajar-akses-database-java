package com.muhardin.endy.training.java.aksesdb.service.springjdbc;

import com.muhardin.endy.training.java.aksesdb.service.PenjualanService;
import com.muhardin.endy.training.java.aksesdb.service.ProdukServiceTest;
import javax.sql.DataSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:com/muhardin/**/spring-jdbc-ctx.xml")
public class ProdukServiceSpringJdbcTest extends ProdukServiceTest {

    @Autowired private DataSource dataSource;
    @Autowired private PenjualanService penjualanService;
    
    @Override
    public PenjualanService getPenjualanService() {
        return penjualanService;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
