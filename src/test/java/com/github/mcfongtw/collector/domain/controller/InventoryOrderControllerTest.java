package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.InventoryOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.Instant;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryOrderControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private HttpHeaders headers = new HttpHeaders();

    private static String BASE_URL;

    @Before
    public void setUp() {
        BASE_URL = "http://localhost:" + port + "/orders";
    }

    @Test
    public void testCreateAndUpdateAndDeleteInventoryOrder() {
        InventoryOrder inventoryOrder = new InventoryOrder();
        inventoryOrder.setOrderedPrice(1.234);
        inventoryOrder.setOrderedType(InventoryOrder.OrderType.BUY);
        inventoryOrder.setOrderedDate(Date.from(Instant.now()));

        HttpEntity<InventoryOrder> createdReq = new HttpEntity<>(inventoryOrder, headers);

        ResponseEntity<InventoryOrder> createResponseEntity = this.testRestTemplate.postForEntity(BASE_URL, createdReq, InventoryOrder.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", createResponseEntity.getHeaders());

        Assert.assertEquals(createResponseEntity.getStatusCode(), HttpStatus.CREATED);


        inventoryOrder = createResponseEntity.getBody();
        inventoryOrder.setOrderedPrice(12345.6789);
        HttpEntity<InventoryOrder> updatedReq = new HttpEntity<>(inventoryOrder, headers);

        ResponseEntity<InventoryOrder> updateResponseEntity = this.testRestTemplate.exchange(BASE_URL + "/" + inventoryOrder.getId(), HttpMethod.PUT, updatedReq, InventoryOrder.class);
        Assert.assertEquals(updateResponseEntity.getStatusCode(), HttpStatus.OK);
        if(updateResponseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(updateResponseEntity.getBody().getOrderedPrice(), 12345.6789, 0.1);

        String deleteId = updateResponseEntity.getBody().getId();
        this.testRestTemplate.delete(BASE_URL + "/" + deleteId);
    }


    @Test
    public void testGetInventoryOrderByIdNotFound() {
        ResponseEntity<InventoryOrder> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/id/ABCADFADFADSFADSFADSF", InventoryOrder.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getHeaders().toString());
        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getBody());

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        Assert.assertNull(responseEntity.getBody());
    }

    @Test
    public void testCount() {
        ResponseEntity<Integer> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/count", Integer.class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody() > 0);
    }

    @Test
    public void testGetAllInventoryOrders() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/all", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderAbovePurchasedPrice() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/above/buy/0.1", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderAboveSoldPrice() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/above/sold/0.1", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderBelowPurchasedPrice() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/below/buy/1111111111111111111", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderBelowSoldPrice() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/below/sold/11111111", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderBeforePurchasedDate() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/before/buy/01-01-2119", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetListOfOrderAfterSoldDate() {
        ResponseEntity<InventoryOrder[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/search/after/sold/01-01-1970", InventoryOrder[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }
}
