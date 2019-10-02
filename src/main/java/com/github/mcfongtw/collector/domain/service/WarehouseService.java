package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.dao.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Slf4j
@Transactional
@Service
public class WarehouseService implements CRUDService<Warehouse> {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public Optional<Warehouse> findById(String id) {
        return warehouseRepository.findById(id);
    }

    public Optional<Warehouse> findByName(String name) {
        for(Warehouse warehouse: warehouseRepository.findAll()) {
            if(warehouse.getName().equals(name)) {
                return Optional.of(warehouse);
            }
        }
        return Optional.empty();
    }

    @Override
    public Warehouse saveAndFlush(Warehouse entity) {
        return warehouseRepository.saveAndFlush(entity);
    }

    public Warehouse saveOrUpdate(Warehouse entity) {
        return warehouseRepository.save(entity);
    }

    @Override
    public void deleteById(String uuid) {
        warehouseRepository.deleteById(uuid);
    }

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public void clear() {
        warehouseRepository.deleteAll();
    }

    @Override
    public void deleteWithoutAssociations(String id) {
        Warehouse warehouse = entityManager.find(Warehouse.class, id);

        //remove associations at child entity
        for(Inventory inventory: warehouse.getInventories()) {
            inventory.setWarehouse(null);
        }

        //remove parent entity
        entityManager.remove(warehouse);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Map<String, String> getWarhousesAsMap() {
        Map<String, String> result = new HashMap<>();

        for(Warehouse warehouse: warehouseRepository.findAll()) {
            result.put(warehouse.getId(), warehouse.getName());
        }

        return result;
    }

    public Map<String, String> getInventoriesAsMap(Warehouse warehouse) {
        Map<String, String> result = new HashMap<>();

        for(Inventory inventory: warehouse.getInventories()) {
            result.put(inventory.getId(), inventory.getName());
        }

        return result;
    }
}
