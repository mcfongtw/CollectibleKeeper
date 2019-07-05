package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
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

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WarehouseControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private HttpHeaders headers = new HttpHeaders();

    private static String BASE_URL;

    @Before
    public void setUp() {
        BASE_URL = "http://localhost:" + port + "/warehouses";
    }

    @Test
    public void testCreateAndUpdateAndDeleteWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("testCreate");
        HttpEntity<Warehouse> createdReq = new HttpEntity<>(warehouse, headers);

        ResponseEntity<Void> createResponseEntity = this.testRestTemplate.postForEntity(BASE_URL, createdReq, Void.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", createResponseEntity.getHeaders());

        Assert.assertEquals(createResponseEntity.getStatusCode(), HttpStatus.CREATED);

        ResponseEntity<Warehouse> getResponseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/testCreate", Warehouse.class);
        if(getResponseEntity.getBody() == null) {
            Assert.fail();
        }

        warehouse = getResponseEntity.getBody();
        warehouse.setName("testUpdate");
        HttpEntity<Warehouse> updatedReq = new HttpEntity<>(warehouse, headers);

        ResponseEntity<Warehouse> updateResponseEntity = this.testRestTemplate.exchange(BASE_URL + "/" + warehouse.getId(), HttpMethod.PUT, updatedReq, Warehouse.class);
        Assert.assertEquals(updateResponseEntity.getStatusCode(), HttpStatus.OK);
        if(updateResponseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(updateResponseEntity.getBody().getName(), "testUpdate");

        String deleteId = updateResponseEntity.getBody().getId();
        this.testRestTemplate.delete(BASE_URL + "/" + deleteId);
    }


    @Test
    public void testGetWarehouseByName() {
        ResponseEntity<Warehouse> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/Taipei", Warehouse.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getHeaders().toString());
        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getBody());

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(responseEntity.getBody().getName(), "Taipei");
    }

    @Test
    public void testGetWarehouseByNameNotFound() {
        ResponseEntity<Warehouse> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/ABCADFADFADSFADSFADSF", Warehouse.class);

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
    public void testGetAllWarehouses() {
        ResponseEntity<Warehouse[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/all", Warehouse[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

    @Test
    public void testGetInventories() {
        ResponseEntity<Warehouse> getRespEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/Taipei", Warehouse.class);

        Assert.assertEquals(getRespEntity.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Inventory[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/inventories/" + getRespEntity.getBody().getId(), Inventory[].class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getHeaders().toString());
        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getBody());

        Assert.assertEquals(getRespEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }

}


