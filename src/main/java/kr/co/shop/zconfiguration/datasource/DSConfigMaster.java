package kr.co.shop.zconfiguration.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;

import kr.co.shop.common.datasource.ABCHikariDatasource;
import lombok.extern.slf4j.Slf4j;

@Configuration
@MapperScan(annotationClass = Mapper.class, sqlSessionFactoryRef = "sqlSessionFactory", basePackages = {
		"kr.co.shop.*.*.repository.master.**" })
@Slf4j
public class DSConfigMaster extends DsConfig {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	@ConfigurationProperties(prefix = "datasource.master")
	public HikariConfig hiskariConfigMaster() {
		return new HikariConfig();
	}

	@Primary
	@Bean
	public DataSource dataSource() {

		HikariConfig hc = hiskariConfigMaster();

		log.debug("Config => {}", hc);
		log.debug("getJdbcUrl => {}", hc.getJdbcUrl());
		log.debug("getDriverClassName => {}", hc.getDriverClassName());
		log.debug("getUsername => {}", hc.getUsername());

		return new ABCHikariDatasource(hc);
	}

	@Primary
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {

		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));

		Resource[] resources = addResource(
				// shop.common.jar에 있음.
				applicationContext.getResources("classpath*:mappers/Paging.xml"),
				applicationContext.getResources("classpath*:mappers/master/**/**/*.xml"));

		sessionFactoryBean.setMapperLocations(resources);
		return sessionFactoryBean.getObject();
	}

	@Primary
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionTemplate) throws Exception {
		return new SqlSessionTemplate(sqlSessionTemplate);
	}

	@Primary
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
		return transactionManager;
	}

}
