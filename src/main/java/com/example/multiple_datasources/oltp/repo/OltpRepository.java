package com.example.multiple_datasources.oltp.repo;

import com.example.multiple_datasources.oltp.entity.OltpEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OltpRepository extends CrudRepository<OltpEntity, Integer> {
    OltpEntity findByName(String name);
}
