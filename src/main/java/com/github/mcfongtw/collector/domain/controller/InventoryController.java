package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.repository.InventoryRepository;
import com.github.mcfongtw.collector.domain.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/inventories", produces = MediaType.APPLICATION_JSON_VALUE)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Inventory> create(@RequestBody Inventory warehouse) {
        log.debug("create");

        Inventory savedInventory = inventoryService.saveAndFlush(warehouse);

        if(savedInventory == null) {
            return ResponseEntity.notFound().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedInventory.getId()).toUri();

        return ResponseEntity.created(location).body(savedInventory);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(inventoryService.count());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Inventory> findById(@PathVariable(name = "id") String id) {
        log.debug("findById with [{}]", id);

        Optional<Inventory> result = inventoryService.findById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public ResponseEntity<Inventory> findInventoriesByName(@PathVariable(name = "name") String name) {
        log.debug("findInventoriesByName with [{}]", name);

        Optional<Inventory> result = inventoryService.findByName(name);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sku/{sku}")
    @ResponseBody
    public ResponseEntity<Inventory> findInventoriesBySku(@PathVariable(name = "sku") String sku) {
        log.debug("findInventoriesBySku with [{}]", sku);

        Optional<Inventory> result = inventoryService.findBySku(sku);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Inventory> update(@PathVariable(name = "id") String id, @RequestBody Inventory replacement) {
        log.debug("update with [{}]", id);

        Optional<Inventory> warehouseOptional = inventoryRepository.findById(id);

        if (!warehouseOptional.isPresent())
            return ResponseEntity.notFound().build();

        Inventory result = new Inventory();

        BeanUtils.copyProperties(replacement, result);

        inventoryRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void>  deleteById(@PathVariable(name = "id") String id) {
        log.debug("delete with [{}]", id);

        inventoryService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
