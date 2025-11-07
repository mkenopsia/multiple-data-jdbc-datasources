package com.example.multiple_datasources.oltp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TEST_TABLE_OLTP")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OltpEntity {
    @Id
    private Integer id;

    private String name;
}
