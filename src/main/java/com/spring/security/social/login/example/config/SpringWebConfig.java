package com.spring.security.social.login.example.config;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan("com.spring.security.social.login.example")
@PropertySources({ @PropertySource("classpath:application.properties") })
@ImportResource("classpath:spring-persistent-servlet.xml")
public class SpringWebConfig extends WebMvcConfigurerAdapter {

//	private static final Logger logger = LoggerFactory.getLogger(SpringWebConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("/authen")
		 .addResourceLocations("/authen/**");
		 registry.addResourceHandler("/services")
		 .addResourceLocations("/services/*");
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/pages/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Autowired
	private Environment env;

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(env.getProperty("database.driver"));
		driverManagerDataSource.setUrl(
				env.getProperty("database.url") + "?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull");
		driverManagerDataSource.setUsername(env.getProperty("database.username"));
		driverManagerDataSource.setPassword(env.getProperty("database.password"));
		return driverManagerDataSource;
	}

	 @Bean
	 public LocalSessionFactoryBean sessionFactory() {
	 System.out.println("Creating entity Manager");
	 LocalSessionFactoryBean factoryBean=new LocalSessionFactoryBean();
	 factoryBean.setDataSource(dataSource());
	 factoryBean.setPackagesToScan(new String[]
	 {"com.spring.security.social.login.example.database"});
	 factoryBean.setHibernateProperties(hibernateProperties());
	 return factoryBean;
	 }

	Properties hibernateProperties() {
		return new Properties() {
			private static final long serialVersionUID = -8624811876845574880L;

			{
				setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
				setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
				setProperty("hibernate.show.sql", env.getProperty("hibernate.show.sql"));
			}
		};
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);

		return txManager;
	}

}
