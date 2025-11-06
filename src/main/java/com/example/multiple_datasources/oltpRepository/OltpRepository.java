package com.example.multiple_datasources.oltpRepository;

import com.example.multiple_datasources.oltpEntity.OltpEntity;
import org.springframework.data.repository.CrudRepository;

public interface OltpRepository extends CrudRepository<OltpEntity, Integer> {
    OltpEntity findByName(String name);
}
