package com.example.multiple_datasources.config;

import com.example.multiple_datasources.oltp.repo.OltpRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
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
import org.springframework.data.relational.RelationalManagedTypes;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Optional;

@EnableJdbcRepositories(
        repositoryFactoryBeanClass = OltpJdbcRepositoryFactoryBean.class,
        transactionManagerRef = "oltp",
        basePackageClasses = OltpRepository.class
)
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class}
)
@Configuration
@ConfigurationPropertiesScan
public class OltpJdbcConfig extends AbstractJdbcConfiguration {

    private final AbstractJdbcConfiguration base;

    public OltpJdbcConfig(ApplicationContext applicationContext) {
        this.base = new AbstractJdbcConfiguration();
        this.base.setApplicationContext(applicationContext);
    }

    @Bean
    @Qualifier("oltp")
    public DataSource oltpDatasource(final @Value("${spring.datasource.oltp.url}") String oltpUrl,
                                     final @Value("${spring.datasource.oltp.username}") String oltpUsername,
                                     final @Value("${spring.datasource.oltp.password}") String oltpPassword,
                                     final @Value("${spring.datasource.oltp.pool-name}") String oltpPoolName) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(oltpUrl);
        dataSource.setUsername(oltpUsername);
        dataSource.setPassword(oltpPassword);
        dataSource.setPoolName(oltpPoolName);
        return dataSource;
    }

    @Bean
    @Qualifier("oltp")
    public PlatformTransactionManager oltpTransactionManager(final @Qualifier("oltp") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    @Qualifier("oltp")
    public NamedParameterJdbcOperations oltpNamedParameterJdbcOperations(@Qualifier("oltp") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    @Qualifier("oltp")
    public RelationalManagedTypes oltpJdbcManagedTypes() throws ClassNotFoundException {
        return base.jdbcManagedTypes();
    }

    @Bean
    @Qualifier("oltp")
    public JdbcCustomConversions oltpJdbcCustomConversions() {
        return base.jdbcCustomConversions();
    }

    @Bean
    @Qualifier("oltp")
    public JdbcMappingContext oltpJdbcMappingContext(
            Optional<NamingStrategy> namingStrategy,
            @Qualifier("oltp") JdbcCustomConversions customConversions,
            @Qualifier("oltp") RelationalManagedTypes jdbcManagedTypes) {
        return base.jdbcMappingContext(namingStrategy, customConversions, jdbcManagedTypes);
    }

    @Bean
    @Qualifier("oltp")
    public JdbcConverter oltpJdbcConverter(
            @Qualifier("oltp") JdbcMappingContext mappingContext,
            @Qualifier("oltp") NamedParameterJdbcOperations operations,
            @Qualifier("oltp") @Lazy RelationResolver relationResolver,
            @Qualifier("oltp") JdbcCustomConversions conversions,
            @Qualifier("oltp") Dialect dialect) {
        return base.jdbcConverter(mappingContext, operations, relationResolver, conversions, dialect);
    }

    @Bean
    @Qualifier("oltp")
    public JdbcAggregateTemplate oltpJdbcAggregateTemplate(
            ApplicationContext applicationContext,
            @Qualifier("oltp") JdbcMappingContext mappingContext,
            @Qualifier("oltp") JdbcConverter converter,
            @Qualifier("oltp") DataAccessStrategy oltpDataAccessStrategy) {
        return base.jdbcAggregateTemplate(applicationContext, mappingContext, converter, oltpDataAccessStrategy);
    }

    @Bean
    @Qualifier("oltp")
    public DataAccessStrategy oltpDataAccessStrategy(
            @Qualifier("oltp") NamedParameterJdbcOperations operations,
            @Qualifier("oltp") JdbcConverter jdbcConverter,
            @Qualifier("oltp") JdbcMappingContext context,
            @Qualifier("oltp") Dialect dialect) {
        return base.dataAccessStrategyBean(operations, jdbcConverter, context, dialect);
    }

    @Bean
    @Qualifier("oltp")
    public Dialect oltpJdbcDialect(@Qualifier("oltp") NamedParameterJdbcOperations operations) {
        return base.jdbcDialect(operations);
    }

    @Bean
    public DataSourceInitializer oltpDataSourceInitializer(final @Qualifier("oltp") DataSource oltpDataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(oltpDataSource);
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(new ClassPathResource("schema_oltp.sql"));
        initializer.setDatabasePopulator(databasePopulator);
        return initializer;
    }
}
