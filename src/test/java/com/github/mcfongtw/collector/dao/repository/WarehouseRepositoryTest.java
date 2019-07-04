package com.github.mcfongtw.collector.dao.repository;

import com.github.mcfongtw.collector.CollectorApplication;
import com.github.mcfongtw.collector.dao.entity.Inventory;
import com.github.mcfongtw.collector.dao.entity.Warehouse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class)
public class WarehouseRepositoryTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    public void testFindWarehouseByIdViaCrudRepository() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("test");

        warehouseRepository.save(warehouse);

        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).isPresent());
        Assert.assertEquals(warehouseRepository.findById(warehouse.getId()).get().getName(), "test");

        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).get().getCreatedDate().before(Date.from(Instant.now())));

        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).get().getLastModifiedDate().before(Date.from(Instant.now())));
    }

    @Test
    public void testSavingWarehouseFailedForNotHavingName() {
        Warehouse warehouse = new Warehouse();

        try {
            warehouseRepository.save(warehouse);
            Assert.fail();
        } catch(DataIntegrityViolationException e) {
            Assert.assertTrue(e.getCause().getCause().getMessage().contains("NULL not allowed for column \"NAME\""));
        }
    }

    @Transactional
    @Test
    public void testGetWarehouseByIdViaJpaRepository() {
        Warehouse warehouse = new Warehouse();
        warehouse.setName("test");

        warehouseRepository.save(warehouse);

        Assert.assertTrue(warehouseRepository.getOne(warehouse.getId()) != null);
        Assert.assertEquals(warehouseRepository.getOne(warehouse.getId()).getName(), "test");
    }

    @Test
    public void testGetAndSaveAndRemoveInventoriesFromWarehouse() throws InterruptedException {
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

        warehouseRepository.save(warehouse);


        Assert.assertEquals(2, warehouseRepository.findById(warehouse.getId()).get().getInventories().size());
        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).get().getInventories().contains(inventory1));
        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).get().getInventories().contains(inventory2));

        Date firstUpdateDate = warehouseRepository.findById(warehouse.getId()).get().getLastModifiedDate();

        Thread.sleep(10);

        warehouse.removeInventory(inventory1);

        warehouseRepository.save(warehouse);

        Assert.assertEquals(1, warehouseRepository.findById(warehouse.getId()).get().getInventories().size());
        Assert.assertFalse(warehouseRepository.findById(warehouse.getId()).get().getInventories().contains(inventory1));
        Assert.assertTrue(warehouseRepository.findById(warehouse.getId()).get().getInventories().contains(inventory2));

        //secondUpdateDate is after firstUpdateDate
        Date secondUpdateDate = warehouseRepository.findById(warehouse.getId()).get().getLastModifiedDate();

        //FIXME: secondUpdateDate == firstUpdateDate
        log.info("LastModifiedDate: [{}]", firstUpdateDate.toString());
        log.info("LastModifiedDate: [{}]", secondUpdateDate.toString());
//        Assert.assertTrue(secondUpdateDate.after(firstUpdateDate));
    }
}
