package com.foo;

import java.math.BigDecimal;
import java.util.*;

public class ItemGroupServiceImpl extends ItemGroupServiceAd {

    public ItemGroupServiceImpl() {
        super(ServiceBeanFactory.getInstance().getServiceBean(SkuService.class),
                ServiceBeanFactory.getInstance().getServiceBean(PriceService.class),
                ServiceBeanFactory.getInstance().getServiceBean(InventoryService.class)
        );
    }

    @Override public List<ItemInfo> itemGroup(List<String> skuIds) {
        Map<String, SkuInfoDTO> entitys = this.querySkuEntity(skuIds);
        Map<String, BigDecimal> inventorys = this.querySkuInventory(skuIds);
        Map<String, BigDecimal> prices = this.querySkuPrice(skuIds);
        List<ItemInfo> itemInfoList = new ArrayList<>();
        if (null == entitys) return itemInfoList;
        entitys.forEach((id, skuInfoDTO) -> {
            ItemInfo item = new ItemInfo();
            item.setId(id);
            item.setArtNo(skuInfoDTO.getArtNo());
            item.setName(skuInfoDTO.getName());
            item.setSpuId(skuInfoDTO.getSpuId());
            item.setSkuType(skuInfoDTO.getSkuType());
            item.setInventory(inventorys.containsKey(id) ? inventorys.get(id) : new BigDecimal(0));
            item.setPrice(prices.containsKey(id) ? prices.get(id) : new BigDecimal(0));
            itemInfoList.add(item);
        });
        System.out.println(String.format("Query %d item.", itemInfoList.size()));
        List<ItemInfo> originItems =
                ItemGroupServiceFactory.getService(ItemType.ORIGIN).itemGroupByType(itemInfoList);
        System.out.println(String.format("Origin item: %d", originItems.size()));
        List<ItemInfo> digitalItems =
                ItemGroupServiceFactory.getService(ItemType.DIGITAL).itemGroupByType(itemInfoList);
        System.out.println(String.format("Origin item: %d", digitalItems.size()));
        List<ItemInfo> res = new ArrayList<>();
        res.addAll(originItems);
        res.addAll(digitalItems);
        return res;
    }
}
