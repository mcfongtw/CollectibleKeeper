package com.github.mcfongtw.collector.dao.service;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import com.github.mcfongtw.collector.dao.repository.InventoryOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_BUY;
import static com.github.mcfongtw.collector.dao.entity.InventoryOrder.ORDER_TYPE_SELL;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;


    @Transactional
    @Test
    public void testGetOrdersAccordingToPrice() {
        Inventory inventory1 = new Inventory();
        inventory1.setName("AAA");
        inventory1.setSku("xxx");


        InventoryOrder inventoryOrder1 = new InventoryOrder();
        inventoryOrder1.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder1.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder1.setOrderedPrice(new Double(10.00));

        inventoryOrder1.setInventory(inventory1);

        inventoryOrderRepository.save(inventoryOrder1);

        Inventory inventory2 = new Inventory();
        inventory2.setName("BBB");
        inventory2.setSku("yyy");


        InventoryOrder inventoryOrder2 = new InventoryOrder();
        inventoryOrder2.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder2.setOrderedType(ORDER_TYPE_SELL);
        inventoryOrder2.setOrderedPrice(new Double(5.00));

        inventoryOrder2.setInventory(inventory2);

        inventoryOrderRepository.save(inventoryOrder2);

        //Test Above
        Assert.assertEquals(orderService.getListOfOrderAbovePurchasedPrice(1.00).size(), 2);
        Assert.assertEquals(orderService.getListOfOrderAbovePurchasedPrice(20.00).size(), 0);

        Assert.assertEquals(orderService.getListOfOrderAboveSoldPrice(1.00).size(), 2);
        Assert.assertEquals(orderService.getListOfOrderAboveSoldPrice(20.00).size(), 0);

        //Test Below
        Assert.assertEquals(orderService.getListOfOrderBelowPurchasedPrice(1.00).size(), 0);
        Assert.assertEquals(orderService.getListOfOrderBelowPurchasedPrice(20.00).size(), 2);

        Assert.assertEquals(orderService.getListOfOrderBelowSoldPrice(1.00).size(), 0);
        Assert.assertEquals(orderService.getListOfOrderBelowSoldPrice(20.00).size(), 2);


    }

    @Transactional
    @Test
    public void testGetOrdersAccordingToDate() {
        Inventory inventory1 = new Inventory();
        inventory1.setName("AAA");
        inventory1.setSku("xxx");


        InventoryOrder inventoryOrder1 = new InventoryOrder();
        inventoryOrder1.setOrderedDate(Date.from(Instant.parse("2014-01-01T10:15:30.000Z")));
        inventoryOrder1.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder1.setOrderedPrice(new Double(10.00));

        inventoryOrder1.setInventory(inventory1);

        inventoryOrderRepository.save(inventoryOrder1);

        Inventory inventory2 = new Inventory();
        inventory2.setName("BBB");
        inventory2.setSku("yyy");


        InventoryOrder inventoryOrder2 = new InventoryOrder();
        inventoryOrder2.setOrderedDate(Date.from(Instant.parse("2015-01-01T10:15:30.000Z")));
        inventoryOrder2.setOrderedType(ORDER_TYPE_SELL);
        inventoryOrder2.setOrderedPrice(new Double(5.00));

        inventoryOrder2.setInventory(inventory2);

        inventoryOrderRepository.save(inventoryOrder2);

        //Test Before
        Assert.assertEquals(orderService.getListOfOrderBeforePurchasedDate(Date.from(Instant.now())).size(), 2);
        Assert.assertEquals(orderService.getListOfOrderBeforePurchasedDate(Date.from(Instant.EPOCH)).size(), 0);

        Assert.assertEquals(orderService.getListOfOrderBeforeSoldDate(Date.from(Instant.now())).size(), 2);
        Assert.assertEquals(orderService.getListOfOrderBeforeSoldDate(Date.from(Instant.EPOCH)).size(), 0);

        //Test After
        Assert.assertEquals(orderService.getListOfOrderAfterPurchasedDate(Date.from(Instant.now())).size(), 0);
        Assert.assertEquals(orderService.getListOfOrderAfterPurchasedDate(Date.from(Instant.EPOCH)).size(), 2);

        Assert.assertEquals(orderService.getListOfOrderAfterSoldDate(Date.from(Instant.now())).size(), 0);
        Assert.assertEquals(orderService.getListOfOrderAfterSoldDate(Date.from(Instant.EPOCH)).size(), 2);
    }

    @Transactional
    @Test
    public void testGetAggregatedNumberOfInventoryFromOrder() {
        /////////////////////////////////////////////////////////////
        Inventory inventory1 = new Inventory();
        inventory1.setName("AAA");
        inventory1.setSku("xxx");

        InventoryOrder inventoryOrder1 = new InventoryOrder();
        inventoryOrder1.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder1.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder1.setOrderedPrice(new Double(10.00));

        inventoryOrder1.setInventory(inventory1);

        inventoryOrderRepository.save(inventoryOrder1);

        /////////////////////////////////////////////////////////////
        Inventory inventory2 = new Inventory();
        inventory2.setName("AAA");
        inventory2.setSku("xxx");

        InventoryOrder inventoryOrder2 = new InventoryOrder();
        inventoryOrder2.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder2.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder2.setOrderedPrice(new Double(5.00));

        inventoryOrder2.setInventory(inventory2);

        inventoryOrderRepository.save(inventoryOrder2);

        /////////////////////////////////////////////////////////////
        Inventory inventory3 = new Inventory();
        inventory3.setName("BBB");
        inventory3.setSku("yyy");

        InventoryOrder inventoryOrder3 = new InventoryOrder();
        inventoryOrder3.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder3.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder3.setOrderedPrice(new Double(3.00));

        inventoryOrder3.setInventory(inventory3);

        inventoryOrderRepository.save(inventoryOrder3);

        Assert.assertEquals(orderService.getAggregatedNumberOfInventoryFromOrder("yyy"), 1);
        Assert.assertEquals(orderService.getAggregatedNumberOfInventoryFromOrder("xxx"), 2);
    }
}
