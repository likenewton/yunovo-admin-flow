package cn.yunovo.iov.fc.web.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

@Configuration()
@MapperScan(basePackages = {"cn.yunovo.iov.fc.dao"}, sqlSessionTemplateRef = "clwFcSqlSessionTemplateRef")
public class DataSourceConfiguration {

	
	@Bean(name = "clwDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.clw")
    public DataSource testDataSource() {
        return new DruidDataSource();
    }
	
	@Bean(name = "clwFcSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("clwDataSource") DataSource dataSource) throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:cn/yunovo/iov/fc/dao/*.xml"));
        
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = paginationInterceptor();
        /*plugins[1] = new PerformanceInterceptor();
        */
        
        factoryBean.setPlugins(plugins);
        
        /*GlobalConfig global = new GlobalConfig();
        global.set
        factoryBean.setGlobalConfig(global);*/
        
        //关闭驼峰规则匹配
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(false);
        mybatisConfiguration.setCacheEnabled(false);
        mybatisConfiguration.setLogImpl(Slf4jImpl.class);
        factoryBean.setConfiguration(mybatisConfiguration);
        return factoryBean.getObject();
    }
	
    @Bean(name="clwTransactionManager")
    public DataSourceTransactionManager clwTransactionManager(@Qualifier("clwDataSource") DataSource dataSource){
        return  new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "clwFcSqlSessionTemplateRef")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("clwFcSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
     * 分页插件
     */
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
    
    
    


}
