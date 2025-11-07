package com.example.multiple_datasources.dm.repo;

import com.example.multiple_datasources.dm.entity.DmEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmRepository extends CrudRepository<DmEntity, Integer> {
    DmEntity findByName(String name);
}
