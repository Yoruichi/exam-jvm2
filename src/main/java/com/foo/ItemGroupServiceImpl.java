package com.foo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ItemGroupServiceImpl extends ItemGroupServiceAd {

    private static final String ORIGIN = "ORIGIN";
    private static final String DIGITAL = "DIGITAL";

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
            item.setMinPrice(prices.containsKey(id) ? prices.get(id) : new BigDecimal(0));
            itemInfoList.add(item);
        });
        System.out.println(String.format("Query %d item.", itemInfoList.size()));
        Map<String, List<ItemInfo>> originItems =
                itemInfoList.stream().filter(s -> ORIGIN.equals(s.getSkuType()))
                        .collect(Collectors.groupingBy(ItemInfo::getArtNo));
        System.out.println(String.format("Origin item: %d", originItems.size()));
        Map<String, List<ItemInfo>> digitalItems =
                itemInfoList.stream().filter(s -> DIGITAL.equals(s.getSkuType()))
                        .collect(Collectors.groupingBy(ItemInfo::getSpuId));
        System.out.println(String.format("Origin item: %d", digitalItems.size()));
        List<ItemInfo> res = new ArrayList<>();
        originItems.entrySet().stream().forEach(e -> {
            ItemInfo item = new ItemInfo();
            item.setName(e.getValue().get(0).getName());
            item.setArtNo(e.getKey());
            item.setSkuType(ORIGIN);
            Optional<BigDecimal> inventory =
                    e.getValue().stream().map(ItemInfo::getInventory).reduce((a, b) -> a.add(b));
            if (inventory.isPresent()) item.setInventory(inventory.get());
            Optional<BigDecimal> minPrice =
                    e.getValue().stream().map(ItemInfo::getMinPrice).min(Comparator.naturalOrder());
            if (minPrice.isPresent()) item.setMinPrice(minPrice.get());
            Optional<BigDecimal> maxPrice =
                    e.getValue().stream().map(ItemInfo::getMinPrice).max(BigDecimal::compareTo);
            if (minPrice.isPresent()) item.setMaxPrice(maxPrice.get());
            res.add(item);
        });
        digitalItems.entrySet().stream().forEach(e -> {
            ItemInfo item = new ItemInfo();
            item.setName(e.getValue().get(0).getName());
            item.setSpuId(e.getKey());
            item.setSkuType(DIGITAL);
            Optional<BigDecimal> inventory =
                    e.getValue().stream().map(ItemInfo::getInventory).reduce((a, b) -> a.add(b));
            if (inventory.isPresent()) item.setInventory(inventory.get());
            Optional<BigDecimal> minPrice =
                    e.getValue().stream().map(ItemInfo::getMinPrice).min(Comparator.naturalOrder());
            if (minPrice.isPresent()) item.setMinPrice(minPrice.get());
            Optional<BigDecimal> maxPrice =
                    e.getValue().stream().map(ItemInfo::getMinPrice).max(BigDecimal::compareTo);
            if (minPrice.isPresent()) item.setMaxPrice(maxPrice.get());
            res.add(item);
        });
        return res;
    }
}
