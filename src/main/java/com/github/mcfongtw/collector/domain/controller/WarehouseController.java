package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.dao.repository.WarehouseRepository;
import com.github.mcfongtw.collector.domain.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping(value = "/warehouses", produces = "application/json")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Warehouse warehouse) {
        log.debug("create");

        Warehouse savedWarehouse = warehouseService.saveAndFlush(warehouse);

        if(savedWarehouse == null) {
            return ResponseEntity.notFound().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedWarehouse.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Warehouse>> findAll() {
        return ResponseEntity.ok(warehouseService.findAll());
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(warehouseService.count());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Warehouse> findById(@PathVariable(name = "id") String id) {
        log.debug("findById with [{}]", id);

        Optional<Warehouse> result = warehouseService.findById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    @ResponseBody
    public ResponseEntity<Warehouse> findByName(@PathVariable(name = "name") String name) {
        log.debug("findByName with [{}]", name);

        Optional<Warehouse> result = warehouseService.findByName(name);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/inventories/{id}")
    @ResponseBody
    public ResponseEntity<Set<Inventory>> findInventoriesById(@PathVariable(name = "id") String id) {
        log.debug("findInventoriesById with [{}]", id);

        Optional<Warehouse> result = warehouseService.findById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get().getInventories());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Warehouse> update(@PathVariable(name = "id") String id, @RequestBody Warehouse replacement) {
        log.debug("update with [{}]", id);

        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);

        if (!warehouseOptional.isPresent())
            return ResponseEntity.notFound().build();

        Warehouse result = new Warehouse();

        BeanUtils.copyProperties(replacement, result);

        warehouseRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void>  deleteById(@PathVariable(name = "id") String id) {
        log.debug("delete with [{}]", id);

        warehouseService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
