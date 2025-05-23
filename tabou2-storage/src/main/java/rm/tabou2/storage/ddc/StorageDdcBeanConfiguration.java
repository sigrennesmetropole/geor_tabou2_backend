package rm.tabou2.storage.ddc;


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

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "ddcEntityManagerFactory",
        transactionManagerRef = "ddcTransactionManager",
        basePackages = "rm.tabou2.storage.ddc.dao"
)
@Configuration
public class StorageDdcBeanConfiguration {

    @Value("${spring.ddc.datasource.hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${spring.ddc.datasource.hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${spring.ddc.datasource.hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;

    @Bean(name = "ddcDataSource")
    @ConfigurationProperties(prefix = "spring.ddc.datasource")
    public DataSource ddcDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean(name = "ddcEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ddcEntityManagerFactory(@Qualifier("ddcDataSource") DataSource dataSource, @Qualifier("entityManagerDdcFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .properties(hibernateProperties())
                .packages("rm.tabou2.storage.ddc.entity")
                .persistenceUnit("ddcPU")
                .build();
    }


    @Bean(name = "entityManagerDdcFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerDdcFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean(name = "ddcTransactionManager")
    public PlatformTransactionManager ddcTransactionManager(@Qualifier("ddcEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map hibernateProperties() {


        HashMap<String, String> hibernateProperties = new HashMap<>();

        hibernateProperties.put("hibernate.show_sql", hibernateFormatSql);
        hibernateProperties.put("hibernate.format_sql", hibernateShowSql);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);


        return hibernateProperties;
    }


}



