package com.github.mcfongtw.collector.domain.controller;

import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InventoryControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${server.servlet.context-path}")
    private String apiUrlPrefix;

    @LocalServerPort
    private int port;

    private HttpHeaders headers = new HttpHeaders();

    private static String BASE_URL;

    @Before
    public void setUp() {
        BASE_URL = "http://localhost:" + port + apiUrlPrefix +  "/inventories";
        log.info("Base URL : [{}]", BASE_URL);
    }

    @Test
    public void testCreateAndUpdateAndDeleteInventory() {
        Inventory inventory = new Inventory();
        inventory.setName("testCreate");
        inventory.setSku("asdfdasfafds");
        HttpEntity<Inventory> createdReq = new HttpEntity<>(inventory, headers);

        ResponseEntity<Inventory> createResponseEntity = this.testRestTemplate.postForEntity(BASE_URL, createdReq, Inventory.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", createResponseEntity.getHeaders());

        Assert.assertEquals(createResponseEntity.getStatusCode(), HttpStatus.CREATED);


        inventory = createResponseEntity.getBody();
        inventory.setName("testUpdate");
        HttpEntity<Inventory> updatedReq = new HttpEntity<>(inventory, headers);

        ResponseEntity<Inventory> updateResponseEntity = this.testRestTemplate.exchange(BASE_URL + "/" + inventory.getId(), HttpMethod.PUT, updatedReq, Inventory.class);
        Assert.assertEquals(updateResponseEntity.getStatusCode(), HttpStatus.OK);
        if(updateResponseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(updateResponseEntity.getBody().getName(), "testUpdate");

        String deleteId = updateResponseEntity.getBody().getId();
        this.testRestTemplate.delete(BASE_URL + "/" + deleteId);
    }


    @Test
    public void testGetInventoryByName() {
        ResponseEntity<Inventory> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/111", Inventory.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getHeaders().toString());
        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getBody());

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(responseEntity.getBody().getName(), "111");
    }

    @Test
    public void testGetInventoryBySku() {
        ResponseEntity<Inventory> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/sku/AAA", Inventory.class);

        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getHeaders().toString());
        log.info(">>>>>>>>>>>>>>>>>>>>> [{}]", responseEntity.getBody());

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertEquals(responseEntity.getBody().getSku(), "AAA");
    }

    @Test
    public void testGetInventoryByNameNotFound() {
        ResponseEntity<Inventory> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/name/ABCADFADFADSFADSFADSF", Inventory.class);

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
    public void testGetAllInventorys() {
        ResponseEntity<Inventory[]> responseEntity = this.testRestTemplate.getForEntity(BASE_URL + "/all", Inventory[].class);

        Assert.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

        if(responseEntity.getBody() == null) {
            Assert.fail();
        }

        Assert.assertTrue(responseEntity.getBody().length > 0);
    }


}


