package com.github.mcfongtw.collector.dao;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import com.github.mcfongtw.collector.domain.service.InventoryOrderService;
import com.github.mcfongtw.collector.domain.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_BUY;
import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_SELL;

@Slf4j
@Component
public class DataBootstrap implements ApplicationRunner {

    @Autowired
    private InventoryOrderService inventoryOrderService;

    @Autowired
    private WarehouseService warehouseService;

    @Value("${production.mode}")
    private String productionMode;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count = inventoryOrderService.count();
        log.info("inventory count: [{}]", count);

        boolean isProductionMode = Boolean.parseBoolean(productionMode);
        log.info("Production mode: [{}]", isProductionMode);

        if(count == 0 && !isProductionMode) {
            Warehouse warehouse1 = new Warehouse();
            warehouse1.setName("Taipei");

            Inventory inventory1 = new Inventory();
            inventory1.setName("111");
            inventory1.setSku("AAA");
            inventory1.setWarehouse(warehouse1);

            Inventory inventory2 = new Inventory();
            inventory2.setName("222");
            inventory2.setSku("BBB");
            inventory2.setWarehouse(warehouse1);

            warehouseService.saveAndFlush(warehouse1);

            Warehouse warehouse2 = new Warehouse();
            warehouse2.setName("L.A.");

            Inventory inventory3 = new Inventory();
            inventory3.setName("333");
            inventory3.setSku("CCC");
            inventory3.setWarehouse(warehouse2);

            warehouseService.saveAndFlush(warehouse2);

            InventoryOrder inventoryOrder1 = new InventoryOrder();
            inventoryOrder1.setOrderedDate(Date.from(Instant.now()));
            inventoryOrder1.setOrderedType(ORDER_TYPE_BUY);
            inventoryOrder1.setOrderedPrice(new Double(10.00));
            inventoryOrder1.setInventory(inventory1);
            inventory1.setInventoryOrder(inventoryOrder1);

            inventoryOrderService.saveAndFlush(inventoryOrder1);

            InventoryOrder inventoryOrder2 = new InventoryOrder();
            inventoryOrder2.setOrderedDate(Date.from(Instant.now()));
            inventoryOrder2.setOrderedType(ORDER_TYPE_SELL);
            inventoryOrder2.setOrderedPrice(new Double(5.00));
            inventoryOrder2.setInventory(inventory2);

            inventory2.setInventoryOrder(inventoryOrder2);

            inventoryOrderService.saveAndFlush(inventoryOrder2);

            InventoryOrder inventoryOrder3 = new InventoryOrder();
            inventoryOrder3.setOrderedDate(Date.from(Instant.now()));
            inventoryOrder3.setOrderedType(ORDER_TYPE_BUY);
            inventoryOrder3.setOrderedPrice(new Double(1.00));
            inventoryOrder3.setInventory(inventory3);

            inventory3.setInventoryOrder(inventoryOrder3);

            inventoryOrderService.saveAndFlush(inventoryOrder3);
        }
    }
}
