package com.github.mcfongtw.collector.domain.controller.view;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.domain.service.InventoryOrderService;
import com.github.mcfongtw.collector.domain.service.InventoryService;
import com.github.mcfongtw.collector.domain.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Controller
public class SimpleModelViewController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryOrderService inventoryOrderService;

    @Autowired
    private WarehouseService warehouseService;

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
        Map<String, String> mappedWarehouses = warehouseService.getMappedWarehouses();

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

        //welcome.html
        return "welcome";
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

        //welcome.html
        return "welcome";
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

        //welcome.html
        return "welcome";
    }
}
