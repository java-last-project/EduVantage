package com.sist.web.global.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * [WHAT] DB 연결정보 주입한 datasource 생성
 * [WHY] 단일 DB인 경우: 자동생성됨
 */
@Configuration
public class DatasourceConfig {
    //기본: 메인 DB(오라클)
    @Primary
    @Bean(name="oracleDataSource")
    @ConfigurationProperties(prefix = "spirng.datasource.oracle")
    public DataSource oracleDataSource(@Value("${spring.datasource.oracle.url}") String url,
                                       @Value("${spring.datasource.oracle.username}") String username,
                                       @Value("${spring.datasource.oracle.password}") String password,
                                       @Value("${spring.datasource.oracle.driver-class-name}") String driverClassName){
        //spirng.datasource.oracle의 접속 정보를 DataSource에 채워서 반환
        /**
         *   datasource:
         *     oracle:
         *       url: ${TEAMDB_URL}
         *       username: ${TEAMDB_USERNAME}
         *       password: ${DB_PASSWORD}
         *       driver-class-name: oracle.jdbc.driver.OracleDriver
         */
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
        return ds;
    }

    //벡터DB(PostgreSQL/pgvector)
    @Bean(name="vectorDataSource")
    @ConfigurationProperties(prefix = "spirng.datasource.vector")
    public DataSource vectorDataSource(){
        //spirng.datasource.vector 접속 정보를 DataSource에 채워서 반환
        return DataSourceBuilder.create().build();
    }

    //필요할 경우: 벡터DB 전용 연산자(<==>) 사용을 위해 네이티브 SQL 생성 시 보조메서드
}
