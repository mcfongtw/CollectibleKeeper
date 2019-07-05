package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.repository.InventoryOrderRepository;
import com.github.mcfongtw.collector.domain.service.InventoryOrderService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/json")
public class InventoryOrderController {

    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;

    @Autowired
    private InventoryOrderService inventoryOrderService;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @PostMapping
    public ResponseEntity<InventoryOrder> create(@RequestBody InventoryOrder inventoryOrder) {
        log.debug("create");

        InventoryOrder savedOrder = inventoryOrderService.saveAndFlush(inventoryOrder);

        if(savedOrder == null) {
            return ResponseEntity.notFound().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedOrder.getId()).toUri();

        return ResponseEntity.created(location).body(savedOrder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryOrder>> findAll() {
        log.debug("findAll");

        List<InventoryOrder> result = inventoryOrderService.findAll();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Integer> count() {
        return ResponseEntity.ok(inventoryOrderService.count());
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<InventoryOrder> findById(@PathVariable(name = "id") String id) {
        log.debug("findById with [{}]", id);

        Optional<InventoryOrder> result = inventoryOrderService.findById(id);

        if(result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<InventoryOrder> update(@PathVariable(name = "id") String id, @RequestBody InventoryOrder replacement) {
        log.debug("update with [{}]", id);

        Optional<InventoryOrder> warehouseOptional = inventoryOrderService.findById(id);

        if (!warehouseOptional.isPresent())
            return ResponseEntity.notFound().build();

        InventoryOrder result = new InventoryOrder();

        BeanUtils.copyProperties(replacement, result);

        inventoryOrderRepository.save(result);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void>  deleteById(@PathVariable(name = "id") String id) {
        log.debug("delete with [{}]", id);

        inventoryOrderRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/search/above/buy/{price}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderAbovePurchasedPrice(@PathVariable(name = "price") String price) {
        log.debug("getListOfOrderAbovePurchasedPrice");

        List<InventoryOrder> result = inventoryOrderService.getListOfOrderAbovePurchasedPrice(Double.valueOf(price));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/above/sold/{price}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderAboveSoldPrice(@PathVariable(name = "price") String price) {
        log.debug("getListOfOrderAboveSoldPrice");

        List<InventoryOrder> result = inventoryOrderService.getListOfOrderAboveSoldPrice(Double.valueOf(price));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/below/buy/{price}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderBelowPurchasedPrice(@PathVariable(name = "price") String price) {
        log.debug("getListOfOrderBelowPurchasedPrice");

        List<InventoryOrder> result = inventoryOrderService.getListOfOrderBelowPurchasedPrice(Double.valueOf(price));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/below/sold/{price}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderBelowSoldPrice(@PathVariable(name = "price") String price) {
        log.debug("getListOfOrderBelowSoldPrice");

        List<InventoryOrder> result = inventoryOrderService.getListOfOrderBelowSoldPrice(Double.valueOf(price));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/before/buy/{date}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderBeforePurchasedDate(@PathVariable(name = "date") String date) {
        log.debug("getListOfOrderBeforePurchasedDate");

        List<InventoryOrder> result = Lists.newArrayList();
        try {
            result.addAll(inventoryOrderService.getListOfOrderBeforePurchasedDate(formatter.parse(date)));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/before/sold/{date}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderBeforeSoldDate(@PathVariable(name = "date") String date) {
        log.debug("getListOfOrderBeforeSoldDate");

        List<InventoryOrder> result = Lists.newArrayList();
        try {
            result.addAll(inventoryOrderService.getListOfOrderBeforePurchasedDate(formatter.parse(date)));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/after/buy/{date}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderAfterPurchasedDate(@PathVariable(name = "date") String date) {
        log.debug("getListOfOrderAfterPurchasedDate");

        List<InventoryOrder> result = Lists.newArrayList();
        try {
            result.addAll(inventoryOrderService.getListOfOrderAfterPurchasedDate(formatter.parse(date)));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/after/sold/{date}")
    public ResponseEntity<List<InventoryOrder>> getListOfOrderAfterSoldDate(@PathVariable(name = "date") String date) {
        log.debug("getListOfOrderAfterSoldDate");

        List<InventoryOrder> result = Lists.newArrayList();
        try {
            result.addAll(inventoryOrderService.getListOfOrderAfterSoldDate(formatter.parse(date)));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(result);
    }

}


