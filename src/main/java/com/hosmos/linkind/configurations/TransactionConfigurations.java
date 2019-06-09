package com.hosmos.linkind.configurations;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.beans.PropertyVetoException;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.hosmos.linkind.dao", "com.hosmos.linkind.services"})
@PropertySource("classpath:application.properties")
public class TransactionConfigurations implements EnvironmentAware {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean(destroyMethod = "close")
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        logger.trace("Driver class: " + environment.getProperty("connection.driver_class"));
//        logger.trace("URL: " + environment.getProperty("connection.url"));
//        logger.trace("Username: " + environment.getProperty("connection.username"));
//        logger.trace("Password: " + environment.getProperty("connection.password"));
        dataSource.setDriverClass(environment.getProperty("connection.driver_class"));
        dataSource.setJdbcUrl(environment.getProperty("connection.url"));
        dataSource.setUser(environment.getProperty("connection.username"));
        dataSource.setPassword(environment.getProperty("connection.password"));
        dataSource.setAcquireIncrement(10);
        dataSource.setIdleConnectionTestPeriod(60);
        dataSource.setMaxPoolSize(100);
        dataSource.setMaxStatements(50);
        dataSource.setMinPoolSize(10);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() throws PropertyVetoException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.hosmos.linkind.dao");
        return mapperScannerConfigurer;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws PropertyVetoException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("sqlmap-config.xml"));
        return sqlSessionFactoryBean;
    }

    @PostConstruct
    public void init() {
        logger.info("Transactions initialized.");
    }
}
