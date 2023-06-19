package cn.iecas.message.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = "cn.iecas.message.mappers")
@Import(value = TransSpringConfiguration.class)
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /*@Bean
    public SqlSessionFactory sqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        try {
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
            factoryBean.setTypeAliasesPackage("cn.iecas.message.domain");
            factoryBean.setDataSource(dataSource);
            sqlSessionFactory = factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }*/
}
