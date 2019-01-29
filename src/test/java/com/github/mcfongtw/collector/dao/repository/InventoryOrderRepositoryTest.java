package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
@SpringBootTest(classes = CollectorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryOrderRepositoryTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private InventoryOrderRepository inventoryOrderRepository;

    @Test
    public void testFindDefaultOrderByIdViaCrudRepository() {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).isPresent());
        Assert.assertEquals(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCurrency(), InventoryOrder.DEFAULT_CURRENCY_TYPE);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Test
    public void testFindOrderWithDifferentCurrencyByIdViaCrudRepository() {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setCurrency(InventoryOrder.Currency.USD);
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).isPresent());
        Assert.assertEquals(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCurrency(), InventoryOrder.Currency.USD);

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.findById(inventoryOrder.getId()).get().getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Transactional
    @Test
    public void testFindDefaultOrderByIdViaJpaRepository() {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        Assert.assertNotNull(inventoryOrderRepository.getOne(inventoryOrder.getId()));
        Assert.assertEquals(inventoryOrderRepository.getOne(inventoryOrder.getId()).getCurrency(), InventoryOrder.DEFAULT_CURRENCY_TYPE);

        Assert.assertTrue(inventoryOrderRepository.getOne(inventoryOrder.getId()).getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(inventoryOrderRepository.getOne(inventoryOrder.getId()).getLastModifiedDate().before(Date.from(Instant.now())));
    }

    //    @WithMockUser(username="user")
    @Test
    public void testGetDefaultOrderByIdViaRestfulAPI() {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));
        inventoryOrder.setOrderedType(ORDER_TYPE_BUY);
        inventoryOrder.setOrderedPrice(new Double(10.00));

        inventoryOrderRepository.save(inventoryOrder);

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/orders/{id}");
        Map<String, Object> uriParams = new HashMap<String, Object>();
        uriParams.put("id", inventoryOrder.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                builder.buildAndExpand(uriParams).toUri().toString(),
                HttpMethod.GET, entity, String.class);
        Assert.assertTrue("testGetInventoryOrders Fail:\n" + response.getBody(),
                response.getStatusCode().is2xxSuccessful());
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
