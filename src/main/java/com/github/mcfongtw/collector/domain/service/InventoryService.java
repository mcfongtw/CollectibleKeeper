package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class InventoryService implements CRUDService<Inventory> {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    @Override
    public Optional<Inventory> findById(String id) {
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> findByName(String name) {
        for(Inventory inventory: inventoryRepository.findAll()) {
            if(inventory.getName().equals(name)) {
                return Optional.of(inventory);
            }
        }
        return Optional.empty();
    }

    public Optional<Inventory> findBySku(String sku) {
        for(Inventory inventory: inventoryRepository.findAll()) {
            if(inventory.getSku().equals(sku)) {
                return Optional.of(inventory);
            }
        }
        return Optional.empty();
    }

    @Override
    public Inventory saveAndFlush(Inventory entity) {
        return inventoryRepository.saveAndFlush(entity);
    }

    @Override
    public void deleteById(String uuid) {
        inventoryRepository.deleteById(uuid);
    }

    @Override
    public int count() {
        return findAll().size();
    }

    @Override
    public void clear() {
        inventoryRepository.deleteAll();
    }

    public Map<String, String> getInventoriesAsMap() {
        Map<String, String> result = new HashMap<>();

        for(Inventory inventory: inventoryRepository.findAll()) {
            result.put(inventory.getId(), inventory.getName());
        }

        return result;
    }

    public Map<String, String> getOrdersAsMap(Inventory inventory) {
        Map<String, String> result = new HashMap<>();

        InventoryOrder order = inventory.getInventoryOrder();
        result.put(order.getId(), order.getCurrency().name());

        return result;
    }
}
