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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class)
public class WarehouseServiceTest {

    @Autowired
    private WarehouseService warehouseService;

    @Before
    public void setUp() {
        warehouseService.clear();
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
}
