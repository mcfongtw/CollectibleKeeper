package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface CRUDService<T extends AbstractEntity> {

    List<T> findAll();

    Optional<T> findById(String id);

    T saveAndFlush(T entity);

    void deleteById(String uuid);

    int count();

    void clear();
}
