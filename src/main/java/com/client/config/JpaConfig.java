package com.client.config;

import com.client.Application;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = Application.class)
public class JpaConfig implements TransactionManagementConfigurer {

    private static final DBConfig OPEN_SHIFT_CONFIG = new DBConfig("com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQL5InnoDBDialect", "userNQX", "ctsRX0Ao52bS6yiv", "jdbc:mysql://mysql:3306/sampledb?useUnicode=true&characterEncoding=utf8");
    private static final DBConfig LOCAL_CONFIG = new DBConfig("com.mysql.jdbc.Driver", "org.hibernate.dialect.MySQL5InnoDBDialect", "root", "", "jdbc:mysql://localhost/neolands?useUnicode=true&characterEncoding=utf8");
    private static final DBConfig HEROKU_CONFIG = DBConfig.getHerokuConfig();

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.format_sql}")
    private String formatSql;
    @Value("${dataSource.isProd}")
    private boolean isProd;

    private DBConfig getDBConfig() {
        if (isProd) {
            if (DBConfig.isHeroku() && Objects.nonNull(HEROKU_CONFIG)) {
                return HEROKU_CONFIG;
            }
            return OPEN_SHIFT_CONFIG;
        }
        return LOCAL_CONFIG;
    }

    @Bean
    public DataSource configureDataSource() {
        HikariConfig config = new HikariConfig();
        DBConfig dbConfig = getDBConfig();
        System.out.println("Using DB config: " + dbConfig.toString());
        config.setDriverClassName(dbConfig.getDriver());

        config.setJdbcUrl(dbConfig.getUrl());
        config.setUsername(dbConfig.getUsername());
        config.setPassword(dbConfig.getPassword());
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        HikariDataSource dataSource = new HikariDataSource(config);
        dataSource.setMaxLifetime(TimeUnit.SECONDS.toMillis(30L));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean configureEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(configureDataSource());
        entityManagerFactoryBean.setPackagesToScan("com.client");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.put(Environment.DIALECT, getDBConfig().getDialect());
        jpaProperties.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        jpaProperties.put(Environment.SHOW_SQL, showSql);
        jpaProperties.put(Environment.FORMAT_SQL, formatSql);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new JpaTransactionManager();
    }
}
