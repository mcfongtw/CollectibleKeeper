package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_BUY;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class)
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Before
    public void setUp() {
        inventoryRepository.deleteAll();
    }


    @Test
    public void testFindInventoryByIdViaCrudRepository() {
        Inventory inventory = new Inventory();
        inventory.setName("test");
        inventory.setSku("xxx");

        inventoryRepository.save(inventory);

        Assert.assertTrue(inventoryRepository.findById(inventory.getId()).isPresent());
        Assert.assertEquals(inventoryRepository.findById(inventory.getId()).get().getName(), "test");
        Assert.assertEquals(inventoryRepository.findById(inventory.getId()).get().getSku(), "xxx");

        Assert.assertTrue(inventoryRepository.findById(inventory.getId()).get().getCreatedDate().before(Date.from(Instant.now())));
    }

    @Test
    public void testSavingInventoryFailedForNotHavingName() {
        Inventory inventory = new Inventory();

        try {
            inventoryRepository.save(inventory);
            Assert.fail();
        } catch(DataIntegrityViolationException e) {
            Assert.assertTrue(e.getCause().getCause().getMessage().contains("NULL not allowed for column \"NAME\""));
        }
    }

    @Transactional
    @Test
    public void testGetInventoryByIdViaJpaRepository() {
        Inventory inventory = new Inventory();
        inventory.setName("test");
        inventory.setSku("xxx");

        inventoryRepository.save(inventory);

        Assert.assertTrue(inventoryRepository.getOne(inventory.getId()) != null);
        Assert.assertEquals(inventoryRepository.getOne(inventory.getId()).getName(), "test");
        Assert.assertEquals(inventoryRepository.getOne(inventory.getId()).getSku(), "xxx");
    }

    @Test
    public void testUpdateInventory() {
        Inventory origInventory = new Inventory();
        origInventory.setName("test");
        origInventory.setSku("sku");

        inventoryRepository.save(origInventory);

        int origSize = inventoryRepository.findAll().size();

        origInventory.setName("test1");

        Inventory updatedInventory = inventoryRepository.saveAndFlush(origInventory);

        Assert.assertEquals(updatedInventory.getName(), "test1");
        Assert.assertEquals(updatedInventory.getId(), origInventory.getId());
        Assert.assertEquals(inventoryRepository.findAll().size(), origSize);

    }

    @Transactional
    @Test
    public void testGetInventoriestByWarehouseName() {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("warehouse1");

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setName("warehouse2");

        Inventory inventory1 = new Inventory();
        inventory1.setName("test1");
        inventory1.setSku("111");
        inventory1.setWarehouse(warehouse1);

        inventoryRepository.save(inventory1);

        Inventory inventory2 = new Inventory();
        inventory2.setName("test2");
        inventory2.setSku("222");
        inventory2.setWarehouse(warehouse2);

        inventoryRepository.save(inventory2);

        Inventory inventory3 = new Inventory();
        inventory3.setName("test3");
        inventory3.setSku("333");
        inventory3.setWarehouse(warehouse2);

        inventoryRepository.save(inventory3);

        Assert.assertEquals(inventoryRepository.findByWarehouseName(warehouse1.getName()).size(), 1);
        Assert.assertEquals(inventoryRepository.findByWarehouseName(warehouse2.getName()).size(), 2);
    }

    @Transactional
    @Test
    public void testGetInventoriestByWarehouseId() {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("warehouse1");

        Warehouse warehouse2 = new Warehouse();
        warehouse2.setName("warehouse2");

        Inventory inventory1 = new Inventory();
        inventory1.setName("test1");
        inventory1.setSku("111");
        inventory1.setWarehouse(warehouse1);

        inventoryRepository.save(inventory1);

        Inventory inventory2 = new Inventory();
        inventory2.setName("test2");
        inventory2.setSku("222");
        inventory2.setWarehouse(warehouse2);

        inventoryRepository.save(inventory2);

        Inventory inventory3 = new Inventory();
        inventory3.setName("test3");
        inventory3.setSku("333");
        inventory3.setWarehouse(warehouse2);

        inventoryRepository.save(inventory3);

        Assert.assertEquals(inventoryRepository.findByWarehouseId(warehouse1.getId()).size(), 1);
        Assert.assertEquals(inventoryRepository.findByWarehouseId(warehouse2.getId()).size(), 2);
    }

    @Transactional
    @Test
    public void testSaveAndGetInventoryOrder() {
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("warehouse1");

        Inventory inventory = new Inventory();
        inventory.setName("AAA");
        inventory.setSku("xxx");


        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventory.setInventoryOrder(inventoryOrder);
        inventoryOrder.setInventory(inventory);

        inventory.setWarehouse(warehouse1);

        inventoryRepository.save(inventory);

        Assert.assertNotNull(inventoryRepository.getOne(inventory.getId()).getInventoryOrder());
        Assert.assertEquals(inventoryRepository.getOne(inventory.getId()).getInventoryOrder().getOrderedType(), ORDER_TYPE_BUY);
        Assert.assertEquals(inventoryRepository.getOne(inventory.getId()).getInventoryOrder().getOrderedPrice(), 10.00, 0.5);
    }

}
