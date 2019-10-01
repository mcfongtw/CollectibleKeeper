package com.github.mcfongtw.collector.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AbstractCollectable extends AbstractEntity {

    @Column(name = "sku", nullable = false)
    protected String sku;

    @Column(name = "name", nullable = false)
    protected String name;
}
