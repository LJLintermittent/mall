package com.learn.mall.ware;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MallWareApplicationTests {

    @Test
    public void testRandom() {
        Random random = new Random();
        int num = random.nextInt(29) + 1;
        System.out.println(num);

    }

}
