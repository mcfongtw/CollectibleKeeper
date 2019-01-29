package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.AbstractEntity;

import java.util.List;

public interface CRUDService<T extends AbstractEntity> {

    List<T> findAll();

    T getOne(String uuid);

    T saveAndFlush(T entity);

    void deleteById(String uuid);
}
