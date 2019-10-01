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
        model.addAttribute("warhousesAsMap", warehouseService.getWarhousesAsMap());

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
        model.addAttribute("inventoriesAsMap", inventoryService.getInventoriesAsMap());

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
        model.addAttribute("inventoriesAsMap", warehouseService.getInventoriesAsMap(entity));

        //edit-warehouse.html
        return "edit-warehouse";
    }

    @PostMapping("view/warehouses/update")
    public String updateExistingWarehouse   (@ModelAttribute("newWarehouse") Warehouse warehouse,
                                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("warehouses", warehouseService.findAll());

            //show-warehouse.html
            return "show-warehouse";
        }

        log.info("Saving...[{}]", warehouse);

        warehouseService.saveAndFlush(warehouse);

        model.addAttribute("warehouses", warehouseService.findAll());

        //show-warehouse.html
        return "show-warehouse";
    }

    @GetMapping("view/inventories/edit/{id}")
    public String editInventory(@PathVariable("id") String id, Model model) {
        Inventory entity = inventoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inventory Id:" + id));

        log.info("Before update entity: [{}]", entity);

        model.addAttribute("newInventory", entity);
        model.addAttribute("createdDate", entity.getCreatedDate());
        model.addAttribute("lastModifiedDate", entity.getLastModifiedDate());
        model.addAttribute("warhousesAsMap", warehouseService.getWarhousesAsMap());
        model.addAttribute("ordersAsMap", inventoryService.getOrdersAsMap(entity));

        //edit-inventory.html
        return "edit-inventory";
    }

    @PostMapping("view/inventories/update")
    public String updateExistingInventory   (@ModelAttribute("newInventory") Inventory inventory,
                                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("inventories", inventoryService.findAll());

            //show-inventory.html
            return "show-inventory";
        }

        log.info("Saving...[{}]", inventory);

        inventoryService.saveAndFlush(inventory);

        model.addAttribute("inventories", inventoryService.findAll());

        //show-inventory.html
        return "show-inventory";
    }

    @GetMapping("view/orders/edit/{id}")
    public String editOrder(@PathVariable("id") String id, Model model) {
        InventoryOrder entity = inventoryOrderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inventory order Id:" + id));

        log.info("Before update entity: [{}]", entity);

        model.addAttribute("newOrder", entity);
        model.addAttribute("createdDate", entity.getCreatedDate());
        model.addAttribute("lastModifiedDate", entity.getLastModifiedDate());
        model.addAttribute("inventory", entity.getInventory());
        model.addAttribute("inventoriesAsMap", inventoryService.getInventoriesAsMap());

        //edit-warehouse.html
        return "edit-order";
    }

    @PostMapping("view/orders/update")
    public String updateExistingOrder(@ModelAttribute("newOrder") InventoryOrder inventoryOrder,
                                      BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("orders", inventoryOrderService.findAll());

            //show-order.html
            return "show-order";
        }

        Instant now = Instant.now();
        inventoryOrder.setOrderedDate(Date.from(now));

        log.info("Saving...[{}]", inventoryOrder);

        inventoryOrderService.saveAndFlush(inventoryOrder);

        model.addAttribute("orders", inventoryOrderService.findAll());

        //show-order.html
        return "show-order";
    }
}
