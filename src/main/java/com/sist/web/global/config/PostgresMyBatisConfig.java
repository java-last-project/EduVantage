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
        basePackages={"com.sist.web.domain.exam.pgmapper","com.sist.web.domain.mypage.pgmapper"},
        sqlSessionTemplateRef="postgresSessionTemplate"
)
public class PostgresMyBatisConfig {
    @Bean(name="postgresSqlSessionFactory")
    public SqlSessionFactory postgresSqlSessionFactory(@Qualifier("vectorDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factory=new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(Objects.requireNonNull(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/pgmapper/*.xml")));
        return factory.getObject();
    }

    @Bean(name="postgresSessionTemplate")
    public SqlSessionTemplate postgresSessionTemplate(@Qualifier("postgresSqlSessionFactory")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
