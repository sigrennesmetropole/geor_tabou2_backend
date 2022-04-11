package rm.tabou2.service.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Initialisation manuelle de la base de données H2 avec h2gis
 */
public abstract class DatabaseInitializerTest {

    @BeforeEach
    protected void initTestClass(@Qualifier(value = "tabouDataSource") @Autowired DataSource tabouDataSource,
                               @Qualifier(value = "ddcDataSource") @Autowired DataSource ddcDataSource,
                               @Qualifier(value = "sigDataSource") @Autowired DataSource sigDataSource) {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/tabou/common_tabou_data.sql"));
        populator.execute(tabouDataSource);
    }

    @AfterEach
    protected void afterClassTest(@Qualifier(value = "tabouDataSource") @Autowired DataSource tabouDataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/tabou/clean_tabou_data.sql"));
        populator.execute(tabouDataSource);
    }
}
