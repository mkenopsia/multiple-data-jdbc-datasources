package com.example.multiple_datasources.config;

import com.example.multiple_datasources.dm.repo.DmRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@EnableJdbcRepositories(
        basePackageClasses = DmRepository.class,
        jdbcOperationsRef = "dmNamedParameterJdbcOperations",
        transactionManagerRef = "dmTransactionManager"
)
@Configuration
public class DmJdbcConfig extends AbstractJdbcConfiguration {

    @Bean("dmDataSource")
    public DataSource dmDatasource(final @Value("${spring.datasource.dm.url}") String dmUrl,
                                   final @Value("${spring.datasource.dm.username}") String dmUsername,
                                   final @Value("${spring.datasource.dm.password}") String dmPassword) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dmUrl);
        dataSource.setUsername(dmUsername);
        dataSource.setPassword(dmPassword);
        return dataSource;
    }

//    @Bean("dmDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.dm")
//    public DataSource dmDataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean("dmTransactionManager")
    public PlatformTransactionManager dmTransactionManager(final @Qualifier("dmDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("dmJdbcClient")
    public JdbcClient dmJdbcClient(final @Qualifier("dmDataSource") DataSource dmDataSource) {
        return JdbcClient.create(dmDataSource);
    }

    @Bean
    @Qualifier("dmNamedParameterJdbcOperations")
    public NamedParameterJdbcOperations dmNamedParameterJdbcOperations(@Qualifier("dmDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnBean(name = "dmDataSource")
    public DataSourceInitializer dmDataSourceInitializer(final @Qualifier("dmDataSource") DataSource dmDataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dmDataSource);
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("schema_dm.sql"));
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }
}
