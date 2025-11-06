package com.example.multiple_datasources.config;

import com.example.multiple_datasources.oltpRepository.OltpRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.core.convert.Identifier;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.MappingJdbcConverter;
import org.springframework.data.jdbc.core.convert.RelationResolver;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(
        basePackageClasses = OltpRepository.class,
        jdbcOperationsRef = "oltpNamedParameterJdbcOperations",
        transactionManagerRef = "oltpTransactionManager",
        basePackages = "com.example.multiple_datasources.oltpEntity"
)
public class OltpJdbcConfig {

    @Bean("oltpDataSource")
    public DataSource oltpDataSource(final @Value("${spring.datasource.oltp.url}") String oltpUrl,
                                     final @Value("${spring.datasource.oltp.username}") String oltpUsername,
                                     final @Value("${spring.datasource.oltp.password}") String oltpPassword) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(oltpUrl);
        dataSource.setUsername(oltpUsername);
        dataSource.setPassword(oltpPassword);
//        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean("oltpJdbcClient")
    public JdbcClient oltpJdbcClient(final @Qualifier("oltpDataSource") DataSource oltpDataSource) {
        return JdbcClient.create(oltpDataSource);
    }

    @Bean("oltpTransactionManager")
    public PlatformTransactionManager oltpTransactionManager(final @Qualifier("oltpDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    @Primary
    @Qualifier("oltpNamedParameterJdbcOperations")
    public NamedParameterJdbcOperations oltpNamedParameterJdbcOperations(@Qualifier("oltpDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceInitializer oltpDataSourceInitializer(
    final @Qualifier("oltpDataSource") DataSource dmDataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dmDataSource);
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }
}
