package com.example.multiple_datasources.dm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TEST_TABLE_DM")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DmEntity {
    @Id
    private Integer id;

    private String name;
}
