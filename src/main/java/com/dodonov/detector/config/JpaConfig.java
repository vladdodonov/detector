package com.dodonov.detector.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Configuration
@EnableJpaRepositories(basePackages = {"com.dodonov.detector"})
@EnableTransactionManagement
@Slf4j
public class JpaConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.hikari.maximumPoolSize:15}")
    private String maximumPoolSize;

    @Value("${spring.datasource.hikari.minimumIdle:}")
    private String minimumIdle;

    @Value("${spring.datasource.hikari.idleTimeout:}")
    private String idleTimeout;

    @Value("${schema.recreate:false}")
    private boolean schemaRecreate;

    private HikariDataSource hikariDataSource;

    @Bean
    public FlywayMigrationStrategy migrationStrategy() {
        return flyway -> {
            flyway.setDataSource(getDatasource());

            if (schemaRecreate) {
                flyway.clean();
            }
            flyway.repair();
            flyway.migrate(); };
    }

    @Bean
    public DataSource dataSource(){
        return getDatasource();
    }

    private DataSource getDatasource() {
        if (hikariDataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl(url);
            config.setUsername(user);
            config.setPassword(password);
            config.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
            //config.setAutoCommit(false);
            if (isNotEmpty(idleTimeout)) {
                config.setIdleTimeout(Integer.parseInt(idleTimeout));
            }

            if (isNotEmpty(minimumIdle)) {
                config.setMinimumIdle(Integer.parseInt(minimumIdle));
            }
            hikariDataSource = new HikariDataSource(config);
        }
        return hikariDataSource;
    }
    @Bean
    public JdbcTemplate jdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }
}
