package com.example.multiple_datasources.dmRepository;

import com.example.multiple_datasources.dmEntity.DmEntity;
import org.springframework.data.repository.CrudRepository;

public interface DmRepository extends CrudRepository<DmEntity, Integer> {
    DmEntity findByName(String name);
}
