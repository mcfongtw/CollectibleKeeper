package com.github.mcfongtw.collector.domain.controller.view;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.domain.service.InventoryOrderService;
import com.github.mcfongtw.collector.domain.service.InventoryService;
import com.github.mcfongtw.collector.domain.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Slf4j
@Controller
public class SimpleModelViewController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryOrderService inventoryOrderService;

    @Autowired
    private WarehouseService warehouseService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS");

    @GetMapping("/")
    public String main(Model model) {

        //welcome.html
        return "welcome";
    }


    ////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("view/inventories/all")
    public String showAllInventories(Model model) {
        model.addAttribute("inventories", inventoryService.findAll());

        //show-inventory.html
        return "show-inventory";
    }

    @GetMapping("view/orders/all")
    public String showAllOrders(Model model) {
        model.addAttribute("orders", inventoryOrderService.findAll());

        //show-order.html
        return "show-order";
    }

    @GetMapping("view/warehouses/all")
    public String showAllWarehouses(Model model) {
        model.addAttribute("warehouses", warehouseService.findAll());

        //show-warehouse.html
        return "show-warehouse";
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @GetMapping("view/inventories/new")
    public String addInventory(Inventory inventory, Model model) {
        Map<String, String> mappedWarehouses = warehouseService.getMappedWarehouseIdsAndNames();

        model.addAttribute("mappedWarehouses", mappedWarehouses);

        //add-inventory.html
        return "add-inventory";
    }

    @PostMapping("view/inventories/save")
    public String saveNewInventory(@Valid Inventory inventory, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //add-inventory.html
            return "add-inventory";
        }

        inventoryService.saveAndFlush(inventory);

        model.addAttribute("inventories", inventoryService.findAll());

        //show-inventory.html
        return "show-inventory";
    }

    @GetMapping("view/warehouses/new")
    public String addWarehouse(Warehouse warehouse, Model model) {
        //add-warehouse.html
        return "add-warehouse";
    }

    @PostMapping("view/warehouses/save")
    public String saveNewWarehouse(@Valid Warehouse warehouse, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //add-warehouse.html
            return "add-warehouse";
        }

        warehouseService.saveAndFlush(warehouse);

        model.addAttribute("warehouses", warehouseService.findAll());

        //show-warehouse.html
        return "show-warehouse";
    }

    @GetMapping("view/orders/new")
    public String addOrder(InventoryOrder inventoryOrder, Model model) {
        Map<String, String> mappedInventories = inventoryService.getMappedInventories();

        model.addAttribute("mappedInventories", mappedInventories);

        //add-order.html
        return "add-order";
    }

    @PostMapping("view/orders/save")
    public String saveNewOrder(@Valid InventoryOrder inventoryOrder, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //add-order.html
            return "add-order";
        }

        Instant now = Instant.now();
        inventoryOrder.setOrderedDate(Date.from(now));
        inventoryOrderService.saveAndFlush(inventoryOrder);

        model.addAttribute("orders", inventoryOrderService.findAll());

        //show-order.html
        return "show-order";
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @GetMapping("view/warehouses/edit/{id}")
    public String editWarehouse(@PathVariable("id") String id, Model model) {
        Warehouse entity = warehouseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid warehouse Id:" + id));

        log.info("Before update entity: [{}]", entity);

        model.addAttribute("newWarehouse", entity);
        model.addAttribute("createdDate", entity.getCreatedDate());
        model.addAttribute("lastModifiedDate", entity.getLastModifiedDate());
        model.addAttribute("inventories", entity.getInventories());

        //edit-warehouse.html
        return "edit-warehouse";
    }

    @PostMapping("view/warehouses/update")
    public String updateExistingWarehouse   (@ModelAttribute("newWarehouse") Warehouse updatedEntity,
                                             @RequestParam("reqInvId") String reqInvId,
                                             @RequestParam("reqInvName") String reqInvName,
                                             @RequestParam("reqInvSku") String reqInvSku,
                                             @RequestParam("reqInvCreated") String reqInvCreated,
                                             @RequestParam("reqInvModified") String reqInvModified,
                                             BindingResult result, Model model) {
        log.debug("reqInvId: {}", reqInvId);
        String[] invId = reqInvId.split(",");

        log.debug("reqInvName: {}", reqInvName);
        String[] invName = reqInvName.split(",");

        log.debug("reqInvSku: {}", reqInvSku);
        String[] invSku = reqInvSku.split(",");

        log.debug("reqInvCreated: {}", reqInvCreated);
        String[] invCreated = reqInvCreated.split(",");

        log.debug("reqInvModified: {}", reqInvModified);
        String[] invModified = reqInvModified.split(",");

        if (result.hasErrors()) {
            model.addAttribute("warehouses", warehouseService.findAll());

            //show-warehouse.html
            return "show-warehouse";
        }

        Set<Inventory> recoveredInventories = new HashSet<>();
        for(int i = 0; i < invId.length; i++) {

            try {
                Inventory inventory = new Inventory();
                inventory.setId(invId[i]);
                inventory.setName(invName[i]);
                inventory.setSku(invSku[i]);
                inventory.setCreatedDate(simpleDateFormat.parse(invCreated[i]));
                inventory.setLastModifiedDate(simpleDateFormat.parse(invModified[i]));
                inventory.setWarehouse(updatedEntity);

                recoveredInventories.add(inventory);
            } catch (ParseException e) {
                log.error("Error while parsing Inventory: ", e);

                model.addAttribute("warehouses", warehouseService.findAll());

                //show-warehouse.html
                return "show-warehouse";
            }
        }

        updatedEntity.setInventories(recoveredInventories);

        log.info("Saving...[{}]", updatedEntity);

        warehouseService.saveAndFlush(updatedEntity);

        model.addAttribute("warehouses", warehouseService.findAll());

        //show-warehouse.html
        return "show-warehouse";
    }
}
