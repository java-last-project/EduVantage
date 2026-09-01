package com.sist.web.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * [WHAT] JPA가 오라클 DB를 쓸 수 있도록
 *          EntityManagerFactory, 트랜잭션관리자 수동 생성
 * [WHY] 단일 DB였으면 스프링부트가 자동 생성해주지만, DB가 두개 이상일 경우 인식 못함
 */
@Configuration
@EnableJpaRepositories(
        // 패키지 경로 하위의 JPA repository 상속 인터페이스에 Oracle 기본 주입
        basePackages = "com.sist.web.domain",
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager"
)
public class JpaConfig {

    @Primary
    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("oracleDataSource") DataSource oracleDataSource) {

        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.show_sql", true);

        return builder
                //오라클 datasource 연결
                .dataSource(oracleDataSource)
                //패키지 경로 하위의 @Entity가 붙은 클래스 대상에
                .packages("com.sist.web.domain")
                .persistenceUnit("oracle")
                .build();
    }

    @Primary
    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }
}