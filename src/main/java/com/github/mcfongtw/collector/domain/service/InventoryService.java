package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
}
