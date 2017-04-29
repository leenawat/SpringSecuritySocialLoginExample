package com.spring.security.social.login.example.config;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

//@Configuration
//@ComponentScan("com.spring.security.social.login.example")
//@PropertySources({ @PropertySource("classpath:application.properties")})
public class AppConfig {

	@Autowired
	private Environment env;

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(env.getProperty("database.driver"));
		driverManagerDataSource.setUrl(env.getProperty("database.url")
				+ "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
		driverManagerDataSource.setUsername(env.getProperty("database.usernamename"));
		driverManagerDataSource.setPassword(env.getProperty("database.password"));
		return driverManagerDataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
	    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	    viewResolver.setViewClass(JstlView.class);
	    viewResolver.setPrefix("/WEB-INF/pages/");
	    viewResolver.setSuffix(".jsp");
	    return viewResolver;
	}
	@Bean
    public HibernateTransactionManager txManager() {
            return new HibernateTransactionManager(sessionFactory());
    }
	 @Bean
     public SessionFactory sessionFactory() {
             LocalSessionFactoryBuilder builder =
			new LocalSessionFactoryBuilder(dataSource());
             builder.scanPackages("com.spring.security.social.login.example")
                   .addProperties(getHibernateProperties());

             return builder.buildSessionFactory();
     }

	private Properties getHibernateProperties() {
             Properties prop = new Properties();
             prop.put("hibernate.show.sql", "hibernate.show.sql");
             prop.put("hibernate.format_sql", "hibernate.format_sql");
             prop.put("hibernate.show_sql", env.getProperty("hibernate.show.sql"));
             prop.put("hibernate.dialect",env.getProperty("hibernate.dialect"));
             return prop;
     }
}