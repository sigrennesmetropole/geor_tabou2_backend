package rm.tabou2.storage.sig;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "sigEntityManagerFactory",
        transactionManagerRef = "sigTransactionManager",
        basePackages = "rm.tabou2.storage.sig.dao"
)
@Configuration
public class StorageSigBeanConfiguration {

    @Value("${spring.sig.datasource.hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${spring.sig.datasource.hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${spring.sig.datasource.hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;

    @Value("${spring.sig.datasource.hibernate.dialect:org.hibernate.spatial.dialect.postgis.PostgisPG10Dialect}")
    private String hibernateDialect;
    
    @Value("${spring.sig.datasource.hikari.maximum-pool-size:5}")
	private Integer maximumPoolSize;

	@Value("${spring.sig.datasource.hikari.minimum-idle:2}")
	private Integer minIdle;

    @Bean(name = "sigDataSource")
    @ConfigurationProperties(prefix = "spring.sig.datasource")
    public DataSource sigDataSource() {
    	DataSource dataSource = DataSourceBuilder.create().build();
		if (dataSource instanceof HikariDataSource) {
			if (maximumPoolSize != null && maximumPoolSize > 0) {
				((HikariDataSource) dataSource).setMaximumPoolSize(maximumPoolSize);
			}
			if (minIdle != null && minIdle > 0) {
				((HikariDataSource) dataSource).setMinimumIdle(minIdle);
			}
		}
		return dataSource;
    }


    @Bean(name = "sigEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sigEntityManagerFactory(@Qualifier("sigDataSource") DataSource dataSource, @Qualifier("entityManagerSigFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(hibernateProperties())
                .packages("rm.tabou2.storage.sig.entity")
                .persistenceUnit("sigPU")
                .build();
    }

    @Bean(name = "sigTransactionManager")
    public PlatformTransactionManager sigTransactionManager(@Qualifier("sigEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "entityManagerSigFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
				dataSource -> new HashMap<>(), null);
    }


    @Bean(name = "sigEntityManager")
    public EntityManager entityManager(@Qualifier("sigEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }


    private Map<String, String> hibernateProperties() {


        HashMap<String, String> hibernateProperties = new HashMap<>();

        hibernateProperties.put("hibernate.show_sql", hibernateFormatSql);
        hibernateProperties.put("hibernate.format_sql", hibernateShowSql);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        hibernateProperties.put("hibernate.dialect", hibernateDialect);

        return hibernateProperties;
    }


}



