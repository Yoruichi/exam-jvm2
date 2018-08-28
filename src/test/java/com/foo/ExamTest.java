package com.foo;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExamTest {

    private static List<String> skuIds;

    /**
     * 构造100个 skuid 作为测试条件
     */
    @BeforeClass
    public static void setUp() {
        skuIds = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            skuIds.add(String.valueOf(i));
        }
    }

    @AfterClass
    public static void tearDown() {
        skuIds = null;
    }

    @Test
    public void testItemGroup() {
        ItemGroupService service = new ItemGroupServiceImpl();
        List<ItemInfo> items = service.itemGroup(skuIds);
        System.out.println(items.size());
        System.out.println(items);
        Assert.assertTrue(items.size() == 6);
    }

}
