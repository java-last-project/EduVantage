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
 * [WHAT] 데이터베이스를 두개 이상 쓰는 경우 자동연결 X -> 별도 설정 필요
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

    //벡터DB 전용 연산자(<==>) 사용을 위해 네이티브 SQL 생성 시 보조메서드??
    //순수 벡터 전용 쿼리 실행 통로 ??
}
