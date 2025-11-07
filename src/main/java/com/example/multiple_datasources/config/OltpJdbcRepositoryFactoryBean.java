package com.example.multiple_datasources.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.io.Serializable;

public class OltpJdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
        JdbcRepositoryFactoryBean<T, S, ID> {

    public OltpJdbcRepositoryFactoryBean(Class<T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    @Autowired
    public void setDataAccessStrategy(@Qualifier("oltp") DataAccessStrategy dataAccessStrategy) {
        super.setDataAccessStrategy(dataAccessStrategy);
    }

    @Override
    @Autowired
    public void setJdbcOperations(@Qualifier("oltp") NamedParameterJdbcOperations operations) {
        super.setJdbcOperations(operations);
    }

    @Override
    @Autowired
    public void setMappingContext(@Qualifier("oltp") RelationalMappingContext mappingContext) {
        super.setMappingContext(mappingContext);
    }

    @Override
    @Autowired
    public void setDialect(@Qualifier("oltp") Dialect dialect) {
        super.setDialect(dialect);
    }

    @Override
    @Autowired
    public void setConverter(@Qualifier("oltp") JdbcConverter converter) {
        super.setConverter(converter);
    }
}
