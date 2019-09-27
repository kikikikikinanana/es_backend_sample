//package kr.co.shop.zconfiguration.datasource;
//
//import javax.sql.DataSource;
//
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.zaxxer.hikari.HikariConfig;
//
//import kr.co.shop.common.datasource.ABCHikariDatasource;
//import lombok.extern.slf4j.Slf4j;
//
//@Configuration
//@MapperScan(
//        annotationClass = Mapper.class, sqlSessionFactoryRef = "slaveSqlSessionFactory",
//        basePackages = {
//                 "kr.co.shop.*.*.repository.slave.**"
//        }
//)
//@Slf4j
//public class DSConfigSlave extends DsConfig {
//
//    @Autowired
//    private ApplicationContext applicationContext;
//    
// 
//    @Bean
//    @ConfigurationProperties(prefix = "datasource.slave")
//    public HikariConfig hiskariConfigSalve(){
//        return new HikariConfig();
//    }
//	
//    @Bean("slaveDataSource")
//	public DataSource slaveDataSource() {
//		
//		HikariConfig hc = hiskariConfigSalve();
//		
//		log.debug("Config => {}",hc);
//		log.debug("getJdbcUrl => {}",hc.getJdbcUrl());
//		log.debug("getDriverClassName => {}",hc.getDriverClassName());
//		log.debug("getJdbcUrl => {}",hc.getJdbcUrl());
//		
//        return new ABCHikariDatasource(hc);
//	}
// 
//    @Bean("slaveSqlSessionFactory")
//    public SqlSessionFactory slaveSqlSessionFactory() throws Exception {
//    	SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(slaveDataSource());
//        sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
//        
//        Resource[] resources  = addResource(
//    		applicationContext.getResources("classpath*:mappers/slave/**/**/*.xml")
//		);
//        return sessionFactoryBean.getObject();
//    }
//    
//    @Bean("slaveSqlSessionTemplate")
//    public SqlSessionTemplate slaveSqlSessionTemplate(SqlSessionFactory slaveSqlSessionFactory) throws Exception {
//    	return new SqlSessionTemplate(slaveSqlSessionFactory); 
//	}
//
//    @Bean("salveTransactionManager")
//    public PlatformTransactionManager salveTransactionManager() {
//        return new DataSourceTransactionManager(slaveDataSource());
//    }
//}
