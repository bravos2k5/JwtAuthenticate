package com.bravos.steak.jwtauthentication.common.configuration;

import com.bravos.steak.jwtauthentication.common.utils.Env;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(Env.getOrDefault("DB_DRIVER","org.postgresql.Driver"));
        dataSource.setJdbcUrl(getJdbcUrl());
        return dataSource;
    }

    private String getJdbcUrl() {
        String dbUrl = Env.get("DB_URL");
        if(dbUrl == null || dbUrl.isEmpty()) {
            throw new IllegalArgumentException("Database URL is not set in the environment variables.");
        }
        if(!dbUrl.startsWith("jdbc:")) {
            return  "jdbc:" + dbUrl;
        }
        return dbUrl;
    }

}
