package com.github.mcfongtw.collector;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CollectorApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CollectorApplicationTests {

    @Test
    public void contextLoads() {

    }

}

