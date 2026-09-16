package com.sist.web.global.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@MapperScan(
        basePackages= {
                "com.sist.web.domain.admin.mapper",
                "com.sist.web.domain.auth.mapper",
                "com.sist.web.domain.book.mapper",
                "com.sist.web.domain.community.mapper",
                "com.sist.web.domain.course.mapper",
                "com.sist.web.domain.enrollment.mapper",
                "com.sist.web.domain.exam.mapper",
                "com.sist.web.domain.instructor.mapper",
                "com.sist.web.domain.main.mapper",
                "com.sist.web.domain.member.mapper",
                "com.sist.web.domain.mypage.mapper",
                "com.sist.web.domain.notification.mapper"
        },
        sqlSessionTemplateRef="oracleSessionTemplate"
)
public class OracleMyBatisConfig {
    @Bean(name="oracleSqlSessionFactory")
    public SqlSessionFactory oracleSqlSessionFactory(@Qualifier("oracleDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factory=new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTypeAliasesPackage("com.sist.web.domain");
        org.apache.ibatis.session.Configuration mybatisConfig = new org.apache.ibatis.session.Configuration();
        mybatisConfig.setCallSettersOnNulls(true);
        factory.setConfiguration(mybatisConfig);
        factory.setMapperLocations(Objects.requireNonNull(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/**/*.xml")));
        return factory.getObject();
    }

    @Bean(name="oracleSessionTemplate")
    public SqlSessionTemplate oracleSessionTemplate(@Qualifier("oracleSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
