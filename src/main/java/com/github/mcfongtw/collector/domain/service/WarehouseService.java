package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.dao.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class WarehouseService implements CRUDService<Warehouse> {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public Warehouse getOne(String uuid) {
        return warehouseRepository.getOne(uuid);
    }

    @Override
    public Warehouse saveAndFlush(Warehouse entity) {
        return warehouseRepository.saveAndFlush(entity);
    }

    @Override
    public void deleteById(String uuid) {
        warehouseRepository.deleteById(uuid);
    }

    @Override
    public int count() {
        return findAll().size();
    }
}
