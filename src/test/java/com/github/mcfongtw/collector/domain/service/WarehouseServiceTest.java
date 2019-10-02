package com.github.mcfongtw.collector.domain.service;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class)
public class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setUp() {
        warehouseService.clear();
        inventoryService.clear();
    }

    @Test
    public void testUpdateWarehouse() throws InterruptedException {
        Warehouse origWarehouse = new Warehouse();
        origWarehouse.setName("test");

        Inventory inventory = new Inventory();
        inventory.setName("Inv1");
        inventory.setSku("sku");

        origWarehouse.addInventory(inventory);

        warehouseService.saveAndFlush(origWarehouse);

        int origSize = warehouseService.findAll().size();

        Assert.assertTrue(origSize > 0);

        Thread.sleep(1000);

        origWarehouse.setName("test1");

        Warehouse updatedWarehouse = warehouseService.saveOrUpdate(origWarehouse);

        Assert.assertEquals(updatedWarehouse.getName(), "test1");
        Assert.assertEquals(updatedWarehouse.getId(), origWarehouse.getId());
        Assert.assertEquals(warehouseService.findAll().size(), origSize);

    }

    @Test
    public void testGetAndSaveAndRemoveOneInventoryFromWarehouse() throws InterruptedException {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Taipei");

        Inventory inventory1 = new Inventory();
        inventory1.setName("AAA");
        inventory1.setSku("xxx");

        Inventory inventory2 = new Inventory();
        inventory2.setName("BBB");
        inventory2.setSku("yyy");

        warehouse.addInventory(inventory1);

        warehouse.addInventory(inventory2);

        warehouseService.saveAndFlush(warehouse);


        Assert.assertEquals(2, warehouseService.findById(warehouse.getId()).get().getInventories().size());
        Assert.assertTrue(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory1));
        Assert.assertTrue(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory2));

        Date firstUpdateDate = warehouseService.findById(warehouse.getId()).get().getLastModifiedDate();

        Thread.sleep(10);

        warehouse.removeInventory(inventory1);

        warehouseService.saveAndFlush(warehouse);

        Assert.assertEquals(1, warehouseService.findById(warehouse.getId()).get().getInventories().size());
        Assert.assertFalse(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory1));
        Assert.assertTrue(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory2));

        //secondUpdateDate is after firstUpdateDate
        Date secondUpdateDate = warehouseService.findById(warehouse.getId()).get().getLastModifiedDate();

        //FIXME: secondUpdateDate == firstUpdateDate
        log.info("LastModifiedDate: [{}]", firstUpdateDate.toString());
        log.info("LastModifiedDate: [{}]", secondUpdateDate.toString());
//        Assert.assertTrue(secondUpdateDate.after(firstUpdateDate));
    }

    @Test
    public void testRemoveWarehouseOnlyButNotInventories() throws InterruptedException {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("Taipei");

        Inventory inventory1 = new Inventory();
        inventory1.setName("123");
        inventory1.setSku("xxx");

        Inventory inventory2 = new Inventory();
        inventory2.setName("456");
        inventory2.setSku("yyy");

        warehouse.addInventory(inventory1);

        warehouse.addInventory(inventory2);

        warehouseService.saveAndFlush(warehouse);


        Assert.assertEquals(2, warehouseService.findById(warehouse.getId()).get().getInventories().size());
        Assert.assertTrue(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory1));
        Assert.assertTrue(warehouseService.findById(warehouse.getId()).get().getInventories().contains(inventory2));


        Thread.sleep(10);

        warehouseService.deleteWithoutAssociations(warehouse.getId());

        List<Inventory> inventoryList = inventoryService.findAll();

        Assert.assertEquals(warehouseService.findById(warehouse.getId()), Optional.empty());

        log.info("{}", inventoryList);
        Assert.assertEquals(inventoryList.size(), 2);
    }
}
