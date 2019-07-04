package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_BUY;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class)
public class InventoryOrderRepositoryTest {

    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;

    @Transactional
    @Test
    public void testFindDefaultOrderByIdViaCrudRepository() throws InterruptedException {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Thread.sleep(100);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).isPresent());
        Assert.assertEquals(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCurrency(), InventoryOrder.DEFAULT_CURRENCY_TYPE);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Transactional
    @Test
    public void testFindOrderWithDifferentCurrencyByIdViaCrudRepository() throws InterruptedException {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setCurrency(InventoryOrder.Currency.USD);
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Thread.sleep(100);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).isPresent());
        Assert.assertEquals(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCurrency(), InventoryOrder.Currency.USD);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Transactional
    @Test
    public void testFindDefaultOrderByIdViaJpaRepository() throws InterruptedException {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Thread.sleep(100);

        Assert.assertNotNull(inventoryOrderRepository.getOne(inventoryOrder.getId()));
        Assert.assertEquals(inventoryOrderRepository.getOne(inventoryOrder.getId()).getCurrency(), InventoryOrder.DEFAULT_CURRENCY_TYPE);

        Assert.assertTrue(inventoryOrderRepository.getOne(inventoryOrder.getId()).getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.getOne(inventoryOrder.getId()).getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Transactional
    @Test
    public void testGetAndSaveAndRemoveInventoryByInventoryOrderRepository() {
        Inventory inventory = new Inventory();
        inventory.setName("AAA");
        inventory.setSku("xxx");


        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrder.setInventory(inventory);

        inventoryOrderRepository.save(inventoryOrder);

        Assert.assertEquals(inventoryOrderRepository.getOne(inventoryOrder.getId()).getInventory(), inventory);

    }

}
