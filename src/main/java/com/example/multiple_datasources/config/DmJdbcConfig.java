package com.example.multiple_datasources.config;

import com.example.multiple_datasources.dm.repo.DmRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.core.convert.RelationResolver;
import org.springframework.data.jdbc.core.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Optional;

@EnableJdbcRepositories(
        repositoryFactoryBeanClass = DmJdbcConfig.JdbcRepositoryFactoryBeanDb1.class,
        transactionManagerRef = "db1",
        basePackageClasses = DmRepository.class
)
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class}
)
@Configuration
@ConfigurationPropertiesScan
public class DmJdbcConfig extends AbstractJdbcConfiguration {
    private final AbstractJdbcConfiguration base;

    public DmJdbcConfig(ApplicationContext applicationContext) {
        this.base = new AbstractJdbcConfiguration();
        this.base.setApplicationContext(applicationContext);
    }

    @Bean
    @Qualifier("db1")
    public DataSource dataSourceDb1(final @Value("${spring.datasource.dm.url}") String oltpUrl,
                                     final @Value("${spring.datasource.dm.username}") String oltpUsername,
                                     final @Value("${spring.datasource.dm.password}") String oltpPassword,
                                     final @Value("${spring.datasource.dm.pool-name}") String oltpPoolName) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(oltpUrl);
        dataSource.setUsername(oltpUsername);
        dataSource.setPassword(oltpPassword);
        dataSource.setPoolName(oltpPoolName);
        return dataSource;
    }

    @Bean
    @Qualifier("db1")
    public NamedParameterJdbcOperations jdbcOperationsDb1(
            @Qualifier("db1") DataSource dataSource
    ) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("db1")
    public PlatformTransactionManager transactionManagerDb1(
            @Qualifier("db1") DataSource dataSource
    ) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    @Qualifier("db1")
    public RelationalManagedTypes jdbcManagedTypesDb1() throws ClassNotFoundException {
        return base.jdbcManagedTypes();
    }

    @Bean
    @Qualifier("db1")
    @Override
    public JdbcMappingContext jdbcMappingContext(
            Optional<NamingStrategy> namingStrategy,
            @Qualifier("db1") JdbcCustomConversions customConversions,
            @Qualifier("db1") RelationalManagedTypes jdbcManagedTypes) {
        return base.jdbcMappingContext(namingStrategy, customConversions, jdbcManagedTypes);
    }

    @Bean
    @Qualifier("db1")
    @Override
    public JdbcConverter jdbcConverter(
            @Qualifier("db1") JdbcMappingContext mappingContext,
            @Qualifier("db1") NamedParameterJdbcOperations operations,
            @Qualifier("db1") @Lazy RelationResolver relationResolver,
            @Qualifier("db1") JdbcCustomConversions conversions,
            @Qualifier("db1") Dialect dialect) {
        return base.jdbcConverter(mappingContext, operations, relationResolver, conversions, dialect);
    }

    @Bean
    @Qualifier("db1")
    public JdbcCustomConversions jdbcCustomConversionsDb1() {
        return base.jdbcCustomConversions();
    }

    @Bean
    @Qualifier("db1")
    @Override
    public JdbcAggregateTemplate jdbcAggregateTemplate(
            ApplicationContext applicationContext,
            @Qualifier("db1") JdbcMappingContext mappingContext,
            @Qualifier("db1") JdbcConverter converter,
            @Qualifier("db1") DataAccessStrategy dataAccessStrategy) {
        return base.jdbcAggregateTemplate(applicationContext, mappingContext, converter, dataAccessStrategy);
    }

    @Bean
    @Qualifier("db1")
    @Override
    public DataAccessStrategy dataAccessStrategyBean(
            @Qualifier("db1") NamedParameterJdbcOperations operations,
            @Qualifier("db1") JdbcConverter jdbcConverter,
            @Qualifier("db1") JdbcMappingContext context,
            @Qualifier("db1") Dialect dialect) {
        return base.dataAccessStrategyBean(operations, jdbcConverter, context, dialect);
    }

    @Bean
    @Qualifier("db1")
    @Override
    public Dialect jdbcDialect(@Qualifier("db1") NamedParameterJdbcOperations operations) {
        return base.jdbcDialect(operations);
    }

    public static class JdbcRepositoryFactoryBeanDb1<T extends Repository<S, ID>, S, ID extends Serializable> extends
            JdbcRepositoryFactoryBean<T, S, ID> {

        public JdbcRepositoryFactoryBeanDb1(Class<T> repositoryInterface) {
            super(repositoryInterface);
        }

        @Override
        @Autowired
        public void setDataAccessStrategy(@Qualifier("db1") DataAccessStrategy dataAccessStrategy) {
            super.setDataAccessStrategy(dataAccessStrategy);
        }

        @Override
        @Autowired
        public void setJdbcOperations(@Qualifier("db1") NamedParameterJdbcOperations operations) {
            super.setJdbcOperations(operations);
        }

        @Override
        @Autowired
        public void setMappingContext(@Qualifier("db1") RelationalMappingContext mappingContext) {
            super.setMappingContext(mappingContext);
        }

        @Override
        @Autowired
        public void setDialect(@Qualifier("db1") Dialect dialect) {
            super.setDialect(dialect);
        }

        @Override
        @Autowired
        public void setConverter(@Qualifier("db1") JdbcConverter converter) {
            super.setConverter(converter);
        }
    }

    @Bean
    public DataSourceInitializer dmDataSourceInitializer(final @Qualifier("db1") DataSource dmDataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dmDataSource);
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("schema_dm.sql"));
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }
}
